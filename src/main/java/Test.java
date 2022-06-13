import PAY_CARD.PAY_CARD;
import net.sf.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Test {
    private static String partnerCode ="0912345331";
    private static String categoryID ="114";
    private static String productID = "89";
    private static String productAmount = "20000";
    private static String customerID ="";
    private static String partnerTransID = "lvt_0357" ;
    private static String partnerTransDate ="20220613132203";
    private static String data ="1";


    public static void main(String[] args) throws Exception {
        String dataSin = (partnerCode+"|" + categoryID + "|" + productID + "|"
            +productAmount+"|"+customerID + "|"+partnerTransID+"|" + partnerTransDate +"|" +data );
        String sign = Cryption.createSign(dataSin,"merchant_privateKey.der");
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
                .withDataSign(sign)
                .build()).toString();
        System.out.println(dataString);
    };
}
