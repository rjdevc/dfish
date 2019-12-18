package com.rongji.dfish.framework.dto;

import com.rongji.dfish.base.util.StringUtil;
import com.rongji.dfish.framework.util.ServletUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求参数集合,用于数据传输过程参数调用
 * @author lamontYu
 * @since 5.0
 * @date 2019-12-04 13:19
 */
public class RequestParam extends HashMap<String, String[]> {

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
    public String getStringValue(String key) {
        return StringUtil.toString(get(key));
    }

    /**
     * 自定义设置参数值
     * @param key 参数名称
     * @param str 参数值
     * @return this
     */
    public RequestParam putStringValue(String key, String str) {
        this.put(key, str == null ? null : str.split(","));
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
