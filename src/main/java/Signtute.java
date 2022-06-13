import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
public class Signtute {

    public static String Sign(String dataToSign, String privateKey, Boolean isFile) throws Exception {
        RSAPrivateKey _privateKey = LoadPrivateKey(privateKey, isFile);
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(_privateKey);
        signature.update(dataToSign.getBytes());
        byte[] bSigned = signature.sign();
        String sResult = encodeBase64(bSigned);
        return sResult;
    }

    private static RSAPrivateKey LoadPrivateKey(String key, Boolean isFile) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String sReadFile = "";
        if (isFile) {
            File file = new File(key);
            sReadFile = fullyReadFile(file);
        } else {
            sReadFile = key.trim();
        }

        if (sReadFile.startsWith("-----BEGIN PRIVATE KEY-----") && sReadFile.endsWith("-----END PRIVATE KEY-----")) {
            sReadFile = sReadFile.replace("-----BEGIN PRIVATE KEY-----", "");
            sReadFile = sReadFile.replace("-----END PRIVATE KEY-----", "");
            sReadFile = sReadFile.replace("\n", "");
            sReadFile = sReadFile.replace("\r", "");
            sReadFile = sReadFile.replace(" ", "");
        }

        byte[] b = decodeBase64(sReadFile);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(b);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey)factory.generatePrivate(spec);
    }
    private static String fullyReadFile(File file) throws IOException {
        DataInputStream dis = new DataInputStream(new FileInputStream(file));
        byte[] bytesOfFile = new byte[(int)file.length()];
        dis.readFully(bytesOfFile);
        dis.close();
        String sRead = new String(bytesOfFile);
        return sRead.trim();
    }
    private static String encodeBase64(byte[] dataToEncode) {
        BASE64Encoder b64e = new BASE64Encoder();
        String strEncoded = "";

        try {
            strEncoded = b64e.encode(dataToEncode);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return strEncoded;
    }

    private static byte[] decodeBase64(String dataToDecode) {
        BASE64Decoder b64d = new BASE64Decoder();
        byte[] bDecoded = (byte[])null;

        try {
            bDecoded = b64d.decodeBuffer(dataToDecode);
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        return bDecoded;
    }
}
