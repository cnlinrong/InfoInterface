package cn.cemobile.infointerface.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import cn.cemobile.infointerface.APP;

public class HeadUtil {
	
	private static SharedPreferences user_sp = APP.getInstance().getSharedPreferences("user_sp", Context.MODE_PRIVATE);

	public static String getMac() {
		String macSerial = null;
		String str = "";
		try {
			Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);

			for (; null != str;) {
				str = input.readLine();
				if (str != null) {
					macSerial = str.trim();// 去空格
					break;
				}
			}
		} catch (IOException ex) {
			// 赋予默认值
			ex.printStackTrace();
		}
		return macSerial;
	}

	public static String getDeviceId() {
		TelephonyManager telephonemanage = (TelephonyManager) APP.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
		return telephonemanage.getDeviceId();

	}

	@SuppressLint("NewApi")
	public static String getResolution() {
		// TODO Auto-generated method stub
		WindowManager wm = (WindowManager) APP.getInstance().getSystemService(Context.WINDOW_SERVICE);
		Point size = new Point();
		Display display = wm.getDefaultDisplay();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		return width + "*" + height;
	}

	public static String getUserId() {
		return user_sp.getString("userid", "");
	}

	public static String getLawyerId() {
		return user_sp.getString("lawerid", "");
	}

	public static String getUserType() {
		if ("".equals(user_sp.getString("userid", ""))) {
			return 2 + "";
		} else {
			return 1 + "";
		}
	}

	public static String getSession() {
		Log.e("Session", user_sp.getString("Session", ""));
		return user_sp.getString("Session", "");

	}

	public static void setSession(String Session) {

		user_sp.edit().putString("Session", Session).commit();
		Log.e("Session", user_sp.getString("Session", ""));
	}

}
