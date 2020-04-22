package com.rongji.dfish.misc.http;

import com.rongji.dfish.base.DfishException;
import com.rongji.dfish.base.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletResponse;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;

public class HttpInvoker {
    public static final int METHOD_POST = 1;
    public static final int METHOD_PATCH = 3;
    public static final int METHOD_DELETE = 2;
    public static final int METHOD_GET = 4;

    private static Log LOG = LogFactory.getLog(HttpInvoker.class);

//	public static final String MANAGER_PAY = "PAY";
//	public static final String MANAGER_PSC = "PSC";
//	public static final String MANAGER_CMS = "CMS";
//	public static final String MANAGER_SMS = "SMS";
//	public static final String MANAGER_INTF = "INTF";
//	public static final String MANAGER_SSO = "SSO";
//	public static final String MANAGER_ND = "ND";
//	public static final String MANAGER_OTHER = "OTHER";

//    private static HttpClientConnectionManager connectionManager;  
//    private static void rebuildCM(){
//    	SchemeRegistry schemeRegistry = new SchemeRegistry();  
//    	schemeRegistry.register(  
//    	         new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));  
//          try {
//	      	SSLContext ctx = SSLContext.getInstance("TLS");  
//	      	X509TrustManager tm = new X509TrustManager() {  
//	      		@Override  
//	      		public void checkClientTrusted(X509Certificate[] chain,  
//	      				String authType) throws CertificateException {  
//	      		}  
//	      		@Override  
//	      		public void checkServerTrusted(X509Certificate[] chain,  
//	      				String authType) throws CertificateException {  
//	      		}  
//	      		@Override  
//	      		public X509Certificate[] getAcceptedIssuers() {  
//	      			return null;  
//	      		}  
//	      	};
//			ctx.init(null, new TrustManager[]{tm}, null);
//			SSLSocketFactory ssf = new SSLSocketFactory(ctx,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER); 
//			//LinLW 2016-01-20 因为fj.dyejia.cn 上的地址实际上都是由nginx代理出来的，实际域有区别，
//			//必须使用 SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER 才能正常使用这个HTTCLIENT连接池的功能。
//			schemeRegistry.register(new Scheme("https", 443, ssf));  
//		} catch (Exception e) {
//			LOG.error("", e);
//		} 
//          PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();  
//        Integer routeCount = FrameworkHelper.getSystemConfigAsInteger("intf.http.routeCount", 100);
//    	cm.setMaxTotal(routeCount);  
//    	cm.setDefaultMaxPerRoute(routeCount); 
//    	
//  
//        connectionManager = cm;  
//    }
//    static {  
//    	rebuildCM();
//    }

    public static String doInvoke(String uri, String postContent, int methodKey) throws Exception {
        return doInvoke(uri, postContent, methodKey, "application/json", "UTF-8");
    }

    public static String doInvoke(String uri, String postContent, int methodKey, String contentType, String charset) throws Exception {
        Map<String, String> headerParams = new HashMap<String, String>();
        if (methodKey != METHOD_DELETE) {
            headerParams.put("Content-Type", contentType);
        }
        headerParams.put("Accept", contentType);
        headerParams.put("Accept-Language", "zh-cn");
        headerParams.put("Accept-Encoding", "deflate");
        return doInvoke(uri, postContent, methodKey, charset, headerParams);
    }

    protected static String doInvoke(String uri, String postContent, int methodKey, String charset, Map<String, String> headerParams) throws Exception {
        HttpUriRequest method = null;
        switch (methodKey) {
            case METHOD_POST: {
                method = new HttpPost(uri);
                break;
            }
            case METHOD_GET: {
                method = new HttpGet(uri);
                break;
            }
            case METHOD_PATCH: {
                method = new HttpPatch(uri);
                break;
            }
            case METHOD_DELETE: {
                method = new HttpDelete(uri);
                break;
            }
        }

        if (headerParams != null) {
            // 设置参数
            for (Entry<String, String> entry : headerParams.entrySet()) {
                method.setHeader(entry.getKey(), entry.getValue());
            }
        }

        if (Utils.notEmpty(postContent)) {
            switch (methodKey) {
                case METHOD_POST: {
                    HttpPost cast = (HttpPost) method;
                    cast.setEntity(new ByteArrayEntity(postContent.getBytes(charset)));
                    break;
                }
                case METHOD_PATCH: {
                    HttpPatch cast = (HttpPatch) method;
                    cast.setEntity(new ByteArrayEntity(postContent.getBytes(charset)));
                    break;
                }
            }
        }

        String result = getResult(uri, method);
        return result;

    }

    //	private static Map<String, HttpClientBuilder> hcbMap = Collections.synchronizedMap(new HashMap<String, HttpClientBuilder>());
//	private static HttpClientBuilder hcb;
    private static int MAX_TIMEOUT = 10000;

    private static Map<String, ExecutorService> executors = Collections.synchronizedMap(new HashMap<String, ExecutorService>());
    private static Map<String, HttpClientBuilder> builders = Collections.synchronizedMap(new HashMap<String, HttpClientBuilder>());

    private static ExecutorService getExecutor(String host) {
        ExecutorService executor = executors.get(host);
        if (executor == null) {
            int routeCount = getRouteCount(host);
            executor = Executors.newFixedThreadPool(routeCount);
            executors.put(host, executor);
        }
        return executor;
    }

    private static String getHost(String uri) {
        if (uri == null) {
            return null;
        }
        String host = uri;
        int subIndex = host.indexOf("//");
        if (subIndex >= 0) {
            host = host.substring(subIndex + 2);
        }
        subIndex = host.indexOf("/");
        if (subIndex > 0) {
            host = host.substring(0, subIndex);
        }
        return host;
    }

    /**
     * 创建HttpClientBuilder
     *
     * @param host
     * @return
     */
    private static HttpClientBuilder createHttpClientBuilder(String host) {
        HttpClientBuilder hcb = HttpClientBuilder.create();
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            ctx.init(null, new TrustManager[]{tm}, null);
            hcb.setSSLContext(ctx);
            // FIXME 这里因为常量不存在报错先注释
//            hcb.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            int routeCount = getRouteCount(host);
            hcb.setMaxConnPerRoute(routeCount);
            hcb.setMaxConnTotal(routeCount);
            hcb.setConnectionTimeToLive(30, TimeUnit.SECONDS);
            RequestConfig.Builder configBuilder = RequestConfig.custom();
            configBuilder.setConnectTimeout(MAX_TIMEOUT);
            configBuilder.setSocketTimeout(MAX_TIMEOUT);
            configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
            hcb.setDefaultRequestConfig(configBuilder.build());
        } catch (Throwable e) {
            LOG.error(null, e);
        }
        return hcb;
    }

    /**
     * 获取路由数量
     *
     * @param host
     * @return
     */
    public static int getRouteCount(String host) {
        int routeCount = 100;
        // 读取配置
//        try {
//            routeCount = FrameworkHelper.getSystemConfigAsInteger("intf.http.routeCount", 100);
//        } catch (Exception e) {
//        }
        return routeCount;
    }

    /**
     * 根据域名获取HttpClientBuilder
     *
     * @param host
     * @return
     */
    public static HttpClientBuilder getHttpClientBuilder(String host) {

        HttpClientBuilder hcb = builders.get(host);
        if (hcb == null) {
            hcb = createHttpClientBuilder(host);
            builders.put(host, hcb);
        }
        return hcb;
    }

    /**
     * HttpClient上次清理时间
     */
    private static Map<String, Long> lastEvictTimes = new HashMap<String, Long>();

    /**
     * 获取HttpClient
     *
     * @param host
     * @return
     */
    public static CloseableHttpClient getHttpClient(String host) {
        HttpClientBuilder hcb = getHttpClientBuilder(host);
        long now = System.currentTimeMillis();
        // 获取上次清理时间
        Long lastEvictTime = lastEvictTimes.get(host);
        lastEvictTime = lastEvictTime == null ? 0L : lastEvictTime;
        // 清理时间超过30秒的清理废弃的HttpClient
        if (now - lastEvictTime >= 30000L) {
            hcb.evictExpiredConnections();
            hcb.evictIdleConnections(5L, TimeUnit.SECONDS);
            // 更新清理时间
            lastEvictTimes.put(host, now);
        }
        return hcb.build();
    }

//	private static void rebuildCMWithin(long interval){
////		long now=System.currentTimeMillis();
////		if(now>lastRebuild+interval){
////			lastRebuild=now;
////			ClientConnectionManager old=connectionManager;
////			rebuildCM();
////			old.shutdown();
////		}
//	}
//	private static long lastRebuild;


    public static String doPost(String uri, Map<String, String> params) throws Exception {
        if (Utils.notEmpty(params)) {
            List<NameValuePair> paramList = new ArrayList<NameValuePair>(params.size());
            for (Entry<String, String> entry : params.entrySet()) {
                if (Utils.isEmpty(entry.getValue())) {
                    continue;
                }
                paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            return doPost(uri, paramList);
        }
        return null;
    }

    public static String doPost(String uri, List<NameValuePair> paramList) throws Exception {
        Map<String, String> headerParams = new HashMap<String, String>(4);
        headerParams.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        headerParams.put("Accept", "application/json");
        headerParams.put("Accept-Language", "zh-cn");
        headerParams.put("Accept-Encoding", "deflate");
        return doPost(uri, paramList, "UTF-8", headerParams);
    }

    protected static String doPost(String uri, List<NameValuePair> paramList, String charset, Map<String, String> headerParams) throws Exception {
        HttpUriRequest method = new HttpPost(uri);

        if (headerParams != null) {
            for (Entry<String, String> entry : headerParams.entrySet()) {
                method.setHeader(entry.getKey(), entry.getValue());
            }
        }
        HttpPost cast = (HttpPost) method;
        if (Utils.notEmpty(paramList)) {
            //设置表单提交编码
            cast.setEntity(new UrlEncodedFormEntity(paramList, charset));
        }

        String result = getResult(uri, method);
        return result;
    }

    private static String getResult(String uri, HttpUriRequest method) throws Exception {
        String result = null;
        HttpEntity entity = null;
        String host = getHost(uri);
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        try {
            client = getHttpClient(host);

            final CloseableHttpClient fClient = client;
            final HttpUriRequest fMethod = method;
            ExecutorService executor = getExecutor(host);
            Future<CloseableHttpResponse> future = executor.submit(new Callable<CloseableHttpResponse>() {
                @Override
                public CloseableHttpResponse call() throws Exception {
                    return fClient.execute(fMethod);
                }
            });
            response = future.get();
            if (HttpServletResponse.SC_NOT_FOUND == response.getStatusLine().getStatusCode()) {
                throw new DfishException("服务器异常404");
            }
//			if(response.getStatusLine().getStatusCode()==HttpServletResponse.SC_OK){
            entity = response.getEntity();
//			}

            if (entity != null) {
                result = EntityUtils.toString(entity);
            }
        } catch (Exception ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new Exception(ex);
        } finally {
            EntityUtils.consume(entity); // 释放资源到资源池
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }
        return result;
    }

}
