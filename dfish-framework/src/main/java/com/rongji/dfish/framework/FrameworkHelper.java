/*
 * FrameworkHelper.java
 * 
 * Copyright 2009 Rongji Enterprise, Inc. All rights reserved.
 * 
 * Rongji PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package com.rongji.dfish.framework;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rongji.dfish.base.util.StringUtil;
import com.rongji.dfish.framework.config.PersonalConfigHolder;
import com.rongji.dfish.framework.config.SystemConfigHolder;
//import com.rongji.dfish.framework.service.NewIdGetter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.dao.PubCommonDAO;
import com.rongji.dfish.framework.plugin.exception.service.WrappedLog;
import com.rongji.dfish.ui.JsonObject;
import com.rongji.dfish.ui.json.J;

/**
 *  框架帮助类
 * 
 * @author	I-Task Team
 * @version	1.0.0
 * @since	1.0.0	ZHL		2009-9-8
 */
public class FrameworkHelper{
	
	public static final String[] ENCODINGS={"unknown","utf-8","gbk","gb2312","iso8859-1","unicode"};

	/**
	 * @see
	 */

	public static final int INT_ENCODING_UNKNOWN=0;
	public static final int INT_ENCODING_UTF_8=1;
	public static final int INT_ENCODING_GBK=2;
	public static final int INT_ENCODING_GB2312=3;
	public static final int INT_ENCODING_ISO8859_1=4;
	public static final int INT_ENCODING_UNICODE=5;
	
	public static final String ENDLINE = "\r\n";
	
	/**
	 * 默认字符集
	 */
	public static final String ENCODING = ENCODINGS[INT_ENCODING_UTF_8];
	
	public static final Log LOG = new WrappedLog(LogFactory.getLog(FrameworkHelper.class));
	
	/**
	 * 登录人员信息在session中的名字
	 */
	public static final String LOGIN_USER_KEY = "com.rongji.dfish.LOGIN_USER_KEY";
//	/**
//	 * 图标
//	 */
//	public static final String ICON_SAVE="img/b/save.gif";
//	public static final String ICON_DELETE="img/b/delete.gif";
//	public static final String ICON_NEW="img/b/new.gif";
	/** 资源文件包路径 */
	private static final String LANG_FILE_PATH="com.rongji.dfish.lang.framework";
//	public static final String OPENER_PATH = "javascript:DFish.g_dialog(this).fromView.path";
//	public static final String VALUE_STRING_ZERO = "0";
//	public static final String VALUE_STRING_ONE = "1";
//	/** true */
//	public static final String VALUE_STRING_TRUE="true";
//	/** false */
//	public static final String VALUE_STRING_FALSE="false";
	/** 资源文件缓存 */
	private static HashMap<Locale, ResourceBundle> languageResource=new HashMap<Locale, ResourceBundle>();
	

//	FrameworkCache frameworkCache;
//	NewIdGetter newIdGetter;
	PersonalConfigHolder personnalConfig;
	SystemConfigHolder systemConfig;
	
	private static Executor SINGLE_EXECUTOR=Executors.newSingleThreadExecutor();
	
	/**
	 * 
	 * 取得request中的Locale定义 首先是session中是否有定义 -- 用户选择的 其次看request本身的字符集 -- 浏览器的
	 * 最后看服务器的字符集 -- 当没有足够的信息的时候，使用服务器默认字符集
	 * 
	 * @param request
	 * @return
	 */

	/**
	 * 取得服务器默认字符集。 与Locale.getDefault()不同，它会是用配置的默认字符集。
	 * 
	 * @return
	 */
	public static Locale getLocale(){
		String value = FrameworkContext.getInstance().getSystemConfig().getProperty(
				"framework.pub.sysdefaultlocale");
		if (value != null) {
			Locale[] locs = Locale.getAvailableLocales();
			for (Locale locale : locs) {
				if (value.equals(locale.toString())) {
                    return locale;
                }
			}
		}
		return Locale.getDefault();
	}

	/**
	 * 取得定制的语言
	 * 
	 * @param loc
	 *            Locale 方言
	 * @param msgKey
	 *            String 消息关键字
	 * @param args
	 *            Object
	 * @return String
	 */
	public static String getMsg(Locale loc,String msgKey,Object... args){
		String value = null;
		try {
			value = findLangResource(loc).getString(msgKey);
		} catch (MissingResourceException ex) {
			return msgKey;
		}
		if (args == null || args.length == 0) {
			return value;
		}
		return java.text.MessageFormat.format(value, args);
	}

	/**
	 * 找到语言资源文件
	 * 
	 * @param loc
	 *            Locale
	 * @return ResourceBundle
	 */
	private static ResourceBundle findLangResource(Locale loc){
		ResourceBundle rb = languageResource.get(loc);
		if (rb != null) {
			return rb;
		}
		if (loc == null) {
			loc = Locale.ENGLISH;
		}
		try {
			rb = ResourceBundle.getBundle(LANG_FILE_PATH, loc);
			if (rb != null) {
				languageResource.put(loc, rb);
			}
			return rb;
		} catch (MissingResourceException ex) {
			LOG.error("can not find language resource file: " + ex.getClassName(), ex);
			throw ex;
		}
	}
	
	public static void outputJson(HttpServletResponse response, JsonObject jsonObject) {
		outputJson(response, jsonObject.asJson());
	}
	
	public static void outputJson(HttpServletResponse response, final String content) {
		outputContent(response, content, "text/json");
		SINGLE_EXECUTOR.execute(new Runnable(){
			@Override
			public void run(){
				if(LOG.isDebugEnabled()){
					// debug级别的字符输出需要格式化
					LOG.debug("\r\n" + J.formatJson(content));
				}
			}
		});
	}
	
	private static void outputContent(HttpServletResponse response, final String content, String contentType) {
		BufferedOutputStream bos = null;
		try {
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setHeader("Content-Type", contentType + "; charset=" + ENCODING);
			bos = new BufferedOutputStream(response.getOutputStream());
			// 调试用语句
			bos.write(content.getBytes(ENCODING));
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
			} catch (Exception ex1) {
				ex1.printStackTrace();
			}
		}
	}

	/**
	 * 如果提交的数据有附件等mutipart的部分，不可以用AJAX提交
	 * 这时候用的是传统HTTP提交方式。
	 * 所以返回的时候需要利用一个页面进行引导跳转。
	 * 这时候，不能调用outPutXML 方法，调用这个方法返回既可以
	 *
	 * @param request
	 * @param response
	 * @param xml
	 * @return
	 */
	public static ModelAndView setViewFeedbkForMultiDataForm(MultipartHttpServletRequest request,HttpServletResponse response,String xml){
		request.setAttribute("type", "view");
		request.setAttribute("fbk", xml);
		return new ModelAndView("/webapp/pub/pub_jump.jsp");
	}

	public static ModelAndView setJspFeedbkForMultiDataForm(MultipartHttpServletRequest request,HttpServletResponse response,String xml){
		request.setAttribute("type", "jsp");
		request.setAttribute("fbk", xml);
		return new ModelAndView("/webapp/pub/pub_jump.jsp");
	}

//	/**
//	 * 对SPRING MVC WEB的支持 由于底层的问题。所以我们在前端如果有批量文件上传的时候， 如果名字为file1
//	 * 实际上在web页面中由多个文件上传的控件。 他们的名字分别是file1-1 file-2这样子后面的序号有可能不连续。 因为前端本身可以取消上传。
//	 * 这个方法。就是为了透明的取得file1名字的文件。并以数组的形式存在 类似于request.getParameterValues(String);
//	 *
//	 * @param request
//	 * @param attachFileName
//	 * @return
//	 */
//	public static MultipartFile[] getFiles(MultipartHttpServletRequest request,String attachFileName){
//		Iterator<?> iter = request.getFileNames();
//		final int nameLen = attachFileName.length() + 1;
//		List<MultipartFile> list = new ArrayList<MultipartFile>();
//		while (iter.hasNext()) {
//			MultipartFile file = request.getFile((String) iter.next());
//			if (file.getName().startsWith(attachFileName) && file.getOriginalFilename() != null
//					&& !file.getOriginalFilename().equals("")) {
//				list.add(file);
//			}
//		}
//		Collections.sort(list, new Comparator<MultipartFile>() {
//			public int compare(MultipartFile o1, MultipartFile o2) {
//				String s1 = o1.getName().substring(nameLen);
//				String s2 = o1.getName().substring(nameLen);
//
//				return Integer.parseInt(s1, 32) - Integer.parseInt(s2, 32);
//			}
//		});
//
//		return list.toArray(new MultipartFile[list.size()]);
//	}
//
//	/**
//	 * 上传一个文件。该文件将存放于系统的上传路径下。默认是
//	 * ${web-root}/upload文件夹
//	 * @param urlPattern 比如说mydir/yyyyMM/ATTACH_ID.EXT其中有特殊含义的字符解释如下
//	 * <ul>
//	 * <li>yyyy 表示当前日期的年份</li>
//	 * <li>MM 表示当前日期的月份</li>
//	 * <li>dd 表示当前日期的日期</li>
//	 * <li>HH 表示当前时间的小时数</li>
//	 * <li>mm 表示当前时间的分钟数</li>
//	 * <li>ss 表示当前时间的秒数</li>
//	 * <li>ATTACH_ID 表示使用这个文件在数据库中对应的编号</li>
//	 * <li>.EXT 表示使用这个文件的扩展名，由于安全性考虑.jsp的文件扩展名会自动扩充成.jsp.txt</li>
//	 * </ul>
//	 * @param file 需要上传的附件
//	 * @param userId 操作人员编号 用于标识这个文件是哪个人上传的。
//	 * @return
//	 * @throws IOException
//	 * @deprecated 在过程资源库没完成前，这个暂时不推荐使用
//	 */
//	@Deprecated
//	public static String[] uploadFile(String urlPattern,MultipartFile file,String userId) throws IOException{
//
//		String attachId = getNewId("PubAttach", "attachId", "00000001");// GetUniqueId.getPubAttachId();
//		String fileName = file.getName();
//		int j = fileName.lastIndexOf(".");
//		String ext = "";
//		if (j >= 0) {
//			ext = fileName.substring(j);
//			if (ext.equalsIgnoreCase(".jsp")) {
//				ext += ".txt";
//			}
//		}
////		long fileSize = file.getSize();
//		String relaPath = "";
//		String relaFile = attachId + ext;
//		String realFileName = attachId + ext;
//		if (urlPattern != null && !urlPattern.equals("") && !urlPattern.equals("/")) {
//			String now = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
//			String year = now.substring(0, 4);
//			String month = now.substring(4, 6);
//			String date = now.substring(6, 8);
//			String hour = now.substring(8, 10);
//			String minute = now.substring(10, 12);
//			String second = now.substring(12, 14);
//			relaFile = urlPattern.replace("yyyy", year).replace("MM", month).replace("dd", date)
//					.replace("HH", hour).replace("mm", minute).replace("ss", second).replace(
//							"ATTACH_ID", attachId).replace(".EXT", ext);
//			if (relaFile.lastIndexOf("/") > 0) {
//				relaPath = relaFile.substring(0, relaFile.lastIndexOf("/") + 1);
//				realFileName = relaFile.substring(relaFile.lastIndexOf("/") + 1);
//			}
//		}
//
////		String attachUrl = FrameworkContext.getInstance().getServletInfo().getServletRealPath()
////				+ "upload/" + relaFile; // 直接存变化的url
//		String saveToPath = FrameworkContext.getInstance().getServletInfo().getServletRealPath()
//				+ "upload/";
//		FileUtil.saveFile(file.getInputStream(), saveToPath + relaPath, realFileName);
////		PubAttach att = new PubAttach();
////		att.setAttachId(attachId);
////		att.setAttachName(fileName);
////		att.setAttachSize(fileSize);
////		att.setAttachUrl(attachUrl);
////		att.setUpTime(new Date());
////		att.setUpAuthor(userId);
////		getDAO().saveObject(att);
//		return new String[] { attachId, relaFile };
//	}
	
	/**
	 * 取得这个登录人员使用的皮肤的配色，具体配色代码含义要根据皮肤本身的定义。
	 * @param request
	 * @return
	 */
	public static int getSkinColor(HttpServletRequest request){
		Integer skinColor = (Integer) request.getSession().getAttribute("com.rongji.dfish.SKIN_COLOR");
		return skinColor == null ? 1 : skinColor.intValue();
	}

	/**
	 * 获取数据库操作通用接口（PubCommonDAO）对象实例
	 * 
	 * @return
	 */
	public static PubCommonDAO getDAO(){
		return (PubCommonDAO) FrameworkContext.getInstance().getBeanFactory().getBean("pubCommonDAO");
	}

	/**
	 * 获取数据库操作通用接口对象实例
	 * @param daoName DAO的名字
	 * @return
	 */
	public static Object getDAO(String daoName){
		return  FrameworkContext.getInstance().getBeanFactory().getBean(daoName);
	}

	/**
	 * 获取bean工厂
	 * @return
	 */
	public static BeanFactory getBeanFactory() {
		BeanFactory factory = FrameworkContext.getInstance().getBeanFactory();
		if(factory==null){
			throw new NullPointerException("Bean factory is not initialized. Please check your environment. (applicationContext.xml) ");
		}
		return factory;
	}
	
	/**
	 * 获取Bean对象
	 * @param beanClass the class of bean
	 * @return T bean对象
	 */
	public static <T> T getBean(Class<T> beanClass) {
		return getBean(getBeanFactory(), null, beanClass);
	}
	
	/**
	 * 获取Bean对象
	 * @param beanId 声明的bean id
	 * @param beanClass the class of bean
	 * @return T bean对象
	 */
	public static <T> T getBean(String beanId, Class<T> beanClass) {
		return getBean(getBeanFactory(), beanId, beanClass);
	}
	
	/**
	 * 获取Bean对象
	 * @param beanId 声明的bean id
	 * @return bean对象
	 */
	public static Object getBean(String beanId) {
		return getBean(getBeanFactory(), beanId, null);
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * 获取Bean对象
	 * @param factory bean工厂
	 * @param beanId 声明的bean id
	 * @param beanClass the class of bean
	 * @return T bean对象
	 * @return
	 */
	private static <T> T getBean(BeanFactory factory, String beanId, Class<T> beanClass) {
		if (factory == null || (Utils.isEmpty(beanId) && beanClass == null)) {
			return null;
		}
		T bean = null;
		if (Utils.notEmpty(beanId) && beanClass != null) {
			bean = factory.getBean(beanId, beanClass);
		} else if (Utils.notEmpty(beanId)) {
			bean = (T) factory.getBean(beanId);
		} else {
			try {
				bean = factory.getBean(beanClass);
	        } catch (NoUniqueBeanDefinitionException e) {
	        	// 取出来bean异常的原因有可能是很多Service继承PubService,导致根据Type找的时候找到多个匹配的实例会报错
	        	beanId = beanClass.getSimpleName();
	        	if (Utils.notEmpty(beanId)) {
	        		beanId = beanId.substring(0, 1).toLowerCase() + beanId.substring(1);
	    			bean = factory.getBean(beanId, beanClass);
	        	} else {
	        		throw e;
	        	}
	        }
		}
		
		return bean;
	}
	
//	/**
//	 * 根据类获取bean对象,若该类有多个实现类,会再尝试根据该类的默认id再获取bean对象
//	 * @param beanClass bean对象对应的类
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//    public static <T> T getBean(Class<T> beanClass) {
//		BeanFactory bf = FrameworkContext.getInstance().getBeanFactory();
//		T bean = null;
//		try {
//			bean = (T) BEAN_MAP.get(beanClass);
//			if (bean == null) {
//				bean = bf.getBean(beanClass);
//			}
//        } catch (NoUniqueBeanDefinitionException e) {
//        	String beanId = beanClass.getSimpleName();
//        	if (Utils.notEmpty(beanId)) {
//        		beanId = beanId.substring(0, 1).toLowerCase() + beanId.substring(1);
//    			bean = getBean(beanId, beanClass);
//        	} else {
//        		throw e;
//        	}
//        }
//		if (bean != null) {
//			BEAN_MAP.put(beanClass, bean);
//		}
//		
//		return bean;
//	}
//	
//	/**
//	 * 根据声明的类bean的id获取bean对象
//	 * @param beanId
//	 * @return
//	 */
//	public static Object getBean(String beanId) {
//		return FrameworkContext.getInstance().getBeanFactory().getBean(beanId);
//	}
//	
//	/**
//	 * 根据声明的类bean的id和该类获取bean对象
//	 * @param beanId 
//	 * @param beanClass
//	 * @return
//	 */
//	public static <T> T getBean(String beanId, Class<T> beanClass) {
//		return FrameworkContext.getInstance().getBeanFactory().getBean(beanId, beanClass);
//	}

	/**
	 * 是否调试模式
	 * 
	 * @author ZHL V1.0.0;
	 * @return
	 * @deprecated 现在分级别，不再使用这个参数
	 */
	@Deprecated
	public static boolean isDebugOn(){
		boolean isDebugOn = false;
		if ("1".equals(getSystemConfig("framework.pub.debugOn", "1"))) {
			isDebugOn = true;
		}
		return isDebugOn;
	}
	
	/**
	 * 获取当前登录用户
	 * @param request
	 * @return
	 */
	public static String getLoginUser(HttpServletRequest request){
		return (String) request.getSession().getAttribute(LOGIN_USER_KEY);
	}
	
	public static void setLoginUser(HttpServletRequest request, String loginUser) {
		request.getSession().setAttribute(LOGIN_USER_KEY, loginUser);
	}
	
	public static void removeLoginUser(HttpServletRequest request) {
		request.getSession().removeAttribute(LOGIN_USER_KEY);
	}
	
//	/**
//	 * 缓存名称-配置
//	 */
//	private static final String CACHE_CONFIG = "SYSTEM_CONFIG";
//	/**
//	 * 配置缓存
//	 */
//	private static Cache<String, String> configCache;
//	
//	@SuppressWarnings("unchecked")
//    private static Cache<String, String> getConfigCache() {
//		// FIXME 有可能将缓存方法放在这里还是实现中取待考虑
//		// 获取缓存
//		if (configCache == null) {
//			try {
//				configCache = getBean(Cache.class);
//            } catch (Exception e) {
//            }
//		}
//		if (configCache == null) {
//			// 默认缓存,容错处理
//			configCache = new SizeAndTimeLimitCache<String, String>();
//		}
//		return configCache;
//	}
	
	/**
	 * 取得系统配置，
	 * 
	 * @param key
	 *            关键字 如sysarg.attach.freetaskAttachPath等
	 * @param defaultValue
	 *            默认值。如果系统配置信息没有的时候，则显示默认值
	 * @return
	 */
	public static String getSystemConfig(String key,String defaultValue){
//		Cache<String, String> cache = getConfigCache();
//		String value = cache.get(key);
//		if (Utils.isEmpty(value)) { // 缓存中获取不到或者缓存已过期
//			value = FrameworkContext.getInstance().getSystemConfig().getProperty(key);// properties.getProperty(key);
//		}
//		if (Utils.notEmpty(value)) {
//			// 需要加到缓存中
//			cache.put(key, value);
//			return value;
//		}
		String value = FrameworkContext.getInstance().getSystemConfig().getProperty(key);
		return Utils.isEmpty(value) ? defaultValue : value;
	}
	public static Integer getSystemConfigAsInteger(String key,Integer defaultValue){
//		Cache<String, String> cache = getConfigCache();
//		String strValue = cache.get(key);
//		if (Utils.isEmpty(strValue)) {
//			strValue = FrameworkContext.getInstance().getSystemConfig().getProperty(key);// properties.getProperty(key);
//		}
		String strValue = getSystemConfig(key, null);
		if (Utils.notEmpty(strValue)) {
			try{
				Integer intValue = new Integer(strValue);
				// 需要加到缓存中
//			cache.put(key, strValue);
				return intValue;
			}catch(Exception ex){}
		}
	
		return defaultValue;
	}

	/**
	 * 将系统配置保存至配置文件中（xml）
	 * 
	 * @param key
	 * @param value
	 */
	public static void setSystemConfig(String key,String value){
		FrameworkContext.getInstance().getSystemConfig().setProperty(key, value);
//		Cache<String, String> cache = getConfigCache();
//		// 配置设置成功后加入缓存中去
//		cache.put(key, value);
	}

	/**
	 * 更新缓存 
	 *
	 */
	public static void resetSystemConfig(){
//	    String realPath=FrameworkContext.getInstance().getServletInfo().getServletRealPath();
//	    
//		String configPath =realPath+"WEB-INF/config/dfish-config.xml";
		FrameworkContext.getInstance().getSystemConfig().reset();
//		Cache<String, String> cache = getConfigCache();
//		cache.clear();
	}

	/**
	 * 取得人员个人配置中，每页显示多少条数据的配置值
	 * 
	 * @param userId
	 * @return
	 */
	public static int getPersonalPageSize(String userId){
		String pageSize = getPersonalConfig(userId, "person.rows_per_page");
		try {
			return Integer.parseInt(pageSize);
		} catch (Exception ex) {
			return 10;
		}
	}

	/**
	 * 创建一个Page实体,总行数和总页数会在数据库查询的时候填充.
	 * 
	 * @param userId
	 * @param currentPage
	 * @return
	 */
	public static Page createPersonalPage(String userId,String currentPage){
		Page page=new Page();
		
		int curPage = 1;
		if (Utils.notEmpty(currentPage)) {
			curPage = Integer.parseInt(currentPage);
		}
		page.setCurrentPage(curPage);
		page.setPageSize(getPersonalPageSize(userId));
		
		return page;
	}

	/**
	 * 根据个人用户的每页行数,计算当前高亮行
	 * 
	 * @param sum
	 * 			总行数
	 * @param userId
	 * @return
	 */
	public static int getCurrentHightLight(int sum,String userId){
		int current=0;
	
		int pageSize=getPersonalPageSize(userId);
		
		if(sum>0){//确保删除第一个时是在第一上
			current=(sum-1) % pageSize;
		}
		
		return current;
	}

	/**
	 * 根据个人用户的每页行数,计算当前页
	 * 
	 * @param sum
	 * @param userId
	 * @return
	 */
	public static int getCurrentPage(int sum,String userId){
		int current=0;
		
		int pageSize=getPersonalPageSize(userId);
		
		current=(sum-1)/pageSize+1;
		
		return current;
	}

	/**
	 * 公用获取个人配置参数配置方法 如果当前个人设置为空则自动取默认值
	 * 
	 * @param userId
	 *            String
	 * @param argStr
	 *            String
	 * @return String
	 */
	public static String getPersonalConfig(String userId,String argStr){
		return FrameworkContext.getInstance().getPersonalConfig().getProperty(userId,argStr);
		
	}
	public static Integer getPersonalConfigAsInteger(String userId,String argStr){
		String strValue= FrameworkContext.getInstance().getPersonalConfig().getProperty(userId,argStr);
		if(strValue==null) {
            return null;
        }
		try{
			return new Integer(strValue);
		}catch(Exception ex){
			if(!"default".equals(userId)){
				strValue=FrameworkContext.getInstance().getPersonalConfig().getProperty("default",argStr);
				if(strValue==null) {
                    return null;
                }
				try{
					return new Integer(strValue);
				}catch(Exception ex2){}
			}
		}
		return null;
	}

	/**
	 * 公用设置个人配置参数配置方法 如果当前个人设置为空则自动新增
	 * 
	 * @param userId
	 *            String
	 * @param argStr
	 *            String
	 * @param value
	 *            String
	 * @return String
	 */
	public static void setPersonalConfig(String userId,String argStr,String value){
		FrameworkContext.getInstance().getPersonalConfig().setProperty(userId,argStr,value);
	}


	/**
	 * 查询数据库，获取满足条件对象的数量,如果结果集中有多条,也只取第1条
	 * 调用时需明确查询的结果是否结果集可能为空比如 select max(t.xxx)...当数据库中都没有记录是返回的是空集,此时调用时要判断空值,否则会抛出异常.
	 * 
	 * @param strSql
	 *            类似：SELECT COUNT(*) FROM PubUser...
	 * @param args
	 * @return
	 */
	public static Integer getIntegerFromDataBase(final String strSql,final Object... args){
		Integer intRes=null;
		List<?> list = getDAO().getQueryList(strSql, args);
//		assert (list.size() == 1);
		if(Utils.notEmpty(list)){
			Object o=list.get(0);
			if(o!=null){
				intRes=((Number) o).intValue();
			}
		}
		return intRes;
	}

	/**
	 * 查询数据库当结果集中只有一条记录或只想取结果集中的第一条记录时,返回该记录的实体对象,
	 * 
	 * @param strSql
	 * @param args
	 * @return
	 */
	public static Object getSingleObjectFromDataBase(String strSql,Object... args){
		Object o=null;
		List<?> list = getDAO().getQueryList(strSql, args);
		if(Utils.notEmpty(list)){
			o=list.get(0);
		}
		return o;
	}

//	/**
//	 * 向数据库中插入一条新数据时，该数据的主键不可重复，可通过此方法获取该编号。 ID是一个十进制数，位数是一定的。如果前面没有数字，会自动补0。
//	 * 这适用于只需确保在一个表中ID唯一的的情况，不适合新建人员时人员编号的生成。
//	 *
//	 * @param clzName
//	 *            持久化对象名字
//	 * @param idName
//	 *            ID字段字
//	 * @param initId
//	 *            如果这个表里面并没有内容，那么将使用这个ID。否则用表里面最大的ID加+1。注意，长度与原先表最大的ID一致。
//	 *            当initId>1时,表示该编号是从设定的初始值开始递增,小于初始值的编号是预留的.//ZHL
//	 * @return
//	 */
//	public static String getNewId(String clzName,String idName,String initId){
//		return FrameworkContext.getInstance().getNewIdGetter().getNewId(clzName,idName,initId);
//	}
	
	/**
	 * 获取IP
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request){
		String ip=request.getHeader("X-Forwarded-For");
		if(Utils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)){
			ip=request.getHeader("Proxy-Client-IP");
		}
		if(Utils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)){
			ip=request.getHeader("WL-Proxy-Client-IP");
		}
		if(Utils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)){
			ip=request.getRemoteAddr();
		}
		
		return ip;   
	}  

	public static void traceHttpParameters(HttpServletRequest request){
		StringBuilder sb=new StringBuilder();
		sb.append("============PARAMETERS IN HTTP REQUEST================\r\n");
		sb.append("URI = ");
		sb.append(request.getRequestURI());
		sb.append("\r\n");
		Enumeration<?> enum1= request.getParameterNames();
		while (enum1.hasMoreElements()){
			String key=(String) enum1.nextElement();
			String[] values=request.getParameterValues(key);
			if(values==null||values.length==0){
				sb.append(key);
				sb.append(" : ");
				sb.append("NULL");
				sb.append("\r\n");
			}else if(values.length==1){
				sb.append(key);
				sb.append(" : ");
				sb.append(values[0]);
				sb.append("\r\n");
			}else{
				sb.append(key);
				sb.append(" : [");
				for(int i=0;i<values.length;i++){
					if(i>0) {
                        sb.append(", ");
                    }
					sb.append(values[i]);
				}
				sb.append("]\r\n");
			}
		}
		LOG.info(sb);
	}
	/**
	 * <p>取得某个路径下的文本文档的内容</p>
	 * <p>文本可以是JS/HTML/CSS等，但一般不建议使用JSP的内容。
	 * 因为其动态内容不会被执行。</p>
	 * <p>path 为从webcontext开始的绝对路径<br/>
	 * 如一个应用访问地址是 http://www.foo.com/myapp/<br/>
	 * 他的一个动态页面地址是http://www.foo.com/myapp/service/testingServ.sp?act=index<br/>
	 * 里面需要一个HTML的内容。这个HTML位于http://www.foo.com/myapp/res/introduce/total.html<br/>
	 * 那么这个path应该是/res/introduce/total.html或前面无斜杠res/introduce/total.html<br/>
	 * 而不是../res/introduce/total.html<br/>
	 * 也不是/myapp/res/introduce/total.html<br/>
	 * </p>
	 * <p>这个方法会自动识别文本的字符集</p>
	 * <p>典型用法:</p>
	 * <pre>
	 * HtmlPanel htmlPanel =new HtmlPanel("f_intro",null);
	 * htmlPanel.setFilter(false);
	 * htmlPanel.setHtml(getFileText("/res/introduce/total.html"));
	 * </pre>
	 * @param path 路径
	 * @return 文本内容
	 * @since 2012/2/2
	 */
	public static String getFileText(String path){
		@SuppressWarnings("deprecation")
		String rootPath=FrameworkContext.getInstance().getServletInfo().getServletRealPath();
		if(path.startsWith("/")||path.startsWith("\\")){
			path=path.substring(1);
		}
		try{
			String text= getFileText(new File(rootPath+path));
			return text;
		}catch(IOException ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 取得某个文件的文本。自动识别字符集
	 * swf等非文本文件，会出现乱码。
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String getFileText(File file)throws IOException{
		InputStream is=null;
		try{
			is=new BufferedInputStream(new FileInputStream(file));
			return getText(is);
		}catch(IOException ex){
			throw ex;
		}finally{
			if(is!=null){
				is.close();
			}
		}
	}
	/**
	 * 从流中读取文件内容。自动识别字符集
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String getText(InputStream is) throws IOException{
		StreamContent content=getContent(is);
		return new String(content.baos.toByteArray(),content.findCharset);
	}
	private static StreamContent getContent(InputStream is) throws IOException {
		final StreamContent content = new StreamContent();
//		nsDetector det = new nsDetector(nsDetector.CHINESE);
//		det.Init(new nsICharsetDetectionObserver() {
//			public void Notify(String s) {
//				content.findCharset = s;
//			}
//		});
		byte buff[] = new byte[1024];
		boolean done = false;
		boolean isAscii = true;
		int j;
		while ((j = is.read(buff, 0, buff.length)) >= 0) {
			content.baos.write(buff, 0, j);
			content.findCharset=StringUtil.detCharset(buff);
//			if (isAscii)
//				isAscii = det.isAscii(buff, j);
//			if (!isAscii && !done)
//				done = det.DoIt(buff, j, false);// 模糊匹配的话，可以第一次找到就跳出
		}


//		if (content.findCharset == null) {
//			String chars[] = det.getProbableCharsets();
//			if (chars != null && chars.length > 1) {
//				content.findCharset = chars[0];
//			} else {
//				content.findCharset = System.getProperty("file.encoding");
//			}
//		}
//		if("GB2312".equalsIgnoreCase(content.findCharset)&&Charset.isSupported("GBK")){
//			content.findCharset="GBK";//一般中文机器支持GBK
//		}
		return content;
	}

	private static class StreamContent{
		String findCharset;
		ByteArrayOutputStream baos =new ByteArrayOutputStream();
	}
	static final String[] MOBILE_SPECIFIC_SUBSTRING = {   
        "iPad","iPhone","Android",
        "Opera Mobi", "Opera Mini","IEMobile","MSIEMobile","UCWEB", 
        "Symbian","Windows Phone","Windows CE","WindowsCE","BlackBerry",
        "HP iPAQ","Smartphone","MIDP",
        "240x320","176x220","320x320","160x160",  
        };
	/**
	 * 判断请求是否是移动设备发来，
	 * 该判断是通过UA来判断的，所以只能识别正规的请求。
	 * @param request
	 * @return
	 */
	public static boolean isFromMobile(HttpServletRequest request) {
	     String userAgent = request.getHeader("user-agent");
	     if(userAgent==null) {
             return false;
         }
	     for (String mobile: MOBILE_SPECIFIC_SUBSTRING){
	           if (userAgent.contains(mobile)||
	        		   userAgent.contains(mobile.toLowerCase())||
	        		   userAgent.contains(mobile.toUpperCase())){
	                  return true;
	          }
	     }
	     return false;
	}

	/**
	 * 判断请求是否来自DFish框架
	 * @param request
	 * @return
	 */
	public static boolean isFromDFish(HttpServletRequest request) {
		String req = request.getHeader("x-requested-with");
		return "dfish".equals(req);
	}


}