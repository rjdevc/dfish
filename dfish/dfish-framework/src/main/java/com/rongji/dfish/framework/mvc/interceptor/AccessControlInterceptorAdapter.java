package com.rongji.dfish.framework.mvc.interceptor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rongji.dfish.framework.mvc.interceptor.AccessControl;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.rongji.dfish.framework.FrameworkHelper;

/**
 * 一般业务方法需要扩展与这个类实现hasPermission(String loginUser, String[] keys);方法。
 * 判定是否有权限。这个方法默认cacheExpired毫秒期间是相同参数是不是调用第二次的。用于提高性能。
 * 你可能需要在applicationContext-mvc.xml 增加一段
 * <mvc:interceptors>    
        <!-- 多个拦截器,顺序执行 -->    
        <mvc:interceptor>    
          <!-- 如果不配置或/**,将拦截所有的Controller -->  
           <mvc:mapping path="/**" />   
           <!-- 界面展示之前做一些通用处理   -->  
           <bean class="com.rongji.fzwp.common.controller.YourInterceptorAdapter">
            	<property name="cacheExpired" value="300000"/>
           </bean>    
        </mvc:interceptor>    
    </mvc:interceptors> 
 * @author DFish Team
 *
 */
public abstract class AccessControlInterceptorAdapter extends HandlerInterceptorAdapter{
	private long cacheExpired=300000L;
	
	public long getCacheExpired() {
		return cacheExpired;
	}

	public void setCacheExpired(long cacheExpired) {
		this.cacheExpired = cacheExpired;
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {  
        
		if(!(handler instanceof HandlerMethod)){
			//对于访问 图片和css等静态资源的请求直接放过
			return true;
		}
        //处理Permission Annotation，实现方法级权限控制  
        HandlerMethod method = (HandlerMethod)handler;  
        AccessControl permission = method.getMethodAnnotation(AccessControl.class);
        //如果为空在表示该方法不需要进行权限验证  
        if (permission == null) { 
        	permission = method.getMethod().getDeclaringClass().getAnnotation(AccessControl.class);
        	if(permission == null){
        		return true;  
        	}
        }  
          
        //验证是否具有权限  
        String loginUser=FrameworkHelper.getLoginUser(request);
        if(!hasPermissionCache(loginUser, permission.keys())){
        	response.sendError(HttpServletResponse.SC_FORBIDDEN,"you have no permission to acess "+request.getRequestURI());
        	return false;
        }
        return true;  
        //注意此处必须返回true，否则请求将停止  
    }
	Map<PermKey,PermResult> permMap= Collections.synchronizedMap(new HashMap<PermKey,PermResult>());
	/**
	 * 如果缓存中有数据并且数据时间不超过expired 则启用缓存中的数据。
	 * @param loginUser
	 * @param keys
	 * @return
	 */
	private boolean hasPermissionCache(String loginUser, String[] keys) {
		if(cacheExpired<=0){
			return hasPermission(loginUser, keys);
		}
		PermKey pk=new PermKey(loginUser,keys);
		PermResult cacheRet=permMap.get(pk);
		if(cacheRet==null||cacheRet.getBorn()+cacheExpired<System.currentTimeMillis()){
			boolean ret=hasPermission(loginUser,keys);
			cacheRet=new PermResult(ret);
			permMap.put(pk, cacheRet);
		}
		return cacheRet.isOk();
	}
	/**
	 * 判断登录人员是否有这些key的权限。
	 * @param loginUser
	 * @param keys
	 * @return
	 */
	protected abstract boolean hasPermission(String loginUser, String[] keys);
	
	static class PermKey{
		static char PAD='$';
		int _hash;
		String core=null;
		public PermKey(String loginUser, String[] keys){
			StringBuilder sb=new StringBuilder(loginUser);
			for(String key:keys){
				sb.append(PAD);
				sb.append(key);
			}
			core=sb.toString();
		}
		public int hashCode(){
			if(_hash==0){
				_hash=core.hashCode();
			}
			return _hash;
		}
		public boolean equals(Object o){
			if(o==this){
				return true;
			}
			PermKey cast=(PermKey)o;
			return cast.core.equals(core);
		}
	}
	
	static class PermResult{
		boolean ok;
		long born;
		public boolean isOk() {
			return ok;
		}
		public long getBorn() {
			return born;
		}
		public PermResult(boolean ok){
			this.ok=ok;
			born=System.currentTimeMillis();
		}
	}
  
}
