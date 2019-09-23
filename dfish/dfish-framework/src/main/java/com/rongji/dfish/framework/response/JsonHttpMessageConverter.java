package com.rongji.dfish.framework.response;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.rongji.dfish.ui.JsonObject;
import com.rongji.dfish.ui.json.J;
import com.rongji.dfish.ui.template.DFishTemplate;

/**
 * 用于转化JSON 并输出
 * @author LinLW
 *
 */
public class JsonHttpMessageConverter extends AbstractHttpMessageConverter<Object>{
	/**
	 * 日志器
	 */
	public static final Log LOG = LogFactory.getLog(JsonHttpMessageConverter.class);
	/**
	 * 构造函数
	 */
	public JsonHttpMessageConverter(){
		super(new MediaType[] { new MediaType("text", "json", CHARSET), MediaType.ALL });
	}
	
	@Override
	protected Object readInternal(Class<? extends Object> clz,
			HttpInputMessage inputMessage) throws IOException,
			HttpMessageNotReadableException {
		return null;
	}

	@Override
	protected boolean supports(Class<?> clz) {
		return JsonObject.class.isAssignableFrom(clz);
	}
	
	private static Charset CHARSET=Charset.forName("UTF-8");
	private static List<Charset> ACCEPT_CHARSETS= Collections.singletonList(CHARSET);
	private static final Executor SINGLE_EXECUTOR=Executors.newSingleThreadExecutor();
	@Override
	protected void writeInternal(final Object obj, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		
		final String objJson=getObjectJson(obj);
		byte[] ba=objJson.getBytes(CHARSET);
		outputMessage.getHeaders().setAcceptCharset(ACCEPT_CHARSETS);
		outputMessage.getHeaders().setContentType(new MediaType("text","json",CHARSET));
		outputMessage.getHeaders().setExpires(0);
		outputMessage.getHeaders().setContentLength(ba.length);
		outputMessage.getBody().write(ba);
		outputMessage.getBody().close();
		if(LOG.isDebugEnabled()){
			SINGLE_EXECUTOR.execute(new Runnable(){
				public void run(){
					LOG.debug((obj instanceof DFishTemplate) ? objJson : J.formatJson(objJson));
				}
			});
		}
	}
	
	protected String getObjectJson(Object obj) {
		if (obj == null) {
			return "";
		}
		return ((JsonObject) obj).asJson();
	}

}
