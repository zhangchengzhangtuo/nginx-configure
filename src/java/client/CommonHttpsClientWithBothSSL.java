package java.client;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

/**
 * Created by Administrator on 2016/12/30.
 */
public class CommonHttpsClientWithBothSSL extends CommonClient{

    public static final String KEY_STORE_PASS="apinji";

    public static final String KEY_STORE_PATH="C:\\Users\\Administrator\\Desktop\\https\\client.jks";

    public CommonHttpsClientWithBothSSL(){
        RegistryBuilder<ConnectionSocketFactory> registryBuilder=RegistryBuilder.create();
        ConnectionSocketFactory connectionSocketFactory=new PlainConnectionSocketFactory();
        registryBuilder.register("http",connectionSocketFactory);
        try{
            KeyStore trustStore=KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream inputStream=new FileInputStream(new File(KEY_STORE_PATH));
            trustStore.load(inputStream,KEY_STORE_PASS.toCharArray());
            SSLContext sslContext= SSLContexts.custom().useTLS().loadTrustMaterial(trustStore,new TrustSelfSignedStrategy()).build();

            LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            registryBuilder.register("https",sslSF);
        }catch (Exception e){
            e.printStackTrace();
        }
        Registry<ConnectionSocketFactory> registry = registryBuilder.build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
        httpClient=HttpClientBuilder.create().setConnectionManager(connManager).build();
    }
}
