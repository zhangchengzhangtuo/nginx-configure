package java.client;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

/**
 * Created by Administrator on 2016/12/30.
 */
public class CommonHttpClient extends CommonClient{
    private static final String userAgent="Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11";

    //����ʱ��
    private static final int connectionTimeout=2000;

    //����ʱ��
    private static final int socketTimeout=10000;

    //�������ĸ���
    private static final int maxConnectionNumber=100;

    //ÿ��·���������Ӹ���
    private static final int connectionNumberPerRoute=10;

    public CommonHttpClient(){
        //1.����������Ŀ
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager=new PoolingHttpClientConnectionManager();
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(connectionNumberPerRoute);
        poolingHttpClientConnectionManager.setMaxTotal(maxConnectionNumber);

        //2.���������Ƿ񱣳ֻ���
        ConnectionReuseStrategy connectionReuseStrategy=new ConnectionReuseStrategy() {
            @Override
            public boolean keepAlive(HttpResponse response, HttpContext context) {
                return false;
            }
        };

        //3.�������ӱ���ʱ��
        ConnectionKeepAliveStrategy connectionKeepAliveStrategy=new ConnectionKeepAliveStrategy() {
            @Override
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                return 2*3600*1000;
            }
        };

        //4.�������������Timeout
        RequestConfig requestConfig=RequestConfig.custom()
                .setConnectTimeout(connectionTimeout)
                .setSocketTimeout(socketTimeout)
                .build();


        //5.����HttpClient
        httpClient= HttpClientBuilder.create()
                .setUserAgent(userAgent)
                .setConnectionReuseStrategy(connectionReuseStrategy)
                .setKeepAliveStrategy(connectionKeepAliveStrategy)
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(poolingHttpClientConnectionManager)
                .build();
    }


}
