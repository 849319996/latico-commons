/*
 *	This file is part of dhcp4java, a DHCP API for the Java language.
 * (c) 2006 Stephan Hadinger
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
 *	You should have received a copy of the GNU Lesser General Public
 *	License along with this library; if not, write to the Free Software
 *	Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.latico.commons.net.dhcp.server.common;


import com.latico.commons.common.util.net.Ipv4Utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * <PRE>
 * DHCP参数常量，包括：
 * 1、数据包协议类型，请求类型还是响应类型；
 * 2、硬件类型编号；
 * 3、消息类型编号;
 * 4、功能选项码编号;
 * 5、options82的子选项的选项码；
 * 6、其它常量参数。
 * 
 * Options号
Options作用
1	设置子网掩码选项。
3	设置网关地址选项。
6	设置DNS服务器地址选项。
12	设置域名选项。
15	设置域名后缀选项。
33	设置静态路由选项。该选项中包含一组有分类静态路由（即目的地址的掩码固定为自然掩码，不能划分子网），客户端收到该选项后，将在路由表中添加这些静态路由。如果存在Option121，则忽略该选项。
44	设置NetBios服务器选项。
46	设置NetBios节点类型选项。
50	设置请求IP选项。
51	设置IP地址租约时间选项。
52	设置Option附加选项。
53	设置DHCP消息类型。
54	设置服务器标识。
55	设置请求参数列表选项。客户端利用该选项指明需要从服务器获取哪些网络配置参数。该选项内容为客户端请求的参数对应的选项值。
58	设置续约T1时间，一般是租期时间的50%。
59	设置续约T2时间。一般是租期时间的87.5%。
60	设置厂商分类信息选项，用于标识DHCP客户端的类型和配置。
61	设置客户端标识选项。
66	设置TFTP服务器名选项，用来指定为客户端分配的TFTP服务器的域名。
67	设置启动文件名选项，用来指定为客户端分配的启动文件名。
77	设置用户类型标识。
82	设置DHCP Relay相关参数，改参数以它各个子选项组成,比如某个华为路由器的：DHO_DHCP_AGENT_OPTIONS(82)={1}"Ethernet0/0/1:4096.4096 CSG2/0/0/0/0/0",{2}"T².Q"， Ethernet0/0/1是DHCP Relay机器报文入端口CSG2/0/0/0/0/0是出端口
121	设置无分类路由选项。该选项中包含一组无分类静态路由（即目的地址的掩码为任意值，可以通过掩码来划分子网），客户端收到该选项后，将在路由表中添加这些静态路由。
148	EasyDeploy中Commander的IP地址。
149	SFTP和FTPS服务器的IP地址。
150	设置TFTP服务器地址选项，指定为客户端分配的TFTP服务器的地址。
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2017年7月16日</B>
 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @since     <B>JDK1.6</B>
 */
public final class DhcpConstants {

    // Suppresses default constructor, ensuring non-instantiability.
	private DhcpConstants() {
		throw new UnsupportedOperationException();
	}
	
    // ========================================================================
    // DHCP Constants

    /** DHCP BOOTP CODES 数据包协议类型，请求类型还是响应类型 **/
    public static final byte BOOTREQUEST    = 1;
    public static final byte BOOTREPLY	    = 2;

    /** DHCP HTYPE CODES 硬件类型编号 **/
    public static final byte HTYPE_ETHER	= 1;
    public static final byte HTYPE_IEEE802	= 6;
    public static final byte HTYPE_FDDI		= 8;
    public static final byte HTYPE_IEEE1394	= 24;	// rfc 2855

    /** DHCP MESSAGE CODES 下面DHCP开头的是消息类型编号 **/
    public static final byte DHCPDISCOVER   =  1;
    public static final byte DHCPOFFER      =  2;
    public static final byte DHCPREQUEST    =  3;
    public static final byte DHCPDECLINE    =  4;
    public static final byte DHCPACK        =  5;
    public static final byte DHCPNAK        =  6;
    public static final byte DHCPRELEASE    =  7;
    public static final byte DHCPINFORM     =  8;
    public static final byte DHCPFORCERENEW =  9;
    public static final byte DHCPLEASEQUERY = 10; // RFC 4388
    public static final byte DHCPLEASEUNASSIGNED = 11; // RFC 4388
    public static final byte DHCPLEASEUNKNOWN = 12; // RFC 4388
    public static final byte DHCPLEASEACTIVE = 13; // RFC 4388
//	1 15 3 6 44 46 47 31 33 249 43
    /** DHCP OPTIONS CODE 下面DHO_开头的是功能选项码编号 **/
    public static final byte DHO_PAD                          =   0;
    
    /** DHO_SUBNET_MASK 子网掩码 */
    public static final byte DHO_SUBNET_MASK                  =   1;
    
    public static final byte DHO_TIME_OFFSET                  =   2;
    
    /** DHO_ROUTERS 网关 */
    public static final byte DHO_ROUTERS                      =   3;
    
    public static final byte DHO_TIME_SERVERS                 =   4;
    public static final byte DHO_NAME_SERVERS                 =   5;
    
    /** DHO_DOMAIN_NAME_SERVERS DNS */
    public static final byte DHO_DOMAIN_NAME_SERVERS          =   6;
    
    public static final byte DHO_LOG_SERVERS                  =   7;
    
    /** DHO_COOKIE_SERVERS cookies，固定值 */
    public static final byte DHO_COOKIE_SERVERS               =   8;
    
    public static final byte DHO_LPR_SERVERS                  =   9;
    public static final byte DHO_IMPRESS_SERVERS              =  10;
    public static final byte DHO_RESOURCE_LOCATION_SERVERS    =  11;
    
    /** DHO_HOST_NAME 域名选项 */
    public static final byte DHO_HOST_NAME                    =  12;
    
    public static final byte DHO_BOOT_SIZE                    =  13;
    public static final byte DHO_MERIT_DUMP                   =  14;
    
    /** DHO_DOMAIN_NAME 域名后缀 */
    public static final byte DHO_DOMAIN_NAME                  =  15;
    
    public static final byte DHO_SWAP_SERVER                  =  16;
    public static final byte DHO_ROOT_PATH                    =  17;
    public static final byte DHO_EXTENSIONS_PATH              =  18;
    public static final byte DHO_IP_FORWARDING                =  19;
    public static final byte DHO_NON_LOCAL_SOURCE_ROUTING     =  20;
    public static final byte DHO_POLICY_FILTER                =  21;
    public static final byte DHO_MAX_DGRAM_REASSEMBLY         =  22;
    public static final byte DHO_DEFAULT_IP_TTL               =  23;
    public static final byte DHO_PATH_MTU_AGING_TIMEOUT       =  24;
    public static final byte DHO_PATH_MTU_PLATEAU_TABLE       =  25;
    public static final byte DHO_INTERFACE_MTU                =  26;
    public static final byte DHO_ALL_SUBNETS_LOCAL            =  27;
    public static final byte DHO_BROADCAST_ADDRESS            =  28;
    public static final byte DHO_PERFORM_MASK_DISCOVERY       =  29;
    public static final byte DHO_MASK_SUPPLIER                =  30;
    public static final byte DHO_ROUTER_DISCOVERY             =  31;
    public static final byte DHO_ROUTER_SOLICITATION_ADDRESS  =  32;
    public static final byte DHO_STATIC_ROUTES                =  33;
    public static final byte DHO_TRAILER_ENCAPSULATION        =  34;
    public static final byte DHO_ARP_CACHE_TIMEOUT            =  35;
    public static final byte DHO_IEEE802_3_ENCAPSULATION      =  36;
    public static final byte DHO_DEFAULT_TCP_TTL              =  37;
    public static final byte DHO_TCP_KEEPALIVE_INTERVAL       =  38;
    public static final byte DHO_TCP_KEEPALIVE_GARBAGE        =  39;
    public static final byte DHO_NIS_SERVERS                  =  41;
    public static final byte DHO_NTP_SERVERS                  =  42;
    public static final byte DHO_VENDOR_ENCAPSULATED_OPTIONS  =  43;
    
    /** DHO_NETBIOS_NAME_SERVERS 网络基本输入输出API服务,使用DHCP Relay的地址或者DHCP服务器的地址即可 */
    public static final byte DHO_NETBIOS_NAME_SERVERS         =  44;
    
    public static final byte DHO_NETBIOS_DD_SERVER            =  45;
    public static final byte DHO_NETBIOS_NODE_TYPE            =  46;
    public static final byte DHO_NETBIOS_SCOPE                =  47;
    public static final byte DHO_FONT_SERVERS                 =  48;
    public static final byte DHO_X_DISPLAY_MANAGER            =  49;
    public static final byte DHO_DHCP_REQUESTED_ADDRESS       =  50;
    
    /** DHO_DHCP_LEASE_TIME 租期 */
    public static final byte DHO_DHCP_LEASE_TIME              =  51;
    
    public static final byte DHO_DHCP_OPTION_OVERLOAD         =  52;
    
    /** DHO_DHCP_MESSAGE_TYPE 消息类型 */
    public static final byte DHO_DHCP_MESSAGE_TYPE            =  53;
    
    /** DHO_DHCP_SERVER_IDENTIFIER DHCP服务器标识，续约租期时的定向请求，一般使用服务器IP，如果有DHCP Relay就用Relay机器的地址， */
    public static final byte DHO_DHCP_SERVER_IDENTIFIER       =  54;
    
    /** DHO_DHCP_PARAMETER_REQUEST_LIST 请求参数列表 */
    public static final byte DHO_DHCP_PARAMETER_REQUEST_LIST  =  55;
    
    /** DHO_DHCP_MESSAGE 字符串消息 */
    public static final byte DHO_DHCP_MESSAGE                 =  56;
    
    public static final byte DHO_DHCP_MAX_MESSAGE_SIZE        =  57;
    
    /** DHO_DHCP_RENEWAL_TIME 第一次更新租期时间点（T1,相对于获取租期算起） */
    public static final byte DHO_DHCP_RENEWAL_TIME            =  58;
    
    /** DHO_DHCP_REBINDING_TIME 第二次更新租期时间点（T2,相对于获取租期算起） */
    public static final byte DHO_DHCP_REBINDING_TIME          =  59;
    
    /** DHO_VENDOR_CLASS_IDENTIFIER 厂家类型标识，一般都是设备型号或者厂家型号 */
    public static final byte DHO_VENDOR_CLASS_IDENTIFIER      =  60;
    
    /** DHO_DHCP_CLIENT_IDENTIFIER DHCP客户端唯一标识
     Code Len Type Client-Identifier
     +—–+—–+—–+—–+—–+—
     | 61 | n | t1 | i1 | i2 | …
     +—–+—–+—–+—–+—–+—
     t1: 是类型，
     如果是0x01开头，代表后面是硬件地址，
     如果是0xff开头，表示IAID，
     如果是0x0002开头，表示后面是DUID
     DUID
     DUID（DHCP Unique Identifier，DHCP唯一标识符）是唯一标识一台
     DHCPv6设备（包括客户端、中继和服务器）的标识符，用于DHCPv6设备之间的相互验证。我们设备采用RFC 3315规定的DUID-EN（DUID Vendor-assigned unique ID based on Enterprise
     Number，基于企业编号的DUID）作为DHCPv6设备的标识。DUID组成如下
      DUID type：DUID类型。取值为 0x0002；
      Identifier：标识号。 格式图如下：
      */
    public static final byte DHO_DHCP_CLIENT_IDENTIFIER       =  61;
    
    public static final byte DHO_NWIP_DOMAIN_NAME             =  62; // rfc 2242
    public static final byte DHO_NWIP_SUBOPTIONS              =  63; // rfc 2242
    public static final byte DHO_NISPLUS_DOMAIN               =  64;
    public static final byte DHO_NISPLUS_SERVER               =  65;
    public static final byte DHO_TFTP_SERVER                  =  66;
    public static final byte DHO_BOOTFILE                     =  67;
    public static final byte DHO_MOBILE_IP_HOME_AGENT         =  68;
    public static final byte DHO_SMTP_SERVER                  =  69;
    public static final byte DHO_POP3_SERVER                  =  70;
    public static final byte DHO_NNTP_SERVER                  =  71;
    public static final byte DHO_WWW_SERVER                   =  72;
    public static final byte DHO_FINGER_SERVER                =  73;
    public static final byte DHO_IRC_SERVER                   =  74;
    public static final byte DHO_STREETTALK_SERVER            =  75;
    public static final byte DHO_STDA_SERVER                  =  76;
    public static final byte DHO_USER_CLASS                   =  77; // rfc 3004
    /** DHO_FQDN 客户端全域名 */
    public static final byte DHO_FQDN                         =  81;
  
    /** DHO_DHCP_AGENT_OPTIONS DHCP relay的选项，该选项下有子选项 */
    public static final byte DHO_DHCP_AGENT_OPTIONS           =  82; // rfc 3046
    
    public static final byte DHO_NDS_SERVERS                  =  85; // rfc 2241
    public static final byte DHO_NDS_TREE_NAME                =  86; // rfc 2241
    public static final byte DHO_NDS_CONTEXT					 =  87; // rfc 2241
    public static final byte DHO_CLIENT_LAST_TRANSACTION_TIME =  91; // rfc 4388
    public static final byte DHO_ASSOCIATED_IP				 =  92; // rfc 4388
    public static final byte DHO_USER_AUTHENTICATION_PROTOCOL =  98;
    public static final byte DHO_AUTO_CONFIGURE               = 116;
    public static final byte DHO_NAME_SERVICE_SEARCH          = 117; // rfc 2937
    public static final byte DHO_SUBNET_SELECTION             = 118; // rfc 3011
    public static final byte DHO_DOMAIN_SEARCH	             = 119; // rfc 3397
    public static final byte DHO_CLASSLESS_ROUTE				 = 121;	// rfc 3442
    public static final byte DHO_END                          =  -1;

    /** 下面的DHO_DHCP_AGENT_OPTIONS开头的是options82的子选项的选项码 */
    /** DHO_DHCP_AGENT_OPTIONS_CIRCUIT_ID relay的子选项电路ID， 1表示代理电路id（circuit id）子项 */
    public static final byte DHCP_AGENT_OPTIONS_CIRCUIT_ID   =  1; // rfc 3046
    /** DHO_DHCP_AGENT_OPTIONS_REMOTE_ID   2表示代理远程id（remote id）子项 */
    public static final byte DHCP_AGENT_OPTIONS_REMOTE_ID           =  2; // rfc 3046
    /** DHO_DHCP_AGENT_OPTIONS_LINK_SELECTION 5表示链路选择（link selection）子项 */
    public static final byte DHCP_AGENT_OPTIONS_LINK_SELECTION           =  5; // rfc 3046
    
    /** Any address 0.0.0.0,任意地址，也是没地址 */
    public static final InetAddress INADDR_ANY = Ipv4Utils.getInaddrAny();
    
    /** Broadcast Address 255.255.255.255,广播地址，也是面向整个局域网的地址 */
    public static final InetAddress INADDR_BROADCAST = Ipv4Utils.getInaddrBroadcast();

    // sanity check values
    public static final int _DHCP_MIN_LEN           = 548;
    public static final int _DHCP_DEFAULT_MAX_LEN   = 576;	// max default size for client
    public static final int _BOOTP_ABSOLUTE_MIN_LEN = 236;
    public static final int _DHCP_MAX_MTU           = 1500;
    public static final int _DHCP_UDP_OVERHEAD      = 14 + 20 + 8;
    public static final int _BOOTP_VEND_SIZE        = 64;
    
    // Magic cookie
    /** _MAGIC_COOKIE 是DHCP四个字符组成的整数值，假如有options功能选项内容的话就会在读完标准格式内容后有这个4个字节的提示头 */
    public static final int _MAGIC_COOKIE = 0x63825363;
    
    /** BROADCAST_ADDR 广播地址 */
    public static final String BROADCAST_ADDR = "255.255.255.255";
    
    /** BOOTP_REQUEST_PORT DHCP的服务器和DHCP Relay监听端口 */
    public static final int BOOTP_REQUEST_PORT = 67;
    
    /** BOOTP_REPLY_PORT 客户端的发送报文时用的本地端口，也是服务端响应的端口 */
    public static final int BOOTP_REPLY_PORT   = 68;

    // Maps for "code" to "string" conversion
    public static final Map<Byte, String> _BOOT_NAMES;
    public static final Map<Byte, String> _HTYPE_NAMES;
    public static final Map<Byte, String> _DHCP_CODES;
    public static final Map<Byte, String> _DHO_NAMES;
    public static final Map<String, Byte> _DHO_NAMES_REV;
    public static final Map<Byte, String> _DHCP_AGENT;
    public static final Map<String, Byte> _DHCP_AGENT_REV;

    /*
     * preload at startup Maps with constants
     * allowing reverse lookup
     */
    static {
    	
    	Map<Byte, String> bootNames  = new LinkedHashMap<Byte, String>();
    	Map<Byte, String> htypeNames = new LinkedHashMap<Byte, String>();
        Map<Byte, String> dhcpCodes  = new LinkedHashMap<Byte, String>();
        Map<Byte, String> dhoNames   = new LinkedHashMap<Byte, String>();
        Map<String, Byte> dhoNamesRev = new LinkedHashMap<String, Byte>();
        Map<Byte, String> dhcpAgentNames   = new LinkedHashMap<Byte, String>();
        Map<String, Byte> dhcpAgentNamesRev = new LinkedHashMap<String, Byte>();
        
        _BOOT_NAMES = Collections.unmodifiableMap(bootNames);
        _HTYPE_NAMES = Collections.unmodifiableMap(htypeNames);
        _DHCP_CODES = Collections.unmodifiableMap(dhcpCodes);
        _DHO_NAMES = Collections.unmodifiableMap(dhoNames);
        _DHO_NAMES_REV = Collections.unmodifiableMap(dhoNamesRev);
        _DHCP_AGENT = Collections.unmodifiableMap(dhcpAgentNames);
        _DHCP_AGENT_REV = Collections.unmodifiableMap(dhcpAgentNamesRev);
        
        // do some introspection to list constants
        Field[] fields = DhcpConstants.class.getDeclaredFields();

        // parse internal fields
        try {
            for (Field field : fields) {
                int    mod  = field.getModifiers();
                String name = field.getName();

                // parse only "public final static byte"
                if (Modifier.isFinal(mod) && Modifier.isPublic(mod) && Modifier.isStatic(mod) &&
                    field.getType().equals(byte.class)) {
                    byte code = field.getByte(null);

                    if(name.startsWith("DHCP_AGENT")){
                    	dhcpAgentNames.put(code, name);
                    	dhcpAgentNamesRev.put(name, code);
                    }else if (name.startsWith("BOOT")) {
                        bootNames.put(code, name);
                    } else if (name.startsWith("HTYPE_")) {
                        htypeNames.put(code, name);
                    } else if (name.startsWith("DHCP")) {
                        dhcpCodes.put(code, name);
                    } else if (name.startsWith("DHO_")) {
                        dhoNames.put(code, name);
                        dhoNamesRev.put(name, code);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            // we have a problem
            throw new IllegalStateException("Fatal error while parsing internal fields");
        }
       
    }
    
    /**
     * Returns a map associating a BootCode and the user-readable name.
     * 
     * <P>Currently:<br>
     * 	1=BOOTREQUEST<br>
     * 	2=BOOTREPLY
     * @return the map
     */
    public static final Map<Byte, String> getBootNamesMap() {
    	return _BOOT_NAMES;
    }
    
    /**
     * Returns a map associating a HType and the user-readable name.
     * 
     * <p>Ex: 1=HTYPE_ETHER
     * @return the map
     */
    public static final Map<Byte, String> getHtypesMap() {
    	return _HTYPE_NAMES;
    }

    /**
     * Returns a map associating a DHCP code and the user-readable name.
     * 
     * <p>ex: 1=DHCPDISCOVER
     * @return the map
     */
    public static final Map<Byte, String> getDhcpCodesMap() {
    	return _DHCP_CODES;
    }

    /**
     * Returns a map associating a DHCP option code and the user-readable name.
     * 
     * <p>ex: 1=DHO_SUBNET_MASK, 51=DHO_DHCP_LEASE_TIME, 
     * @return the map
     */
    public static final Map<Byte, String> getDhoNamesMap() {
    	return _DHO_NAMES;
    }

    /**
     * Returns a map associating a user-readable DHCP option name and the option code.
     * 
     * <p>ex: "DHO_SUBNET_MASK"=1, "DHO_DHCP_LEASE_TIME"=51 
     * @return the map
     */
    public static final Map<String, Byte> getDhoNamesReverseMap() {
    	return _DHO_NAMES_REV;
    }

    /**
     * Converts a DHCP option name into the option code.
     * @param name user-readable option name
     * @return the option code
     * @throws NullPointerException name is <tt>null</t>.
     */
    public static final Byte getDhoNamesReverse(String name) {
    	if (name == null) {
    		throw new NullPointerException();
    	}
    	return _DHO_NAMES_REV.get(name);
    }

    /**
     * Converts a DHCP code into a user-readable DHCP option name.
     * @param code DHCP option code
     * @return user-readable DHCP option name
     */
    public static final String getDhoName(byte code) {
    	return _DHO_NAMES.get(code);
    }
}
