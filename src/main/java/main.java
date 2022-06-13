import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class main {
    private static String partnerCode ="0912345331";
    private static String categoryID ="114";
    private static String productID = "91";
    private static String productAmount = "100000";
    private static String customerID ="";
    private static String partnerTransID ="20220609092026971";
    private static String partnerTransDate ="20220609092026";
    private static String data ="1";


    public static String dataSign = "0912345331|114|91|20000||20220609092026971|20220609092026|1";
    public static String sign = "iAkeSc8otdg6E3DFK5kQV27RJCavhjfRfc80j3Ksgwb2sdtJdm3z6yv9zukmI1o6wfO6oYeVcPVFRpJhB1dF9L2P08FH+BagfWRGVh23WOOwi5npLpBzIuizKFnyB+WKnZ9Vzk7Um3f+4aerBuclcjmmFVACjzLk6NrBOsWt9Do=";

    public static void main(String[] args) throws Exception {
        String test = "0912345331|0|0|0|20200804141823426|20220610081900584|20220610081900|355740";
       String sign = Cryption.createSign(dataSign,"merchant_privateKey.der");
       if(sign.isEmpty()==false){
           System.out.println(Cryption.verifySign(dataSign,sign,"0912345331_publicKey.der"));
       }else {
           System.out.println("Null");
       }
        System.out.println(sign);

    }


}
