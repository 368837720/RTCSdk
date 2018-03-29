package com.yaya.sdk.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.yaya.sdk.MLog;

public class NetworkUtil {

	// 上网类型
		/** 没有网络 */
		public static final byte NETWORK_TYPE_INVALID = 0x0;
		/** wifi网络 */
		public static final byte NETWORK_TYPE_WIFI = 0x1;
		/** 3G和3G以上网络，或统称为快速网络 */
		public static final byte NETWORK_TYPE_3G = 0x2;
		/** 2G网络 */
		public static final byte NETWORK_TYPE_2G = 0x3;
		public static final byte XIAO_MI_SHARE_PC_NETWORK=9;
		public static final byte NETWORK_TYPE_4G = 0x4;

		/**
		 * 获取网络状态，wifi,3g,2g,无网络。
		 * 
		 * @param context
		 *            上下文
		 * @return byte 网络状态 {@link #NETWORK_TYPE_WIFI}, {@link #NETWORK_TYPE_3G},
		 *         {@link #NETWORK_TYPE_2G}, {@link #NETWORK_TYPE_INVALID}
		 */
		public static byte getNetWorkType(Context context) {
			byte mNetWorkType = NETWORK_TYPE_INVALID;
			if (context == null)
				return mNetWorkType;
			ConnectivityManager manager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = manager.getActiveNetworkInfo();
			
			if (networkInfo != null && networkInfo.isConnected()) {
				int nType = networkInfo.getType();
				MLog.i("NetworkUtil", nType+"");
				if (nType == ConnectivityManager.TYPE_WIFI) {
					mNetWorkType = NETWORK_TYPE_WIFI;
				} else if (nType == ConnectivityManager.TYPE_MOBILE) {
					// String proxyHost =
					// android.net.Proxy.getDefaultHost();//TextUtils.isEmpty(proxyHost)=false为wap网络
//					mNetWorkType = (isFastMobileNetwork(context) ? NETWORK_TYPE_3G
//							: NETWORK_TYPE_2G);
					int subType = networkInfo.getSubtype();
					if (subType == TelephonyManager.NETWORK_TYPE_CDMA
							|| subType == TelephonyManager.NETWORK_TYPE_GPRS
							|| subType == TelephonyManager.NETWORK_TYPE_EDGE) {
						mNetWorkType = NETWORK_TYPE_2G;
					} else if (subType == TelephonyManager.NETWORK_TYPE_UMTS
							|| subType == TelephonyManager.NETWORK_TYPE_HSDPA
							|| subType == TelephonyManager.NETWORK_TYPE_EVDO_A
							|| subType == TelephonyManager.NETWORK_TYPE_EVDO_0
							|| subType == TelephonyManager.NETWORK_TYPE_EVDO_B) {
						mNetWorkType = NETWORK_TYPE_3G;
					} else if (subType == TelephonyManager.NETWORK_TYPE_LTE) {// LTE是3g到4g的过渡，是3.9G的全球标准
						mNetWorkType = NETWORK_TYPE_4G;
					}
				}else if(nType==XIAO_MI_SHARE_PC_NETWORK){
					mNetWorkType=NETWORK_TYPE_WIFI;
				}
				
			} else {
				mNetWorkType = NETWORK_TYPE_INVALID;
				
			}

			return mNetWorkType;
		}

	    /**
	     * 检测网络是否可用
	     * @return
	     */
	    public static boolean isNetworkConnected(Context context) {
	        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo ni = cm.getActiveNetworkInfo();
	        return ni != null && ni.isConnectedOrConnecting();
	    }
	    
	    /** 
	     * 检测当的网络（WLAN、3G/2G）状态 
	     * @param context Context 
	     * @return true 表示网络可用 
	     */  
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null && info.isAvailable()) {
				// 当前网络是连接的
				if (info.getState() == NetworkInfo.State.CONNECTED
						&& (!info.getExtraInfo().contains("ssid>") && !info
								.getExtraInfo().contains("0x"))) {
					// 当前所连接的网络可用
					return true;
				}
			}
		}
		return false;
	}
	    
		/**
		 * 判断是2G网络还是3G以上网络 false:2G网络;true:3G以上网络
		 */
		private static boolean isFastMobileNetwork(Context context) {
			TelephonyManager telephonyManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			switch (telephonyManager.getNetworkType()) {
			case TelephonyManager.NETWORK_TYPE_UNKNOWN:// 0
				return false;
			case TelephonyManager.NETWORK_TYPE_GPRS:// 1
				return false; // ~ 100 kbps
			case TelephonyManager.NETWORK_TYPE_EDGE:// 2
				return false; // ~ 50-100 kbps
			case TelephonyManager.NETWORK_TYPE_UMTS:// 3
				return true; // ~ 400-7000 kbps
			case TelephonyManager.NETWORK_TYPE_CDMA:// 4
				return false; // ~ 14-64 kbps
			case TelephonyManager.NETWORK_TYPE_EVDO_0:// 5
				return true; // ~ 400-1000 kbps
			case TelephonyManager.NETWORK_TYPE_EVDO_A:// 6
				return true; // ~ 600-1400 kbps
			case TelephonyManager.NETWORK_TYPE_1xRTT:// 7
				return false; // ~ 50-100 kbps
			case TelephonyManager.NETWORK_TYPE_HSDPA:// 8
				return true; // ~ 2-14 Mbps
			case TelephonyManager.NETWORK_TYPE_HSUPA:// 9
				return true; // ~ 1-23 Mbps
			case TelephonyManager.NETWORK_TYPE_HSPA:// 10
				return true; // ~ 700-1700 kbps
			case TelephonyManager.NETWORK_TYPE_IDEN:// 11
				return false; // ~25 kbps
				// SDK4.0才支持以下接口
			case 12:// TelephonyManager.NETWORK_TYPE_EVDO_B://12
				return true; // ~ 5 Mbps
			case 13:// TelephonyManager.NETWORK_TYPE_LTE://13
				return true; // ~ 10+ Mbps
			case 14:// TelephonyManager.NETWORK_TYPE_EHRPD://14
				return true; // ~ 1-2 Mbps
			case 15:// TelephonyManager.NETWORK_TYPE_HSPAP://15
				return true; // ~ 10-20 Mbps
			default:
				return false;
			}
		}

}
