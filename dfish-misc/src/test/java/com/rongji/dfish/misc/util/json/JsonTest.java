package com.rongji.dfish.misc.util.json;

import com.rongji.dfish.base.util.json.JsonBuilder;
import com.rongji.dfish.misc.util.json.impl.JsonBuilder4Fastjson;
import com.rongji.dfish.misc.util.json.impl.JsonBuilder4Gson;
import com.rongji.dfish.misc.util.json.impl.JsonBuilder4Jackson;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author lamontYu
 * @since
 */
public class JsonTest {

    private static class TestUser {
        private String userId;
        private String userName;
        private Date birthday = new Date();

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
//        long begin = System.currentTimeMillis();
        TestUser user1 = new TestUser("1", "用户1");
        String userJson = builder.toJson(user1);
        sb.append("user1:").append(userJson).append("\r\n");
        sb.append("user1.birthday:").append(user1.getBirthday().getTime()).append("\r\n");

        TestUser parsedUser = builder.parseObject(userJson, TestUser.class);
        sb.append("parsedUser===userId:").append(parsedUser.getUserId()).append(",userName:").append(parsedUser.getUserName()).append("\r\n");

        TestUser user2 = new TestUser(null, "用户2");
        List<TestUser> userList = Arrays.asList(user1, user2);

        String listJson = builder.toJson(userList);
        sb.append("listJson:").append(listJson).append("\r\n");

        List<TestUser> parsedList = builder.parseArray(listJson, TestUser.class);
        sb.append("parsedList[0].userName=").append(parsedList.get(0).getUserName()).append("\r\n");
//        long end = System.currentTimeMillis();
        System.out.println(sb);
//        System.out.println("cost:" + (end - begin) + "ms\r\n");
    }

}
