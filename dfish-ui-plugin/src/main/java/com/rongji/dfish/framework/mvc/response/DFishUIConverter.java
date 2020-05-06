package com.rongji.dfish.framework.mvc.response;

import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.ui.Node;
import com.rongji.dfish.ui.json.JsonFormat;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;

/**
 * 用于转化JSON 并输出
 * @author LinLW
 *
 */
public class DFishUIConverter extends ObjectToJsonConverter {

	@Override
	protected boolean supports(Class<?> clz) {
		return Node.class.isAssignableFrom(clz);
	}

    @Override
    protected void writeInternal(final Object obj, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {

        final String objJson = JsonFormat.toJson(obj);//输出使用DFish专有的输出，td可能会用简写格式
        byte[] ba = objJson.getBytes(CHARSET);
        outputMessage.getHeaders().setAcceptCharset(ACCEPT_CHARSETS);
        outputMessage.getHeaders().setContentType(new MediaType("text", "json", CHARSET));
        outputMessage.getHeaders().setExpires(0);
        outputMessage.getHeaders().setContentLength(ba.length);
        outputMessage.getBody().write(ba);
        outputMessage.getBody().close();
//        LogUtil.debug(objJson);
        LogUtil.lazyDebug(()-> JsonFormat.formatJson(objJson));//输出和日志不一致，日志带带格式
    }

}
