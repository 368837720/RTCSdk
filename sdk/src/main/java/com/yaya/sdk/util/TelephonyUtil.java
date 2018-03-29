package com.yaya.sdk.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.yaya.sdk.MLog;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by wy on 14-3-11.
 */
public class TelephonyUtil {
	private static final String TAG = "TelephonyUtil";
	public static final String CPU_TYPE_DEFAULT = "0";
	public static final String CPU_TYPE_ARM_V5 = "1";
	public static final String CPU_TYPE_ARM_V6 = "2";
	public static final String CPU_TYPE_ARM_V7 = "3";

	/**
	 * 获取操作系统类型
	 * 
	 * @return
	 */
	public static String getOsType() {
		return "1"; // 1为android
	}

	/**
	 * 获取应用版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppVersion(Context context) {
		try {
			PackageManager pm = context.getPackageManager();//
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			return pi.versionName;
		} catch (Exception e) {
			MLog.e(TAG,
					"getAppVersion failure.exception:"
							+ ExceptionUtil.getStackTrace(e));
			return null;
		}
	}

	/**
	 * 获取应用版本号
	 * 
	 * @param context
	 * @return
	 */
	public static int getAppVersionCode(Context context) {
		try {
			PackageManager pm = context.getPackageManager();//
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			return pi.versionCode;
		} catch (Exception e) {
			MLog.e(TAG,
					"getAppVersion failure.exception:"
							+ ExceptionUtil.getStackTrace(e));
			return 0;
		}
	}

	/**
	 * 获取应用环境
	 * 
	 * @param context
	 * @return
	 */
	public static String getEnvironment(Context context) {
		try {
			ApplicationInfo applicationInfo = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			return applicationInfo.metaData.getString("environment");
		} catch (Exception e) {
			MLog.e(TAG,
					"getChannelId failure.exception:"
							+ ExceptionUtil.getStackTrace(e));
			return null;
		}
	}

	/**
	 * 获取渠道编号
	 * 
	 * @return
	 */
	public static String getChannelId(Context context) {
		return null;
//		try {
//			ApplicationInfo applicationInfo = context.getPackageManager()
//					.getApplicationInfo(context.getPackageName(),
//							PackageManager.GET_META_DATA);
//			return applicationInfo.metaData.getString("channel_id");
//		} catch (Exception e) {
//			Log.e(TAG,
//					"getChannelId failure.exception:"
//							+ ExceptionUtil.getStackTrace(e));
//			return null;
//		}
	}

	/**
	 * 获取CPU类型
	 * 
	 * @return String CPU类型：{@link #CPU_TYPE_DEFAULT}, {@link #CPU_TYPE_ARM_V5},
	 *         {@link #CPU_TYPE_ARM_V6}, {@link #CPU_TYPE_ARM_V7},
	 */
	public static String getCpuType() {
		String cpuName = getCpuName();
		if (null == cpuName) {
			return CPU_TYPE_DEFAULT;
		} else if (cpuName.contains("ARMv7")) {
			return CPU_TYPE_ARM_V7;
		} else if (cpuName.contains("ARMv6")) {
			return CPU_TYPE_ARM_V6;
		} else if (cpuName.contains("ARMv5")) {
			return CPU_TYPE_ARM_V5;
		}

		return CPU_TYPE_DEFAULT;
	}

	public static String getCpuName() {
		FileReader fr = null;
		BufferedReader br = null;
		String[] array = null;
		try {
			fr = new FileReader("/proc/cpuinfo");
			br = new BufferedReader(fr);
			String text = br.readLine();
			array = text.split(":\\s+", 2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (fr != null) {
					fr.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (array != null && array.length >= 2) {
			return array[1];
		}
		return null;
	}

	/**
	 * 获取分辨率
	 * 
	 * @param context
	 * @return
	 */
	public static String getDisplay(Context context) {
		return null;
	}

	/**
	 * 获取手机型号
	 * 
	 * @return
	 */
	public static String getTelephonyModel() {
		try {
			return Build.MODEL;
		} catch (Exception e) {
			MLog.e(TAG,
					"getTelephonyModel failure.exception:"
							+ ExceptionUtil.getStackTrace(e));
			return null;
		}
	}

	/**
	 * 获取手机厂商名称
	 * 
	 * @return
	 */
	public static String getManufacturer() {
		try {
			return Build.MANUFACTURER;
		} catch (Exception e) {
			MLog.e(TAG,
					"getManufacturer failure.exception:"
							+ ExceptionUtil.getStackTrace(e));
			return null;
		}
	}

	/**
	 * 获取系统版本编号
	 * 
	 * @return
	 */
	public static int getSystemVersionCode() {
		try {
			return Build.VERSION.SDK_INT;
		} catch (Exception e) {
			MLog.e(TAG, "getSystemVersionCode failure.exception:"
					+ ExceptionUtil.getStackTrace(e));
			return -1;
		}
	}

	/**
	 * 获取系统版本名称
	 * 
	 * @return
	 */
	public static String getSystemVersionName() {
		try {
			return Build.VERSION.RELEASE;
		} catch (Exception e) {
			MLog.e(TAG, "getSystemVersionName failure.exception:"
					+ ExceptionUtil.getStackTrace(e));
			return null;
		}
	}

	/**
	 * 获取MAC地址
	 * 
	 * @param context
	 * @return
	 */
	public static String getMac(Context context) {
		// 在wifi未开启状态下，仍然可以获取MAC地址，但是IP地址必须在已连接状态下否则为0
		try {
			WifiManager wifiMgr = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = (null == wifiMgr ? null : wifiMgr
					.getConnectionInfo());
			if (null != info) {
				return info.getMacAddress();
			} else {
				return null;
			}
		} catch (Exception e) {
			MLog.e(TAG,
					"getMac failure.exception:"
							+ ExceptionUtil.getStackTrace(e));
			return null;
		}
	}

	/**
	 * 获取IMSI号
	 * 
	 * @param context
	 * @return
	 */
	public static String getImsi(Context context) {
		try {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			return tm.getSubscriberId();
		} catch (Exception e) {
			MLog.w(TAG,
					"getImsi failure.exception:"
							+ ExceptionUtil.getStackTrace(e));
			return null;
		}
	}

	/**
	 * 获取IMEI号
	 * 
	 * @param context
	 * @return
	 */
	public static String getImei(Context context) {
		try {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			return tm.getDeviceId();
		} catch (Exception e) {
			MLog.w(TAG,
					"getImei failure.exception:"
							+ ExceptionUtil.getStackTrace(e));
			return null;
		}
	}
}
