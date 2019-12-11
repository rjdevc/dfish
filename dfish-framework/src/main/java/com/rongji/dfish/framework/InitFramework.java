package com.rongji.dfish.framework;

import com.rongji.dfish.base.context.BaseBeanContextHodler;
import com.rongji.dfish.base.context.BeanContextHolder;
import com.rongji.dfish.base.context.PropertiesContextHolder;
import com.rongji.dfish.base.context.SystemContext;
import com.rongji.dfish.base.info.DataBaseInfo;
import com.rongji.dfish.base.info.EthNetInfo;
import com.rongji.dfish.base.info.SystemInfo;
import com.rongji.dfish.base.util.FileUtil;
import com.rongji.dfish.framework.config.PersonalConfigHolder;
import com.rongji.dfish.framework.config.SystemConfigHolder;
import com.rongji.dfish.framework.config.impl.DefaultPersonalConfig;
import com.rongji.dfish.framework.config.impl.DefaultSystemConfig;
import com.rongji.dfish.framework.info.ServletInfo;
import com.rongji.dfish.framework.util.WrappedLog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class InitFramework implements ServletContextAware, ApplicationContextAware {
	private static final Log LOG = LogFactory.getLog(InitFramework.class);
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

	@SuppressWarnings("deprecation")
	public void init() {
		Locale.setDefault(Locale.SIMPLIFIED_CHINESE);

		SystemContext.getInstance().register(new PropertiesContextHolder("systemProperties",System.getProperties()));
		SystemContext.getInstance().register(new PropertiesContextHolder("systemEnv",System.getenv()));
		BaseBeanContextHodler frameworkContext=new BaseBeanContextHodler ("frameworkContext");
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

		LOG.info("====== initing ServletContext ======");
		ServletInfo servi=new ServletInfo(servletContext);
		frameworkContext.add(servi);
		Map<String,String> servletInfoProps=new TreeMap<String,String>();
		servletInfoProps.put("serverInfo",servi.getServerInfo());
		servletInfoProps.put("servletRealPath",servi.getServletRealPath());
		servletInfoProps.put("servletVersion",servi.getServletVersion());
		SystemContext.getInstance().register(new PropertiesContextHolder("servletInfo",servletInfoProps));
		LOG.info("====== ServletContext inited ======");
		LOG.info("====== initing ApplicationContext ======");
		frameworkContext.add(applicationContext);
		frameworkContext.add(servletContext);
		LOG.info("====== ApplicationContext inited ======");
		// FIXME personalConfig systemConfig cache newIdGetter
		LOG.info("====== initing configs ======");
//		FrameworkCache<?, ?> cache = null;
//		try {
//			cache = (FrameworkCache<?, ?>) FrameworkContext.getInstance().getBeanFactory().getBean("frameworkCacheImpl");
//		} catch (Throwable t) {
//		}
//		if (cache == null) {
//			cache = new DefaultCache();
//			LOG.info("'frameworkCacheImpl' not define. use default cache");
//		}
//		LOG.info("load cache: " + cache.getClass().getName());
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
			LOG.info("'systemConfigImpl' not define. use default config");
		}
		LOG.info("load system config: " + systemConfig.getClass().getName());
		frameworkContext.add(systemConfig);

		PersonalConfigHolder personalConfig = null;
		try {
			personalConfig = (PersonalConfigHolder) SystemContext.getInstance()
			        .get("personalConfigImpl");
		} catch (Throwable t) {
		}
		if (personalConfig == null) {
			personalConfig = new DefaultPersonalConfig();
			LOG.info("'personalConfigImpl' not define. use default config");
		}
		LOG.info("load personal config: " + personalConfig.getClass().getName());
		frameworkContext.add(personalConfig);

//		NewIdGetter newIdGetter = null;
//		try {
//			newIdGetter = (NewIdGetter) FrameworkContext.getInstance().getBeanFactory().getBean("newIdGetterImpl");
//		} catch (Throwable t) {
//		}
//		if (newIdGetter == null) {
//			newIdGetter = new DefaultIdGetter();
//			LOG.info("'newIdGetterImpl' not define. use default getter");
//		}
//		LOG.info("load new id getter: " + newIdGetter.getClass().getName());
//		FrameworkContext.getInstance().setNewIdGetter(newIdGetter);

		try {
			LOG.info("====== initing database ======");
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
			LOG.info("====== database inited ======");
		} catch (Throwable t) {
			LOG.error("====== init database fail! ======", t);
		}
		LOG.info("====== application has been inited ======");
		LOG.info("====  os infomation  ====");
		LOG.info("os name       = " + si.getOperationSystem());
		LOG.info("file encoding = " + si.getFileEncoding());
		LOG.info("vm name       = " + si.getVmName());
		LOG.info("vm version    = " + si.getVmVersion());
		LOG.info("vm vendor     = " + si.getVmVendor());

		LOG.info("====  servlet infomation  ====");
		LOG.info("version   = " + servi.getServletVersion());
		LOG.info("real path = " + servi.getServletRealPath());
		LOG.info("====  local mac(s)  ====");

		try {
			systemInfoProps.put("macAddress",EthNetInfo.getMacAddress());
			for (Iterator<?> iter = EthNetInfo.getAllMacAddress().iterator(); iter.hasNext();) {
				String item = (String) iter.next();
				LOG.info(item);
			}
		} catch (Exception ex) {
			LOG.error("CAN NOT FIND MACS", ex);
		}

		DataBaseInfo dbInfo=SystemContext.getInstance().get(DataBaseInfo.class);
		if (dbInfo != null) {
			LOG.info("====  database infomation  ====");
			LOG.info("db product name = " + dbInfo .getDatabaseProductName() + " ("
			        + dbInfo .getDatabaseProductVersion() + ")");
			LOG.info("db connect url = " + dbInfo.getDatabaseUrl());
			LOG.info("db connect user = " + dbInfo.getDatabaseUsername());
			LOG.info("db driver name = " + dbInfo.getDriverName() + " ("
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
