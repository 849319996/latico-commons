package com.latico.commons.common.util.time;

import com.latico.commons.common.envm.DateFormat;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.math.NumberUtils;
import com.latico.commons.common.util.string.StringUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <PRE>
 *  日期/时间工具
 * </PRE>
 * @Author: latico
 * @Date: 2018/12/16 03:03:19
 * @Version: 1.0
 */
public class DateTimeUtils extends org.apache.commons.lang3.time.DateUtils {

	/** LOG 日志工具 */
	private static final Logger LOG = LoggerFactory.getLogger(DateTimeUtils.class);
	
	/** 默认时间值 */
	private final static String DEFAULT_TIME = "0000-00-00 00:00:00.000";

	/** 默认GMT时间值 */
	private final static String DEFAULT_GMT = "Thu, 01 Jan 1970 08:00:00 GMT+08:00";

	/** 日期格式： yyyy-MM-dd HH:mm:ss */
	public final static String FORMAT_YMDHMS = DateFormat.YYYY_MM_DD_HH_MM_SS_HORIZONTAL_BAR;

	/** 日期格式： yyyy-MM-dd HH:mm:ss.SSS */
	public final static String FORMAT_YMDHMSS = DateFormat.YYYY_MM_DD_HH_MM_SS_SSS_HORIZONTAL_BAR;

	/** GMT日期格式(多用于cookie的有效时间)： EEE, dd MMM yyyy HH:mm:ss z */
	public final static String FORMAT_GMT = DateFormat.GMT;

	/** 北京时差：8小时 */
	public final static int PEKING_HOUR_OFFSET = 8;

	/** "天"换算为millis单位 */
	public final static long DAY_UNIT = 86400000L;

	/** "小时"换算为millis单位 */
	public final static long HOUR_UNIT = 3600000L;

	/** "分钟"换算为millis单位 */
	public final static long MIN_UNIT = 60000L;

	/**
	 * 今天的时间偏移量正则，能匹配的时间格式如:06:12:33、06:12:33.103
	 */
	private final static Pattern todayTimeOffsetPattern = Pattern.compile("(\\d+):(\\d+):(\\d+)\\.?(\\d*)");

	/** 私有化构造函数 */
	protected DateTimeUtils() {}

	/**
	 * 生成SimpleDateFormat对象
	 * @param format 时间格式
	 * @return SimpleDateFormat对象
	 */
	public final static SimpleDateFormat createSimpleDateFormatEnglish(String format) {

		// Locale.ENGLISH用于设定所生成的格式字符串中的符号为英文标识
		return new SimpleDateFormat(format, Locale.ENGLISH);
	}
	/**
	 * 生成SimpleDateFormat对象
	 * @param format 时间格式
	 * @return SimpleDateFormat对象
	 */
	public final static SimpleDateFormat createSimpleDateFormatChina(String format) {

		// Locale.CHINA用于设定所生成的格式字符串中的符号为中国标识
		return new SimpleDateFormat(format, Locale.CHINA);
	}

	/**
	 * 把[Date时间]转换为[UTC时间]
	 * @param date Date时间
	 * @return UTC时间(转换失败则返回0)
	 */
	public static long toUTC(Date date) {
		long millis = 0L;
		if(date == null) {
			return millis;
		}

		SimpleDateFormat sdf = createSimpleDateFormatEnglish(FORMAT_YMDHMS);
		String ymdhms = sdf.format(date);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		try {
			millis = sdf.parse(ymdhms).getTime();
		} catch (Exception e) {
			LOG.error("转换UTC时间失败.", e);
		}
		return millis;
	}

	/**
	 * 把[Date时间]转换为[cookie有效时间]
	 * @param date Date时间
	 * @return cookie有效时间(转换失败则返回默认值 Thu, 01 Jan 1970 08:00:00 GMT+08:00)
	 */
	public static String toCookieExpires(Date date) {
		String expires = DEFAULT_GMT;
		if(date != null) {
			SimpleDateFormat sdf = createSimpleDateFormatEnglish(FORMAT_GMT);
			expires = sdf.format(date);
		}
		return expires;
	}

	/**
	 * 把[millis时间]转换为[yyyy-MM-dd HH:mm:ss格式字符串]
	 * @param millis millis时间
	 * @return yyyy-MM-dd HH:mm:ss格式字符串
	 */
	public static String toStrDefault(long millis) {
		return toStr(millis, FORMAT_YMDHMS);
	}

	/**
	 * 把[millis时间]转换为指定格式字符串
	 * @param millis millis时间
	 * @param format 日期格式字符串
	 * @return 指定格式字符串
	 */
	public static String toStr(long millis, String format) {
		return toStr((millis >= 0 ? new Date(millis) : null), format);
	}

	/**
	 * 把[Date时间]转换为[yyyy-MM-dd HH:mm:ss格式字符串]
	 * @param date Date时间
	 * @return yyyy-MM-dd HH:mm:ss格式字符串
	 */
	public static String toStrDefault(Date date) {
		return toStr(date, FORMAT_YMDHMS);
	}

	/**
	 * 把[Date时间]转换为指定格式字符串
	 * @param date Date时间
	 * @param format 日期格式字符串
	 * @return 指定格式字符串
	 */
	public static String toStr(Date date, String format) {
		String sDate = DEFAULT_TIME;
		if(date != null) {
			SimpleDateFormat sdf = createSimpleDateFormatEnglish(format);
			sDate = sdf.format(date);
		}
		return sDate;
	}

	/**
	 * 获取[yyyy-MM-dd HH:mm:ss.SSS格式]的当前系统时间
	 * @return 当前系统时间
	 */
	public static String getSystemDate() {
		return getSystemDate(FORMAT_YMDHMSS);
	}

	/**
	 * 获取指定格式的当前系统时间
	 * @param format 指定日期格式
	 * @return 当前系统时间
	 */
	public static String getSystemDate(String format) {
		SimpleDateFormat sdf = createSimpleDateFormatEnglish(format);
		return sdf.format(new Date(System.currentTimeMillis()));
	}

	/**
	 * 把[yyyy-MM-dd HH:mm:ss格式字符串]转换为[Date时间]
	 * @param ymdhms yyyy-MM-dd HH:mm:ss格式字符串
	 * @return Date时间 (转换失败则返回起始时间 1970-1-1 08:00:00)
	 */
	public static Date toDateBy_ymdhms(String ymdhms) throws ParseException {
		return parseDate(ymdhms, FORMAT_YMDHMS);
	}

	/**
	 * 把[Timestamp时间]转换为[Date时间]
	 * @param timestamp Timestamp时间
	 * @return Date时间
	 */
	public static Date toDate(Timestamp timestamp) {
		return (timestamp == null ? new Date(0) : new Date(timestamp.getTime()));
	}

	/**
	 * 把[Date时间]转换为[Timestamp时间]
	 * @param date Date时间
	 * @return Timestamp时间
	 */
	public static Timestamp toTimestamp(Date date) {
		return (date == null ? new Timestamp(0) : new Timestamp(date.getTime()));
	}

	/**
	 * 把[yyyy-MM-dd HH:mm:ss格式字符串]转换为[毫秒时间]
	 * @param date 
	 * @return 毫秒时间
	 */
	public static long toMillis(Date date) {
		return (date == null ? 0 : date.getTime());
	}

	/**
	 * 把[yyyy-MM-dd HH:mm:ss格式字符串]转换为[毫秒时间]
	 * @param ymdhms yyyy-MM-dd HH:mm:ss格式字符串
	 * @return 毫秒时间
	 */
	public static long toMillisBy_ymdhms(String ymdhms) throws ParseException {
		return toMillis(toDateBy_ymdhms(ymdhms));
	}

	/**
	 * 把[format格式字符串]转换为[毫秒时间]
	 * @param sDate 时间字符串
	 * @param format 时间字符串格式
	 * @return 毫秒时间
	 */
	public static long toMillis(String sDate, String format) throws ParseException {
		return toMillis(parseDate(sDate, format));
	}

	/**
	 * 判断是否 [time<=endTime]
	 * @param time 被判定时间点
	 * @param endTime 参照时间点
	 * @return 若 [time<=endTime] 返回true; 反之返回false
	 */
	public static boolean isBefore(long time, long endTime) {
		return time <= endTime;
	}

	/**
	 * 判断是否 [time>bgnTime]
	 * @param time 被判定时间点
	 * @param bgnTime 参照时间点
	 * @return 若 [time>bgnTime] 返回true; 反之返回false
	 */
	public static boolean isAfter(long time, long bgnTime) {
		return bgnTime <= time;
	}

	/**
	 * 判断是否 [bgnTime<=time<=endTime]
	 * @param time 被判定时间点
	 * @param bgnTime 参照时间起点
	 * @param endTime 参照时间终点
	 * @return 若 [bgnTime<=time<=endTime] 返回true; 反之返回false
	 */
	public static boolean isBetween(long time, long bgnTime, long endTime) {
		return (bgnTime <= time) & (time <= endTime);
	}

	/**
	 * 获取指定时间前n个小时的时间
	 * @param time 指定时间
	 * @param hour 小时数
	 * @return 指定时间前n个小时的时间
	 */
	public static long getBeforeHour(long time, int hour) {
		return time - 3600000 * hour;
	}

	/**
	 * 获取指定时间后n个小时的时间
	 * @param time 指定时间
	 * @param hour 小时数
	 * @return 指定时间后n个小时的时间
	 */
	public static long getAfterHour(long time, int hour) {
		return time + 3600000 * hour;
	}

	/**
	 * 获取上一个正点时间
	 * @return 上一个正点时间
	 */
	public static long getLastOnTime() {
		return getCurOnTime() - 3600000;
	}

	/**
	 * 获取当前正点时间
	 * @return 当前正点时间
	 */
	public static long getCurOnTime() {
		long now = System.currentTimeMillis();
		return now - (now % 3600000);
	}

	/**
	 * 获取下一个正点时间
	 * @return 下一个正点时间
	 */
	public static long getNextOnTime() {
		return getCurOnTime() + 3600000;
	}

	/**
	 * 以当前时间为参考，获取 ±Day 的日期
	 * @param beforeOrAfterDay 正负天数
	 * @return yyyy-MM-dd HH:mm:ss型时间
	 */
	public static String getDate(int beforeOrAfterDay) {
		SimpleDateFormat sdf = createSimpleDateFormatEnglish(FORMAT_YMDHMS);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, beforeOrAfterDay);
		return sdf.format(new Date(cal.getTime().getTime()));
	}

	/**
	 * 根据计数器获取毫秒时间: 计数值越大，毫秒值越大.
	 * 毫秒时间 = 2^(cnt-1) * 1000
	 *
	 * @param cnt 计数值(1~31)
	 * @return 毫秒时间(ms).
	 */
	public static long getMillisTime(final int cnt) {
		return getMillisTime(cnt, 0, Long.MAX_VALUE);
	}

	/**
	 * 根据计数器获取毫秒时间: 计数值越大，毫秒值越大.
	 * 毫秒时间 = 2^(cnt-1) * 1000
	 *
	 * @param cnt 计数值(1~31)
	 * @param maxMillisTime 最大毫秒值(ms)
	 * @return 毫秒时间(ms).
	 */
	public static long getMillisTime(final int cnt, final long maxMillisTime) {
		return getMillisTime(cnt, 0, maxMillisTime);
	}

	/**
	 * 根据计数器获取毫秒时间: 计数值越大，毫秒值越大.
	 * 毫秒时间 = 2^(cnt-1) * 1000
	 *
	 * @param cnt 计数值(1~31)
	 * @param minMillisTime 最小毫秒值(ms)
	 * @param maxMillisTime 最大毫秒值(ms)
	 * @return 毫秒时间(ms).
	 */
	public static long getMillisTime(int cnt,
									 final long minMillisTime, final long maxMillisTime) {
		long millisTime = 0;
		if(cnt > 0) {
			cnt = (cnt > 32 ? 32 : cnt);
			millisTime = (1L << (cnt - 1));
			millisTime *= 1000;
			millisTime = NumberUtils.min(millisTime, maxMillisTime);
		}
		millisTime = NumberUtils.max(millisTime, minMillisTime);
		return millisTime;
	}

	/**
	 * 获取当前的小时值（默认为北京时间8小时时差）
	 * @return 当前小时
	 */
	public static int getCurHour() {
		return getCurHour(PEKING_HOUR_OFFSET);
	}

	/**
	 * 获取当前的小时值
	 * @param offset 时差值
	 * @return 当前小时
	 */
	public static int getCurHour(int offset) {
		long hour = ((System.currentTimeMillis() % DAY_UNIT) / HOUR_UNIT);
		hour = (hour + offset + 24) % 24;	// 时差
		return (int) hour;
	}

	/**
	 * 获取当前的分钟数
	 * @return 当前分钟数
	 */
	public static int getCurMinute() {
		return (int) (System.currentTimeMillis() % DAY_UNIT % HOUR_UNIT / MIN_UNIT);
	}

	/**
	 * 获取当天零点毫秒时间（默认为北京时间8小时时差）
	 * @return 零点毫秒时间
	 */
	public static long getZeroPointMillis() {
		return getZeroPointMillis(PEKING_HOUR_OFFSET);
	}

	/**
	 * 获取当天零点毫秒时间，指定时区的时差
	 * @param offset 时差值,比如北京是8
	 * @return 零点毫秒时间
	 */
	public static long getZeroPointMillis(int offset) {
		int curHour = getCurHour(offset);
		long zero = System.currentTimeMillis() + (curHour < offset ? DAY_UNIT : 0);
		zero = zero / DAY_UNIT * DAY_UNIT;
		zero = zero - HOUR_UNIT * offset;
		return zero;
	}

	/**
	 * 计算时间长度，用中文描述表达
	 System.out.println(DateTimeUtils.countMillisTimeDescribeCn(5000));
	 System.out.println(DateTimeUtils.countMillisTimeDescribeCn(59000));
	 System.out.println(DateTimeUtils.countMillisTimeDescribeCn(60000));
	 System.out.println(DateTimeUtils.countMillisTimeDescribeCn(61000));
	 System.out.println(DateTimeUtils.countMillisTimeDescribeCn(120000));
	 System.out.println(DateTimeUtils.countMillisTimeDescribeCn(121000));
	 System.out.println(DateTimeUtils.countMillisTimeDescribeCn(360000));
	 System.out.println(DateTimeUtils.countMillisTimeDescribeCn(3600000));
	 System.out.println(DateTimeUtils.countMillisTimeDescribeCn(3666000));
	 System.out.println(DateTimeUtils.countMillisTimeDescribeCn(3660000));
	 System.out.println(DateTimeUtils.countMillisTimeDescribeCn(36600000));
	 System.out.println(DateTimeUtils.countMillisTimeDescribeCn(86400000));
	 System.out.println(DateTimeUtils.countMillisTimeDescribeCn(86430000));
	 System.out.println(DateTimeUtils.countMillisTimeDescribeCn(96430000));

	 0天0小时0分钟5秒0毫秒
	 0天0小时0分钟59秒0毫秒
	 0天0小时1分钟0秒0毫秒
	 0天0小时1分钟1秒0毫秒
	 0天0小时2分钟0秒0毫秒
	 0天0小时2分钟1秒0毫秒
	 0天0小时6分钟0秒0毫秒
	 0天1小时0分钟0秒0毫秒
	 0天1小时1分钟6秒0毫秒
	 0天1小时1分钟0秒0毫秒
	 0天10小时10分钟0秒0毫秒
	 1天0小时0分钟0秒0毫秒
	 1天0小时0分钟30秒0毫秒
	 1天2小时47分钟10秒0毫秒
	 * @param millis 毫秒
	 * @return
	 */
	public static String countMillisTimeDescribeCn(long millis){
		final long dayCount = millis/86400000;
		long remainMillis = millis%86400000;
		final long hourCount = remainMillis/3600000;
		remainMillis = millis%3600000;
		final long minuteCount = remainMillis/60000;
		remainMillis = millis%60000;
		final long secondCount = remainMillis/1000;
		remainMillis = millis%1000;

		return StringUtils.join(dayCount, "天", hourCount, "小时", minuteCount, "分钟", secondCount, "秒", remainMillis, "毫秒");
	}
	/**
	 * 计算时间长度，用中文描述表达
	 *
	 System.out.println(DateTimeUtils.countSecondTimeDescribeCn(5));
	 System.out.println(DateTimeUtils.countSecondTimeDescribeCn(59));
	 System.out.println(DateTimeUtils.countSecondTimeDescribeCn(60));
	 System.out.println(DateTimeUtils.countSecondTimeDescribeCn(61));
	 System.out.println(DateTimeUtils.countSecondTimeDescribeCn(120));
	 System.out.println(DateTimeUtils.countSecondTimeDescribeCn(121));
	 System.out.println(DateTimeUtils.countSecondTimeDescribeCn(360));
	 System.out.println(DateTimeUtils.countSecondTimeDescribeCn(3600));
	 System.out.println(DateTimeUtils.countSecondTimeDescribeCn(3666));
	 System.out.println(DateTimeUtils.countSecondTimeDescribeCn(3660));
	 System.out.println(DateTimeUtils.countSecondTimeDescribeCn(36600));
	 System.out.println(DateTimeUtils.countSecondTimeDescribeCn(86400));
	 System.out.println(DateTimeUtils.countSecondTimeDescribeCn(86430));
	 System.out.println(DateTimeUtils.countSecondTimeDescribeCn(96430));
	 0天0小时0分钟5秒
	 0天0小时0分钟59秒
	 0天0小时1分钟0秒
	 0天0小时1分钟1秒
	 0天0小时2分钟0秒
	 0天0小时2分钟1秒
	 0天0小时6分钟0秒
	 0天1小时0分钟0秒
	 0天1小时1分钟6秒
	 0天1小时1分钟0秒
	 0天10小时10分钟0秒
	 1天0小时0分钟0秒
	 1天0小时0分钟30秒
	 1天2小时47分钟10秒
	 * @param seconds 秒
	 * @return
	 */
	public static String countSecondTimeDescribeCn(long seconds){
		final long dayCount = seconds/86400;
		long remainSeconds = seconds%86400;
		final long hourCount = remainSeconds/3600;
		remainSeconds = seconds%3600;
		final long minuteCount = remainSeconds/60;
		remainSeconds = seconds%60;

		return StringUtils.join(dayCount, "天", hourCount, "小时", minuteCount, "分钟", remainSeconds, "秒");
	}

	/**
	 * 给定今天的一个时间点时间偏移量，返回该时间点的Date形式
	 System.out.println(DateTimeUtils.parseDateAssignTodayTimeOffset("05:02:34").getTime());
	 System.out.println(DateTimeUtils.parseDateAssignTodayTimeOffset("05:02:34.100").getTime());
	 System.out.println(DateTimeUtils.parseDateAssignTodayTimeOffset("05:02:34.01").getTime());
	 System.out.println(DateTimeUtils.parseDateAssignTodayTimeOffset("05:02:34.11").getTime());
	 System.out.println(new Timestamp(DateTimeUtils.parseDateAssignTodayTimeOffset("05:02:34").getTime()));
	 System.out.println(new Timestamp(DateTimeUtils.parseDateAssignTodayTimeOffset("05:02:34.100").getTime()));
	 System.out.println(new Timestamp(DateTimeUtils.parseDateAssignTodayTimeOffset("05:02:34.01").getTime()));
	 System.out.println(new Timestamp(DateTimeUtils.parseDateAssignTodayTimeOffset("05:02:34.11").getTime()));
	 * @param todayTimeOffset 格式如:06:12:33、06:12:33.103
	 * @return
	 */
	public static Date parseDateAssignTodayTimeOffset(String todayTimeOffset) {
		Calendar cal = Calendar.getInstance();
		return parseDateAssignTodayTimeOffset(cal, todayTimeOffset);
	}

	/**
	 * 解析数据，添加偏移量
	 * @param cal 时间
	 * @param todayTimeOffset 偏移量
	 * @return
	 */
	public static Date parseDateAssignTodayTimeOffset(Calendar cal, String todayTimeOffset) {
		if(todayTimeOffset !=null){

			Matcher mat = todayTimeOffsetPattern.matcher(todayTimeOffset);
			if (mat.find()) {
				int hour = Integer.parseInt(mat.group(1));
				int minute = Integer.parseInt(mat.group(2));
				int second = Integer.parseInt(mat.group(3));
				int millis = 0;

				//对于毫秒部分需要补全到3位
				String millisStr = mat.group(4);
				if (StringUtils.isNotBlank(millisStr)) {
					millisStr = StringUtils.rightPad(millisStr, 3, "0");
					millis = Integer.parseInt(millisStr);
				}

				cal.set(Calendar.HOUR_OF_DAY, hour);
				cal.set(Calendar.MINUTE, minute);
				cal.set(Calendar.SECOND, second);
				cal.set(Calendar.MILLISECOND, millis);
			}
		}
		return cal.getTime();
	}

	/**
	 * 对一个日志加上偏移量
	 *
	 System.out.println(DateTimeUtils.parseDateAssignTodayTimeOffset(new Timestamp(Sysrem), "05:02:34").getTime());
	 System.out.println(DateTimeUtils.parseDateAssignTodayTimeOffset("05:02:34.100").getTime());
	 System.out.println(DateTimeUtils.parseDateAssignTodayTimeOffset("05:02:34.01").getTime());
	 System.out.println(DateTimeUtils.parseDateAssignTodayTimeOffset("05:02:34.11").getTime());
	 System.out.println(new Timestamp(DateTimeUtils.parseDateAssignTodayTimeOffset("05:02:34").getTime()));
	 System.out.println(new Timestamp(DateTimeUtils.parseDateAssignTodayTimeOffset("05:02:34.100").getTime()));
	 System.out.println(new Timestamp(DateTimeUtils.parseDateAssignTodayTimeOffset("05:02:34.01").getTime()));
	 System.out.println(new Timestamp(DateTimeUtils.parseDateAssignTodayTimeOffset("05:02:34.11").getTime()));

	 * @param date
	 * @param todayTimeOffset 偏移量
	 * @return
	 */
	public static Date parseDateAssignTodayTimeOffset(Date date, String todayTimeOffset) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return parseDateAssignTodayTimeOffset(cal, todayTimeOffset);
	}

	/**
	 * @return 当前年
	 */
	public static int getCurYear(){
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}

	/**
	 * @return 当前日期
	 */
	public static Date getSysDate(){
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * @return 当前timestamp
	 */
	public static Timestamp getSysTimestamp(){
		return new Timestamp(System.currentTimeMillis());
	}

}
