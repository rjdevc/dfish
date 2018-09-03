package com.rongji.dfish.misc.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonUtilTest {

	class TestObject {
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
		obj.setList(Arrays.asList("1", "2", "3"));
		obj.setArray(new String[]{ "11", "22", "33" });
		obj.setTime(new Date());
		obj.setTimeList(Arrays.asList(new Date(), new Date(), new Date()));
		System.out.println(JsonUtil.toJson(obj));
		
		JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd" ;
		System.out.println(JSON.toJSONString(obj,SerializerFeature.WriteMapNullValue,
				SerializerFeature.DisableCircularReferenceDetect,
				SerializerFeature.WriteDateUseDateFormat));
	}

}
