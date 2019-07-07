package com.latico.commons.net.snmp.mib;

import net.percederberg.mibble.*;
import net.percederberg.mibble.value.ObjectIdentifierValue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <PRE>
 * mib 文件分析类库,严格检查依赖，使用不方便
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2017年6月6日</B>
 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @since     <B>JDK1.6</B>
 */
@Deprecated
public class MibBrowserUtils {
	/**
	 * 获取所有mib
	 * @param mibfilePath
	 * @return
	 */
	public static List<MibModel> getAllOid(String mibfilePath) {
		File file=checkFile(mibfilePath);
		if(file==null)
			return null;
		MibModel map ;
		Mib mib;
		List<MibModel> list=new ArrayList<MibModel>();
		try {
			mib = loadMib(file);
		    Iterator<?>   iter = mib.getAllSymbols().iterator();
		    MibSymbol  symbol;
		    MibValue   value;
		    while (iter.hasNext()) {
		        symbol = (MibSymbol) iter.next();
		        value = extractOid(symbol);
		        if (value != null) {
		        	map=new MibModel();
		        	map.setName(symbol.getName());
		        	map.setOid(value.toString());
		        	System.out.println(map.getOid());
		        	map.setDetail(symbol.toString());
		        	list.add(map);
//		        	System.out.println(map);
		        }
		    }
		} catch (MibLoaderException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 检查该oid是否属于mib
	 * 
	 * @param mibfilePath
	 * @param oid
	 * @return
	 */
	public static boolean checkOid(String mibfilePath, String oid) {
		List<MibModel> list=getAllOid(mibfilePath);
		return checkOid(list,oid);
	}
	/**
	 * 检查该oid是否属于mib
	 * @param list
	 * @param oid
	 * @return
	 */
	public static boolean checkOid(List<MibModel> list, String oid){
		if(list==null)
			return false;
		for (MibModel mibModel : list) {
			if(mibModel.getOid().equals(oid))
				return true;
		}
		return false;
	}
	/**
	 * 获取oid信息
	 * 
	 * @param mibfilePath
	 * @param oid
	 * @return
	 */
	public static MibModel getOidInfo(String mibfilePath, String oid) {
		List<MibModel> list=getAllOid(mibfilePath);
		return getOidInfo(list,oid);
	}
	/**
	 * 获取oid信息
	 * 
	 * @param mibfilePath
	 * @param oid
	 * @return
	 */
	public static MibModel getOidInfo(List<MibModel> list, String oid) {
		//System.out.println(list.size());
		if(list==null)
			return null;
		for (MibModel mibModel : list) {
			if(mibModel.getOid().equals(oid))
				return mibModel;
		}
		return null;
	}
	/**
	 * 根据文件提取出mib
	 * @param file地址
	 * @return
	 * @throws MibLoaderException
	 * @throws IOException
	 */
	public static Mib loadMib(File file) throws MibLoaderException, IOException {
	    MibLoader  loader = new MibLoader();
	    loader.addDir(file.getParentFile());
	    return loader.load(file);
	}
	/**
	 * 提取oid出来
	 * @param symbol
	 * @return
	 */
	public static ObjectIdentifierValue extractOid(MibSymbol symbol) {
	    MibValue  value;
	    if (symbol instanceof MibValueSymbol) {
	        value = ((MibValueSymbol) symbol).getValue();
	        if (value instanceof ObjectIdentifierValue) {
	            return (ObjectIdentifierValue) value;
	        }
	    }
	    return null;
	}
	/**
	 * 检测文件是否文件，存不存在
	 * @param mibfilePath
	 * @return
	 */
	public static File checkFile(String mibfilePath){
		File file=new File(mibfilePath);
		if(!file.exists())
			return null;
		if(!file.isFile())
			return null;
		return file;
	}
	
	/**
	 * 获取指定oid的信息
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		MibModel model = MibBrowserUtils.getOidInfo(
						"mibs/RFC1315-MIB",
						"1.3.6.1.2.1.10");
		System.out.println(model.getName());
	}
}