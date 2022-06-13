import PAY_CARD.PAY_CARD;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
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

public class GetCardData_Main {

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



    private static String partnerCode ="0912345331";
    private static String categoryID ="0";
    private static String productID = "0";
    private static String productAmount = "0";
    private static String customerID ="20200804141823426";
    private static String partnerTransID ="20220610081900584";
    private static String partnerTransDate ="20220610081900";
    private static String data ="355740";


    private static String key3Des = "ff39fc173e7ed3c35e01d139e6042e64";


    public static void main(String[] args) throws Exception {
        String url = "http://117.103.207.81:8080/share/GetInfo/get-carddata";

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

        callApi(url,dataString);

    }

    public static String genSig() throws Exception {
        String dataSin = (partnerCode+"|" + categoryID + "|" + productID + "|"
            +productAmount+"|"+customerID + "|"+partnerTransID+"|" + partnerTransDate +"|" +data );
        String sign = Cryption.createSign(dataSin,"merchant_privateKey.der");
        return sign;
    }

    public static void callApi(String url,String jsonBody) throws Exception {
        sendPOSTRequest(url,jsonBody);
        System.out.println("----SEND POST REQUEST---");
        log.debug(JSONObject.fromObject(jsonBody).toString(2));
        sendPOSTRequest(url,jsonBody);
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
            HttpPost httpPost = new HttpPost(url);
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

    public static void sendRequst(){
        try {
            URL url = new URL("");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            String line="";
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder reponse =new StringBuilder();
            while ((line = bufferedReader.readLine())!=null){
                reponse.append(line);
            }
            bufferedReader.close();
            System.out.println("Reponse: " + reponse.toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void ParseJsonResponse(){
        try {
            URL url = new URL("https://jsonplaceholder.typicode.com/photos");
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            //adding header

            String line="";
            InputStreamReader inputStreamReader=new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
            StringBuilder response=new StringBuilder();
            while ((line=bufferedReader.readLine())!=null){
                response.append(line);
            }
            bufferedReader.close();
            System.out.println("Response : "+response.toString());
            JSONArray jsonArray=new JSONArray(response.toString());
            for (int i=0;i<jsonArray.length();i++){
                System.out.println("Title : "+jsonArray.getJSONObject(i).getString("title"));
                System.out.println("ID : "+jsonArray.getJSONObject(i).getInt("id"));
                System.out.println("URL : "+jsonArray.getJSONObject(i).getString("url"));
                System.out.println("===========================================================\n");
            }

        }
        catch (Exception e){
            System.out.println("Error in Making Get Request");
        }
    }
}
