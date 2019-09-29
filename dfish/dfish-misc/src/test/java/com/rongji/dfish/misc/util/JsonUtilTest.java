package com.rongji.dfish.misc.util;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.serializer.SerializeConfig;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonUtilTest {

    static class TestObject {
        private static final long serialVersionUID = 5152257293173984525L;
        private String name;
        private List<String> list;
        private String[] array;
        private Date time;
        private List<Date> timeList;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }

        public String[] getArray() {
            return array;
        }

        public void setArray(String[] array) {
            this.array = array;
        }

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }

        public List<Date> getTimeList() {
            return timeList;
        }

        public void setTimeList(List<Date> timeList) {
            this.timeList = timeList;
        }
    }

    @Test
    public void test() {

        TestObject obj = new TestObject();
        obj.setName("名称");
//		obj.setList(Arrays.asList("1", "2", "3"));
        obj.setArray(new String[]{"11", "22", "33"});
        obj.setTime(new Date());
        obj.setTimeList(Arrays.asList(new Date(), new Date(), new Date()));
        System.out.println(JsonUtil.toJson(obj));


        SerializeConfig config = new SerializeConfig();
        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
//		System.out.println(JSON.toJSONString(obj,SerializerFeature.WriteMapNullValue,
//				SerializerFeature.DisableCircularReferenceDetect,
//				SerializerFeature.WriteDateUseDateFormat));

        System.out.println(JSON.toJSONString(obj, SerializerFeature.WriteDateUseDateFormat));
        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ssS";
        System.out.println(JSON.toJSONString(obj, SerializerFeature.WriteDateUseDateFormat));

        TestObject parseObj = JSON.parseObject("{\"array\":[\"11\",\"22\",\"33\"],\"name\":\"名称\",\"time\":\"2019-09-29 10:29:44455\",\"timeList\":[\"2019-09-29 10:39:55\",\"2019-09-29 11:29:44\",\"2019-09-29 10:29:44455\"]}", TestObject.class);
        System.out.println(JSON.toJSONString(obj, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteNullListAsEmpty));

    }

}
