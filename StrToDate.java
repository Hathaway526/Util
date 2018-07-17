package util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StrToDate {
	public static Date strToDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}
}
