package com.yaya.sdk.account.store;

import android.util.Base64;

import com.yaya.sdk.MLog;
import com.yaya.sdk.config.SdkConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ober on 2016/10/28.
 */
public class YayaAccountStore implements AccountStore {

    private static final String TAG = "YayaAccountStore";

    private final String filePath;
    private final String fileName;

    public static AccountStore with(SdkConfig config) {
        return new YayaAccountStore(config);
    }

    private YayaAccountStore(SdkConfig config) {
        filePath = config.getAccessInfoPath();
        fileName = config.getAccessInfoFileName();
    }

    @Override
    public String[] getUserInfo() {
        MLog.d(TAG, "getUserInfo()");
        File infoFile = new File(filePath, fileName);
        if(!infoFile.exists()) {
            MLog.i(TAG, "not exists");
            return null;
        }
        try {
            FileInputStream fis = new FileInputStream(infoFile);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            byte[] decrypted = Base64.decode(data, Base64.DEFAULT);
            String infoStr = new String(decrypted);
            String[] infos = infoStr.split("\\|");
            if(infos.length != 2) {
                MLog.w(TAG, "bad file");
                infoFile.delete();
                return null;
            }
            String usr = infos[INDEX_USR];
            String psswd = infos[INDEX_PSSWD];
            if(usr == null || psswd == null) {
                MLog.w(TAG, "bad file");
                infoFile.delete();
                return null;
            }
            return new String[] {usr, psswd};
        } catch (IOException e) {
            MLog.e(TAG, e.getMessage());
            return null;
        }
    }

    @Override
    public void putUserInfo(String[] userInfo) {
        checkNotNull(userInfo);
        MLog.d(TAG, "putUserInfo(" + userInfo[0] + "," + userInfo[1] + ")");
        File dir = new File(filePath);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        File infoFile = new File(filePath, fileName);
        if(infoFile.exists()) {
            infoFile.delete();
        }
        String input = userInfo[INDEX_USR] + "|" + userInfo[INDEX_PSSWD];
        byte[] data = Base64.encode(input.getBytes(), Base64.DEFAULT);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(infoFile);
            fos.write(data);
            fos.flush();
            MLog.d(TAG, "put success");
        } catch (IOException e) {
            MLog.e(TAG, e.getMessage());
        } finally {
            if(fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    MLog.e(TAG, e.getMessage());
                }
            }
        }
    }

    @Override
    public void clearInfo() {
        MLog.d(TAG, "clearInfo");
        File target = new File(filePath, fileName);
        if(target.exists()) {
            target.delete();
        }
    }

    private static void checkNotNull(String[] info) {
        if(info == null) {
            throw new NullPointerException("null userInfo");
        }
        if(info.length != 2) {
            throw new IllegalArgumentException("unknown userInfo");
        }
        for(String s : info) {
            if(s == null) {
                throw new NullPointerException("null userInfo");
            }
        }
    }
}
