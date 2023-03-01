package kr.co.pulmuone.v1.batch.send.push;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import kr.co.pulmuone.v1.batch.send.push.dto.vo.SendManualPushVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class FcmSdkService {
    private final String DATABASE_URL = "TBD";
    private FirebaseApp firebaseApp;

    public void init() throws IOException {
        FileInputStream refreshToken = new FileInputStream(ResourceUtils.getFile("classpath:service-account.json"));

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(refreshToken))
                .setDatabaseUrl(DATABASE_URL)
                .build();

        firebaseApp = FirebaseApp.initializeApp(options, "MALL-APP");
    }

    public void sendMulticast(MulticastMessage message) throws FirebaseMessagingException, IOException {
        if (firebaseApp == null) init();
        FirebaseMessaging.getInstance(firebaseApp).sendMulticast(message);
    }

    public MulticastMessage getAndroidMessage(SendManualPushVo vo, List<String> pushKeyList) {
        return MulticastMessage.builder()
                .setNotification(
                        Notification.builder()
                                .setTitle(vo.getAndroidTitle())
                                .setBody(vo.getContent())
                                .setImage(vo.getImage())
                                .build()
                )
//                .putData("subTitle", vo.getAndroidSubTitle())
                .putData("openURL", vo.getLink())
                .putData("openImage", vo.getImage())
                .addAllTokens(pushKeyList)
                .build();
    }

    public MulticastMessage getIosMessage(SendManualPushVo vo, List<String> pushKeyList) {
        return MulticastMessage.builder()
                .setNotification(
                        Notification.builder()
                                .setTitle(vo.getIosTitle())
                                .setBody(vo.getContent())
                                .setImage(vo.getImage())
                                .build()
                )
                .putData("openUrl", vo.getLink())
                .putData("openImage", vo.getImage())
                .addAllTokens(pushKeyList)
                .build();
    }
}
