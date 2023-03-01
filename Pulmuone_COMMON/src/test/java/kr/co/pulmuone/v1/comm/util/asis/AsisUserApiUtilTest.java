package kr.co.pulmuone.v1.comm.util.asis;

import org.junit.jupiter.api.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

class AsisUserApiUtilTest {

    @Test
    void 통합회원_비밀번호_암호화() {

        String strSRCData = null;
        String strENCData = null;
        strSRCData = "157766";

        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytData = strSRCData.getBytes();
            md.update(bytData);
            byte[] digest = md.digest();

            Base64.Encoder encoder = Base64.getEncoder();
            byte[] encodedBytes = encoder.encode(digest);
            strENCData = new String(encodedBytes);
        }
        catch (NoSuchAlgorithmException var8)
        {
            strENCData = null;
            var8.printStackTrace();
        }
        catch (Exception var9)
        {
            strENCData = null;
            var9.printStackTrace();
        }

        System.out.println(strSRCData + " ======> " + strENCData);
    }

    @Test
    void 임직원_비밀번호_암호화() {
        String planText = "157766";

        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(planText.getBytes());
            byte byteData[] = md.digest();

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            StringBuffer hexString = new StringBuffer();
            for (int i=0;i<byteData.length;i++) {
                String hex=Integer.toHexString(0xff & byteData[i]);
                if(hex.length()==1){
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            System.out.println(planText + " ======> " + hexString.toString());
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}