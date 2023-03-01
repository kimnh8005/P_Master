package kr.co.pulmuone.v1.batch.send.push;

import com.github.pagehelper.page.PageMethod;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.gson.JsonObject;
import kr.co.pulmuone.v1.batch.send.push.dto.vo.SendDeviceInfoVo;
import kr.co.pulmuone.v1.batch.send.push.dto.vo.SendManualPushVo;
import kr.co.pulmuone.v1.comm.enums.SendEnums;
import kr.co.pulmuone.v1.comm.mappers.batch.master.send.SendPushBatchMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SendPushBatchService {
    private final FcmLegacyHttp fcmLegacyHttp;

    private final SendPushBatchMapper sendPushBatchMapper;

    protected void runSendPush() throws FirebaseMessagingException, IOException, InterruptedException {
        //전송 대상 조회
        List<SendManualPushVo> manualPushVoList = sendPushBatchMapper.getManualPush();

        for (SendManualPushVo vo : manualPushVoList) {
            sendPushBatchMapper.putManualPush("W", vo.getSnManualPushId());

            //Legacy HTTP  : 1000개 가능
            //Java Sdk : 500개 가능
            int fcmBatchCount = 1000;
            int firstIndex = 1;
            int lastIndex = fcmBatchCount;
            List<SendDeviceInfoVo> deviceList;
            do {
                PageMethod.startPage(firstIndex, lastIndex);

                //device OS 별 전송 대상 조회
                deviceList = sendPushBatchMapper.getDeviceInfo(vo.getSnManualPushId(), vo.getOsType()).getResult();
                if (deviceList == null || deviceList.size() <= 0) break;
                List<String> pushKeyList = new ArrayList<>();
                List<Long> snPushSendIdList = new ArrayList<>();
                for (SendDeviceInfoVo deviceVo : deviceList) {
                    pushKeyList.add(deviceVo.getPushKey());
                    snPushSendIdList.add(deviceVo.getSnPushSendId());
                }

                //전송할 푸쉬 메시지 설정
                //FCM Legacy HTTP 방식 - start
                JsonObject jsonObject = null;
                if (vo.getOsType().equals(SendEnums.AppOsType.ANDROID.getCode())) {
                    jsonObject = fcmLegacyHttp.getAndroidMessage(vo, pushKeyList);
                } else if (vo.getOsType().equals(SendEnums.AppOsType.IOS.getCode())) {
                    jsonObject = fcmLegacyHttp.getIosMessage(vo, pushKeyList);
                }

                //FCM 전송
                if (jsonObject != null) {
                    fcmLegacyHttp.sendMessage(jsonObject);
                }
                //FCM Legacy HTTP 방식 - end

                //FCM SDK - 방식 - start
//                MulticastMessage message = null;
//                if (vo.getOsType().equals(SendEnums.AppOsType.ANDROID.getCode())) {
//                    message = fcmSdkService.getAndroidMessage(vo, pushKeyList);
//                } else if (vo.getOsType().equals(SendEnums.AppOsType.IOS.getCode())) {
//                    message = fcmSdkService.getIosMessage(vo, pushKeyList);
//                }
//
//                //FCM 전송
//                fcmSdkService.sendMulticast(message);
                //FCM SDK - 방식 - end

                //전송이후 전송여부 - Y 처리
                sendPushBatchMapper.putPushSend(snPushSendIdList);

                firstIndex += fcmBatchCount;
                lastIndex += fcmBatchCount;
            } while (deviceList.size() > 0);
            Thread.sleep(100);
            sendPushBatchMapper.putManualPush("Y", vo.getSnManualPushId());
        }
    }
}
