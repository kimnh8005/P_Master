package kr.co.pulmuone.v1.comm.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SHA256Util
{
	private static String secretKey;

	@Autowired
	SHA256Util( @Value("${app.secretKey}") String secretKeyValue ) {
		SHA256Util.secretKey = secretKeyValue;
    }

	public static String getSHA256(String str)
	{
		String SHA = "";
		try
		{
			MessageDigest sh = MessageDigest.getInstance("SHA-256");
			sh.update(str.getBytes());
			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++)
			{
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			SHA = sb.toString();

		}
		catch (NoSuchAlgorithmException e)
		{
			// e.printStackTrace();
			SHA = null;
		}
		return SHA;
	}

	public static String getUserPassword(String str) {
		String result = "";
		try {
		     Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		     SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
		     sha256_HMAC.init(secret_key);

		     byte[] bt = sha256_HMAC.doFinal(str.getBytes());
		     Encoder encoder = Base64.getEncoder();
		     String hash = encoder.encodeToString(bt);
		     result = hash;
		    }
		    catch (Exception e){
		    	result = null;
		    }
		return result;
	}

	public static String get(String str, String salt) {
		String result = "";
		try {
		     Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		     String key = secretKey + salt;
		     SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");
		     sha256_HMAC.init(secret_key);

		     byte[] bt = sha256_HMAC.doFinal(str.getBytes());
		     Encoder encoder = Base64.getEncoder();
		     String hash = encoder.encodeToString(bt);
		     result = hash;
		    }
		    catch (Exception e){
		    	result = null;
		    }
		return result;
	}
}
