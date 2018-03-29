package yaya.tlv;

import android.util.Log;
import android.util.SparseArray;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import yaya.tlv.annotation.TlvHeaderField;
import yaya.tlv.annotation.TlvMsg;
import yaya.tlv.annotation.TlvSignalField;
import yaya.tlv.annotation.TlvVoMsg;
import yaya.tlv.convertor.ConvertorFactory;
import yaya.tlv.header.TlvAccessHeader;
import yaya.tlv.meta.TlvFieldMeta;
import yaya.tlv.meta.TlvHeaderFieldMeta;
import yaya.tlv.signal.TlvSignal;
import yaya.tlv.transform.TransformerFactory;

public class DefaultTlvStore implements TlvStore {
    private static final String TAG = "DefaultTlvStore";
    private SparseArray<SparseArray<Class<? extends Tlvable>>> typeMetaCache = new SparseArray<>();

    private Map<Class, SparseArray<TlvFieldMeta>> tlvFieldMetaCache = new HashMap<>();
    private Map<Class, SortedSet<TlvHeaderFieldMeta>> tlvHeaderFieldMetaCache = new HashMap<>();

    public DefaultTlvStore() throws Exception {
        initialRegisterTlvHeaderFieldMeta();
    }

    public void initialRegisterTlvHeaderFieldMeta() throws Exception {
        registerTlvHeaderFieldMeta(TlvAccessHeader.class);
    }

    public void registerTlvHeaderFieldMeta(Class type) throws Exception {
        SortedSet<TlvHeaderFieldMeta> tlvFieldMetas = new TreeSet<TlvHeaderFieldMeta>();
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(TlvHeaderField.class)) {
                TlvHeaderField tlvHeaderField = field
                        .getAnnotation(TlvHeaderField.class);
                TlvHeaderFieldMeta tlvFieldMeta = new TlvHeaderFieldMeta();
                tlvFieldMeta.setUnsigned(tlvHeaderField.unsigned());
                tlvFieldMeta.setIndex(tlvHeaderField.index());
                tlvFieldMeta.setConvertor(ConvertorFactory.build(field
                        .getType()));
                tlvFieldMeta.setField(field);

                tlvFieldMetas.add(tlvFieldMeta);
            }
        }

        if (tlvFieldMetas.size() > 0) {
            tlvHeaderFieldMetaCache.put(type, tlvFieldMetas);
        }
    }

    @Override
    public Class getTypeMeta(int moduleId, int msgCode) {
        SparseArray<Class<? extends Tlvable>> mappers = typeMetaCache
                .get(moduleId);
        if (null != mappers) {
            return mappers.get(msgCode);
        } else {
            return null;
        }
    }

    @Override
    public SparseArray<TlvFieldMeta> getTlvFieldMeta(Class type) {
        return tlvFieldMetaCache.get(type);
    }

    @Override
    public SortedSet<TlvHeaderFieldMeta> getTlvHeaderFieldMeta(Class type) {
        return tlvHeaderFieldMetaCache.get(type);
    }

    private boolean checkSupportFieldType(Field field) throws Exception {
        Class type = field.getType();
        if (String.class.isAssignableFrom(type)) {
            return true;
        } else if (Byte.class.isAssignableFrom(type) || type.equals(byte.class)) {
            return true;
        } else if (Short.class.isAssignableFrom(type)
                || type.equals(short.class)) {
            return true;
        } else if (Character.class.isAssignableFrom(type)
                || type.equals(char.class)) {
            return true;
        } else if (Integer.class.isAssignableFrom(type)
                || type.equals(int.class)) {
            return true;
        } else if (Long.class.isAssignableFrom(type) || type.equals(long.class)) {
            return true;
        } else if (Float.class.isAssignableFrom(type)
                || type.equals(float.class)) {
            return true;
        } else if (Double.class.isAssignableFrom(type)
                || type.equals(double.class)) {
            return true;
        } else if (TlvSignal.class.isAssignableFrom(type)) {
            return true;
        } else if (List.class.isAssignableFrom(type)) {
            return true;
        } else if (type.isArray() && type.getComponentType().equals(byte.class)) {
            return true;
        }

        throw new Exception("field isn't support." + field);
    }

    public void registerTlvFieldMeta(Class<? extends TlvSignal> type)
            throws Exception {
        SparseArray<TlvFieldMeta> tlvFieldMetas = new SparseArray<>();

        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(TlvSignalField.class)) {
                checkSupportFieldType(field);
                TlvSignalField tlvSignalField = field
                        .getAnnotation(TlvSignalField.class);
                TlvFieldMeta tlvFieldMeta = new TlvFieldMeta();
                if (List.class.isAssignableFrom(field.getType())) {
                    Type fc = field.getGenericType();
                    if (fc instanceof ParameterizedType) {
                        ParameterizedType pt = (ParameterizedType) fc;
                        Class genericClass = (Class) pt
                                .getActualTypeArguments()[0]; // generic type
                        tlvFieldMeta.setTransformer(TransformerFactory
                                .build(genericClass));
                    } else {
                        Log.e(TAG, "array list must generic,type:" + type);
                        throw new Exception("array list must generic.");
                    }
                } else {
                    tlvFieldMeta.setTransformer(TransformerFactory.build(field
                            .getType()));
                }
                tlvFieldMeta.setField(field);
                tlvFieldMeta.setTag(tlvSignalField.tag());
                tlvFieldMeta.setUnsigned(tlvSignalField.unsigned());

                tlvFieldMetas.put(tlvSignalField.tag(), tlvFieldMeta);

                if (field.getType().isAnnotationPresent(TlvVoMsg.class)) {
                    registerTlvFieldMeta((Class<? extends TlvSignal>) field
                            .getType());
                } else if (List.class.isAssignableFrom(field.getType())) {
                    Type fc = field.getGenericType();
                    if (fc instanceof ParameterizedType) {
                        ParameterizedType pt = (ParameterizedType) fc;
                        Class genericClass = (Class) pt
                                .getActualTypeArguments()[0]; // generic type

                        if (genericClass.isAnnotationPresent(TlvVoMsg.class)) {
                            if (!tlvFieldMetaCache.containsKey(genericClass)) {
                                registerTlvFieldMeta((Class<? extends TlvSignal>) genericClass);
                            }
                        }
                    }
                }
            }
        }

        if (tlvFieldMetas.size() > 0) {
            tlvFieldMetaCache.put(type, tlvFieldMetas);
        }
    }

    public void addTypeMetaCache(Class<? extends Tlvable> type)
            throws Exception {
        if (type.isAnnotationPresent(TlvMsg.class)) {
            TlvMsg tlvMsg = type.getAnnotation(TlvMsg.class);
            SparseArray<Class<? extends Tlvable>> mappers = typeMetaCache
                    .get(tlvMsg.moduleId());
            if (null == mappers) {
                mappers = new SparseArray<>();
            }
            mappers.put(tlvMsg.msgCode(), type);
            typeMetaCache.put(tlvMsg.moduleId(), mappers);
        }

        if (TlvSignal.class.isAssignableFrom(type)) {
            registerTlvFieldMeta((Class<? extends TlvSignal>) type);
        }
    }

    public void setTypeMetaCacheByMap(
            SparseArray<SparseArray<Class<? extends Tlvable>>> typeMetaCache)
            throws Exception {
        if (null != typeMetaCache) {
            this.typeMetaCache = typeMetaCache;
            for (int i = 0, size = typeMetaCache.size(); i < size; i++) {
                SparseArray<Class<? extends Tlvable>> mappers = typeMetaCache.valueAt(i);
                for (int j = 0, size1 = mappers.size(); j < size1; j++) {
                    Class<? extends Tlvable> clazz = mappers.valueAt(j);
                    if (TlvSignal.class.isAssignableFrom(clazz)) {
                        registerTlvFieldMeta((Class<? extends TlvSignal>) clazz);
                    }
                }
            }
        }
    }

    public void setTypeMetaCacheByList(List<Class<? extends Tlvable>> types)
            throws Exception {
        if (null != types) {
            for (Class<? extends Tlvable> type : types) {
                if (type.isAnnotationPresent(TlvMsg.class)) {
                    TlvMsg tlvMsg = type.getAnnotation(TlvMsg.class);
                    SparseArray<Class<? extends Tlvable>> mappers = typeMetaCache
                            .get(tlvMsg.moduleId());
                    if (null == mappers) {
                        mappers = new SparseArray<>();
                    }
                    mappers.put(tlvMsg.msgCode(), type);
                    typeMetaCache.put(tlvMsg.moduleId(), mappers);
                }

                if (TlvSignal.class.isAssignableFrom(type)) {
                    registerTlvFieldMeta((Class<? extends TlvSignal>) type);
                }
            }
        }
    }
}
