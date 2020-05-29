package com.rongji.dfish.misc.util.json.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.base.util.json.impl.AbstractJsonBuilder;

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
        }
        return objectMapper;
    }

    @Override
    public String toJson(Object object) throws Exception {
        return getObjectMapper().writeValueAsString(object);
    }

    @Override
    public <T> T parseObject(String json, Class<T> objectClass) throws Exception {
        return getObjectMapper().readValue(json, objectClass);
    }

    @Override
    public <T> List<T> parseArray(String json, Class<T> objectClass) throws Exception {
        ObjectMapper mapper = getObjectMapper();
        CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, objectClass);
        List<T> list = mapper.readValue(json, collectionType);
        return list;
    }
}
