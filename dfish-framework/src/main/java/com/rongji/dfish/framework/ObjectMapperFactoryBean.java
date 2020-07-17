package com.rongji.dfish.framework;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.beans.factory.FactoryBean;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * 用于spring-mvc默认输出JSON的时候的格式。如果需要请自行在spring配置中增加
 *  <bean id="objectMapper" class="com.rongji.dfish.framework.ObjectMapperFactoryBean"/>
 */
public class ObjectMapperFactoryBean implements FactoryBean<ObjectMapper> {

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    private String dateFormat ="yyyy-MM-dd HH:mm:ss";
    @Override
    public ObjectMapper getObject() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.setDateFormat(new SimpleDateFormat(dateFormat));
        return objectMapper;
    }

    @Override
    public Class<?> getObjectType() {
        return ObjectMapper.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
