package com.rongji.dfish.misc.util.json.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.rongji.dfish.base.Utils;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author lamontYu
 * @since
 */
public class JsonBuilder4Jackson extends AbstractJsonBuilder {

    private ObjectMapper objectMapper;

    protected ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            if (Utils.notEmpty(dateFormat)) {
                objectMapper.setDateFormat(new SimpleDateFormat(dateFormat));
            }
            // 属性为null或“”的不序列化
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        }
        return objectMapper;
    }

    @Override
    public String toJson(Object object) throws Exception {
        if (object == null) {
            return null;
        }
        return getObjectMapper().writeValueAsString(object);
    }

    @Override
    public <T> T parseObject(String json, Class<T> objectClass) throws Exception {
        if (Utils.isEmpty(json)) {
            return null;
        }
        return getObjectMapper().readValue(json, objectClass);
    }

    @Override
    public <T> List<T> parseArray(String json, Class<T> objectClass) throws Exception {
        if (Utils.isEmpty(json)) {
            return null;
        }
        ObjectMapper mapper = getObjectMapper();
        CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, objectClass);
        List<T> list = mapper.readValue(json, collectionType);
        return list;
    }
}
