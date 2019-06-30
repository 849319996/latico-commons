package com.latico.commons.net.snmp.client.impl;

import com.latico.commons.net.snmp.SnmpUtils;
import com.latico.commons.net.snmp.bean.SnmpLine;
import com.latico.commons.net.snmp.bean.SnmpRow;
import com.latico.commons.net.snmp.bean.SnmpTable;
import com.latico.commons.net.snmp.client.AbstractSnmpClient;
import com.latico.commons.net.snmp.enums.VersionEnum;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.string.StringUtils;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.MessageProcessingModel;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.PDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


/**
 * <PRE>
 * SNMP4J 方式实现SNMP
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2017年3月28日</B>
 * @author    <B><a href="mailto:latico@qq.com"> 蓝鼎栋 </a></B>
 * @since     <B>JDK1.6</B>
 */
public class Snmp4jClient extends AbstractSnmpClient implements PDUFactory {

	/** 日志 */
	private static final Logger LOG = LoggerFactory.getLogger(Snmp4jClient.class);

	/** 连接协议协议 */
	private static final String PROTOCOL = "udp";

	/** target 连接目标配置信息 */
	private Target target = null;

	/** snmp连接操作对象 */
	private Snmp snmp = null;
	
	/** timeoutModel 连接的时候的超时模型 */
	private static final TimeoutModel timeoutModel = new TimeoutModel() {
        
        @Override
        public long getRetryTimeout(int retryCount, int totalNumberOfRetries, long targetTimeout) {
            return 5000;
        }
        
        @Override
        public long getRequestTimeout(int totalNumberOfRetries, long targetTimeout) {
            return 3000;
        }
    };
	
	@Override
	protected boolean connect() {
		if(status){
			return true;
		}
		try {
			// 初始化SNMP连接监听对象
			initSnmp();

			// 下面开始初始化SNMP连接监听对象需要使用的配置信息
			initTarget();
			status = true;
			status = testSnmpConn();
			if(status){
				LOG.info("测试{}", getConnInfo());
			}else{
				LOG.error("测试{}", getConnInfo());
			}
		} catch (Exception e) {
			status = false;
			LOG.error("{}:Snmp4J初始化失败", ip, e);
		}

		return status;
	}

	/**
	 * 初始化SNMP连接监听对象
	 * @throws IOException
	 */
	private void initSnmp() throws IOException {
		TransportMapping<?> transport = new DefaultUdpTransportMapping();
		snmp = new Snmp(transport);

		// 设置安全模式
		if (VersionEnum.V3 == version) {
			USM usm = new USM(SecurityProtocols.getInstance(),
			        new OctetString(MPv3.createLocalEngineID()), 0);
			SecurityModels.getInstance().addSecurityModel(usm);
		}

		transport.listen();
	}

	/**
	 * 初始化SNMP连接监听对象需要使用的配置信息
	 */
	private void initTarget() {

		// 通信的版本
		if (VersionEnum.V1 == version) {
			this.target = new CommunityTarget();
			target.setVersion(SnmpConstants.version1);

			// 通信团体名称
			((CommunityTarget) target).setCommunity(new OctetString(readCommunity));

		} else if (VersionEnum.V2C == version || version == VersionEnum.V2){
			this.target = new CommunityTarget();
			target.setVersion(SnmpConstants.version2c);

			// 通信团体名称
			((CommunityTarget) target).setCommunity(new OctetString(readCommunity));

		} else if (VersionEnum.V3 == version) {
			// 添加用户
			OID authenticationProtocol = null;// 认证的协议类型
			if (AuthProtocolEnum.SHA_AUTH == authProtocol) {
				authenticationProtocol = AuthSHA.ID;

			} else if (AuthProtocolEnum.MD5_AUTH == authProtocol) {
				authenticationProtocol = AuthMD5.ID;

			} else {
				authenticationProtocol = AuthMD5.ID;
			}
			snmp.getUSM().addUser(new OctetString(username),
			        new UsmUser(new OctetString(username),
			                authenticationProtocol,
			                new OctetString(authPassword), PrivDES.ID,
			                new OctetString(privPassword)));

			target = new UserTarget();
			// 设置安全级别
			((UserTarget) target).setSecurityLevel(SecurityLevel.AUTH_PRIV);
			((UserTarget) target).setSecurityName(new OctetString(username));
			target.setVersion(SnmpConstants.version3);
		}

		// 通信地址
		String url = PROTOCOL + ":" + ip + "/" + getPort;
		Address targetAddress = GenericAddress.parse(url);
		target.setAddress(targetAddress);

		// 通信不成功时的重试次数
		target.setRetries(retries);

		// 读取超时时间,毫秒
		target.setTimeout(timeout);

		snmp.setTimeoutModel(timeoutModel);
	}

	@Override
	public Map<String, String> get(String oid) {
		
		Map<String, String> re = send(oid, PDU.GET);
		if (re == null || re.size() <= 0) {
			return null;
		} else {
			
			return re;
		}
	}

	/**
	 * get操作，获取一个详情对象
	 *
	 * @param oid
	 * @return
	 */
	@Override
	public SnmpRow getDetail(String oid) {
		if(!status){
			return null;
		}
		SnmpRow snmpRow = null;
		try {
			oid = SnmpUtils.formatterOid(oid);
			// 组装PDU数据
			OID oidObj = new OID(oid);
			PDU pdu = new PDU();
			VariableBinding vb = new VariableBinding(oidObj);
			pdu.add(vb);
			pdu.setType(PDU.GET);

			// 解析响应对象
			ResponseEvent respEvnt;

			respEvnt = snmp.send(pdu, target);
			if (respEvnt != null && respEvnt.getResponse() != null) {
				Vector<? extends VariableBinding> variableBindings = respEvnt.getResponse().getVariableBindings();
				for (int i = 0; i < variableBindings.size(); i++) {
					VariableBinding recVB = variableBindings.get(i);
					snmpRow = new SnmpRow();
					snmpRow.setOid(SnmpUtils.formatterOid(SnmpUtils.formatterOid(vb.getOid().toString())));
					snmpRow.setTypeInt(recVB.getVariable().getSyntax());
					snmpRow.setTypeStr(recVB.getVariable().getSyntaxString());
					snmpRow.setValue(recVB.getVariable().toString());
					break;
				}
			}
		} catch (Exception e) {
			LOG.error("{}:", e, ip);
		}

		return snmpRow;
	}

	@Override
	public Map<String, String> get(List<String> oids) {
		if (oids == null || oids.size() <= 0) {
			return null;
		}
		return send(oids, PDU.GET);
	}
	
	/**
	 * snmp设置
	 * @param oid
	 * @param val
	 * @throws IOException
	 */
	public void setString(String oid, String val) throws IOException {
		Variable value = new OctetString(val);
		
		setValue(oid, value);
	}

	@Override
	public Map<String, String> getNext(List<String> oids) {
		if (oids == null || oids.size() <= 0) {
			return null;
		}
		return send(oids, PDU.GETNEXT);
	}

	@Override
	public Map<String, String> getNext(String oid) {
		Map<String, String> re = send(oid, PDU.GETNEXT);
		if (re == null || re.size() <= 0) {
			return null;
		} else {
			return re;
		}
	}
	
	/**
	 * 设值
	 * @param oid
	 * @param value
	 * @throws IOException
	 */
	private Map<String, String> setValue(String oid, Variable value) throws IOException {
		if(!status){
			return null;
		}
		PDU pdu = new PDU();
		OID oidObj = new OID(oid);
		pdu.add(new VariableBinding(oidObj, value));
		pdu.setType(PDU.SET);
		ResponseEvent respEvnt = snmp.send(pdu, target);

		return readResponseMap(respEvnt);
	}

	public void setInt(String oid, int i) throws IOException {
		Variable value = new Integer32(i);
		setValue(oid, value);

	}
	
	/**
	 * 封装PDU协议单元，发送并解析返回
	 * @param oids OID节点字符串集合
	 * @param PDUType PDU协议类型
	 * @return map String, Ojbcet
	 */
	private Map<String, String> send(List<String> oids, int PDUType) {
		if(!status){
			return null;
		}
		// 组装PDU数据
		PDU pdu = new PDU();
		int size = oids.size();
		List<VariableBinding> vbs = new ArrayList<VariableBinding>();
		String oid = null;
		for (int i = 0; i < size; i++) {
			oid = oids.get(i);
			vbs.add(new VariableBinding(new OID(oid)));
		}
		pdu.addAll(vbs);
		pdu.setType(PDUType);

		// 解析响应对象
		Map<String, String> value = null;
		ResponseEvent respEvnt;
		try {
			respEvnt = snmp.send(pdu, target);
			value = readResponseMap(respEvnt);
		} catch (Exception e) {
			LOG.error("{}:", e, ip);
		}

		return value;
	}

	/**
	 * 封装PDU协议单元，然后发送，解析返回
	 * @param oid OID节点
	 * @param PDUType PDU协议
	 * @return
	 */
	private Map<String, String> send(String oid, int PDUType) {
		if(!status){
			return null;
		}
		
		// 组装PDU数据
		OID oidObj = new OID(oid);
		PDU pdu = new PDU();
		VariableBinding vb = new VariableBinding(oidObj);
		pdu.add(vb);
		pdu.setType(PDUType);

		// 解析响应对象
		Map<String, String> value = null;
		ResponseEvent respEvnt;
		try {
			respEvnt = snmp.send(pdu, target);
			value = readResponseMap(respEvnt);
		} catch (Exception e) {
			LOG.error("{}:", e, ip);
		}

		return value;
	}
	
	/**
	 * 返回内容解析
	 * @param respEvnt 响应事件
	 * @return Map String, Object
	 * @throws IOException
	 */
	private Map<String, String> readResponseMap(ResponseEvent respEvnt) throws IOException {
		// 解析Response
		Map<String, String> map = null;
		if (respEvnt != null && respEvnt.getResponse() != null) {
			List<? extends VariableBinding> recVBs = respEvnt.getResponse().getVariableBindings();
			map = new LinkedHashMap<String, String>();
			VariableBinding recVB = null;
			for (int i = 0; i < recVBs.size(); i++) {
				recVB = recVBs.get(i);
				map.put(recVB.getOid().toString(), recVB.getVariable().toString());
			}
		}
		return map;
	}
	
	@Override
	public Map<String, String> walk(String walkOid) {
		if(!status){
			return null;
		}
		walkOid = SnmpUtils.formatterOid(walkOid);
		Map<String, String> map = new LinkedHashMap<String, String>();
		try {
			PDU pdu = new PDU();
			OID targetOID = new OID(walkOid);
			pdu.add(new VariableBinding(targetOID));

			boolean finished = false;
			VariableBinding vb = null;
			ResponseEvent respEvent = null;
			PDU response = null;
			int tryCount = 0;
			String lastOid = walkOid;
			while (!finished && tryCount++ <= walkMaxLines) {
				respEvent = snmp.getNext(pdu, target);
				response = respEvent.getResponse();

				if (null == response) {
					finished = true;
					break;
				} else {
					vb = response.get(0);
				}
				
				// check finish
				finished = checkWalkFinished(targetOID, pdu, vb);
				String currentOid = SnmpUtils.formatterOid(vb.getOid().toString());
				if(StringUtils.equals(lastOid, currentOid)){
					finished = true;
					LOG.info("{}:遍历到相同OID,终止遍历, OID:{}", ip, currentOid);
				}
				lastOid = currentOid;

				if (!finished) {
					map.put(SnmpUtils.getOidLastIndex(walkOid, vb.getOid().toString()), vb.getVariable().toString());
					
					// Set up the variable binding for the next entry.
					pdu.setRequestID(new Integer32(0));
					pdu.set(0, vb);
				}
			}
		} catch (Exception e) {
			LOG.error("{}:walk异常", ip, e);
		}
		return map;
	}

	/**
	 * SNMP walk 详细信息
	 *
	 * @param walkOid
	 * @return
	 */
	@Override
	public List<SnmpRow> walkDetail(String walkOid) {
		if(!status){
			return null;
		}
		walkOid = SnmpUtils.formatterOid(walkOid);
		List<SnmpRow> snmpRows = new ArrayList<>();
		try {
			PDU pdu = new PDU();
			OID targetOID = new OID(walkOid);
			pdu.add(new VariableBinding(targetOID));

			boolean finished = false;
			VariableBinding vb = null;
			ResponseEvent respEvent = null;
			PDU response = null;
			int tryCount = 0;

//			用来获取列号
			final String oidReplaceStr = walkOid + ".";
			String lastOid = walkOid;
			while (!finished && tryCount++ <= walkMaxLines) {
				respEvent = snmp.getNext(pdu, target);
				response = respEvent.getResponse();

				if (null == response) {
					finished = true;
					break;
				} else {
					vb = response.get(0);
				}

				// check finish
				finished = checkWalkFinished(targetOID, pdu, vb);
				String currentOid = SnmpUtils.formatterOid(vb.getOid().toString());
				if(StringUtils.equals(lastOid, currentOid)){
					finished = true;
					LOG.info("{}:遍历到相同OID,终止遍历, OID:{}", ip, currentOid);
				}
				lastOid = currentOid;
				if (!finished) {
					SnmpRow snmpRow = new SnmpRow();
					snmpRow.setOid(currentOid);
					snmpRow.setId(currentOid.replace(oidReplaceStr, ""));
					snmpRow.setTypeInt(vb.getVariable().getSyntax());
					snmpRow.setTypeStr(vb.getVariable().getSyntaxString());
					snmpRow.setValue(vb.getVariable().toString());
					snmpRows.add(snmpRow);

					// Set up the variable binding for the next entry.
					pdu.setRequestID(new Integer32(0));
					pdu.set(0, vb);
				}
			}
		} catch (Exception e) {
			LOG.error("{}:walk异常", ip, e);
		}
		return snmpRows;
	}

	@Override
	public Map<String, Map<String, String>> getTable(List<String> colOidList) {
		if (colOidList == null || colOidList.size() == 0){
			return null;
		}
		// 设备节点信息
		List<Map<String, String>> valueMaps = new ArrayList<Map<String, String>>();
		int colOidSize = colOidList.size();
		String oid = null;
		Map<String, String> valueMap = null;
		for (int i = 0; i < colOidSize; i++) {
			oid = colOidList.get(i).toString();
			valueMap = walk(oid);
			if (valueMap == null || valueMap.size() == 0) {
				LOG.error("{}:getTable时walk节点 [{}] 获取不到数据", ip, oid);
				valueMap = new LinkedHashMap<String, String>();
			}
			valueMaps.add(valueMap);
		}

		Map<String, Map<String, String>> oidColumsMap = new LinkedHashMap<String, Map<String, String>>();
		String colOid = null;
		for (int i = 0; i < colOidSize; i++) {
			valueMap = valueMaps.get(i);
			colOid = colOidList.get(i);
			oidColumsMap.put(colOid, valueMap);
		}
		return oidColumsMap;
	}

	/**
	 * <PER> 判断walk是否结束/判断是否有子节点<br>
	 * 
	 * 1)responsePDU == null<br>
	 * 2)responsePDU.getErrorStatus() != 0<br>
	 * 3)responsePDU.get(0).getOid() == null<br>
	 * 4)responsePDU.get(0).getOid().size() < targetOID.size()<br>
	 * 5)targetOID.leftMostCompare(targetOID.size(),responsePDU.get(0).getOid())
	 * !=0<br>
	 * 6)Null.isExceptionSyntax(responsePDU.get(0).getVariable().getSyntax())<br>
	 * 7)responsePDU.get(0).getOid().compareTo(targetOID) <= 0<br>
	 * </PER>
	 * 
	 * @param targetOID new OID("节点")
	 * @param targetOID PDU
	 * @param pdu
	 * @param vb
	 * @return
	 */
	private static boolean checkWalkFinished(OID targetOID, PDU pdu,
	        VariableBinding vb) {
		boolean finished = false;
		if (pdu.getErrorStatus() != 0) {
			finished = true;
		} else if (vb.getOid() == null) {
			finished = true;
		} else if (vb.getOid().size() < targetOID.size()) {
			finished = true;
		} else if (targetOID.leftMostCompare(targetOID.size(),
		        vb.getOid()) != 0) {
			finished = true;
		} else if (Null.isExceptionSyntax(vb.getVariable().getSyntax())) {
			finished = true;
		} else if (vb.getOid().compareTo(targetOID) <= 0) {
			finished = true;
		}
		return finished;
	}
	
	/**
	 * 异步遍历遍历snmp子节点 该方法仅作参考
	 * 
	 * @param walkOid 需要遍历的父节点,如：.1.3.6.1.2.1.1, 结尾不带0
	 * @return Map String, Object
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws Exception
	 */
	public Map<String, String> asyncWalk(String walkOid) throws InterruptedException, IOException{
		return asyncWalk(walkOid, 10);
	}

	@Override
	public Map<String, String> asyncWalk(String walkOid, int seconds) {
		if(!status){
			return null;
		}
		final Map<String, String> map = new LinkedHashMap<String, String>();
		final PDU pdu = new PDU();
		final OID targetOID = new OID(walkOid);
		final CountDownLatch latch = new CountDownLatch(1);
		pdu.add(new VariableBinding(targetOID));
		
		//创建监听对象
		ResponseListener listener = new ResponseListener() {
			@Override
			public void onResponse(ResponseEvent event) {
				((Snmp) event.getSource()).cancel(event.getRequest(), this);
				try {
					PDU response = event.getResponse();
					if (response == null) {
					} else if (response.getErrorStatus() != 0) {
					} else {
						VariableBinding vb = response.get(0);

						boolean finished = checkWalkFinished(targetOID, pdu, vb);
						if (!finished) {
							map.put(vb.getOid().toString(), vb.getVariable().toString());

							pdu.setRequestID(new Integer32(0));
							pdu.set(0, vb);
							((Snmp) event.getSource()).getNext(pdu, target, null, this);
						}
					}
				} catch (Exception e) {
					LOG.error("{}:", e, ip);
					latch.countDown();
				}
			}
		};

		try {
			snmp.getNext(pdu, target, null, listener);
			latch.await(seconds, TimeUnit.SECONDS);
		} catch (Exception e) {
			LOG.error("", e);
		}
		return map;
	}

	@Override
	public void close() {
		try {
			if (snmp != null) {
				snmp.close();
			}
		} catch (IOException e) {
		}
		status = false;
	}

    @Override
    public SnmpTable getSnmpTable(String tableOid, List<Object> columnIndexs) {
        if(!status){
            return null;
        }
        if (tableOid == null || columnIndexs == null || columnIndexs.size() == 0) {
            return null;
        }
        int oidSize = columnIndexs.size();
        OID[] oids = new OID[oidSize];

        for (int i = 0; i < oidSize; i++) {
            oids[i] = new OID(StringUtils.join(tableOid, ".", columnIndexs.get(i)));
        }

        TableUtils tu = new TableUtils(this.snmp, this);
        List<TableEvent> list = tu.getTable(target, oids, null, null);
        SnmpTable snmpTable = new SnmpTable();
        List<SnmpLine> lines = new ArrayList<SnmpLine>();
        snmpTable.setLines(lines);

        for (int i = 0; i < list.size(); ++i) {
            TableEvent te = list.get(i);
            int status = te.getStatus();
            if (status != 0) {
                String oid = oids[i].toString();
                LOG.error("{}:getTable({})出错,status={},错误信息:{}, {}", ip, oid, te.getStatus(), te.getErrorMessage(), te.getException());
                continue;
            }

            VariableBinding[] vbs = te.getColumns();
            if (vbs != null && vbs.length >= 1) {
                SnmpLine line = new SnmpLine();
                lines.add(line);
                Map<String, String> columnIdValues = new LinkedHashMap<String, String>();
                line.setColumnIdValues(columnIdValues);
                for (int j = 0; j < vbs.length; ++j) {
                    VariableBinding vb = vbs[j];
                    if (vb != null) {
                        String key = vb.getOid().toString();
                        String colID = SnmpUtils.getColID(key, tableOid);
                        
                        if (line.getId() == null) {
                            line.setId(SnmpUtils.getRowID(key, colID, tableOid));
                        }
                        columnIdValues.put(colID, vb.getVariable().toString());
                    }
                }
            }
        }
        return snmpTable;

    }

    @Override
    public PDU createPDU(Target target) {
        PDU request = new PDU();
        request.setType(-96);
        return request;
    }

    @Override
    public PDU createPDU(MessageProcessingModel messageProcessingModel) {
        PDU request = new PDU();
        request.setType(-96);
        return request;
    }

}
