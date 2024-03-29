package com.rongji.dfish.misc.util.json.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.rongji.dfish.base.util.json.impl.AbstractJsonBuilder;
import com.rongji.dfish.base.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Json对象构建器采用fastjson实现
 * @author lamontYu
 * @since DFish5.0
 */
public class JsonBuilder4Fastjson extends AbstractJsonBuilder {
    
    static {
        // 因安全漏洞需设置安全模式,涉及到autoType的业务可能有问题
        ParserConfig.getGlobalInstance().setSafeMode(true);
    }

    @Override
    public void setDateFormat(String dateFormat) {
        super.setDateFormat(dateFormat);
        // FIXME fastjson全局设置日期格式,这个写法有问题,定制化的写法应该考虑实现方案
        JSON.DEFFAULT_DATE_FORMAT = dateFormat;
    }

    @Override
    public String toJson(Object obj) {
        return JSON.toJSONString(obj, SerializerFeature.WriteDateUseDateFormat);
    }

    @Override
    public <T> T parseObject(String json, Class<T> objClass) {
        if (Utils.isEmpty(json)) {
            return null;
        }
        return JSON.parseObject(json, objClass);
    }

    @Override
    public <T> List<T> parseArray(String json, Class<T> objClass) {
        if (Utils.isEmpty(json)) {
            return new ArrayList<>(0);
        }
        return JSON.parseArray(json, objClass);
    }

}
