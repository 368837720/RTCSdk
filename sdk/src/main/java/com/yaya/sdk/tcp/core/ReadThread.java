package com.yaya.sdk.tcp.core;

import android.os.SystemClock;

import com.yaya.sdk.MLog;
import com.yaya.sdk.tcp.ResponseDispatcher;
import com.yaya.sdk.tcp.TcpExceptionHandler;
import com.yaya.sdk.tlv.YayaTlvStore;
import com.yaya.sdk.util.GZipUtil;

import java.io.IOException;
import java.net.SocketException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import yaya.tlv.header.TlvAccessHeader;
import yaya.tlv.signal.TlvSignal;
import yaya.tlv.util.TlvCodecUtil;

/**
 * Created by ober on 2016/11/7.
 */
public class ReadThread extends Thread {
    private static final String TAG = "ReadThread";

    private static final byte COMPRESED = 1;

    private final Selector mSelector;
    private final ByteBuffer mReadBuffer, mWriteBuffer;
    private final SocketChannel mSocketChannel;
    private final SelectionKey mSelectionKey;

    private volatile boolean isRun;
    private int emptySelects;

    boolean mBufferPositionFix;
    private TlvAccessHeader mHeader;

    private final Object mWriteLock = new Object();

    private TcpExceptionHandler mExceptionHandler;

    private ResponseDispatcher mResponseDispatcher;

    ReadThread(SocketChannel socketChannel, Selector selector,
               SelectionKey key, int objBufferSize, int writeBufferSize,
               ResponseDispatcher responseDispatcher,
               TcpExceptionHandler exceptionHandler) {
        this.mSocketChannel = socketChannel;
        this.mSelector = selector;
        this.mSelectionKey = key;
        this.mReadBuffer = ByteBuffer.allocate(objBufferSize);
        this.mWriteBuffer = ByteBuffer.allocate(writeBufferSize);
        this.mResponseDispatcher = responseDispatcher;
        this.mExceptionHandler = exceptionHandler;

        this.mReadBuffer.flip();
    }

    //todo test code
    public synchronized int getWriteBufferRemaining() {
        synchronized (mWriteLock) {
            return mWriteBuffer.position();
        }
    }

    public synchronized void quitSafely() {
        isRun = false;
    }

    public synchronized boolean isRunning() {
        return isRun;
    }

    public synchronized void send(byte[] data) {

        if(!isRunning()) {
            MLog.w(TAG, "send(),  but isRun = false");
            return;
        }

        synchronized (mWriteLock) {
            int start = mWriteBuffer.position();
            try {
                mWriteBuffer.put(data);
            } catch (BufferOverflowException e) {
                MLog.e(TAG, e.getMessage());
                handleException(TcpExceptionHandler.WRITE_BUFFER_OVER_FLOW, e);
                return;
            }

            if(start != 0) {
                mSelectionKey.selector().wakeup();
                return;
            }

            // write to socket if no data was queued.
            boolean write;
            try {
                write = writeToSocket();
            } catch (IOException e) {
                MLog.e(TAG, e.getMessage());
                handleException(TcpExceptionHandler.SOCKET_WRITE_EXCEPTION, e);
                mSelectionKey.interestOps(SelectionKey.OP_READ
                        | SelectionKey.OP_WRITE);
                return;
            }

            if(!write) {
                // A partial write, set OP_WRITE to be notified when more
                // writing can occur
                mSelectionKey.interestOps(SelectionKey.OP_READ
                        | SelectionKey.OP_WRITE);
                return;
            }

            // Full write, wake up selector so idle event will be fired.
            mSelectionKey.selector().wakeup();
        }
    }

    @Override
    public void run() {
        MLog.d(TAG, "start tid = " + currentThread().getId());
        isRun = true;
        while (isRunning()) {
            long startTime = System.currentTimeMillis();
            int select;
            try {
                select = mSelector.select(20);
            } catch (IOException e) {
                MLog.e(TAG, e.getMessage());
                handleException(TcpExceptionHandler.SELECT_EXCEPTION, e);
                return;
            }
            if(select == 0) {
                emptySelects ++;
                if(emptySelects == 100) {
                    emptySelects = 0;
                    long elapsedTime = System.currentTimeMillis()
                            - startTime;
                    //MLog.w(TAG, "so many empty selects elapsedTime=" + elapsedTime);
                    if (elapsedTime < 25) {
                        SystemClock.sleep(25 - elapsedTime);
                    }
                }
                continue;
            }

            emptySelects = 0;

            final Set<SelectionKey> keys = mSelector.selectedKeys();
            //todo 这里为什么要synchronized? 可以去掉吗？
            synchronized (keys) {
                for(Iterator<SelectionKey> iterator = keys.iterator(); iterator.hasNext(); ) {
                    if(!isRunning()) {
                        break;
                    }
                    final SelectionKey sk = iterator.next();
                    iterator.remove();
                    int ops = sk.readyOps();
                    if((ops & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
                        SocketChannel socketChannel = (SocketChannel) sk.channel();
                        while (isRunning()) {
                            TlvSignal signal;
                            //MLog.d(TAG, "read from socket channel");
                            try {
                                signal = read(socketChannel);
                            } catch (Exception e) {
                                if(e instanceof UnknownSignalException) {
                                    //ignore
                                    continue;
                                }
                                MLog.w(TAG, "to stop, cause:" +
                                        e.getClass().getSimpleName() + ":" + e.getMessage());
                                e.printStackTrace();
                                isRun = false;
                                break;
                            }
                            if(signal == null) {
                                break;
                            }
                            dispatchSignal(signal);
                        }
                    }
                    if((ops & SelectionKey.OP_WRITE) == SelectionKey.OP_WRITE) {
                        try {
                            writeOperations();
                        } catch (IOException e) {
                            MLog.w(TAG, "to stop, cause:" +
                                    e.getClass().getSimpleName() + ":" + e.getMessage());
                            isRun = false;
                            break;
                        }
                    }
                }
            }
        }
        MLog.d(TAG, "end tid = " + currentThread().getId());
    }

    private void dispatchSignal(TlvSignal signal) {
        mResponseDispatcher.dispatch(signal);
    }

    private boolean writeToSocket() throws IOException {
        ByteBuffer buffer = mWriteBuffer;
        buffer.flip();
        while (buffer.hasRemaining() && isRun) {
            if(mBufferPositionFix) {
                buffer.compact();
                buffer.flip();
            }
            int write = mSocketChannel.write(buffer);
            if(write == 0) {
                break;
            }
        }
        buffer.compact();

        return buffer.position() == 0;
    }

    private void writeOperations() throws IOException {
        synchronized (mWriteLock) {
            boolean write;
            try {
                write = writeToSocket();
            } catch (IOException e) {
                MLog.e(TAG, e.getMessage());
                handleException(TcpExceptionHandler.SOCKET_WRITE_EXCEPTION, e);
                throw e;
            }

            if(write) {
                mSelectionKey.interestOps(SelectionKey.OP_READ);
            }
        }
    }

    private synchronized TlvSignal read(SocketChannel socketChannel) throws Exception {
        if (mHeader == null) {
            if (mReadBuffer.remaining() < TlvAccessHeader.HEADER_LENGTH) {
                mReadBuffer.compact();
                int readBytes;
                try {
                    readBytes = socketChannel.read(mReadBuffer);
                } catch (IOException e) {
                    MLog.e(TAG, e.getMessage());
                    handleException(TcpExceptionHandler.SOCKET_READ_EXCEPTION, e);
                    throw e;
                }
                if (readBytes == -1) {
                    SocketException e = new SocketException("Connection closed");
                    handleException(TcpExceptionHandler.SOCKET_READ_NEGATIVE, e);
                    throw e;
                }

                mReadBuffer.flip();
                if (mReadBuffer.remaining() < TlvAccessHeader.HEADER_LENGTH) {
                    MLog.i(TAG, "mReadBuffer.remaining() < TlvAccessHeader.HEADER_LENGTH");
                    return null;
                }
            }

            byte[] headerData = new byte[TlvAccessHeader.HEADER_LENGTH];
            mReadBuffer.get(headerData);
            try {
                mHeader = TlvCodecUtil.decodeHeader(TlvAccessHeader.class,
                        headerData, YayaTlvStore.getInstance());
            } catch (Exception e) {
                MLog.e(TAG, "" + e.getMessage());
                handleException(TcpExceptionHandler.TLV_DECODE_HEADER_EXCEPTION, e);
                throw e;
            }

        }

        int bodyLength = mHeader.getLength() - TlvAccessHeader.HEADER_LENGTH;

        if(bodyLength > mReadBuffer.capacity()) {
            //readBuffer容量设置不足,进入这里已经是异常状态
            MLog.e(TAG, "!!!!!!!TLV Body is too big!!!!!!!!!");
            MLog.e(TAG, mHeader.toString());
            UnknownSignalException e = new UnknownSignalException("TLV Body is too big");
            handleException(TcpExceptionHandler.SOCKET_READ_EXCEPTION, e);
            throw e;
        }

        if(bodyLength < 0) {
            MLog.e(TAG, "!!!!!!TLV Body Negative!!!!!!!!!");
            MLog.e(TAG, mHeader.toString());
            UnknownSignalException e = new UnknownSignalException("Negative TLV Body");
            handleException(TcpExceptionHandler.SOCKET_READ_EXCEPTION, e);
            throw e;
        }

        if(mReadBuffer.remaining() < bodyLength) {
            // Fill the tcp input stream
            mReadBuffer.compact();
            int readBytes;
            try {
                readBytes = socketChannel.read(mReadBuffer);
            } catch (IOException e) {
                MLog.e(TAG, e.getMessage());
                handleException(TcpExceptionHandler.SOCKET_READ_EXCEPTION, e);
                throw e;
            }
            mReadBuffer.flip();
            if(readBytes == -1) {
                SocketException e = new SocketException("Connection closed");
                handleException(TcpExceptionHandler.SOCKET_READ_NEGATIVE, e);
                throw e;
            }
            if(mReadBuffer.remaining() < bodyLength) {
                return null;
            }
        }
        byte[] bodyData = new byte[bodyLength];
        mReadBuffer.get(bodyData);

        if(mHeader.getCompresed() == COMPRESED) {
            if(bodyData.length > 0) {
                try {
                    bodyData = GZipUtil.decompress(bodyData);
                } catch (Exception e) {
                    MLog.e(TAG, "" + e.getMessage());
                    handleException(TcpExceptionHandler.TLV_BODY_DECOMPRESS_EXCEPTION, e);
                    throw e;
                }
            }
        }

        TlvSignal tlvSignal = null;
        try {
            tlvSignal = TlvCodecUtil.decodeTlvSignal(mHeader,
                    bodyData, YayaTlvStore.getInstance());
        } catch (Exception e) {
            MLog.e(TAG, "" + e.getMessage());
            handleException(TcpExceptionHandler.TLV_SIGNAL_DECODE_EXCEPTION, e);
        }

        if(tlvSignal == null) {
            MLog.e(TAG, "!!!!!!!!! not implement msgCode = " + mHeader.getMsgCode());
            UnknownSignalException signalException = new UnknownSignalException(
                    "TlvSignal isn't implement. MsgCode=" + mHeader.getMsgCode());
            mHeader = null;
            handleException(TcpExceptionHandler.TLV_NOT_IMPLEMENT, signalException);
            throw signalException;
        }

        //// TODO: 2016/11/7 这里可优化？
        TlvAccessHeader header = new TlvAccessHeader();
        header.setCompresed(mHeader.getCompresed());
        header.setEncrypted(mHeader.getEncrypted());
        header.setEsbAddr(mHeader.getEsbAddr());
        header.setLength(mHeader.getLength());
        header.setVersion(mHeader.getVersion());
        header.setMsgCode(mHeader.getMsgCode());
        header.setTag(mHeader.getTag());
        header.setMessageId(mHeader.getMessageId());

        tlvSignal.setHeader(header);

        mHeader = null;
        return tlvSignal;
    }

    private void handleException(int code, Exception e) {
        MLog.w(TAG, "handleException " + code + ","
                + e.getClass().getSimpleName() + ":" + e.getMessage());
        if(mExceptionHandler != null) {
            mExceptionHandler.onTcpException(code, e);
        }
    }
}
