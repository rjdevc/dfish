package com.rongji.dfish.framework;

import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ServletContextAware;

import com.rongji.dfish.base.info.DataBaseInfo;
import com.rongji.dfish.base.info.EthNetInfo;
import com.rongji.dfish.base.info.ServletInfo;
import com.rongji.dfish.base.info.SystemInfo;
import com.rongji.dfish.base.util.FileUtil;
import com.rongji.dfish.framework.plugin.exception.service.WrappedLog;
import com.rongji.dfish.framework.singletonimpl.DefaultIdGetter;
import com.rongji.dfish.framework.singletonimpl.DefaultPersonalConfig;
import com.rongji.dfish.framework.singletonimpl.DefaultSystemConfig;

public class InitApp implements ServletContextAware, ApplicationContextAware {
	private static final Log LOG = LogFactory.getLog(InitApp.class);
	private ServletContext servletContext;
	private ApplicationContext applicationContext;

	public void setServletContext(ServletContext context) {
		this.servletContext = context;

	}

	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.applicationContext = context;

	}

	@SuppressWarnings("deprecation")
	public void init() {
		FileUtil.LOG = new WrappedLog(FileUtil.LOG);// 记录日志错误

		LOG.info("====== initing ServletContext ======");
		SystemData.getInstance().setServletInfo(new ServletInfo(servletContext));
		LOG.info("====== ServletContext inited ======");
		LOG.info("====== initing ApplicationContext ======");
		SystemData.getInstance().setBeanFactory(applicationContext);
		LOG.info("====== ApplicationContext inited ======");
		// FIXME personalConfig systemConfig cache newIdGetter
		LOG.info("====== initing configs ======");
//		FrameworkCache<?, ?> cache = null;
//		try {
//			cache = (FrameworkCache<?, ?>) SystemData.getInstance().getBeanFactory().getBean("frameworkCacheImpl");
//		} catch (Throwable t) {
//		}
//		if (cache == null) {
//			cache = new DefaultCache();
//			LOG.info("'frameworkCacheImpl' not define. use default cache");
//		}
//		LOG.info("load cache: " + cache.getClass().getName());
//		SystemData.getInstance().setCache(cache);

		SystemConfigHolder systemConfig = null;
		try {
			systemConfig = (SystemConfigHolder) SystemData.getInstance().getBeanFactory().getBean("systemConfigImpl");
		} catch (Throwable t) {
		}
		if (systemConfig == null) {
			DefaultSystemConfig dConfig = new DefaultSystemConfig();
			systemConfig = dConfig;
			dConfig.setConfigFile("dfish-config.xml");
			LOG.info("'systemConfigImpl' not define. use default config");
		}
		LOG.info("load system config: " + systemConfig.getClass().getName());
		SystemData.getInstance().setSystemConfig(systemConfig);

		PersonalConfigHolder personalConfig = null;
		try {
			personalConfig = (PersonalConfigHolder) SystemData.getInstance().getBeanFactory()
			        .getBean("personalConfigImpl");
		} catch (Throwable t) {
		}
		if (personalConfig == null) {
			personalConfig = new DefaultPersonalConfig();
			LOG.info("'personalConfigImpl' not define. use default config");
		}
		LOG.info("load personal config: " + personalConfig.getClass().getName());
		SystemData.getInstance().setPersonalConfig(personalConfig);

		NewIdGetter newIdGetter = null;
		try {
			newIdGetter = (NewIdGetter) SystemData.getInstance().getBeanFactory().getBean("newIdGetterImpl");
		} catch (Throwable t) {
		}
		if (newIdGetter == null) {
			newIdGetter = new DefaultIdGetter();
			LOG.info("'newIdGetterImpl' not define. use default getter");
		}
		LOG.info("load new id getter: " + newIdGetter.getClass().getName());
		SystemData.getInstance().setNewIdGetter(newIdGetter);

		try {
			LOG.info("====== initing database ======");
			// PubCommonDAO dao = (PubCommonDAO)
			// SystemData.getInstance().getBeanFactory().getBean("PubCommonDAO");
			// SessionFactory sf =
			// dao.getHibernateTemplate().getSessionFactory();
			// ConnectionProvider cp =
			// ((SessionFactoryImplementor)sf).getConnectionProvider();
			// Session session = sf.openSession();
			// Connection conn = cp.getConnection();
			// SystemData.getInstance().setDataBaseInfo(new DataBaseInfo(conn));
			// cp.closeConnection(conn);
			// session.close();

			DataSource ds = (DataSource) SystemData.getInstance().getBeanFactory().getBean("dataSource");
			SystemData.getInstance().setDataBaseInfo(new DataBaseInfo(ds.getConnection()));
			LOG.info("====== database inited ======");
		} catch (Throwable t) {
			LOG.warn("====== init database fail! ======");
		}
		LOG.info("====== application has been inited ======");
		LOG.info("====  os infomation  ====");
		SystemInfo si = SystemData.getInstance().getSysinfo();
		LOG.info("os name       = " + si.getOperationSystem());
		LOG.info("file encoding = " + si.getFileEncoding());
		LOG.info("vm name       = " + si.getVmName());
		LOG.info("vm version    = " + si.getVmVersion());
		LOG.info("vm vendor     = " + si.getVmVendor());

		LOG.info("====  servlet infomation  ====");
		LOG.info("version   = " + SystemData.getInstance().getServletInfo().getServletVersion());
		LOG.info("real path = " + SystemData.getInstance().getServletInfo().getServletRealPath());
		LOG.info("====  local mac(s)  ====");
		try {
			for (Iterator<?> iter = EthNetInfo.getAllMacAddress().iterator(); iter.hasNext();) {
				String item = (String) iter.next();
				LOG.info(item);
			}
		} catch (Exception ex) {
			LOG.info("CAN NOT FIND MACS");
			LOG.error(null, ex);
		}

		if (SystemData.getInstance().getDataBaseInfo() != null) {
			LOG.info("====  database infomation  ====");
			LOG.info("db product name = " + SystemData.getInstance().getDataBaseInfo().getDatabaseProductName() + " ("
			        + SystemData.getInstance().getDataBaseInfo().getDatabaseProductVersion() + ")");
			LOG.info("db connect url = " + SystemData.getInstance().getDataBaseInfo().getDatabaseUrl());
			LOG.info("db connect user = " + SystemData.getInstance().getDataBaseInfo().getDatabaseUsername());
			LOG.info("db driver name = " + SystemData.getInstance().getDataBaseInfo().getDriverName() + " ("
			        + SystemData.getInstance().getDataBaseInfo().getDriverVersion() + ")");
		}
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
