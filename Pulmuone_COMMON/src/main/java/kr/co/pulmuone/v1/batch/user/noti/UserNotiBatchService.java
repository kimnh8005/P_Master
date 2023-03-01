package kr.co.pulmuone.v1.batch.user.noti;

import kr.co.pulmuone.v1.batch.system.cache.auth.SystemAuthBatchBiz;
import kr.co.pulmuone.v1.batch.user.noti.dto.vo.NotiBatchVo;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.mappers.batch.master.user.UserNotiBatchMapper;
import kr.co.pulmuone.v1.user.noti.dto.UserNotiRequestDto;
import kr.co.pulmuone.v1.user.noti.service.UserNotiBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserNotiBatchService {

    private final UserNotiBatchMapper userNotiBatchMapper;

    @Autowired
    private SystemAuthBatchBiz systemAuthBatchBiz;

    @Autowired
    private UserNotiBiz userNotiBiz;

    /**
     * BOS 알림 메시지 Batch 실행
     */
    public void runUserNoti() throws Exception {
        addUserNoti(UserEnums.UserNotiType.EXHIBIT_START);
        addUserNoti(UserEnums.UserNotiType.EVENT_START);
        addUserNoti(UserEnums.UserNotiType.EVENT_END);
    }

    private void addUserNoti(UserEnums.UserNotiType userNotiType) throws Exception {
        String pgId = "";
        List<NotiBatchVo> notiBatchVoList = null;
        // 알림 타겟 조회
        if (userNotiType.equals(UserEnums.UserNotiType.EXHIBIT_START)) {
            pgId = "exhibitMgm";
            notiBatchVoList = userNotiBatchMapper.getNotiExhibitStart(UserEnums.UserNotiType.EXHIBIT_START.getCode());
            if (notiBatchVoList != null && notiBatchVoList.size() > 0) {
                notiBatchVoList.forEach(vo -> {
                    vo.setUserNotiType(UserEnums.UserNotiType.EXHIBIT_START.getCode());
                    vo.setNotiMessage(UserEnums.UserNotiType.EXHIBIT_START.getMsg().replace("{기획전명}", vo.getTargetTitle()));
                });
            }
        } else if (userNotiType.equals(UserEnums.UserNotiType.EVENT_START)) {
            pgId = "eventMgm";
            notiBatchVoList = userNotiBatchMapper.getNotiEventStart(UserEnums.UserNotiType.EVENT_START.getCode());
            if (notiBatchVoList != null && notiBatchVoList.size() > 0) {
                notiBatchVoList.forEach(vo -> {
                    vo.setUserNotiType(UserEnums.UserNotiType.EVENT_START.getCode());
                    vo.setNotiMessage(UserEnums.UserNotiType.EVENT_START.getMsg().replace("{이벤트명}", vo.getTargetTitle()));
                });
            }
        } else if (userNotiType.equals(UserEnums.UserNotiType.EVENT_END)) {
            pgId = "eventMgm";
            notiBatchVoList = userNotiBatchMapper.getNotiEventEnd(UserEnums.UserNotiType.EVENT_END.getCode());
            if (notiBatchVoList != null && notiBatchVoList.size() > 0) {
                notiBatchVoList.forEach(vo -> {
                    vo.setUserNotiType(UserEnums.UserNotiType.EVENT_END.getCode());
                    vo.setNotiMessage(UserEnums.UserNotiType.EVENT_END.getMsg().replace("{이벤트명}", vo.getTargetTitle()));
                });
            }
        }

        if (notiBatchVoList == null || notiBatchVoList.size() == 0) return;

        // 알림 대상자 조회
        List<Long> userList = systemAuthBatchBiz.getSystemProgramUserList(pgId);

        for (NotiBatchVo vo : notiBatchVoList) {
            // 알림 진행
            userNotiBiz.addNoti(UserNotiRequestDto.builder()
                    .urUserIdList(userList)
                    .userNotiType(userNotiType.getCode())
                    .notiMessage(vo.getNotiMessage())
                    .targetType(vo.getTargetType())
                    .targetPk(vo.getNotiTargetId())
                    .build());
        }

        // 알림 히스토리 저장
        userNotiBatchMapper.addNotiHistory(notiBatchVoList);
    }

}
