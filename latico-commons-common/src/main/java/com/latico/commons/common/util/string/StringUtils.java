package com.latico.commons.common.util.string;

import com.latico.commons.common.envm.LineSeparator;
import com.latico.commons.common.util.collections.CollectionUtils;
import com.latico.commons.common.util.conversion.EscUtils;
import com.latico.commons.common.util.reflect.ObjectUtils;
import com.latico.commons.common.util.regex.RegexUtils;
import com.latico.commons.common.util.verify.VerifyUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;
import java.util.regex.Pattern;

/**
 * <PRE>
 *  字符串处理工具，集成了apache的进行扩展
 * </PRE>
 * @Author: LanDingDong
 * @Date: 2019-06-27 11:50:44
 * @Version: 1.0
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

	/** 全角字符集 */
	private final static char[] FULL_WIDTH_CH = {
			'０', '１', '２', '３' , '４', '５' , '６', '７', '８', '９', 
			'Ａ', 'Ｂ', 'Ｃ', 'Ｄ', 'Ｅ' , 'Ｆ', 'Ｇ' , 'Ｈ', 'Ｉ', 'Ｊ', 'Ｋ', 
			'Ｌ', 'Ｍ', 'Ｎ', 'Ｏ', 'Ｐ' , 'Ｑ', 'Ｒ' , 'Ｓ', 'Ｔ', 'Ｕ', 'Ｖ', 
			'Ｗ', 'Ｘ', 'Ｙ', 'Ｚ', 'ａ' , 'ｂ', 'ｃ' , 'ｄ', 'ｅ', 'ｆ', 'ｇ', 
			'ｈ', 'ｉ', 'ｊ', 'ｋ', 'ｌ' , 'ｍ', 'ｎ' , 'ｏ', 'ｐ', 'ｑ', 'ｒ', 
			'ｓ', 'ｔ', 'ｕ', 'ｖ', 'ｗ' , 'ｘ', 'ｙ' , 'ｚ', '：', '；', '，', 
			'（', '）', '【', '】', '｛', '｝', '‘', '’', '“', '”', 
			'＜', '＞', '《', '》', '？', '。'
	};
	
	/** 半角字符集 */
	private final static char[] HALF_WIDTH_CH = {
			'0', '1', '2', '3' , '4', '5' , '6', '7', '8', '9', 
			'A', 'B', 'C', 'D', 'E' , 'F', 'G' , 'H', 'I', 'J', 'K', 
			'L', 'M', 'N', 'O', 'P' , 'Q', 'R' , 'S', 'T', 'U', 'V', 
			'W', 'X', 'Y', 'Z', 'a' , 'b', 'c' , 'd', 'e', 'f', 'g', 
			'h', 'i', 'j', 'k', 'l' , 'm', 'n' , 'o', 'p', 'q', 'r', 
			's', 't', 'u', 'v', 'w' , 'x', 'y' , 'z', ':', ';', ',', 
			'(', ')', '[', ']', '{', '}', '\'', '\'', '\"', '\"', 
			'<', '>', '<', '>', '?', '.'
	};
	
	/**
	 * 判断字符串集中所有字符串是否均相同
	 * @param strs 字符串集
	 * @return true:所有字符串均相同; false:存在差异的字符串 或 字符串集数量<=1
	 */
	public static boolean equalsAll(String... strs) {
		boolean isEquals = true;
		if(strs == null || strs.length <= 1) {
			isEquals = false;
			
		} else {
			String s = strs[0];
			if(s == null) {
				for(String str : strs) {
					isEquals &= (str == null);
					if(!isEquals) {
						break;
					}
				}
				
			} else {
				for(String str : strs) {
					isEquals &= (s.equals(str));
					if(!isEquals) {
						break;
					}
				}
			}
		}
		return isEquals;
	}
	
	/**
	 * 令字符串中的所有空字符可视
	 * @param str 字符串
	 * @return 所含空字符变成可视字符的字符串
	 */
	public static String view(final String str) {
		StringBuilder sb = new StringBuilder();
		char[] cs = str.toCharArray();
		for(char c : cs) {
			sb.append(view(c));
		}
		return sb.toString();
	}
	
	/**
	 * 令空字符变成可视字符串
	 * @param emptyChar 空字符
	 * @return 可视字符
	 */
	public static String view(final char emptyChar) {
		String str = null;
		switch(emptyChar) {
			case '\\' : { str = "\\\\"; break; } 
			case '\t' : { str = "\\t"; break; } 
			case '\r' : { str = "\\r"; break; } 
			case '\n' : { str = "\\n"; break; } 
			case '\0' : { str = "\\0"; break; } 
			case '\b' : { str = "\\b"; break; } 
			default : { str = String.valueOf(emptyChar); }
		}
		return str;
	}
	
	
	/**
	 * 移除字符串中的所有空字符
	 * @param str 原字符串
	 * @return 移除空字符后的字符串
	 */
	public static String removeAllBlank(final String str) {
		return (str == null ? "" : str.replaceAll("\\s", ""));
	}
	
	/**
	 * <pre>
	 * 对字符串进行固定长度断行.
	 * ---------------------------
	 *  如原字符串为: ABCDEFGHIJK
	 *  若断行长度为: 3
	 *  则断行后的字符串为：
	 *  		  ABC
	 *  		  DEF
	 *  		  GHI
	 *  		  JK
	 * </pre>
	 * @param str 原字符串
	 * @param len 断行长度
	 * @return 断行后的字符串
	 */
	public static String breakLine(String str, int len) {
		return fill(str, len, LineSeparator.CRLF);
	}
	
	/**
	 * 在字符串中，每隔特定距离填充另1个字符串
	 * @param str 原字符串
	 * @param interval 填充间隔
	 * @param fillStr 填充字符串
	 * @return 填充后的字符串
	 */
	public static String fill(String str, int interval, String fillStr) {
		StringBuilder sb = new StringBuilder();
		if(str != null) {
			if(interval <= 0) {
				sb.append(str);
				
			} else {
				int cnt = 0;
				char[] chs = str.toCharArray();
				for(char ch : chs) {
					sb.append(ch);
					if(++cnt >= interval) {
						cnt = 0;
						sb.append(fillStr);
					}
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * <PRE>
	 * 根据首尾切割符，切割字符串.
	 * 
	 * 	如: s = xyzAAqqqZZabcAAwwwZZrstAAeeeZZzyx
	 * 		bgnDelmiter = AA
	 * 		endDelmiter = ZZ
	 * 	则切割成 qqq、www、eee
	 * </PRE>
	 * @param s 被切割字符串
	 * @param bgnDelmiter 首切割符
	 * @param endDelmiter 尾切割符
	 * @return 切割字符串集
	 */
	public static String[] splitByStartEnd(String s, String bgnDelmiter, String endDelmiter) {
		s = (s == null ? "" : s);
		String[] ss = null;
		
		if(isNotEmpty(bgnDelmiter) && isNotEmpty(endDelmiter)) {
			String regex = join(EscUtils.toRegexESC(bgnDelmiter),
					"([\\s\\S]*?)", EscUtils.toRegexESC(endDelmiter));
			List<String> subs = RegexUtils.findBrackets(s, regex);
			ss = new String[subs.size()];
			subs.toArray(ss);
			
		} else if(isNotEmpty(bgnDelmiter)) {
			ss = s.split(bgnDelmiter);
			
		} else if(isNotEmpty(endDelmiter)) {
			ss = s.split(endDelmiter);
			
		} else {
			ss = new String[] { s };
		}
		return ss;
	}
	
	/**
	 * 使用delmiter切割字符串，并把得到的切割子串转换成clazz对象
	 * @param s 被切割字符串
	 * @param delmiter 切割符
	 * @param clazz 子串的强制转换类型
	 * @return 被切割对象集
	 */
	public static Object[] splitAndConvertToObject(String s, String delmiter, Class<?>[] clazz) throws Exception {
		s = (s == null ? "" : s);
		delmiter = (delmiter == null ? "" : delmiter);
		if(clazz == null) {
			return s.split(delmiter);
		}
		
		String[] ss = s.split(delmiter, clazz.length);
		Object[] os = new Object[ss.length];
		for (int i = 0; i < os.length; i++) {
			os[i] = ObjectUtils.toObj(ss[i], clazz[i]);
		}
		return os;
	}
	
	/**
	 * <PRE>
	 * 把字符串切割成不同长度的子串 (第i个子串的长度为len[i])
	 * </PRE>
	 * @param str 原字符串
	 * @param lens 每个子串的长度
	 * @return 切割后的子串
	 */
	public static String[] splitBySpecialLengths(final String str, final int... lens) {
		if(lens == null) {
			return (str == null ? new String[] {""} : new String[] {str});
		}
		
		String s = (str == null ? "" : str);
		String[] sAry = new String[lens.length];
		int sLen = s.length();
		
		for(int i = 0; i < lens.length; i++) {
			int len = lens[i];
			
			if(sLen >= len) {
				sAry[i] = s.substring(0, len);
				s = s.substring(len);
				sLen = s.length();
				
			} else {
				sAry[i] = s.substring(0, sLen);
				s = "";
				sLen = 0;
			}
		}
		return sAry;
	}
	

	/**
	 * 在字符串s中截取第amount次出现的mark（不包括）之前的子串
	 * @param s 原字符串
	 * @param mark 标记字符串
	 * @param amount 标记字符串出现次数
	 * @return 子串
	 */
	public static String substrMarkBeforeNotContain(String s, String mark, int amount) {
		String sub = (s == null ? "" : s);
		if(isNotEmpty(s) && isNotEmpty(mark) && amount > 0) {
			int len = mark.length();
			int sumIdx = 0;
			int subIdx = -1;
			int cnt = 0;
			do {
				subIdx = sub.indexOf(mark);
				if(subIdx < 0) {
					break;
				}
				
				sumIdx += (subIdx + len);
				sub = sub.substring(subIdx + len);
			} while(++cnt < amount);
			sub = (subIdx >= 0 ? s.substring(0, sumIdx - 1) : s);
		}
		return sub;
	}
	
	/**
	 * <PRE>
	 * 截取字符串摘要.
	 * 	若字符串长度超过128字符，则截取前128个字符，并在末尾补省略号[...]
	 * </PRE>
	 * @param str 原字符串
	 * @return 字符串摘要
	 */
	public static String abbreviate(String str) {
		return abbreviate(str, 128);
	}
	
	/**
	 * 获取字符串中的中文个数
	 * @param s 原字符串
	 * @return 中文个数
	 */
	public static int countCn(final String s) {
		int cnt = 0;
		if(s != null) {
			char[] cs = s.toCharArray();
			for(char c : cs) {
				cnt += (VerifyUtils.isChinese(c) ? 1 : 0);
			}
		}
		return cnt;
	}
	
	/**
	 * 检查字符串中是否包含了中文
	 * @param s 原字符串
	 * @return true:含中文; false:不含
	 */
	public static boolean containsCn(final String s) {
		boolean isContains = false;
		if(s != null) {
			char[] cs = s.toCharArray();
			for(char c : cs) {
				if(VerifyUtils.isChinese(c)) {
					isContains = true;
					break;
				}
			}
		}
		return isContains;
	}
	
	/**
	 * <PRE>
	 * 计算字符串的中文长度.
	 * 	（默认情况下java的中文字符和英文字符均占长度为1字符，此方法以 [1中文长度=2英文长度] 换算字符串长度）
	 * </PRE>
	 * @param s 原字符串
	 * @return 中文长度
	 */
	public static int strWithCnLen(final String s) {
		int len = 0;
		if(s != null) {
			len = s.length() + countCn(s);
		}
		return len;
	}
	
	/**
	 * 把字符串中的 [全角/中文字符] 转换成 [半角/英文字符]
	 * @param fullWidth 含 [全角/中文字符] 的字符串
	 * @return 含 [半角/英文字符] 的字符串
	 */
	public static String toHalfWidth(final String fullWidth) {
		String halfWidth = "";
		if(fullWidth != null) {
			halfWidth = fullWidth;
			int size = FULL_WIDTH_CH.length;
			for(int i = 0; i < size; i++) {
				halfWidth = halfWidth.replace(
						FULL_WIDTH_CH[i], HALF_WIDTH_CH[i]);
			}
		}
		return halfWidth;
	}
	
	/**
	 * 检查变量的值是否在指定的范围数组内
	 * @param variable 变量（允许为null值）
	 * @param ranges 范围数组
	 * @return true:在范围内; false:在范围外
	 */
	public static boolean inRange(String variable, String... ranges) {
		boolean inRange = false;
		if(ranges != null && ranges.length > 0) {
			inRange = new HashSet<String>(Arrays.asList(ranges)).
					contains(variable);
		}
		return inRange;
	}

	/**
	 * 判断输入的字符串是否是整数，包括正负整数的判别
	 *
	 * @param str
	 * @return
	 */
	public static boolean isInt(String str) {
		if (str != null) {
			try {
				Integer.parseInt(str);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}

		}
		return false;
	}

	/**
	 * 判断一个字符串是否是double类型
	 *
	 * @param str
	 * @return
	 */
	public static boolean isDouble(String str) {

		if (str != null) {
			try {
				Double.parseDouble(str);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}

		}
		return false;
	}

	/**
	 * 判断是否Long
	 * @param str 字符串
	 * @return
	 */
	public static boolean isLong(String str) {

		if (str != null) {
			try {
				Long.parseLong(str);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}

		}
		return false;
	}

	/**
	 * 是不是数值，包含整数和浮点数
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		if (isBlank(str)) {
			return false;
		}
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 使用场景，拼装SQL的时候，字符串字段的值需要用单引号包装
	 * 例子，对于一个集合装着的String，
	 * 会组拼出所有对所有元素添加头尾并用分隔符分隔的字符串
	 * 如：'abc', 'bcd', 'cdf'
	 * @param before 每个元素前面添加的内容
	 * @param behind 每个元素后面添加的内容
	 * @param separate 元素之间的分割字符
	 * @param objs 数据
	 * @return
	 */
	public static String joinWithBeforeBehindAndSeparate(String before, String behind,
														 String separate, Collection<?> objs) {
		if (objs == null) {
			return "";
		}
		if (before == null) {
			before = "";
		}
		if (behind == null) {
			behind = "";
		}
		if (separate == null) {
			separate = "";
		}
		StringBuilder sb = new StringBuilder();
		for (Object obj : objs) {
			sb.append(before).append(obj.toString()).append(behind)
					.append(separate);
		}
		String s = sb.toString();
		if (s.length() >= separate.length()) {
			s = s.substring(0, s.length() - separate.length());
		}
		return s;

	}

	/**
	 * 判断集合中是否包含元素
	 * @param objs 列表
	 * @param obj 指定元素
	 * @return
	 */
	public static boolean equals(Collection<?> objs, Object obj) {
		boolean flag = false;
		if (obj == null || objs == null) {
			return flag;
		}

		// 如果直接包含
		if (objs.contains(obj)) {
			flag = true;

		} else {
			String str = obj.toString();
			for (Object o : objs) {
				if (o == null) {
					continue;
				}
				if (str.equals(o.toString())) {
					flag = true;
					break;
				}
			}
		}

		return flag;
	}

	/**
	 * 包含或者忽略大小写比较
	 * @param objs
	 * @param obj
	 * @return
	 */
	public static boolean equalsIgnoreCase(Collection<?> objs, Object obj) {
		boolean flag = false;
		if (obj == null || objs == null) {
			return flag;
		}

		// 如果直接包含
		if (objs.contains(obj)) {
			flag = true;

		} else {
			String str = obj.toString();
			for (Object o : objs) {
				if (o == null) {
					continue;
				}
				if (str.equalsIgnoreCase(o.toString())) {
					flag = true;
					break;
				}
			}
		}

		return flag;
	}

	/**
	 * 通过换行符切割字符串
	 * @param data
	 * @return
	 */
	public static String[] splitByLine(String data) {
		if(data == null){
			return new String[]{};
		}
		return data.split("[\r\n]{1,2}");
	}

	/**
	 * 不等于
	 * @param cs1
	 * @param cs2
	 * @return
	 */
	public static boolean isNotEquals(final CharSequence cs1, final CharSequence cs2) {
		return !equals(cs1, cs2);
	}

	/**
	 * 不等于
	 * @param cs1
	 * @param cs2
	 * @return
	 */
	public static boolean isNotEqualsIgnoreCase(final CharSequence cs1, final CharSequence cs2) {
		return !equalsIgnoreCase(cs1, cs2);
	}

	/**
	 * 存在空
	 * @param objs
	 * @return
	 */
	public static boolean isAnyEmpty(final Object... objs) {
		if (ArrayUtils.isEmpty(objs)) {
			return false;
		}
		for (Object obj : objs){
			if (isEmpty(obj)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 存在空
	 * @param objs
	 * @return
	 */
	public static boolean isAnyBlank(final Object... objs) {
		if (ArrayUtils.isEmpty(objs)) {
			return false;
		}
		for (Object obj : objs){
			if (isBlank(obj)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否为空，空白字符算正常字符
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Object obj) {
		return obj == null || isEmpty(obj.toString());
	}

	/**
	 * 是否是null或者空白字符
	 * @param obj
	 * @return
	 */
	public static boolean isBlank(Object obj) {
		return obj == null || isBlank(obj.toString());
	}

	/**
	 * 全都不为空
	 * @param objs
	 * @return
	 */
	public static boolean isNoneEmpty(Object... objs) {
		return !isAnyEmpty(objs);
	}

	/**
	 * 全都不为空白
	 * @param objs
	 * @return
	 */
	public static boolean isNoneBlank(Object... objs) {
		return !isAnyBlank(objs);
	}

	public static boolean isAllEmpty(Object... objs) {
		if (ArrayUtils.isEmpty(objs)) {
			return true;
		}
		for (final Object cs : objs) {
			if (isNotEmpty(cs)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNotEmpty(final Object cs) {
		return !isEmpty(cs);
	}

	/**
	 * 通过占位符的形式替换，实现类型slf4j的日志占位符功能
	 * @param str
	 * @param placeholder 占位符
	 * @param params
	 * @return
	 */
	public static String replaceByPlaceholder(String str, String placeholder, Object... params){
		if(str != null){
			if(params == null || params.length == 0 || isEmpty(placeholder)){
				return str;
			}
			String[] arr = split(str, placeholder);
			if(arr.length == 1){
				return str;
			}

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < arr.length; i++) {
				sb.append(arr[i]);
				if (i < params.length) {
					sb.append(params[i]);
				}
			}
			return sb.toString();
		}else{
			return null;
		}
	}

	/**
	 * 解析字符串形式的表数据表达，
	 * 示例如下，调用方法的时候可以传入：
	 9008_guangtongdasha-IPRAN#show vlan
	 VLAN  Name         PvidPorts           UntagPorts          TagPorts
	 --------------------------------------------------------------------------------
	 1     VLAN0001     xgei_1/2-3,xgei_2/2
	 -3,xgei_3/3-4,xgei_
	 4/1,xgei_4/3,gei_7/
	 2-4,gei_7/6,gei_7/1
	 0,gei_7/12-24
	 1180  VLAN1180     xgei_3/1                                xgei_3/1
	 1181  VLAN1181     xgei_1/4                                xgei_1/4
	 1182  VLAN1182     gei_7/1                                 gei_7/1
	 1183  VLAN1183     gei_7/5                                 gei_7/5
	 1184  VLAN1184     xgei_4/4                                xgei_4/4
	 1185  VLAN1185     xgei_2/4                                xgei_2/4
	 1186  VLAN1186     gei_7/7                                 gei_7/7
	 1187  VLAN1187     gei_7/8                                 gei_7/8
	 1188  VLAN1188     gei_7/9                                 gei_7/9
	 1192  VLAN1192     gei_7/11                                gei_7/11
	 1216  VLAN1216                                             gei_7/15
	 1338  VLAN1338
	 2352  VLAN2352                                             xgei_2/1,xgei_4/2,s
	 martgroup2
	 2368  VLAN2368                                             gei_7/10
	 2369  VLAN2369                                             gei_7/22
	 2370  VLAN2370                                             gei_7/2
	 2936  VLAN2936                                             xgei_1/1,xgei_3/2,s
	 martgroup1
	 2937  VLAN2937                                             xgei_2/1,xgei_4/2,s
	 martgroup2
	 4072  VLAN4072     xgei_1/1,xgei_3/2,s                     xgei_1/1,xgei_3/2,s
	 martgroup1                              martgroup1
	 4073  VLAN4073     xgei_2/1,xgei_4/2,s                     xgei_2/1,xgei_4/2,s
	 martgroup2                              martgroup2


	 9008_guangtongdasha-IPRAN#show lacp counters
	 * @param tableStr 字符串
	 * @param newLineRegex 判断新的一行的正则
	 * @param checkEndRegex 校验结束行的正则
	 * @param rowLengths 每列的长度
	 * @return List每个元素是一行，数组是列对应的值
	 */
	public static List<String[]> parseStrToTable(String tableStr, String newLineRegex, String checkEndRegex, int... rowLengths) {
		List<List<String[]>> lineTemps = new ArrayList<>();

		Pattern newLinePat = Pattern.compile(newLineRegex);

		Pattern checkEndPat = null;
		if (checkEndRegex != null) {
			checkEndPat = Pattern.compile(checkEndRegex);
		}

		String[] strs = splitByLine(tableStr);


		List<String[]> lineTemp = null;
		for (String str : strs) {

			if (checkEndPat != null && lineTemp != null) {
				if (checkEndPat.matcher(str).matches()) {
					break;
				}
			}

			if (newLinePat.matcher(str).matches()) {
				lineTemp = new ArrayList<>();
				lineTemps.add(lineTemp);
			}

			if (lineTemp == null) {
				continue;
			}

			lineTemp.add(splitBySpecialLengths(str, rowLengths));

		}

		List<String[]> lines = new ArrayList<>();
		for (List<String[]> temp : lineTemps) {
			//把每组合并成真正的行
			String[] rows = new String[rowLengths.length];

			for (String[] arr : temp) {
				rows = CollectionUtils.peerAdd(rows, arr);
			}
			lines.add(rows);
		}

		return lines;
	}

	/**
	 * 创建非空数组
	 * @param len
	 * @return
	 */
	public static String[] createNotNullStrArr(int len) {
		String[] rows = new String[len];
		for (int i = 0; i < rows.length; i++) {
			rows[i] = "";
		}
		return rows;
	}

	/**
	 * 比较两个对象是否相等，为了方便适应字符串和整数对比
	 * @param obj1 字符串或者整形
	 * @param obj2 字符串或者整形
	 * @return
	 */
	public static boolean equals(Object obj1, Object obj2) {
		if (obj1 == obj2) {
			return true;
		}
		if (obj1 == null) {
			return false;
		}
		if (obj2 == null) {
			return false;
		}

		if (equals(obj1.toString(), obj2.toString())) {
			return true;
		}
		return false;
	}

}
