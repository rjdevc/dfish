package com.rongji.dfish.misc.util.json;

import com.rongji.dfish.base.util.json.JsonBuilder;
import com.rongji.dfish.misc.util.json.impl.JsonBuilder4Fastjson;
import com.rongji.dfish.misc.util.json.impl.JsonBuilder4Gson;
import com.rongji.dfish.misc.util.json.impl.JsonBuilder4Jackson;
import org.junit.Test;

import java.util.*;

/**
 * @author lamontYu
 * @since
 */
public class JsonTest {

    private static class TestUser {
        private String userId;
        private String userName;
        private Date birthday = new Date();
        private String className;
        private TestUser brother;

        public TestUser() {
        }

        public TestUser(String userId, String userName) {
            this.userId = userId;
            this.userName = userName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public Date getBirthday() {
            return birthday;
        }

        public void setBirthday(Date birthday) {
            this.birthday = birthday;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public TestUser getBrother() {
            return brother;
        }

        public void setBrother(TestUser brother) {
            this.brother = brother;
        }
    }

    @Test
    public void jackson() throws Exception {
        JsonBuilder4Jackson builder = new JsonBuilder4Jackson();
        builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("jackson:");
        jsonTest(builder);
    }

    @Test
    public void fastjson() throws Exception {
        JsonBuilder4Fastjson builder = new JsonBuilder4Fastjson();
        builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("fastjson:");
        jsonTest(builder);
    }

    @Test
    public void gson() throws Exception {
        JsonBuilder4Gson builder = new JsonBuilder4Gson();
        builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("gson:");
        jsonTest(builder);
    }

    private void jsonTest(JsonBuilder builder) throws Exception {
        StringBuilder sb = new StringBuilder();
        TestUser user1 = new TestUser("1", "用户1");
        user1.setClassName("一班");
        String userJson = builder.toJson(user1);
        sb.append("user1:").append(userJson).append("\r\n");
        sb.append("user1.birthday:").append(user1.getBirthday().getTime()).append("\r\n");

        TestUser parsedUser = builder.parseObject(userJson, TestUser.class);
        sb.append("parsedUser===userId:").append(parsedUser.getUserId()).append(",userName:").append(parsedUser.getUserName()).append("\r\n");

        TestUser user2 = new TestUser(null, "用户2");
        user2.setClassName("二班");
        List<TestUser> userList = Arrays.asList(user1, user2);

        String listJson = builder.toJson(userList);
        sb.append("listJson:").append(listJson).append("\r\n");

        List<TestUser> parsedList = builder.parseArray(listJson, TestUser.class);
        sb.append("parsedList[0].userName=").append(parsedList.get(0).getUserName()).append("\r\n");

        Map<String, Object> map = new HashMap<>();
        map.put("testId", "001");
        map.put("testName", "名称1");

        String mapJson = builder.toJson(map);
        sb.append("mapJson:").append(mapJson).append("\r\n");
        Map<String, Object> parsedMap = builder.parseObject(mapJson, Map.class);
        sb.append("parsedMap:").append(parsedMap == null ? null : ("testId:" + parsedMap.get("testId") + ",testName:" + parsedMap.get("testName"))).append("\r\n");

//        Map<String, Object> objectMap = new HashMap<>();
//        objectMap.put("testId", "002");
//        objectMap.put("testName", "名称2");
//        objectMap.put("user1", user1);
//        objectMap.put("user2", user2);
        user1.setBrother(user2);

        String objectMapJson = builder.toJson(user1);
        sb.append("objectMapJson:").append(objectMapJson).append("\r\n");

        TestUser parsedUser1 = builder.parseObject(objectMapJson, TestUser.class);
        sb.append("parsedObjectMap:").append(
//                        "testId:" + parsedObjectMap.get("testId")
//                        + ",testName:" + parsedObjectMap.get("testName")
                        ",user1.name:" + parsedUser1.getUserName()
                        +",user2.name:" + parsedUser1.getBrother().getUserName()
                ).append("\r\n");

        System.out.println(sb);
    }

}
