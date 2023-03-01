package kr.co.pulmuone.v1.batch.send.push;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import kr.co.pulmuone.v1.batch.send.push.dto.vo.SendManualPushVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

@Slf4j
@Service
public class FcmLegacyHttp {
    @Value("${fcm.legacy-http-url}")
    private String baseUrl;

    @Value("${fcm.server_key}")
    private String serverKey;

    @Value("${app.storage.public.public-storage-url}")
    private String publicStorageUrl; // public 저장소 접근 url

    private HttpURLConnection getConnection() throws IOException {
        URL url = new URL(baseUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("Authorization", "key=" + serverKey);
        httpURLConnection.setRequestProperty("Content-Type", "application/json; UTF-8");

        return httpURLConnection;
    }

    /**
     * Send request to FCM message using HTTP.
     *
     * @param fcmMessage Body of the HTTP request.
     * @throws IOException
     */
    public void sendMessage(JsonObject fcmMessage) throws IOException {
        HttpURLConnection connection = getConnection();
        connection.setDoOutput(true);

        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        writer.write(fcmMessage.toString());
        writer.close();

        outputStream.flush();
        outputStream.close();

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            log.info("Message sent to Firebase for delivery, response:");
            log.info(inputstreamToString(connection.getInputStream()));
        } else {
            log.info("Unable to send message to Firebase:");
            log.info(inputstreamToString(connection.getErrorStream()));
        }
    }

    public JsonObject getAndroidMessage(SendManualPushVo vo, List<String> pushKeys) {
//        JsonObject notificationObject = new JsonObject();
//        notificationObject.addProperty("sound", "default");
//        notificationObject.addProperty("title", vo.getAndroidTitle());
//        notificationObject.addProperty("body", vo.getContent());

        JsonObject dataObject = new JsonObject();
        dataObject.addProperty("title", vo.getAndroidTitle());
        dataObject.addProperty("body", vo.getContent());
        dataObject.addProperty("openURL", vo.getLink());
        dataObject.addProperty("imgURL", !vo.getImage().isEmpty() ? publicStorageUrl + vo.getImage() : "");
        dataObject.addProperty("sequence", vo.getSnManualPushId());
        dataObject.addProperty("pushType", vo.getPushSendType());

        JsonArray pushKeyJsonArray = new JsonArray();
        pushKeys.forEach(pushKeyJsonArray::add);

        JsonObject json = new JsonObject();
//        json.add("notification", notificationObject);
        json.add("data", dataObject);
        json.add("registration_ids", pushKeyJsonArray);

        return json;
    }

    public JsonObject getIosMessage(SendManualPushVo vo, List<String> pushKeys) {
        JsonObject notificationObject = new JsonObject();
        notificationObject.addProperty("title", vo.getIosTitle());
        notificationObject.addProperty("body", vo.getContent());

        JsonObject dataObject = new JsonObject();
        dataObject.addProperty("openURL", vo.getLink());
        dataObject.addProperty("imgURL", !vo.getImage().isEmpty() ? publicStorageUrl + vo.getImage() : "");
        dataObject.addProperty("sequence", vo.getSnManualPushId());
        dataObject.addProperty("pushType", vo.getPushSendType());

        JsonArray pushKeyJsonArray = new JsonArray();
        pushKeys.forEach(pushKeyJsonArray::add);

        JsonObject json = new JsonObject();
        json.add("notification", notificationObject);
        json.add("data", dataObject);
        json.add("registration_ids", pushKeyJsonArray);
        json.addProperty("mutable_content", true);
        json.addProperty("priority", "high");

        return json;
    }

    /**
     * Read contents of InputStream into String.
     *
     * @param inputStream InputStream to read.
     * @return String containing contents of InputStream.
     * @throws IOException
     */
    private static String inputstreamToString(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNext()) {
            stringBuilder.append(scanner.nextLine());
        }
        return stringBuilder.toString();
    }
}
