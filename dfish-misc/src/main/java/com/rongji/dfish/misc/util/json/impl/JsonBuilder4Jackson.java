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

    protected ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

//        JavaTimeModule javaTimeModule = new JavaTimeModule();
//        /** 序列化配置,针对java8 时间 **/
//        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
//
//        /** 反序列化配置,针对java8 时间 **/
//        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        if (Utils.notEmpty(dateFormat)) {
            objectMapper.setDateFormat(new SimpleDateFormat(dateFormat));
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
