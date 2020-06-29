package com.rongji.dfish.misc.util.json.impl;

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
            // 属性为null的不序列化
//            objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
//                @Override
//                public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
//                    jsonGenerator.writeString("");
//                }
//            });
            if (Utils.notEmpty(dateFormat)) {
                objectMapper.setDateFormat(new SimpleDateFormat(dateFormat));
            }
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
