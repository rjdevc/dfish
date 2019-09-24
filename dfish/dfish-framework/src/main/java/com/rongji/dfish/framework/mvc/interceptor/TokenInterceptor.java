package com.rongji.dfish.framework.mvc.interceptor;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenInterceptor extends HandlerInterceptorAdapter{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if(!(handler instanceof HandlerMethod)){
			//对于访问 图片和css等静态资源的请求直接放过
			return true;
		}
		HandlerMethod method = (HandlerMethod)handler;  
		if("login".equals(method.getMethod().getName())||"readme".equals(method.getMethod().getName())){
			return true;
		}
		String accessToken=request.getParameter("access_token");
		if(accessToken==null|| "".equals(accessToken.trim())){
			forbiddenTip(response);
			return false;
		}
//		String auth=loginService.getAuth(accessToken);
//		if(auth==null||auth.equals("")){
//			forbiddenTip(response);
//			return false;
//		}
		return true;
//		return super.preHandle(request, response, handler);
	}
	private void forbiddenTip(HttpServletResponse response) {
		ServletOutputStream sos=null;
		try {
			sos= response.getOutputStream();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json;charset=UTF8");
//			JsonResponse<String> ret=new JsonResponse<String>(){};
//			ret.setErrCode("SYS00403");
//			ret.setErrMsg("该接口需要提供access_token");
//			sos.write(J.toJson(ret).getBytes("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(sos!=null){
				try {
					sos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
