package java.client;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.net.ssl.SSLContext;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by Administrator on 2016/12/30.
 */
public class CommonHttpsClientWithSingleSSL extends CommonClient{

    public CommonHttpsClientWithSingleSSL(){
        RegistryBuilder<ConnectionSocketFactory> registryBuilder=RegistryBuilder.create();
        ConnectionSocketFactory connectionSocketFactory=new PlainConnectionSocketFactory();
        registryBuilder.register("http",connectionSocketFactory);
        try{
            KeyStore trustStore=KeyStore.getInstance(KeyStore.getDefaultType());
            TrustStrategy trustStrategy=new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            };
            SSLContext sslContext= SSLContexts.custom().useTLS().loadTrustMaterial(trustStore,trustStrategy).build();
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
