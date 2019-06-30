package com.latico.commons.net.trap.test;

import net.percederberg.mibble.*;
import net.percederberg.mibble.snmp.SnmpObjectType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class TrapParseTest {

    /**
     * @param args
     * @throws MibLoaderException
     * @throws IOException
     */
    public static void main(String[] args){
        TrapParseTest t = new TrapParseTest();
        //String content = t.readFile();
        // System.err.println("content==="+content);
        try {
			t.aa();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MibLoaderException e) {
			e.printStackTrace();
		}
//    	MibBrowserUtils.

    }

    public void aa() throws IOException, MibLoaderException {
        String filePath = "./mibs/RFC1315-MIB";
//        String filePath = this.getClass().getResource("/").getPath() + "mibs/ietf/RFC1231-MIB";
        File file = new File(filePath);
        MibLoader ml = new MibLoader();
//        Mib[] mibs = ml.getAllMibs();
//        for(Mib mib : mibs){
        ml.addDir(file.getParentFile());
        System.out.println(file.getPath());
        System.out.println(file.exists());
        Mib mib = ml.load(file);
//        Mib mib = ml.load("RFC1315-MIB");
        	String mibName = mib.getName();
        	
        	System.err.println("mibName===" + mibName);
        	System.err.println("-------------------------------------");
        	String syntax = "";
        	String access = "";
        	String status = "";
        	List<MibObject> list = new ArrayList<MibObject>();
        	Collection c = mib.getAllSymbols();
        	Iterator it = c.iterator();
        	while (it.hasNext()) {
        		Object obj = it.next();
        		if (obj instanceof MibValueSymbol) {
        			MibObject mo = new MibObject();
        			MibValueSymbol mvs = (MibValueSymbol) obj;
        			SnmpObjectType sot = null;
        			if (mvs.getType() instanceof SnmpObjectType) {
        				sot = (SnmpObjectType) mvs.getType();
        			}
        			if (sot != null ) {
        				syntax = sot.getSyntax().getName();
        				access = sot.getAccess().toString();
        				status = sot.getStatus().toString();
        			}
        			//是否为表的列
        			boolean isTableColumn = mvs.isTableColumn();
        			String name = mvs.getName();
        			MibValue value = mvs.getValue();
        			MibValueSymbol parent = mvs.getParent();
        			String parentValue = "";
        			System.err.println("name==" + name);
        			System.err.println("value==" + value);
        			System.err.println("isTableColumn==" + isTableColumn);
        			if (parent != null) {
        				parentValue = parent.getValue().toString();
        				if (parent.getParent()==null){
        					System.err.println("supperParentName======" + mibName);
        					System.err.println("supperParentValue=====" + parentValue);
        					//parent=root
        					
        				}
        				System.err.println("parentName=" + parent.getName());
        				System.err.println("parentValue=" + parent.getValue());
        				
        			} else {
        			}
        			System.err.println("syntax=" + syntax);
        			System.err.println("access=" + access);
        			System.err.println("status=" + status);
        			System.err.println("-------------------------------------");
        			mo.setName(name);
        			mo.setValue(value.toString());
        			mo.setParent(parentValue);
        			mo.setSyntax(syntax);
        			mo.setAccess(access);
        			mo.setStatus(status);
        			list.add(mo);
        		} 
        		//System.out.println(it.next());
//        	}
        	
        }
        	MibValueSymbol mvs = mib.getSymbolByOid("1.3.6.1.2.1.10");
        	System.err.println("mvs.getName()=" + mvs.getName());
        	System.err.println("mvs.getValue()=" + mvs.getValue());
        	MibValueSymbol parent = mvs.getParent();
        	System.err.println("parent=" + parent);
        	
        	mvs = mib.getSymbolByOid("1.3.6.1.2.1.10.32.4.1");
        	System.err.println("mvs.getName()=" + mvs.getName());
        	System.err.println("mvs.getValue()=" + mvs.getValue());
        	parent = mvs.getParent();
        	System.err.println("parent=" + parent);

    }
    
    public void test() throws IOException, MibLoaderException{
    	 String filePath = "./mibs/RFC1315-MIB";
//       String filePath = this.getClass().getResource("/").getPath() + "mibs/ietf/RFC1231-MIB";
       File file = new File(filePath);
       MibLoader ml = new MibLoader();
//      
       ml.addDir(file.getParentFile());
//       
//       System.out.println(file.getPath());
//       System.out.println(file.exists());
//       Mib mib = ml.load(file);
////       Mib mib = ml.load("RFC1315-MIB");
//       	String mibName = mib.getName();
//       	MibValueSymbol mvs = mib.getSymbolByOid("1.3.6.1.2.1.10");
//    	System.err.println("mvs.getName()=" + mvs.getName());
//    	System.err.println("mvs.getValue()=" + mvs.getValue());
//    	MibValueSymbol parent = mvs.getParent();
//    	System.err.println("parent=" + parent);
       ml.load("RFC1315-MIB");
       Mib[] mibs = ml.getAllMibs();
     for(Mib mib : mibs){
    	 MibValueSymbol mvs = mib.getSymbolByOid("1.3.6.1.2.1.10");
    	 if(mvs != null){
    		 System.err.println("mvs.getName()=" + mvs.getName());
    		 System.err.println("mvs.getValue()=" + mvs.getValue());
    		 MibValueSymbol parent = mvs.getParent();
    		 System.err.println("parent=" + parent);
    		 break;
    	 }
     }
    }
}
