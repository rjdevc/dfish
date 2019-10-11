package com.rongji.dfish.base.info;

import java.io.File;

import javax.servlet.ServletContext;

/**
 * ServletInfo 提供对单前servlet信息的获取。
 * 
 * @author DFish Team
 * 
 */
public final class ServletInfo {

	private String serverInfo;

	private int majorVersion;

	private int minorVersion;

	private String servletVersion;

	private String servletRealPath;
	private ServletContext servletContext;

	/**
	 * 通过传递ServletContext 构建信息对象
	 * 
	 * @param context
	 */
	public ServletInfo(ServletContext context) {
		servletContext = context;
		serverInfo = context.getServerInfo();
		majorVersion = context.getMajorVersion();
		minorVersion = context.getMinorVersion();
		servletVersion = new StringBuffer().append(majorVersion).append('.')
				.append(minorVersion).toString();
//		try {
//			servletRealPath = context.getResource("/").getFile();
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
			servletRealPath=context.getRealPath("/");
//		}
		if (servletRealPath!=null && !servletRealPath.endsWith(File.separator)
				&& !servletRealPath.endsWith("/")) {
			servletRealPath += File.separator;
		}
	}

	/**
	 * 取得主版本号，如tomcat5.5是2.4的servlet容器，这里将取得2
	 * @return
	 */
	public int getMajorVersion() {
		return majorVersion;
	}

	/**
	 * 取得分版本号，如tomcat5.5是2.4的servlet容器，这里将取得4
	 * @return
	 */
	public int getMinorVersion() {
		return minorVersion;
	}
	/**
	 * 取得服务器描述
	 * @return
	 */
	public String getServerInfo() {
		return serverInfo;
	}
	/**
	 * 取得servlet容器版本号，如tomcat5.5是2.4的servlet容器，这里将取得2.4
	 * @return
	 */
	public String getServletVersion() {
		return servletVersion;
	}

	/**
	 * 取得servlet运行期的真实路径。比如说 E:\tomcat5.5\webapps\myapp\
	 * 这个方式不适合war包部署的情况，含websphere上部署的情况，慎用
	 * @return String
	 */
	public String getServletRealPath() {
		return servletRealPath;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}
}
