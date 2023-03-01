package kr.co.pulmuone.v1.comm.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class DateUtil {

	public static String TYPE_DATE = "yyyyMMdd";
	public static String TYPE_TIME = "HHmmss";
	public static String TYPE_DATETIME = "yyyyMMddHHmmss";
	public static String TYPE_YEAR = "yyyy";
	public static String TYPE_MONTH = "MM";
	public static String TYPE_DATE_HOUR = "yyyyMMddHH";
	public static String TYPE_DATE_HOUR_MIN = "yyyyMMddHHmm";

	/**
	 * 현재시간을 문자열로 반환
	 * @param type
	 * @return
	 */
	public static String getDate(String type){
		DateFormat df = new SimpleDateFormat(type);
		return df.format(Calendar.getInstance().getTime());
	}
	public static String getDateTime(){
		return getDate(TYPE_DATETIME);
	}
	public static String getDate(){
		return getDate(TYPE_DATE);
	}
	public static String getTime(){
		return getDate(TYPE_TIME);
	}

	/**
	 * 오늘날짜(yyyyMMdd)를 리턴해준다.
	 * @return 오늘날짜(yyyyMMdd)
	 */
	public static String getCurrentDate() {
		return getCurrentDate( "yyyyMMdd" );
	}
	/**
	 * 오늘 날짜를 포멧에 맞추어 리턴한다.
	 * @param format
	 * @return (String) 포멧에 맞추어진 날짜  문자열
	 */
	public static String getCurrentDate( String format ) {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat( format );
		//SimpleDateFormat sdf = new SimpleDateFormat( format, Locale.getDefault());
		return sdf.format( d );
	}

	/**
	 * 날짜를 리턴해준다.
	 * @param day
	 * @param format
	 * @return
	 */
    public static String getDate( int day, String format ) {
		Calendar cal = Calendar.getInstance(new SimpleTimeZone(0x1ee6280, "KST"));
		cal.add(Calendar.DATE, day);
		java.util.Date weekago = cal.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
		return formatter.format(weekago);
   }

	/**
	 * 날짜 문자열을 데이터 타입으로 리턴한다.
	 * @param (String) s (ex:20161110)
	 * @param (String) format(ex:yyyyMMdd)
	 * @return (Date) 날짜 (ex:Fri Dec 30 00:00:00 KST 2016)
	 */
	public static Date string2Date( String s, String format ) {
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat( format );
		date = sdf.parse( s, new ParsePosition(0) );
		return date;
	}
	/**
	 * 신규 날짜 포멧에 맞추어 날짜를 리턴한다.
	 * @param (String) date (ex:20161110)
	 * @param (String) oldFormat(ex:yyyyMMdd)
	 * @param (String) newFormat(ex:yyyy-MM-dd)
	 * @return (String) 신규포멧에 맞추어진 날짜  문자열
	 */
	public static String convertFormat( String date, String oldFormat, String newFormat ){

		if( "".equals(StringUtil.nvl(date)) ) return "";

		String rtnDate = "";

		try{
			Date cDate = string2Date( date, oldFormat );
			SimpleDateFormat sdf = new SimpleDateFormat( newFormat );
			rtnDate = sdf.format( cDate );
		}catch(Exception e){
			StringBuffer sb = new StringBuffer();
			sb.append( "Exception [S]----------------------------------------------------------------------\n" );
			sb.append( "date : " + date + "format : " + oldFormat + ", " + newFormat + "\n" );
			sb.append( "Exception [E]----------------------------------------------------------------------\n" );

			System.out.println( sb.toString() );

			rtnDate = date;
		}

		return rtnDate;


	}
	/**
	 * 날짜의 요일 언어타입에 따라 문자열을 리턴해준다.
	 * @param (String) day (ex:20161201)
	 * @param (String) LangTp (ex:ENG, KOR)
	 * @return (String) 요일
	 */
	public static String getWeekDay(String day, String LangTp) {
		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.YEAR, Integer.parseInt(day.substring(0,4)));
		cal.set(Calendar.MONTH, Integer.parseInt(day.substring(4,6)) -1);
		cal.set(Calendar.DATE, Integer.parseInt(day.substring(6,8)));

		String weekday = null;

		if( "ENG".equals(StringUtil.nvl(LangTp)) ){
			String[] dayOfWeek = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
			weekday = dayOfWeek[cal.get(Calendar.DAY_OF_WEEK)-1];
		}else{
			String[] dayOfWeek = {"일","월","화","수","목","금","토"};
			weekday = dayOfWeek[cal.get(Calendar.DAY_OF_WEEK)-1];
		}

		return weekday ;
	}
	/**
	 * 시분초 문자열에 구분자를 추가하여 문자열을 리턴한다.
	 * @param (String) str - 시분초
	 * @param (String) ch - 구분자
	 * @return (String) 시간
	 */
	public static String getTime(String str, String ch) {

		String rtnTime = str;

		if( StringUtil.nvl(str).length() == 0 ){
			return rtnTime;
		}

		if( str.length() % 2 == 0 ){

			if( str.length() == 4 ){
				rtnTime = str.substring(0, 2) + ch + str.substring(2, 4);
			}else if( str.length() == 6 ){
				rtnTime = str.substring(0, 2) + ch + str.substring(2, 4) + ch + str.substring(4, 6);
			}
		}

		return rtnTime ;
	}


	/**
	 * <pre>
	 * 	입력 날짜에 입력숫자만큼 더한 날짜를 지정패턴으로 리턴한다
	 *  ex) addDays("20170523", 3, "yyyy/MM/dd") => 2017/05/26
	 * </pre>
	 * @param inputDate 입력 날짜("yyyyMMdd")
	 * @param addDay 더해줄 일자
	 * @param pattern 지정패턴
	 * @return String 지정형식의 결과 날짜
	 * @throws Exception
	 */
	public static String addDays(String inputDate, int addDay, String pattern) throws Exception{

	    SimpleDateFormat inputFormatter = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
	    SimpleDateFormat outputFormatter = new SimpleDateFormat(pattern, Locale.KOREA);
	    Date date = inputFormatter.parse(inputDate);

	    // 하루는 (1/1000초*60초*60분*24시)
	    date.setTime(date.getTime() + ((long)addDay * 1000 * 60 * 60 * 24));

	    // Date형을 String형으로
	    return outputFormatter.format(date);
	}

	public static long getTermTime(String start, String end) {
		Calendar cal01 = Calendar.getInstance();
		Calendar cal02 = Calendar.getInstance();

		cal01.set(
			Integer.parseInt(start.substring(0,4)),
			Integer.parseInt(start.substring(4,6)),
			Integer.parseInt(start.substring(6,8)),
			Integer.parseInt(start.substring(8,10)),
			Integer.parseInt(start.substring(10,12)),
			Integer.parseInt(start.substring(12,14))
		);
		cal02.set(
			Integer.parseInt(end.substring(0,4)),
			Integer.parseInt(end.substring(4,6)),
			Integer.parseInt(end.substring(6,8)),
			Integer.parseInt(end.substring(8,10)),
			Integer.parseInt(end.substring(10,12)),
			Integer.parseInt(end.substring(12,14))
		);

		long time = (cal02.getTime().getTime() - cal01.getTime().getTime())/1000;

		return time;
	}

	public static long getBetweenDays(String targetDate){
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate now = LocalDate.now();
		LocalDate endDate = LocalDate.parse(targetDate, dateFormatter);

		return ChronoUnit.DAYS.between(now, endDate);
	}

	public static String convertFormatNew( String date, String oldFormat, String newFormat ){
		DateTimeFormatter oldFormatter = DateTimeFormatter.ofPattern(oldFormat);
		DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern(newFormat);
		LocalDate localDateTime = LocalDate.parse(date, oldFormatter);
		return localDateTime.format(newFormatter);
	}

	public static boolean validationDate(String checkDate) {

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			dateFormat.setLenient(false);
			dateFormat.parse(checkDate);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	public static String getLastDaysFormat(LocalDate localDate, DateTimeFormatter formatter) {
		YearMonth month = YearMonth.from(localDate);
		return month.atEndOfMonth().format(formatter);
	}
	
	/**
	 * 날짜 계산
	 * Unhandled Exception
	 * addDays메서드는 Exception이 필요함
	 */
	public static String getAddDayOfDate(String base_date, int interval) {
		return getAddDayOfDate(base_date, interval, "yyyyMMdd");
	}
	
	public static String getAddDayOfDate(String base_date, int interval, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		
		LocalDate beginDate = LocalDate.parse(base_date, formatter);
		
		return beginDate.plusDays(interval).format(formatter);
	}
}