package com.rongji.dfish.framework.mvc.response;

import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.JsonUtil;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

public class ObjectToJsonConverter extends AbstractHttpMessageConverter<Object> {

    /**
     * 构造函数
     */
    public ObjectToJsonConverter() {
        super(new MediaType[]{new MediaType("text", "json", CHARSET), MediaType.ALL});
    }

    @Override
    protected Object readInternal(Class<?> clz, HttpInputMessage inputMessage) throws IOException,
            HttpMessageNotReadableException {
        return null;
    }

    private static Charset CHARSET = Charset.forName("UTF-8");
    private static List<Charset> ACCEPT_CHARSETS = Collections.singletonList(CHARSET);

    @Override
    protected void writeInternal(final Object obj, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {

        final String objJson = getObjectJson(obj);
        byte[] ba = objJson.getBytes(CHARSET);
        outputMessage.getHeaders().setAcceptCharset(ACCEPT_CHARSETS);
        outputMessage.getHeaders().setContentType(new MediaType("text", "json", CHARSET));
        outputMessage.getHeaders().setExpires(0);
        outputMessage.getHeaders().setContentLength(ba.length);
        outputMessage.getBody().write(ba);
        outputMessage.getBody().close();
        LogUtil.debug(objJson);
    }

    protected String getObjectJson(Object obj) {
        if (obj == null) {
            return "";
        }
        return JsonUtil.toJson(obj);
    }

    @Override
    protected boolean supports(Class<?> clz) {
        return true;
    }

}
