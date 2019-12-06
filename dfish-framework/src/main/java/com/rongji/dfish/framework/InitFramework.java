package com.rongji.dfish.framework;

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
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.util.Iterator;
import java.util.Locale;

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
		FileUtil.LOG = new WrappedLog(FileUtil.LOG);// 记录日志错误

		Locale.setDefault(Locale.SIMPLIFIED_CHINESE);

		LOG.info("====== initing ServletContext ======");
		FrameworkContext.getInstance().setServletInfo(new ServletInfo(servletContext));
		LOG.info("====== ServletContext inited ======");
		LOG.info("====== initing ApplicationContext ======");
		FrameworkContext.getInstance().setBeanFactory(applicationContext);
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

		SystemConfigHolder systemConfig = null;
		try {
			systemConfig = (SystemConfigHolder) FrameworkContext.getInstance().getBeanFactory().getBean("systemConfigImpl");
		} catch (Throwable t) {
		}
		if (systemConfig == null) {
			DefaultSystemConfig dConfig = new DefaultSystemConfig();
			systemConfig = dConfig;
			dConfig.setConfigFile("dfish-config.json");
			LOG.info("'systemConfigImpl' not define. use default config");
		}
		LOG.info("load system config: " + systemConfig.getClass().getName());
		FrameworkContext.getInstance().setSystemConfig(systemConfig);

		PersonalConfigHolder personalConfig = null;
		try {
			personalConfig = (PersonalConfigHolder) FrameworkContext.getInstance().getBeanFactory()
			        .getBean("personalConfigImpl");
		} catch (Throwable t) {
		}
		if (personalConfig == null) {
			personalConfig = new DefaultPersonalConfig();
			LOG.info("'personalConfigImpl' not define. use default config");
		}
		LOG.info("load personal config: " + personalConfig.getClass().getName());
		FrameworkContext.getInstance().setPersonalConfig(personalConfig);

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

			DataSource ds = (DataSource) FrameworkContext.getInstance().getBeanFactory().getBean("dataSource");
			FrameworkContext.getInstance().setDataBaseInfo(new DataBaseInfo(ds.getConnection()));
			LOG.info("====== database inited ======");
		} catch (Throwable t) {
			LOG.error("====== init database fail! ======", t);
		}
		LOG.info("====== application has been inited ======");
		LOG.info("====  os infomation  ====");
		SystemInfo si = FrameworkContext.getInstance().getSysinfo();
		LOG.info("os name       = " + si.getOperationSystem());
		LOG.info("file encoding = " + si.getFileEncoding());
		LOG.info("vm name       = " + si.getVmName());
		LOG.info("vm version    = " + si.getVmVersion());
		LOG.info("vm vendor     = " + si.getVmVendor());

		LOG.info("====  servlet infomation  ====");
		LOG.info("version   = " + FrameworkContext.getInstance().getServletInfo().getServletVersion());
		LOG.info("real path = " + FrameworkContext.getInstance().getServletInfo().getServletRealPath());
		LOG.info("====  local mac(s)  ====");
		try {
			for (Iterator<?> iter = EthNetInfo.getAllMacAddress().iterator(); iter.hasNext();) {
				String item = (String) iter.next();
				LOG.info(item);
			}
		} catch (Exception ex) {
			LOG.error("CAN NOT FIND MACS", ex);
		}

		if (FrameworkContext.getInstance().getDataBaseInfo() != null) {
			LOG.info("====  database infomation  ====");
			LOG.info("db product name = " + FrameworkContext.getInstance().getDataBaseInfo().getDatabaseProductName() + " ("
			        + FrameworkContext.getInstance().getDataBaseInfo().getDatabaseProductVersion() + ")");
			LOG.info("db connect url = " + FrameworkContext.getInstance().getDataBaseInfo().getDatabaseUrl());
			LOG.info("db connect user = " + FrameworkContext.getInstance().getDataBaseInfo().getDatabaseUsername());
			LOG.info("db driver name = " + FrameworkContext.getInstance().getDataBaseInfo().getDriverName() + " ("
			        + FrameworkContext.getInstance().getDataBaseInfo().getDriverVersion() + ")");
		}
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
