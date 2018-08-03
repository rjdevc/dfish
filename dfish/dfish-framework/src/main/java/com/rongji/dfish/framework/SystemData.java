package com.rongji.dfish.framework;

import java.io.OutputStreamWriter;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;
import org.springframework.beans.factory.BeanFactory;

import com.rongji.dfish.base.info.DataBaseInfo;
import com.rongji.dfish.base.info.ServletInfo;
import com.rongji.dfish.base.info.SystemInfo;


/**
 * 系统信息 这些信息将在启动的时候加载，供其他功能调用
 * 
 * @author DFish Team
 * 
 */
public class SystemData {
	private BeanFactory factory;

	private SystemInfo sysinfo = new SystemInfo();

	/**
	 * 设置spring的bean工厂
	 * 
	 * @param factory
	 */
	public void setBeanFactory(BeanFactory factory) {
		this.factory = factory;
	}

	/**
	 * 取得spring的bean工厂
	 * 比如取得某个Bean 可以使用
	 * <pre>
	 * BeanFactory bf = SystemData.getInstance().getBeanFactory();
	 * MyClass bean = (MyClass) bf.getBean("idDefinedInSpringConfig");
	 * </pre>
	 * @return
	 */
	public BeanFactory getBeanFactory() {
		return factory;
	}
	/**
	 * 取得一个日志记录器
	 * @param clz
	 * @return
	 */
	public static org.apache.log4j.Logger getLogger(Class<?> clz) {
		org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(clz);
		if (!logger.getAllAppenders().hasMoreElements()) {
			System.out
					.println("====== no log4j appender.try to config one. ====== ");
			ConsoleAppender capp = new ConsoleAppender();
			PatternLayout layout = new PatternLayout();
			layout
					.setConversionPattern("%d{yyyy-MM-dd HH:mm:ss,SSS} [%c]-[%p] %m%n");
			capp.setTarget(ConsoleAppender.SYSTEM_OUT);
			capp.setName("default");
			capp.setWriter(new OutputStreamWriter(System.out));
			capp.setLayout(layout);
			
			logger.addAppender(capp);
		}
		return logger;
	}
	public static org.apache.log4j.Logger LOGGER=getLogger(SystemData.class);

	private SystemConfigHolder systemConfig;
	private PersonalConfigHolder PersonalConfig;
	private FrameworkCache<?, ?> cache;
	private NewIdGetter newIdGetter;
	
	private SystemData() {}

	private static SystemData instance;
	/**
	 * 取得实例
	 * @return
	 */
	public static SystemData getInstance() {
		if (instance == null)
			synchronized (SystemData.class) {
				if (instance == null)
					instance = new SystemData();
			}
		return instance;
	}

	/**
	 * 取得系统配置
	 * @return
	 */
	public SystemConfigHolder getSystemConfig() {
		return systemConfig;
	}
	/**
	 * 设置系统配置，这个一般不由应用系统设置，而是由平台调用
	 * @param systemConfig
	 */
	public void setSystemConfig(SystemConfigHolder systemConfig) {
		this.systemConfig = systemConfig;
	}

	private DataBaseInfo dataBaseInfo = null;
	/**
	 * 获取数据库信息
	 * @return
	 */
	public DataBaseInfo getDataBaseInfo() {
		return dataBaseInfo;
	}
	/**
	 * 设置数据库信息，这个一般不由应用系统设置，而是由平台调用
	 * @param dataBaseInfo
	 */
	public void setDataBaseInfo(DataBaseInfo dataBaseInfo) {
		this.dataBaseInfo = dataBaseInfo;
	}

	private ServletInfo servletInfo;
	/**
	 * 设置Servlet信息，这个一般不由应用系统设置，而是由平台调用
	 * @param info
	 */
	public void setServletInfo(ServletInfo info) {
		servletInfo = info;
	}

	/**
	 * 取得Servlet信息
	 */
	public ServletInfo getServletInfo() {
		return servletInfo;
	}

	/**
	 * 取得系统信息
	 */
	public SystemInfo getSysinfo() {
		return sysinfo;
	}

	public PersonalConfigHolder getPersonalConfig() {
		return PersonalConfig;
	}

	public void setPersonalConfig(PersonalConfigHolder personalConfig) {
		PersonalConfig = personalConfig;
	}

	public FrameworkCache<?, ?> getCache() {
		return cache;
	}

	public void setCache(FrameworkCache<?, ?> cache) {
		this.cache = cache;
	}

	public NewIdGetter getNewIdGetter() {
		return newIdGetter;
	}

	public void setNewIdGetter(NewIdGetter newIdGetter) {
		this.newIdGetter = newIdGetter;
	}
	
}
