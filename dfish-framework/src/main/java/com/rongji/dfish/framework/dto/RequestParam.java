package com.rongji.dfish.framework.dto;

import com.rongji.dfish.base.util.Utils;
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
public class RequestParam extends LinkedHashMap<String, String[]> {

    private static final long serialVersionUID = 2528247676699322059L;

    /**
     * 注册所需获取的参数
     * @param key 参数名称
     * @return this
     */
    public RequestParam register(String key) {
        this.put(key, null);
        return this;
    }

    /**
     * 绑定请求,获取所有注册的参数值
     * @param request HttpServletRequest
     * @return this
     */
    public RequestParam bind(HttpServletRequest request) {
        for (String key : this.keySet()) {
            // 调用
            this.put(key, ServletUtil.getParameterValues(request, key));
        }
        return this;
    }

    /**
     * 根据参数名称获取参数值
     * @param key 参数名称
     * @return String 参数值
     */
    public String getParameter(String key) {
        String[] value = get(key);
        return Utils.notEmpty(value) ? value[0] : null;
    }

    /**
     * 自定义设置参数值
     * @param key 参数名称
     * @param str 参数值
     * @return this
     */
    public RequestParam putParameter(String key, String str) {
        this.put(key, str == null ? null : new String[]{ str });
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String[]> entry : this.entrySet()) {
            if (entry.getValue() != null) {
                String key = entry.getKey();
                for (String value : entry.getValue()) {
                    if (value != null && !"".equals(value)) {
                        sb.append('&');
                        sb.append(key);
                        sb.append('=');
                        try {
                            sb.append(java.net.URLEncoder.encode(value, "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            sb.append(value);
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

}
