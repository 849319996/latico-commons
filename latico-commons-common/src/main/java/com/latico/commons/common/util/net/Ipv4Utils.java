/*
 *	This file is part of dhcp4java, a DHCP API for the Java language.
 *	(c) 2006 Stephan Hadinger
 *
 *	This library is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU Lesser General Public
 *	License as published by the Free Software Foundation; either
 *	version 2.1 of the License, or (at your option) any later version.
 *
 *	This library is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *	Lesser General Public License for more details.
 *
 *	You should have received a copySurface of the GNU Lesser General Public
 *	License along with this library; if not, write to the Free Software
 *	Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.latico.commons.common.util.net;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.string.StringUtils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <PRE>
 * IPV4工具
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-06-27 11:45:06
 * @Version: 1.0
 */
public class Ipv4Utils {

    /**
     * 32位掩码
     */
    @SuppressWarnings("serialreader")
    public static final Map<String, String> NETMASK = new HashMap<String, String>() {
        {
            put("1", "128.0.0.0");
            put("2", "192.0.0.0");
            put("3", "224.0.0.0");
            put("4", "240.0.0.0");
            put("5", "248.0.0.0");
            put("6", "252.0.0.0");
            put("7", "254.0.0.0");
            put("8", "255.0.0.0");
            put("9", "255.128.0.0");
            put("10", "255.192.0.0");
            put("11", "255.224.0.0");
            put("12", "255.240.0.0");
            put("13", "255.248.0.0");
            put("14", "255.252.0.0");
            put("15", "255.254.0.0");
            put("16", "255.255.0.0");
            put("17", "255.255.128.0");
            put("18", "255.255.192.0");
            put("19", "255.255.224.0");
            put("20", "255.255.240.0");
            put("21", "255.255.248.0");
            put("22", "255.255.252.0");
            put("23", "255.255.254.0");
            put("24", "255.255.255.0");
            put("25", "255.255.255.128");
            put("26", "255.255.255.192");
            put("27", "255.255.255.224");
            put("28", "255.255.255.240");
            put("29", "255.255.255.248");
            put("30", "255.255.255.252");
            put("31", "255.255.255.254");
            put("32", "255.255.255.255");
        }
    };
    @SuppressWarnings("serialreader")
    public static final Map<String, String> NETMASK_IP_LEN_MAP = new HashMap<String, String>() {
        {
            put("128.0.0.0", "1");
            put("192.0.0.0", "2");
            put("224.0.0.0", "3");
            put("240.0.0.0", "4");
            put("248.0.0.0", "5");
            put("252.0.0.0", "6");
            put("254.0.0.0", "7");
            put("255.0.0.0", "8");
            put("255.128.0.0", "9");
            put("255.192.0.0", "10");
            put("255.224.0.0", "11");
            put("255.240.0.0", "12");
            put("255.248.0.0", "13");
            put("255.252.0.0", "14");
            put("255.254.0.0", "15");
            put("255.255.0.0", "16");
            put("255.255.128.0", "17");
            put("255.255.192.0", "18");
            put("255.255.224.0", "19");
            put("255.255.240.0", "20");
            put("255.255.248.0", "21");
            put("255.255.252.0", "22");
            put("255.255.254.0", "23");
            put("255.255.255.0", "24");
            put("255.255.255.128", "25");
            put("255.255.255.192", "26");
            put("255.255.255.224", "27");
            put("255.255.255.240", "28");
            put("255.255.255.248", "29");
            put("255.255.255.252", "30");
            put("255.255.255.254", "31");
            put("255.255.255.255", "32");
        }
    };

    /**
     * IPV4的正则
     */
    public static final Pattern IPV4_PATTERN = Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.\\d+)");

    /**
     * IpV4的正则表达式，用于判断IpV4地址是否合法
     */
    private static final String IPV4_REGEX = "((\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})";
//    public static final String IPV4_REGEX = "\\d+\\.\\d+\\.\\d+\\.\\d+";

    /**
     * Converts 32 bits int to IPv4 <tt>InetAddress</tt>.
     *
     * @param val int representation of IPv4 address
     * @return the address object
     */
    public static final InetAddress int2InetAddress(int val) {
        byte[] value = {(byte) ((val & 0xFF000000) >>> 24),
                (byte) ((val & 0X00FF0000) >>> 16),
                (byte) ((val & 0x0000FF00) >>> 8),
                (byte) ((val & 0x000000FF))};
        try {
            return InetAddress.getByAddress(value);
        } catch (UnknownHostException e) {
            return null;
        }
    }

    /**
     * Converts 32 bits int packaged into a 64bits long to IPv4 <tt>InetAddress</tt>.
     *
     * @param val int representation of IPv4 address
     * @return the address object
     */
    public static final InetAddress long2InetAddress(long val) {
        if ((val < 0) || (val > 0xFFFFFFFFL)) {
            return int2InetAddress(0);
        }
        return int2InetAddress((int) val);
    }

    /**
     * Converts IPv4 <tt>InetAddress</tt> to 32 bits int.
     *
     * @param addr IPv4 address object
     * @return 32 bits int
     * @throws NullPointerException     <tt>addr</tt> is <tt>null</tt>.
     * @throws IllegalArgumentException the address is not IPv4 (Inet4Address).
     */
    public static final int inetAddress2Int(InetAddress addr) {
        if (!(addr instanceof Inet4Address)) {
            throw new IllegalArgumentException("Only IPv4 supported");
        }

        byte[] addrBytes = addr.getAddress();
        return ((addrBytes[0] & 0xFF) << 24) |
                ((addrBytes[1] & 0xFF) << 16) |
                ((addrBytes[2] & 0xFF) << 8) |
                ((addrBytes[3] & 0xFF));
    }

    /**
     * Converts IPv4 <tt>InetAddress</tt> to 32 bits int, packages into a 64 bits <tt>long</tt>.
     *
     * @param addr IPv4 address object
     * @return 32 bits int
     * @throws NullPointerException     <tt>addr</tt> is <tt>null</tt>.
     * @throws IllegalArgumentException the address is not IPv4 (Inet4Address).
     */
    public static final long inetAddress2Long(InetAddress addr) {
        return (inetAddress2Int(addr) & 0xFFFFFFFFL);
    }

    public static InetAddress getInetAddress(String ip) {
        try {
            if (StringUtils.isEmpty(ip)) {
                return null;
            }
            return InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace(System.err);
        }
        return null;
    }

    /**
     * 返回IP，如果是InetAddress的toString是返回/127.0.0.1,如果用了getHostAddress()就返回纯IP值串
     *
     * @param ip
     * @return
     */
    public static String getIpStr(InetAddress ip) {
        return ip.getHostAddress();
    }

    public static byte[] ip2Byte(String ip) throws UnknownHostException {
        return InetAddress.getByName(ip).getAddress();
    }

    /**
     * Helper methods to convert 4 bytes into one integer.
     *
     * @param buffer the bytes to convert
     * @return an integer representing the 4 bytes
     * @see #intToByte(int)
     */
    public static int byteToInt(byte[] buffer) {
        if (buffer.length != 4) {
            throw new IllegalArgumentException(
                    "buffer length must be 4 bytes!");
        }

        int value = (0xFF & buffer[0]) << 24;
        value |= (0xFF & buffer[1]) << 16;
        value |= (0xFF & buffer[2]) << 8;
        value |= (0xFF & buffer[3]);

        return value;
    }

    public static int ipIntArrToInt(int[] buffer) {
        if (buffer.length != 4) {
            throw new IllegalArgumentException(
                    "buffer length must be 4 bytes!");
        }

        int value = (0xFF & buffer[0]) << 24;
        value |= (0xFF & buffer[1]) << 16;
        value |= (0xFF & buffer[2]) << 8;
        value |= (0xFF & buffer[3]);

        return value;
    }

    /**
     * Method to convert 4 bytes to a textual ip adress representation.
     *
     * @param b the bytes to convert
     * @return the bytes as an ip adress
     */
    public static String byteToIp(byte[] b) {
        StringBuilder back = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            if (i > 0) {
                back.append(".");
            }
            String neu = String.valueOf(0xFF & b[i]);

            back.append(neu);
        }

        return back.toString();
    }

    /**
     * 把整形IP转成字符串形式
     * @param ip
     * @return
     */
    public static String intIpToStrIp(int ip) {
        return byteToIp(intToByte(ip));
    }

    public static InetAddress byteToAddr(byte[] ip) {
        return getInetAddress(byteToIp(ip));
    }

    /**
     * Helper method to convert 6 bytes to a textual mac adress representation.
     *
     * @param b the bytes to convert
     * @return the bytes as a mac adress
     */
    public static String byteToMac(byte[] b) {
        StringBuilder back = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            if (i > 0) {
                back.append(":");
            }
            String neu = Integer.toHexString(0xFF & b[i]);
            if (neu.length() == 1) {
                neu = "0" + neu;
            }

            back.append(neu);
        }

        return back.toString();
    }

    /**
     * Splits the given int into 4 bytes
     *
     * @param val the int to convert
     * @return a byte array containing 4 elements
     * @see #byteToInt(byte[])
     */
    public static byte[] intToByte(int val) {
        byte[] buffer = new byte[4];

        buffer[0] = (byte) (val >>> 24);
        buffer[1] = (byte) (val >>> 16);
        buffer[2] = (byte) (val >>> 8);
        buffer[3] = (byte) val;

        return buffer;
    }

    /**
     * Converts the given ip adress into 4 bytes
     *
     * @param ip te ip adress to convert
     * @return a byte array containing 4 elements
     */
    public static byte[] ipToByte(String ip) {
        String[] parts = ip.split("\\.");
        byte[] back = new byte[4];

        if (parts.length != 4) {
            return back;
        }

        for (int i = 0; i < 4; i++) {
            int val = Integer.valueOf(parts[i]);
            if (val < 0 || val > 255) {
                return new byte[4];
            }

            val -= 256;
            back[i] = (byte) val;
        }

        return back;
    }

    /**
     * Checks whether the given byte[] represents a valid netmask.
     *
     * @param nm the netmask to check
     * @return is the byte[] a netmask?
     */
    public static boolean checkNetmask(byte[] nm) {
        boolean firstFalse = false;

        for (int i = 0; i < 4; i++) {
            boolean[] akt = convertToBits(nm[i]);
            for (int b = 0; b < 8; b++) {
                if (firstFalse && akt[b]) {
                    return false;
                }

                if (!akt[b]) {
                    firstFalse = true;
                }
            }
        }

        return true;
    }

    /**
     * 判断ipV4或者mask地址是否合法，通过正则表达式方式进行判断
     *
     * @param ipv4
     */
    public static boolean ipV4Validate(String ipv4) {
        return ipv4Validate(ipv4, IPV4_REGEX);
    }

    private static boolean ipv4Validate(String addr, String regex) {
        if (addr == null) {
            return false;
        } else {
            return Pattern.matches(regex, addr.trim());
        }
    }

    /**
     * 比较两个ip地址是否在同一个网段中，如果两个都是合法地址，两个都是非法地址时，可以正常比较；
     * 如果有其一不是合法地址则返回false；
     * 注意此处的ip地址指的是如“192.168.1.1”地址
     *
     * @return
     * @throws UnknownHostException
     */
    public static boolean checkSameNetSegment(String ip1, String ip2, String netmask) throws UnknownHostException {
        netmask = formatNetmaskToIpType(netmask);
        return checkSameNetSegment(ipToInt(ip1), ipToInt(ip2), ipToInt(netmask));
    }

    /**
     * 校验两个IP是否在同一个网段
     *
     * @param ip1  第一个IP
     * @param ip2  第二个IP
     * @param mask 子网掩码
     * @return true 是， false不是
     */
    public static boolean checkSameNetSegment(int ip1, int ip2, int mask) {
        return (mask & ip1) == (mask & ip2);
    }

    /**
     * 检查是否在同一个网段
     * @param ip1
     * @param ip2
     * @param mask
     * @return
     */
    public static boolean checkSameNetSegment(int[] ip1, int[] ip2,
                                              int[] mask) {
        return checkSameNetSegment(intArrToByteArr(ip1), intArrToByteArr(ip2),
                intArrToByteArr(mask));
    }

    public static boolean checkSameNetSegment(byte[] ip1, byte[] ip2,
                                              byte[] mask) {
        return checkSameNetSegment(byteToInt(ip1), byteToInt(ip2),
                byteToInt(mask));
    }

    public static byte[] intArrToByteArr(int[] ints) {
        byte[] bytes = new byte[ints.length];
        for (int i = 0; i < ints.length; i++) {
            bytes[i] = (byte) ints[i];
        }
        return bytes;
    }

    /**
     * Method to convert a byte into 8 bits.
     *
     * @param b the byte to convert
     * @return 8 booleans as an array
     */
    private static boolean[] convertToBits(byte b) {
        boolean[] bits = new boolean[8];

        for (int i = 0; i < 8; i++) {
            bits[7 - i] = ((b & (1 << i)) != 0);
        }

        return bits;
    }

    /**
     * Checks wheter the given byte[] represents a valid ip adress which
     * can propably announced to a client?<br>
     * Valid adresses range from 1 to 254 (0 is the net itself,
     * 255 is the broadcast adress).<br>
     * <br>
     * Note that byte is <b>signed</b>, so the following 2 values are
     * interesting for the comparison:<br>
     * <code><pre>
     *   byte    int
     *   -----------
     *   -1   =  255
     *    0   =  0
     * </pre></code>
     *
     * @param ip the ip adress to check
     * @return is the byte[] an ip adress?
     */
    public static boolean checkIp(byte[] ip) {
        return ip.length == 4 && ip[3] != -1 && ip[3] != 0;
    }

    /**
     * Helper method to convert a byte[] into an int[].
     *
     * @param data The byte[] to convert
     * @return an int[] representing the given byte[]
     */
    public static int[] byteArrToIntArr(byte[] data) {
        int[] back = new int[data.length];

        for (int i = 0; i < data.length; i++) {
            back[i] = 0xFF & data[i];
        }

        return back;
    }

    public static int[] strToIntArr(String ip) {
        return byteArrToIntArr(ipToByte(ip));
    }

    public static int ipToInt(String ip) throws UnknownHostException {
        return byteToInt(ip2Byte(ip));
    }

    public static long getIpCompareValue(byte[] ip) {
        int[] iIp = Ipv4Utils.byteArrToIntArr(ip);
        return getIpCompareValue(iIp);
    }

    /**
     * 计算IP的整数值
     *
     * @param ip
     * @return
     */
    public static long getIpCompareValue(int[] ip) {

        StringBuilder sb = new StringBuilder();
        String s = null;
        int zeroNum = 0;
        for (int i : ip) {
            if (i > 255) {
                s = "255";
            } else if (i < 0) {
                s = "0";
            } else {
                s = String.valueOf(i);
            }
            zeroNum = 3 - s.length();
            for (int j = 0; j < zeroNum; j++) {
                sb.append("0");
            }
            sb.append(s);
        }
        return Long.parseLong(sb.toString());
    }

    /**
     * IP是否在这个范围
     * @param ip
     * @param ipStart
     * @param ipEnd
     * @return
     */
    public static boolean inRange(byte[] ip, byte[] ipStart, byte[] ipEnd) {
        return inRange(byteArrToIntArr(ip), byteArrToIntArr(ipStart), byteArrToIntArr(ipEnd));
    }

    /**
     * ip在start和end的范围
     *
     * @param ip
     * @param ipStart
     * @param ipEnd
     * @return
     */
    public static boolean inRange(int[] ip, int[] ipStart, int[] ipEnd) {
        long startIp = getIpCompareValue(ipStart);
        long endIp = getIpCompareValue(ipEnd);
        long curIp = getIpCompareValue(ip);
        if (curIp >= startIp && curIp <= endIp) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 获取ipv4的网关 如ip为4.108.3.5 掩码为255.255.255.252 结果为4.108.3.5
     *
     * @param ipV4
     * @param maskV4
     * @return
     */
    public static String getIpV4GW(String ipV4, String maskV4) {
        maskV4 = formatNetmaskToIpType(maskV4);
        StringBuilder result = new StringBuilder("");
        if (isIpv4(ipV4) || isMask(maskV4)) {
            String[] ips = ipV4.split("\\.");
            String[] masks = maskV4.split("\\.");
            for (int i = 0; i < 4; i++) {
                if (i == 3) {
                    result.append((Integer.valueOf(ips[i])
                            & Integer.valueOf(masks[i])) + 1);
                } else {
                    result.append(Integer.valueOf(ips[i])
                            & Integer.valueOf(masks[i]));
                }
                if (i != 3) {
                    result.append(".");
                }
            }
        }
        return result.toString();
    }

    /**
     * 判断华为密码是否合法 密码长度为6-16 包含大小写 数字及特殊字符
     *
     * @param pwd
     * @return
     */
    public static boolean checkPwdForHw(String pwd) {
        boolean result = false;
        String reg = "((?=.*[a-z]+)(?=.*[A-Z]+)(?=.*[0-9]+)(?=.*\\W+)).{6,16}";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(pwd);
        result = m.find();
        return result;
    }

    /**
     * 获取ipv4的网段地址 如ip为4.108.3.5 掩码为255.255.255.252 结果为4.108.3.4
     * 网段地址如：192.168.1.0/255.255.255.0
     * 网段的网络IP是第一个IP：192.168.1.1
     * 网段的广播IP是最后一个IP：192.168.1.255
     * 有效IP：网络IP和广播IP之间的所有IP
     *
     * @param ipV4   一个IP
     * @param maskV4 一个掩码，支持数字形式
     * @return 网段IP 如：网段地址如：192.168.1.5/255.255.255.0  返回的是192.168.1.0
     */
    public static String getNetworkSegmentIp(String ipV4, String maskV4) {
        maskV4 = formatNetmaskToIpType(maskV4);
        StringBuilder result = new StringBuilder("");
        if (isIpv4(ipV4) && isMask(maskV4)) {
            String[] ips = ipV4.split("\\.");
            String[] masks = maskV4.split("\\.");
            for (int i = 0; i < 4; i++) {
                result.append(Integer.valueOf(ips[i]) & Integer.valueOf(masks[i]));
                if (i != 3) {
                    result.append(".");
                }
            }
        }
        return result.toString();
    }

    /**
     * 获取ipv4的网段地址 如ip为4.108.3.5 掩码为255.255.255.252 结果为4.108.3.4
     *
     * @param ipV4
     * @param maskV4
     * @return
     */
    @Deprecated
    public static String getIpV4Link(String ipV4, String maskV4) {
        return getNetworkSegmentIp(ipV4, maskV4);
    }

    /**
     * 判断ip v4掩码地址是否合法
     *
     * @param netMask
     * @return
     */
    public static boolean isMask(String netMask) {
        boolean result = false;
        if (isIpv4(netMask)) {
            StringBuilder temp = new StringBuilder();
            String[] ips = netMask.split("\\.");
            for (int i = 0; i < ips.length; i++) {
                int tempI = Integer.valueOf(ips[i]);
                String binStr = Integer.toBinaryString(tempI);
                if (binStr.length() < 8) {
                    for (int j = 0; j < 8
                            - Integer.toBinaryString(tempI).length(); j++) {
                        temp.append(0);
                    }
                }
                temp.append(binStr);
                if (i != 3) {
                    temp.append(".");
                }
            }
//            LOG.info("子网掩码" + netMask + "转换为2进制字符串为" + temp.toString());
            String temp2 = temp.toString().replace(".", "");
            if (temp2.indexOf("0") > temp2.lastIndexOf("1")
                    || temp2.indexOf("0") < 0) {
                result = true;
            }

        }
        return result;
    }

    /**
     * 获取掩码长度 如果不是掩码返回-1
     *
     * @param mask
     * @return
     */
    public static int getMaskLength(String mask) {
        int result = -1;
        if (isIpv4(mask)) {
            StringBuilder temp = new StringBuilder();
            StringBuilder temp2 = new StringBuilder();
            String[] ips = mask.split("\\.");
            for (int i = 0; i < ips.length; i++) {
                int tempI = Integer.valueOf(ips[i]);
                String binStr = Integer.toBinaryString(tempI);
                if (binStr.length() < 8) {
                    for (int j = 0; j < 8
                            - Integer.toBinaryString(tempI).length(); j++) {
                        temp.append(0);
                        temp2.append(0);
                    }
                }
                temp.append(binStr);
                temp2.append(binStr);
                if (i != ips.length - 1) {
                    temp2.append(".");
                }
            }
//            LOG.info("子网掩码" + mask + "转换为2进制字符串为" + temp2);
            if (temp.indexOf("0") > temp.lastIndexOf("1")
                    || temp.indexOf("0") < 0) {
                result = temp.lastIndexOf("1") + 1;
            }
        }
        return result;
    }

    /**
     * 获取ipv4格式的掩码的反掩码 如 255.255.255.0的反掩码为0.0.0.255
     *
     * @param mask 如果是整数形式，那就会自动转换成IP形式
     * @return
     */
    public static String getReverseNetmask(String mask) {
        mask = formatNetmaskToIpType(mask);
        if (isIpv4(mask)) {
            StringBuilder strBuf = new StringBuilder();
            String temp[] = mask.split("\\.");
            for (int i = 0; i < temp.length; i++) {
                strBuf.append(255 - Integer.valueOf(temp[i]));
                if (i != 3) {
                    strBuf.append(".");
                }
            }
            return strBuf.toString();
        }
        return null;
    }

    /**
     * 格式化子网掩码，会把整数型子网掩码的二进制位数的形式转换成IP的形式
     *
     * @param netmask
     * @return
     */
    public static String formatNetmaskToIpType(String netmask) {
        if (NETMASK.containsKey(netmask)) {
            return NETMASK.get(netmask);
        } else {
            return netmask;
        }
    }

    /**
     * 把掩码转换成整形
     *
     * @param netmask
     * @return
     */
    public static String formatNetmaskToIntegerType(String netmask) {
        if (netmask == null) {
            return "";
        }
        if (NETMASK_IP_LEN_MAP.containsKey(netmask)) {
            return NETMASK_IP_LEN_MAP.get(netmask);
        } else {
            return netmask;
        }

    }

    /**
     * 判断ip地址是否正确 0.0.0.0这样格式返回true
     *
     * @param ip
     * @return
     */
    public static boolean isIpv4(String ip) {
        if (ip != null && ip.contains(".")) {
            String temp[] = ip.split("\\.");
            if (temp.length == 4) {
                for (int i = 0; i < temp.length; i++) {
                    try {
                        int tempInt = Integer.valueOf(temp[i]);
                        if (0 > tempInt || tempInt > 255) {
                            return false;
                        }
                    } catch (Exception e) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 0.0.0.0,任意地址，也是没地址
     *
     * @return
     */
    public static final InetAddress getInaddrAny() {
        try {
            final byte[] rawAddr = {(byte) 0, (byte) 0, (byte) 0, (byte) 0};
            return InetAddress.getByAddress(rawAddr);
        } catch (UnknownHostException e) {
            // bad luck
            throw new IllegalStateException("Unable to generate INADDR_ANY");
        }
    }

    /**
     * 255.255.255.255,广播地址，也是面向整个局域网的地址
     *
     * @return
     */
    public static final InetAddress getInaddrBroadcast() {
        try {
            final byte[] rawAddr = {(byte) -1, (byte) -1, (byte) -1, (byte) -1};
            return InetAddress.getByAddress(rawAddr);
        } catch (UnknownHostException e) {
            // bad luck
            throw new IllegalStateException(
                    "Unable to generate INADDR_BROADCAST");
        }
    }

    /**
     * 有效的IP地址，非空/非0/非1
     *
     * @param addr
     * @return
     */
    public static final boolean isValidIpV4Addr(InetAddress addr) {
        if (addr == null || getInaddrAny().equals(addr)
                || getInaddrBroadcast().equals(addr)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 无效的IP地址，空/0/1
     *
     * @param addr
     * @return
     */
    public static final boolean isInValidIpV4Addr(InetAddress addr) {
        return !isValidIpV4Addr(addr);
    }

    /**
     * 有效的IP地址，不包含0.0.0.0和255.255.255.255
     *
     * @param addr
     * @return
     */
    public static final boolean isValidIpV4Addr(String addr) {
        return isValidIpV4Addr(getInetAddress(addr));
    }

    /**
     * 无效的IP地址
     *
     * @param addr
     * @return
     */
    public static final boolean isInValidIpV4Addr(String addr) {
        return isInValidIpV4Addr(getInetAddress(addr));
    }

    /**
     * 例如：ip:172.31.2.35,netmask:255.255.255.0
     * 根据IP和子网掩码，计算子网广播地址
     *
     * @param ip
     * @param netmask 子网掩码
     * @return
     */
    public static String getBroadcastAddress(String ip, String netmask) {
        netmask = formatNetmaskToIpType(netmask);
        String[] ips = ip.split("\\.");
        String[] masks = netmask.split("\\.");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ips.length; i++) {
            ips[i] = String.valueOf(
                    (~Integer.parseInt(masks[i])) | (Integer.parseInt(ips[i])));
            sb.append(turnIntToBinaryStr(Integer.parseInt(ips[i])));
            if (i != (ips.length - 1)) {
                sb.append(".");
            }
        }
        return turnBinaryStrToIp(sb.toString());
    }

    /**
     * 把带符号整形转换为二进制
     *
     * @param num
     * @return
     */
    public static String turnIntToBinaryStr(int num) {
        String str = "";
        str = Integer.toBinaryString(num);
        int len = 8 - str.length();
        // 如果二进制数据少于8位,在前面补零.
        for (int i = 0; i < len; i++) {
            str = "0" + str;
        }
        // 如果num为负数，转为二进制的结果有32位，如1111 1111 1111
        // 1111 1111 1111 1101 1110
        // 则只取最后的8位.
        if (len < 0) {
            str = str.substring(24, 32);
        }
        return str;
    }

    /**
     * 把二进制形式的ip，转换为十进制形式的ip
     * 如：192.168.1.1转换成：11000000.10101000.00000001.00000001
     *
     * @param str
     * @return
     */
    public static String turnBinaryStrToIp(String str) {
        String[] ips = str.split("\\.");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ips.length; i++) {
            sb.append(turnBinaryByteToInt(ips[i]));
            sb.append(".");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 把二进制转换为十进制
     *
     * @param str
     * @return
     */
    private static int turnBinaryByteToInt(String str) {
        int total = 0;
        int top = str.length();
        for (int i = 0; i < str.length(); i++) {
            String h = String.valueOf(str.charAt(i));
            top--;
            total += ((int) Math.pow(2, top)) * (Integer.parseInt(h));
        }
        return total;
    }

    /**
     * 获取一个IP的后一个IP
     *
     * @param ip IP字符串形式
     * @return 假如获取到的IP是无效的，就返回0.0.0.0
     */
    public static String nextIp(String ip) {
        return byteToIp(nextIp(ipToByte(ip)));
    }

    /**
     * 计算一个IP地址递增1后的值
     *
     * @param ip 4个字节数组的IP值
     * @return
     */
    public static byte[] nextIp(byte[] ip) {
        int[] akt = {
                0xFF & ip[0], 0xFF & ip[1],
                0xFF & ip[2], 0xFF & ip[3]
        };
        return nextIp(akt);
    }

    /**
     * 计算一个IP地址递增1后的值
     *
     * @param ip 4个int值的数组，每个值是一个字节对应的带符号整数
     * @return
     */
    public static byte[] nextIp(int[] ip) {
        ip[3]++;

        if (ip[3] >= 255) {
            ip[3] = 1;
            ip[2]++;
        }

        if (ip[2] >= 256) {
            ip[2] = 0;
            ip[1]++;
        }

        if (ip[1] >= 256) {
            ip[1] = 0;
            ip[0]++;
            if (ip[0] >= 255) {
                return new byte[]{0, 0, 0, 0};
            }
        }

        byte[] back = {
                (byte) ip[0], (byte) ip[1],
                (byte) ip[2], (byte) ip[3]
        };

        return back;
    }

    /**
     * 获取一个IP的前一个IP
     *
     * @param ip IP字符串形式
     * @return 假如获取到的IP是无效的，就返回0.0.0.0
     */
    public static String beforeIp(String ip) {
        return byteToIp(beforeIp(ipToByte(ip)));
    }

    /**
     * 获取当前IP的前一个
     *
     * @param ip IP每个字节的字节形式数组
     * @return
     */
    public static byte[] beforeIp(byte[] ip) {
        int[] akt = {
                0xFF & ip[0], 0xFF & ip[1],
                0xFF & ip[2], 0xFF & ip[3]
        };
        return beforeIp(akt);
    }

    /**
     * 获取当前IP的前一个
     *
     * @param ip 当前IP每一字节位的int形式数组
     * @return
     */
    private static byte[] beforeIp(int[] ip) {
        ip[3]--;

        if (ip[3] <= 0) {
            ip[3] = 254;
            ip[2]--;
        }

        if (ip[2] < 0) {
            ip[2] = 255;
            ip[1]--;
        }

        if (ip[1] <= 0) {
            ip[1] = 255;
            ip[0]--;
            if (ip[0] <= 0) {
                return new byte[]{0, 0, 0, 0};
            }
        }

        byte[] back = {
                (byte) ip[0], (byte) ip[1],
                (byte) ip[2], (byte) ip[3]
        };

        return back;
    }

    /**
     * 获取本地主机名
     *
     * @return
     */
    public static String getLocalHostName() {
        try {
            return Inet4Address.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace(System.err);
        }
        return null;
    }

    /**
     * 获取本地主机地址
     *
     * @return
     */
    public static String getLocalHostAddress() {
        try {
            return Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace(System.err);
        }
        return null;
    }

    /**
     * 从报文中获取第一个IP地址
     *
     * @param hostInfo
     * @return
     */
    public static String findFirstIp(String hostInfo) {
        if (StringUtils.isEmpty(hostInfo)) {
            return null;
        }
        Matcher mat = IPV4_PATTERN.matcher(hostInfo);
        if (mat.find()) {
            return mat.group(1);
        }
        return null;
    }

    /**
     * 校验某个IP，是否在网段列表中的某一个
     *
     * @param checkedIp 被校验的IP
     * @param networks  网段列表
     * @return true：包含
     */
    public static boolean isIncludeNetwork(String checkedIp,
                                           Map<String, String> networks) throws UnknownHostException {
        boolean isInclude = false;
        for (Map.Entry<String, String> entry : networks.entrySet()) {
            if (Ipv4Utils.checkSameNetSegment(checkedIp, entry.getKey(), entry.getValue())) {
                isInclude = true;
                break;
            }
        }
        return isInclude;
    }

    /**
     * 获取同网段的最小IP地址，并且不能跟srcIp一样
     *
     * @param srcIp
     * @param mask
     * @return
     */
    public static String getSameNetworkMinDiffIp(String srcIp, String mask) throws UnknownHostException {
        String network = getNetworkSegmentIp(srcIp, mask);
        String broadcastAddress = getBroadcastAddress(srcIp, mask);

        String ip = network;
        while (true) {
            ip = intIpToStrIp(ipToInt(ip) + 1);
            if (!checkSameNetSegment(ip, network, mask)) {
                return null;
            }
            if (StringUtils.isNotEquals(srcIp, ip) && StringUtils.isNotEquals(broadcastAddress, ip)) {
                break;
            }
        }
        return ip;
    }

    /**
     * 是不是30位的掩码
     *
     * @param netmask
     * @return
     */
    public static boolean is30bitNetmask(String netmask) {
        if (netmask == null) {
            return false;
        }
        if ("255.255.255.252".equals(netmask) || "30".equals(netmask)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取同网段所有有效IP，不包括网络IP和广播IP
     *
     * @param ip
     * @param mask
     * @return
     */
    public static List<String> getAllSameNetworkIps(String ip, String mask) throws UnknownHostException {
        String gw = getIpV4GW(ip, mask);
        String broadcastAddress = getBroadcastAddress(ip, mask);
        List<String> ips = new ArrayList<String>();
        String nextIp = gw;
        while (true) {
            ips.add(nextIp);
            nextIp = nextIp(nextIp);
            if (!checkSameNetSegment(ip, nextIp, mask) || StringUtils.equals(nextIp, broadcastAddress)) {
                break;
            }

        }
        return ips;
    }

    /**
     * 获取同一个网段所有的IP，包括网络IP和广播IP
     * @param ip
     * @param mask
     * @return
     */
    public static List<String> getAllSameNetworkIpWithNetworkAndBroadcastIp(String ip, String mask) throws UnknownHostException {
        String gw = getNetworkSegmentIp(ip, mask);
        List<String> ips = new ArrayList<String>();
        String nextIp = gw;
        while (true) {
            ips.add(nextIp);
            nextIp = nextIp(nextIp);
            if (!checkSameNetSegment(ip, nextIp, mask)) {
                break;
            }

        }
        return ips;
    }

    /**
     * 是有效的IP，不包含：0.0.0.0和255.255.255.255
     *
     * @param ip
     * @return
     */
    public static boolean isValidIp(String ip) {
        if (StringUtils.isEmpty(ip)) {
            return false;
        }
        if ("0.0.0.0".equals(ip)) {
            return false;
        }
        if ("255.255.255.255".equals(ip)) {
            return false;
        }
        if (ip.matches(Ipv4Utils.IPV4_REGEX)) {
            return true;
        } else {
            return false;
        }
    }
}
