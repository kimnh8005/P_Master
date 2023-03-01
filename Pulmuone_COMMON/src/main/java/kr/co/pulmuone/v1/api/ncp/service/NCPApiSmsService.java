package kr.co.pulmuone.v1.api.ncp.service;

import com.google.api.client.util.Base64;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import kr.co.pulmuone.v1.api.ncp.dto.NcpSmsRequestDto;
import kr.co.pulmuone.v1.comm.enums.SendEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Scanner;

@Slf4j
@Service
public class NCPApiSmsService {
    @Value("${naver.sms.apiUrl}")
    private String apiUrl;

    @Value("${naver.sms.requestUrl}")
    private String requestUrl;

    @Value("${naver.sms.requestUrlTail}")
    private String requestUrlTail;

    @Value("${naver.sms.serviceId}")
    private String serviceId;

    @Value("${naver.sms.accessKeyId}")
    private String accessKey;

    @Value("${naver.sms.secretKey}")
    private String secretKey;

    private HttpURLConnection getConnection() throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        String strMillSecond = getMilliSecond();

        // [START use_access_token]
        URL url = new URL(apiUrl + requestUrl + serviceId + requestUrlTail);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("x-ncp-apigw-timestamp", strMillSecond);
        httpURLConnection.setRequestProperty("x-ncp-iam-access-key", accessKey);
        httpURLConnection.setRequestProperty("x-ncp-apigw-signature-v2", makeSignature(strMillSecond));
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setConnectTimeout(10000);    //컨넥션 타임아웃 10초
        httpURLConnection.setReadTimeout(5000);        //컨텐츠조회 타임아웃 5초
        httpURLConnection.setDoOutput(true);
        return httpURLConnection;
        // [END use_access_token]
    }

    /**
     * Send request to NCP message using HTTP.
     *
     * @param ncpMessage Body of the HTTP request.
     * @throws IOException, NoSuchAlgorithmException, InvalidKeyException {
     */
    protected String sendMessage(JsonObject ncpMessage) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        HttpURLConnection connection = getConnection();

        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        writer.write(ncpMessage.toString());
        writer.close();

        String responseCode = String.valueOf(connection.getResponseCode());
        if (responseCode.equals(SendEnums.SendNcpSmsResponseCode.ACCEPT.getCode())) {
            log.info("Message sent to NCP for delivery, response:");
            log.info(inputStreamToString(connection.getInputStream()));
        } else {
            log.info("Unable to send message to NCP:");
            log.error(inputStreamToString(connection.getErrorStream()));
        }
        return responseCode;
    }

    protected JsonObject getJSONObject(List<NcpSmsRequestDto> smsInfoList, SendEnums.SendNcpSmsType smsType) {
        if (smsInfoList == null || smsInfoList.size() == 0) return new JsonObject();

        JsonArray messageArray = new JsonArray();
        for (NcpSmsRequestDto vo : smsInfoList) {
            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("subject", "test");
            jsonObject.addProperty("content", vo.getContent());
            jsonObject.addProperty("to", vo.getMobile().replaceAll("-", ""));
            messageArray.add(jsonObject);
        }

        JsonObject json = new JsonObject();
        json.addProperty("type", smsType.getCode());
//        json.addProperty("contentType", "COMM");
        json.addProperty("from", smsInfoList.get(0).getSenderTelephone().replaceAll("-", ""));
        json.addProperty("subject", "#풀무원");
        json.addProperty("content", "#풀무원");
        json.add("messages", messageArray);

        return json;
    }

    /**
     * Read contents of InputStream into String.
     *
     * @param inputStream InputStream to read.
     * @return String containing contents of InputStream.
     */
    private String inputStreamToString(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNext()) {
            stringBuilder.append(scanner.nextLine());
        }
        return stringBuilder.toString();
    }

    private String getMilliSecond() {
        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zoneDateTime = localDateTime.atZone(ZoneId.of("UTC+9"));
        return String.valueOf(zoneDateTime.toInstant().toEpochMilli());
    }

    private String makeSignature(String timestamp) throws NoSuchAlgorithmException, InvalidKeyException {
        String space = " ";
        String newLine = "\n";
        String method = "POST";

        String message = method + space + requestUrl + serviceId + requestUrlTail + newLine + timestamp + newLine + accessKey;
        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeBase64String(rawHmac);
    }
}
