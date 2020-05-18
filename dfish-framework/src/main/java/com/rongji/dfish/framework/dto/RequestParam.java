package com.rongji.dfish.framework.dto;

import com.rongji.dfish.framework.util.ServletUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 请求参数集合,用于数据传输过程参数调用
 * @author lamontYu
 * @since DFish5.0
 */
public class RequestParam extends LinkedHashMap<String, Object> {

    private static final long serialVersionUID = 2528247676699322059L;

    /**
     * 注册所需获取的参数
     * @param key 参数名称
     * @return 本身，这样可以继续设置其他属性
     */
    public RequestParam register(String key) {
        this.put(key, null);
        return this;
    }

    /**
     * 绑定请求,获取所有注册的参数值
     * @param request HttpServletRequest
     * @return 本身，这样可以继续设置其他属性
     */
    public RequestParam bind(HttpServletRequest request) {
        for (String key : this.keySet()) {
            String[] values = ServletUtil.getParameterValues(request, key);
            if (values == null || values.length == 0) {
                continue;
            }
            if (values.length > 1) {
                this.put(key, values);
            } else {
                // 调用
                this.put(key, values[0]);
            }
        }
        return this;
    }

    /**
     * 根据参数名称获取参数值
     * @param key 参数名称
     * @return String 参数值
     */
    public String getParameter(String key) {
        Object value = get(key);
        if (value == null) {
            return null;
        } else if (value instanceof String[]) {
            String[] valueArray = (String[]) value;
            return valueArray[0];
        }
        return value.toString();
    }

    public String[] getParameterValues(String key) {
        Object value = get(key);
        if (value == null) {
            return null;
        } else if (value instanceof String[]) {
            return (String[]) value;
        } else {
            return new String[]{ value.toString() };
        }
    }

    /**
     * 自定义设置参数值,方法相当于{@link #put(Object, Object)},使用该方法方便连续调用
     * @param key 参数名称
     * @param value 参数值
     * @return 本身，这样可以继续设置其他属性
     */
    public RequestParam putParameter(String key, Object value) {
        this.put(key, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : this.entrySet()) {
            if (entry.getValue() != null) {
                String entryKey = entry.getKey();
                Object entryValue = entry.getValue();
                if (entryValue != null && !"".equals(entryValue)) {
                    if (entryValue instanceof String[]) {
                        for (String value : (String[]) entryValue) {
                            if (value != null && !"".equals(value)) {
                                sb.append('&');
                                sb.append(entryKey);
                                sb.append('=');
                                try {
                                    sb.append(java.net.URLEncoder.encode(value, "UTF-8"));
                                } catch (UnsupportedEncodingException e) {
                                    sb.append(value);
                                }
                            }
                        }
                    } else {
                        sb.append('&');
                        sb.append(entryKey);
                        sb.append('=');
                        try {
                            sb.append(java.net.URLEncoder.encode(entryValue.toString(), "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            sb.append(entryValue);
                        }
                    }
                }

            }
        }
        return sb.toString();
    }

}
