package com.rongji.dfish.misc.util;

import com.rongji.dfish.base.context.SystemContext;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HttpUtil {

    /**
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static String execute(String url, Map<String, String> params) throws Exception {
        ClientHttpRequestFactory c = SystemContext.getInstance().get(ClientHttpRequestFactory.class);
        RestTemplate restTemplate = c == null ? new RestTemplate() : new RestTemplate(c);
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        List<Charset> cs = Arrays.asList(Charset.forName("UTF-8"));
        headers.setAcceptCharset(cs);
        LinkedMultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<>();
        if (params != null && params.size() > 0) {
            params.forEach((key, value) -> bodyMap.add(key, value));
        }
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity(bodyMap, headers);
        HttpMethod method = params == null || params.size() == 0 ? HttpMethod.GET : HttpMethod.POST;
        ResponseEntity<String> response = restTemplate.exchange(url, method, requestEntity, String.class);
        return response.getBody();
    }

    public static String execute(String url) throws Exception {
        return execute(url, null);
    }

    public ClientHttpRequestFactory getSimpleFacotry() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(5000);
        return factory;
    }
    public ClientHttpRequestFactory getHttpComponentsFacotry() {
        //需要支持apache-http-client
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(5000);
        return factory;
    }
    public ClientHttpRequestFactory getOkHttpFacotry() {
        //需要支持OK-http-client
        OkHttp3ClientHttpRequestFactory factory = new OkHttp3ClientHttpRequestFactory();
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(5000);
        return factory;
    }


//    public static void main(String[] args ) throws Exception {
//        String s=doGet("https://www.baidu.com");
//        System.out.println(s);
//
//        Map<String,String> m=new HashMap<>();
//        m.put("name","Trump");
//        String s2=doPost("http://127.0.0.1:8080/hello.jsp",m);
//        System.out.println(s2);
//    }

}
