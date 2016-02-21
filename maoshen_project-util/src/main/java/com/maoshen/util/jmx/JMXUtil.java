package com.maoshen.util.jmx;

import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.rmi.RMIConnector;
import javax.management.remote.rmi.RMIServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maoshen.util.StringUtil;

/**
 * JMX工具类 * 
 */
public class JMXUtil {

	private static Logger logger = LoggerFactory.getLogger(JMXUtil.class);

	/**
	 * 得到JMX的连接
	 * 
	 * @param ip
	 *            网址
	 * @param port
	 *            端口
	 * @param environment
	 *            环境
	 * @return JMX连接
	 */
	public static JMXConnector getConnect(String ip, String port,
			Map<String, ?> environment) {
		if (StringUtil.isNullOrBlank(ip) || StringUtil.isNullOrBlank(port)) {
			logger.error("ip和端口不能为空.");
			return null;
		}
		try {
			Registry registry = LocateRegistry.getRegistry(ip,
					Integer.parseInt(port));
			RMIServer stub = (RMIServer) registry.lookup("jmxrmi");
			JMXConnector jmxc = new RMIConnector(stub, environment);
			jmxc.connect();
			return jmxc;
		} catch (Exception e) {
			logger.error("连接JMX服务器失败.");
		}
		return null;
	}

	/**
	 * 得到JMX的连接
	 * 
	 * @param ip
	 *            网址
	 * @param port
	 *            端口
	 * @return JMX连接
	 */
	public static JMXConnector getConnect(String ip, String port) {
		return getConnect(ip, port, null);
	}

	/**
	 * 注册MBean
	 * 
	 * @param mbeanObject
	 * @param objectName
	 */
	public static boolean registerMBean(Object mbeanObject, String objectName) {
		try {
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			mbs.registerMBean(mbeanObject, new ObjectName(objectName));
			logger.info("成功注册MBean[{}]", objectName);
			return true;
		} catch (Exception e) {
			logger.error("注册MBean[" + objectName + "]失败", e);
		}
		return false;
	}

	/**
	 * 查询远程MBean
	 * 
	 * @param mbsConnection
	 * @return
	 */
	public static Set<ObjectName> queryMBeanNames(MBeanServerConnection msc,
			String mbeanName) {
		try {
			ObjectName objectName = new ObjectName(mbeanName);
			return msc.queryNames(objectName, null);
		} catch (Exception e) {
			logger.error("查询MBean Name列表[" + mbeanName + "]失败", e);
		}
		return null;
	}

}
