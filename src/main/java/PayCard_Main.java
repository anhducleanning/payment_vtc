import GenPartnerTransID.GenPartnerTransID;
import PAY_CARD.PAY_CARD;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONArray;

import javax.net.ssl.HostnameVerifier;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Properties;

public class PayCard_Main {

    private static final Logger log = Logger.getLogger(PayCard_Main.class);
    static {
        System.setProperty("log4j.category.org.apache.http.wire", "DEBUG");
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "DEBUG");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.wire", "ERROR");
        final Properties logProperties = new Properties();
        logProperties.put("log4j.rootLogger", "DEBUG, CONSOLE, file");
        logProperties.put("log4j.appender.CONSOLE", "org.apache.log4j.ConsoleAppender");
        logProperties.put("log4j.appender.CONSOLE.layout", "org.apache.log4j.PatternLayout");
        logProperties.put("log4j.appender.CONSOLE.layout.ConversionPattern", "%d{dd/MM/yyyy HH:mm:ss,SSS} %5p %c{1}: %m%n");
        logProperties.put("log4j.appender.file", "org.apache.log4j.DailyRollingFileAppender");
        logProperties.put("log4j.appender.file.DatePattern", "'.'yyyy-MM-dd");
        logProperties.put("log4j.appender.file.File", "testing.log");
        logProperties.put("log4j.appender.file.layout", "org.apache.log4j.PatternLayout");
        logProperties.put("log4j.appender.file.layout.ConversionPattern", "%d{dd/MM/yyyy HH:mm:ss,SSS} %5p %c{1}: %m%n");
        PropertyConfigurator.configure(logProperties);
    }
    private static HttpResponse response;
    static GenPartnerTransID genPartnerTransID = new GenPartnerTransID();
    private static String key3Des = "ff39fc173e7ed3c35e01d139e6042e64";

    private static String partnerCode ="0912345331";
    private static String categoryID ="114";
    private static String productID = "89";
    private static String productAmount = "20000";
    private static String customerID ="";
    private static String partnerTransID = genPartnerTransID.genCode() ;
    private static String partnerTransDate ="20220613110746";
    private static String data ="1";


    public static void main(String[] args) throws Exception {
        String url = "http://117.103.207.81:8080/share/Pay/buy-card";

        String dataString = JSONObject.fromObject(
              PAY_CARD.DataObjectBuilder.aDataObject()
                    .withPartnerCode(partnerCode)
                    .withCategoryID(categoryID)
                    .withProductID(productID)
                    .withProductAmount(productAmount)
                    .withCustomerID(customerID)
                    .withPartnerTransID(partnerTransID)
                    .withPartnerTransDate(partnerTransDate)
                    .withData(data)
                    .withDataSign(genSig())
                    .build()).toString();
        System.out.println(dataString);
        callApi(url,dataString);

    }

    public static String genSig() throws Exception {
        String dataSin = (partnerCode+"|" + categoryID + "|" + productID + "|"
            +productAmount+"|"+customerID + "|"+partnerTransID+"|" + partnerTransDate +"|" +data );
        System.out.println(dataSin);
        String sign = Cryption.createSign(dataSin,"merchant_privateKey.der");
        return sign;
    }

    public static void callApi(String url,String jsonBody) throws Exception {
        sendPOSTRequest(url,jsonBody);
        System.out.println("----SEND POST REQUEST---");
        log.debug(JSONObject.fromObject(jsonBody).toString(2));
        HttpResponse response = getResponse();
        String out = EntityUtils.toString(response.getEntity(), "UTF-8");
        log.debug("---------- RESPONSE FROM VTC -----------");
        net.sf.json.JSONObject jsonObject = JSONObject.fromObject(out);
        log.debug(JSONObject.fromObject(out).toString(2));
        System.out.println("Datainfo: " + jsonObject.getString("dataInfo"));
        System.out.println(Cryption.DecryptTripleDES(key3Des,jsonObject.getString("dataInfo")));
    }
    public static void sendPOSTRequest(String urlRequst,String jsonBody ) {

        try {
            // Prepare the URL
            URI url = new URIBuilder( urlRequst).build();
            // Post Request
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(120000).build();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            httpPost.setHeader("content-type", "application/json");
            StringEntity stringEntity = new StringEntity(jsonBody);
            httpPost.setEntity(stringEntity);
            // Execute the request
            HttpResponse httpResponse = getSSLCustomClient().execute(httpPost);
            if (httpResponse != null) {
                response = httpResponse;
            }

        } catch (Exception e) {
            e.getMessage();
        }


    }

    private static SSLConnectionSocketFactory getSSLContext() {
        TrustStrategy trustStrategy = new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        };

        HostnameVerifier allVerifier = new NoopHostnameVerifier();
        SSLConnectionSocketFactory connFactory = null;
        try {
            connFactory = new SSLConnectionSocketFactory(
                SSLContextBuilder.create().loadTrustMaterial(trustStrategy).build(), allVerifier);
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return connFactory;
    }


    public static HttpResponse getResponse() {
        return response;
    }
    public static CloseableHttpClient getSSLCustomClient() {
        HttpClientBuilder clientBuilder = HttpClients.custom();
        clientBuilder.setSSLSocketFactory(getSSLContext());
        CloseableHttpClient client = clientBuilder.build();
        return client;
    }

}
