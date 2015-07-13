package cn.cemobile.infointerface.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	public static String formatDate(Date date, String pattern) {
		String result = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		result = simpleDateFormat.format(date);
		return result;
	}
	
}
