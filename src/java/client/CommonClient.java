package java.client;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/30.
 */
public class CommonClient {

    protected CloseableHttpClient httpClient;

    public CommonClient(){

    }

    public String formUriWithQuery(String uri,Map<String,String> params) throws Throwable{
        //URI:[scheme:][//host:port][path][?query][#fragment]
        StringBuffer sb=new StringBuffer();
        int i=0;
        for(String key:params.keySet()){
            if(i!=0){
                sb.append("&");
            }
            sb.append(key).append("=").append(params.get(key));
            i++;
        }
        String uriWithQuery=uri+"?"+sb.toString();
        return uriWithQuery;
    }

    public HttpGet formHttpGet(String url){
        HttpGet httpGet=new HttpGet(url);
        return httpGet;
    }

    public HttpPost formHttpPostNameValuePairEntity(String url,Map<String,String> headers,Map<String,String> body) throws Throwable{
        HttpPost httpPost=new HttpPost(url);

        for(String key:headers.keySet()){
            httpPost.setHeader(key,headers.get(key));
        }

        List<NameValuePair> list=new ArrayList<>();
        for(String key:body.keySet()){
            NameValuePair nvp=new BasicNameValuePair(key,body.get(key));
            list.add(nvp);
        }
        HttpEntity httpEntity=new UrlEncodedFormEntity(list);
        httpPost.setEntity(httpEntity);

        return httpPost;
    }


    public HttpPost formHttpPostByteArrayEntity(String url,Map<String,String> headers,byte [] body,ContentType contentType) throws Throwable{
        HttpPost httpPost=new HttpPost(url);
        HttpEntity httpEntity=new StringEntity(new String(body),contentType);
        for(String key:headers.keySet()){
            httpPost.setHeader(key,headers.get(key));
        }
        httpPost.setEntity(httpEntity);
        return httpPost;
    }

    public HttpPost formHttpPostStringEntity(String url,Map<String,String> headers,String body,ContentType contentType) throws Throwable{
        HttpPost httpPost=new HttpPost(url);
        HttpEntity httpEntity= new StringEntity(body,contentType);
        for(String key:headers.keySet()){
            httpPost.setHeader(key, headers.get(key));
        }
        httpPost.setEntity(httpEntity);
        return httpPost;
    }


    public HttpPut formHttpPutStringEntity(String url,Map<String,String> headers,String body,ContentType contentType) throws Throwable{
        HttpPut httpPut=new HttpPut(url);
        HttpEntity httpEntity=new StringEntity(body,contentType);
        for(String key:headers.keySet()){
            httpPut.setHeader(key, headers.get(key));
        }
        httpPut.setEntity(httpEntity);
        return httpPut;
    }

    public String response(HttpRequestBase httpRequestBase) throws Throwable{
        CloseableHttpResponse closeableHttpResponse=null;
        try{
            closeableHttpResponse=httpClient.execute(httpRequestBase);
            StatusLine statusLine=closeableHttpResponse.getStatusLine();
            if(statusLine.getStatusCode()!=200){
                throw new RuntimeException("response with errorCode:"+statusLine.getStatusCode());
            }
            HttpEntity httpEntity=closeableHttpResponse.getEntity();
            String response= EntityUtils.toString(httpEntity, "utf-8");
            return response;
        }catch (Throwable e){
            if(null!=closeableHttpResponse){
                closeableHttpResponse.close();
            }
            throw e;
        }
    }

    public void close(){
        if(httpClient!=null){
            try {
                httpClient.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }


}
