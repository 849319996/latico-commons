package com.latico.commons.common.util.other;

import com.latico.commons.common.util.math.NumberUtils;
import com.latico.commons.common.util.thread.ThreadUtils;
import com.latico.commons.common.util.time.DateTimeUtils;

import java.util.UUID;

/**
 * <PRE>
 *  唯一性ID生成器工具.
 * </PRE>
 * @author: latico
 * @date: 2018/12/16 02:57:38
 * @version: 1.0
 */
public class IDUtils {

	private final static byte[] LOCK_SECOND_ID = new byte[1];
	private static volatile int LAST_SECOND_ID = -1;
	
	private final static byte[] LOCK_MILLIS_ID = new byte[1];
	private static volatile long LAST_MILLIS_ID = -1L;
	
	private final static byte[] LOCK_TIME_ID = new byte[1];
	private static volatile long LAST_TIME_ID = -1L;
	
	protected IDUtils() {}
	
	/**
	 * UUID的默认值，获取时空唯一性ID
	 * @return UUID
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 获取UUID，去掉-的纯字母形式，32位长度
	 * @return
	 */
	public static final String getUUID32(){
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 默认值，36位长度
	 * @return
	 */
	public static final String getUUID36(){
		return UUID.randomUUID().toString();
	}

	/**
	 * 去掉-后的前16位
	 * @return
	 */
	public static final String getUUID16(){
		return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
	}

	/**
	 * 去掉-后的前8位
	 * @return
	 */
	public static final String getUUID8(){
		return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
	}

	/**
	 * 去掉-后的前4位
	 * @return
	 */
	public static final String getUUID4(){
		return UUID.randomUUID().toString().replace("-", "").substring(0, 4);
	}


	/**
	 * <PRE>
	 * 获取时间序唯一性ID（秒级）
	 *  当频繁获取ID时，此方法会强制使得每次请求最多延迟1s以保证唯一性
	 * </PRE>
	 * @return 时间序唯一性ID（秒级）
	 */
	public static int getSecondID() {
		int id = -1;
		synchronized (LOCK_SECOND_ID) {
			do {
				id = (int) (System.currentTimeMillis() / 1000);
				if(LAST_SECOND_ID != id) {
					break;
				}
				ThreadUtils.sleep(500);
			} while(true);
			LAST_SECOND_ID = id;
		}
		return id;
	}
	
	/**
	 * 获取时间序唯一性ID（毫秒级）
	 * @return 时间序唯一性ID（毫秒级）
	 */
	public static long getMillisID() {
		long id = -1;
		synchronized (LOCK_MILLIS_ID) {
			do {
				id = System.currentTimeMillis();
			} while(LAST_MILLIS_ID == id);
			LAST_MILLIS_ID = id;
		}
		return id;
	}
	
	/**
	 * 获取时间序唯一性ID（yyyyMMddHHmmssSSS）
	 * @return 时间序唯一性ID（毫秒级）
	 */
	public static long getTimeID() {
		long id = -1;
		synchronized (LOCK_TIME_ID) {
			do {
				id = NumberUtils.toLong(DateTimeUtils.getSystemDate("yyyyMMddHHmmssSSS"));
			} while(LAST_TIME_ID == id);
			LAST_TIME_ID = id;
		}
		return id;
	}
	
}
