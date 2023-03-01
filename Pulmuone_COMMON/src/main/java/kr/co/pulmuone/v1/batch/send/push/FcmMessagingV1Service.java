package kr.co.pulmuone.v1.batch.send.push;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import kr.co.pulmuone.v1.batch.send.push.dto.vo.SendManualPushVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

/**
 * based on : https://github.com/firebase/quickstart-java/blob/9cf1a355682a862b09e1891fc12d5aaa9f89f23b/messaging/src/main/java/com/google/firebase/quickstart/Messaging.java
 */
@Slf4j
@Service
public class FcmMessagingV1Service {
    private static final String PROJECT_ID = "TBD";
    private static final String BASE_URL = "https://fcm.googleapis.com";
    private static final String FCM_SEND_ENDPOINT = "/v1/projects/" + PROJECT_ID + "/messages:send";

    private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private static final String[] SCOPES = {MESSAGING_SCOPE};

    private static final String TITLE = "FCM Notification";
    private static final String BODY = "Notification from FCM";
    public static final String MESSAGE_KEY = "message";

    /**
     * Retrieve a valid access token that can be use to authorize requests to the FCM REST
     * API.
     *
     * @return Access token.
     * @throws IOException
     */
    // [START retrieve_access_token]
    private static String getAccessToken() throws IOException {
        GoogleCredentials googleCredential = GoogleCredentials
                .fromStream(new FileInputStream(ResourceUtils.getFile("classpath:service-account.json")))
                .createScoped(Arrays.asList(SCOPES));
        googleCredential.refreshIfExpired();
        return googleCredential.getAccessToken().getTokenValue();
    }
    // [END retrieve_access_token]

    /**
     * Create HttpURLConnection that can be used for both retrieving and publishing.
     *
     * @return Base HttpURLConnection.
     * @throws IOException
     */
    private static HttpURLConnection getConnection() throws IOException {
        // [START use_access_token]
        URL url = new URL(BASE_URL + FCM_SEND_ENDPOINT);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("Authorization", "Bearer " + getAccessToken());
        httpURLConnection.setRequestProperty("Content-Type", "application/json; UTF-8");
        return httpURLConnection;
        // [END use_access_token]
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
        outputStream.writeBytes(fcmMessage.toString());
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

    public JsonObject getAndroidMessage(SendManualPushVo vo, String pushKey) {
        JsonObject notificationObject = new JsonObject();
        notificationObject.addProperty("title", vo.getAndroidTitle());
        notificationObject.addProperty("body", vo.getContent());
        notificationObject.addProperty("image", vo.getImage());

        JsonObject dataObject = new JsonObject();
//        dataObject.addProperty("subTitle", vo.getAndroidSubTitle());
        dataObject.addProperty("openURL", vo.getLink());

        JsonObject jMessage = new JsonObject();
        jMessage.add("notification", notificationObject);
        jMessage.add("data", dataObject);
        jMessage.addProperty("token", pushKey);

        JsonObject jFcm = new JsonObject();
        jFcm.add(MESSAGE_KEY, jMessage);

        return jFcm;
    }

    public JsonObject getIosMessage(SendManualPushVo vo, String pushKey) {
        JsonObject notificationObject = new JsonObject();
        notificationObject.addProperty("title", vo.getIosTitle());
        notificationObject.addProperty("body", vo.getContent());
        notificationObject.addProperty("image", vo.getImage());

        JsonObject dataObject = new JsonObject();
        dataObject.addProperty("openURL", vo.getLink());

        JsonObject jMessage = new JsonObject();
        jMessage.add("notification", notificationObject);
        jMessage.add("data", dataObject);
        jMessage.addProperty("token", pushKey);

        JsonObject jFcm = new JsonObject();
        jFcm.add(MESSAGE_KEY, jMessage);

        return jFcm;
    }

    /**
     * Read contents of InputStream into String.
     *
     * @param inputStream InputStream to read.
     * @return String containing contents of InputStream.
     * @throws IOException
     */
    private static String inputstreamToString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNext()) {
            stringBuilder.append(scanner.nextLine());
        }
        return stringBuilder.toString();
    }

    /**
     * Pretty print a JsonObject.
     *
     * @param jsonObject JsonObject to pretty print.
     */
    private static void prettyPrint(JsonObject jsonObject) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        log.info(gson.toJson(jsonObject) + "\n");
    }
}
