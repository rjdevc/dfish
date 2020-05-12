package com.rongji.dfish.framework;

import com.rongji.dfish.base.context.BaseBeanContextHolder;
import com.rongji.dfish.base.context.BeanContextHolder;
import com.rongji.dfish.base.context.PropertiesContextHolder;
import com.rongji.dfish.base.context.SystemContext;
import com.rongji.dfish.base.info.DataBaseInfo;
import com.rongji.dfish.base.info.EthNetInfo;
import com.rongji.dfish.base.info.SystemInfo;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.framework.config.PersonalConfigHolder;
import com.rongji.dfish.framework.config.SystemConfigHolder;
import com.rongji.dfish.framework.config.impl.DefaultPersonalConfig;
import com.rongji.dfish.framework.config.impl.DefaultSystemConfig;
import com.rongji.dfish.framework.info.ServletInfo;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * 框架初始化配置类
 */
public class InitFramework implements ServletContextAware, ApplicationContextAware {
	private ServletContext servletContext;
	private ApplicationContext applicationContext;

	@Override
    public void setServletContext(ServletContext context) {
		this.servletContext = context;

	}

	@Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.applicationContext = context;
	}

	/**
	 * 初始化配置信息
	 */
	@SuppressWarnings("deprecation")
	public void init() {
		Locale.setDefault(Locale.SIMPLIFIED_CHINESE);

		SystemContext.getInstance().register(new PropertiesContextHolder("systemProperties",System.getProperties()));
		SystemContext.getInstance().register(new PropertiesContextHolder("systemEnv",System.getenv()));
		BaseBeanContextHolder frameworkContext=new BaseBeanContextHolder("frameworkContext");
		SystemContext.getInstance().register(frameworkContext);
		SystemInfo si=new SystemInfo();
		frameworkContext.add(new SystemInfo());
		Map<String,String> systemInfoProps=new TreeMap<String,String>();
		systemInfoProps.put("fileEncoding",si.getFileEncoding());
		systemInfoProps.put("operationSystem",si.getOperationSystem());
		systemInfoProps.put("vmName",si.getVmName());
		systemInfoProps.put("vmVendor",si.getVmVendor());
		systemInfoProps.put("vmVersion",si.getVmVersion());
		systemInfoProps.put("cpu",si.getCpu());
		systemInfoProps.put("javaVersion",si.getJavaVersion());
		systemInfoProps.put("runtimeName",si.getRuntimeName());
		systemInfoProps.put("runtimeVersion",si.getRuntimeVersion());
		SystemContext.getInstance().register(new PropertiesContextHolder("systemInfo",systemInfoProps));

		LogUtil.info("====== initing ServletContext ======");
		ServletInfo servi=new ServletInfo(servletContext);
		frameworkContext.add(servi);
		Map<String,String> servletInfoProps=new TreeMap<String,String>();
		servletInfoProps.put("serverInfo",servi.getServerInfo());
		servletInfoProps.put("servletRealPath",servi.getServletRealPath());
		servletInfoProps.put("servletVersion",servi.getServletVersion());
		SystemContext.getInstance().register(new PropertiesContextHolder("servletInfo",servletInfoProps));
		LogUtil.info("====== ServletContext inited ======");
		LogUtil.info("====== initing ApplicationContext ======");
		frameworkContext.add(applicationContext);
		frameworkContext.add(servletContext);
		LogUtil.info("====== ApplicationContext inited ======");
		// FIXME personalConfig systemConfig cache newIdGetter
		LogUtil.info("====== initing configs ======");
//		FrameworkCache<?, ?> cache = null;
//		try {
//			cache = (FrameworkCache<?, ?>) FrameworkContext.getInstance().getBeanFactory().getBean("frameworkCacheImpl");
//		} catch (Throwable t) {
//		}
//		if (cache == null) {
//			cache = new DefaultCache();
//			LogUtil.info("'frameworkCacheImpl' not define. use default cache");
//		}
//		LogUtil.info("load cache: " + cache.getClass().getName());
//		FrameworkContext.getInstance().setCache(cache);
		BeanContextHolder beanFactory=new BeanContextHolder(){
			@Override
			public String getScope() {
				return "beanFactory";
			}

			@Override
			public Object get(String name) {
				return applicationContext.getBean(name);
			}

			@Override
			public <T> T get(Class<T> clz) {
				return applicationContext.getBean(clz);
			}
		};
		SystemContext.getInstance().register(beanFactory);
		SystemConfigHolder systemConfig = null;
		try {
			systemConfig = (SystemConfigHolder)applicationContext.getBean("systemConfigImpl");
		} catch (Throwable t) {
		}
		if (systemConfig == null) {
			DefaultSystemConfig dConfig = new DefaultSystemConfig();
			systemConfig = dConfig;
			dConfig.setConfigFile("dfish-config.json");
			LogUtil.info("'systemConfigImpl' not define. use default config");
		}
		LogUtil.info("load system config: " + systemConfig.getClass().getName());
		frameworkContext.add(systemConfig);

		PersonalConfigHolder personalConfig = null;
		try {
			personalConfig = (PersonalConfigHolder) SystemContext.getInstance()
			        .get("personalConfigImpl");
		} catch (Throwable t) {
		}
		if (personalConfig == null) {
			personalConfig = new DefaultPersonalConfig();
			LogUtil.info("'personalConfigImpl' not define. use default config");
		}
		LogUtil.info("load personal config: " + personalConfig.getClass().getName());
		frameworkContext.add(personalConfig);

//		NewIdGetter newIdGetter = null;
//		try {
//			newIdGetter = (NewIdGetter) FrameworkContext.getInstance().getBeanFactory().getBean("newIdGetterImpl");
//		} catch (Throwable t) {
//		}
//		if (newIdGetter == null) {
//			newIdGetter = new DefaultIdGetter();
//			LogUtil.info("'newIdGetterImpl' not define. use default getter");
//		}
//		LogUtil.info("load new id getter: " + newIdGetter.getClass().getName());
//		FrameworkContext.getInstance().setNewIdGetter(newIdGetter);

		try {
			LogUtil.info("====== initing database ======");
			// PubCommonDAO dao = (PubCommonDAO)
			// FrameworkContext.getInstance().getBeanFactory().getBean("PubCommonDAO");
			// SessionFactory sf =
			// dao.getHibernateTemplate().getSessionFactory();
			// ConnectionProvider cp =
			// ((SessionFactoryImplementor)sf).getConnectionProvider();
			// Session session = sf.openSession();
			// Connection conn = cp.getConnection();
			// FrameworkContext.getInstance().setDataBaseInfo(new DataBaseInfo(conn));
			// cp.closeConnection(conn);
			// session.close();

			DataSource ds = (DataSource) SystemContext.getInstance().get("dataSource");
			DataBaseInfo dataBaseInfo=new DataBaseInfo(ds.getConnection());
			frameworkContext.add(dataBaseInfo);
			Map<String,String> dataBaseInfoProps=new TreeMap<String,String>();
			dataBaseInfoProps.put("databaseUrl",dataBaseInfo.getDatabaseUrl());
			dataBaseInfoProps.put("databaseUsername",dataBaseInfo.getDatabaseUsername());
			dataBaseInfoProps.put("databaseProductName",dataBaseInfo.getDatabaseProductName());
			dataBaseInfoProps.put("databaseProductVersion",dataBaseInfo.getDatabaseProductVersion());
			dataBaseInfoProps.put("driverName",dataBaseInfo.getDriverName());
			dataBaseInfoProps.put("driverVersion",dataBaseInfo.getDriverVersion());
			dataBaseInfoProps.put("databaseType",String.valueOf(dataBaseInfo.getDatabaseType()));
			SystemContext.getInstance().register(new PropertiesContextHolder("dataBaseInfo",dataBaseInfoProps));
			LogUtil.info("====== database inited ======");
		} catch (Throwable t) {
			LogUtil.error("====== init database fail! ======", t);
		}
		LogUtil.info("====== application has been inited ======");
		LogUtil.info("====  os infomation  ====");
		LogUtil.info("os name       = " + si.getOperationSystem());
		LogUtil.info("file encoding = " + si.getFileEncoding());
		LogUtil.info("vm name       = " + si.getVmName());
		LogUtil.info("vm version    = " + si.getVmVersion());
		LogUtil.info("vm vendor     = " + si.getVmVendor());

		LogUtil.info("====  servlet infomation  ====");
		LogUtil.info("version   = " + servi.getServletVersion());
		LogUtil.info("real path = " + servi.getServletRealPath());
		LogUtil.info("====  local mac(s)  ====");

		try {
			systemInfoProps.put("macAddress",EthNetInfo.getMacAddress());
			for (Iterator<?> iter = EthNetInfo.getAllMacAddress().iterator(); iter.hasNext();) {
				String item = (String) iter.next();
				LogUtil.info(item);
			}
		} catch (Exception ex) {
			LogUtil.error("CAN NOT FIND MACS", ex);
		}

		DataBaseInfo dbInfo=SystemContext.getInstance().get(DataBaseInfo.class);
		if (dbInfo != null) {
			LogUtil.info("====  database infomation  ====");
			LogUtil.info("db product name = " + dbInfo .getDatabaseProductName() + " ("
			        + dbInfo .getDatabaseProductVersion() + ")");
			LogUtil.info("db connect url = " + dbInfo.getDatabaseUrl());
			LogUtil.info("db connect user = " + dbInfo.getDatabaseUsername());
			LogUtil.info("db driver name = " + dbInfo.getDriverName() + " ("
			        + dbInfo.getDriverVersion() + ")");
		}
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
