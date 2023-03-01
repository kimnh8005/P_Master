package kr.co.pulmuone.v1.batch.system.log;

import kr.co.pulmuone.v1.comm.constants.SystemConstants;
import kr.co.pulmuone.v1.comm.enums.SystemEnums;
import kr.co.pulmuone.v1.order.front.dto.vo.OrderInfoFromIllegalLogVo;
import kr.co.pulmuone.v1.order.front.service.OrderFrontBiz;
import kr.co.pulmuone.v1.system.log.dto.DetectDeviceLogRequestDto;
import kr.co.pulmuone.v1.system.log.dto.IllegalLogRequestDto;
import kr.co.pulmuone.v1.system.log.dto.vo.DeviceLogVo;
import kr.co.pulmuone.v1.system.log.service.SystemLogBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SystemLogBatchService {
    @Autowired
    private SystemLogBiz systemLogBiz;

    @Autowired
    private OrderFrontBiz orderFrontBiz;

    /**
     * 부정거래탐지 - 회원가입
     */
    public void runDetectIllegalUserJoin() {
        String startDateTime = LocalDateTime.now().minusHours(SystemConstants.BATCH_ILLEGAL_DETECT_HOUR).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 부정거래 탐지
        List<DeviceLogVo> detectLogList = systemLogBiz.getDeviceLogDetect(DetectDeviceLogRequestDto.builder()
                .deviceLogType(SystemEnums.DeviceLogType.USER_JOIN)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .detectCount(SystemConstants.BATCH_ILLEGAL_USER_JOIN_CRITERIA)
                .build());

        // 부정거래 저장
        List<IllegalLogRequestDto> addList = detectLogList.stream()
                .map(vo -> IllegalLogRequestDto.builder()
                        .illegalType(SystemEnums.IllegalType.USER)
                        .illegalDetailType(SystemEnums.IllegalDetailType.USER_JOIN)
                        .illegalStatusType(SystemEnums.IllegalStatusType.DETECT)
                        .illegalDetect(vo.getDetectContent())
                        .urPcidCd(vo.getUrPcidCd())
                        .detectStartDate(startDateTime)
                        .detectEndDate(endDateTime)
                        .createId(0L)
                        .userIdList(vo.getUserIdList())
                        .build())
                .collect(Collectors.toList());
        systemLogBiz.addIllegalLogList(addList);
    }

    /**
     * 부정거래탐지 - 로그인
     */
    public void runDetectIllegalLoginFail() {
        String startDateTime = LocalDateTime.now().minusHours(SystemConstants.BATCH_ILLEGAL_DETECT_HOUR).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 부정거래 탐지
        List<DeviceLogVo> detectLogList = systemLogBiz.getDeviceLogDetect(DetectDeviceLogRequestDto.builder()
                .deviceLogType(SystemEnums.DeviceLogType.LOGIN_FAIL)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .detectCount(SystemConstants.BATCH_ILLEGAL_LOGIN_FAIL_CRITERIA)
                .build());

        // 부정거래 저장
        List<IllegalLogRequestDto> addList = detectLogList.stream()
                .map(vo -> IllegalLogRequestDto.builder()
                        .illegalType(SystemEnums.IllegalType.USER)
                        .illegalDetailType(SystemEnums.IllegalDetailType.LOGIN_FAIL)
                        .illegalStatusType(SystemEnums.IllegalStatusType.DETECT)
                        .illegalDetect(vo.getDetectContent())
                        .urPcidCd(vo.getUrPcidCd())
                        .detectStartDate(startDateTime)
                        .detectEndDate(endDateTime)
                        .createId(0L)
                        .userIdList(vo.getUserIdList())
                        .build())
                .collect(Collectors.toList());
        systemLogBiz.addIllegalLogList(addList);
    }

    /**
     * 부정거래탐지 - 주문건수
     */
    public void runDetectIllegalOrderCount() {
        String startDateTime = LocalDateTime.now().minusHours(SystemConstants.BATCH_ILLEGAL_DETECT_HOUR).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 부정거래 탐지
        List<OrderInfoFromIllegalLogVo> detectLogList = orderFrontBiz.getOrderInfoFromIllegalCount(startDateTime, endDateTime, SystemConstants.BATCH_ILLEGAL_ORDER_COUNT_CRITERIA);

        // 부정거래 저장
        List<IllegalLogRequestDto> addList = detectLogList.stream()
                .map(vo -> IllegalLogRequestDto.builder()
                        .illegalType(SystemEnums.IllegalType.ORDER)
                        .illegalDetailType(SystemEnums.IllegalDetailType.ORDER_COUNT)
                        .illegalStatusType(SystemEnums.IllegalStatusType.DETECT)
                        .illegalDetect(vo.getOrderCount())
                        .urPcidCd(vo.getUrPcidCd())
                        .detectStartDate(startDateTime)
                        .detectEndDate(endDateTime)
                        .createId(0L)
                        .userIdList(vo.getUserIdList())
                        .orderIdList(vo.getOrderIdList())
                        .build())
                .collect(Collectors.toList());
        systemLogBiz.addIllegalLogList(addList);
    }

    /**
     * 부정거래탐지 - 주문금액
     */
    public void runDetectIllegalOrderPrice() {
        String startDateTime = LocalDateTime.now().minusHours(SystemConstants.BATCH_ILLEGAL_DETECT_HOUR).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 부정거래 탐지
        List<OrderInfoFromIllegalLogVo> detectLogList = orderFrontBiz.getOrderInfoFromIllegalPrice(startDateTime, endDateTime, SystemConstants.BATCH_ILLEGAL_ORDER_PRICE_CRITERIA);

        // 부정거래 저장
        List<IllegalLogRequestDto> addList = detectLogList.stream()
                .map(vo -> IllegalLogRequestDto.builder()
                        .illegalType(SystemEnums.IllegalType.ORDER)
                        .illegalDetailType(SystemEnums.IllegalDetailType.ORDER_PRICE)
                        .illegalStatusType(SystemEnums.IllegalStatusType.DETECT)
                        .illegalDetect(vo.getOrderPrice())
                        .urPcidCd(vo.getUrPcidCd())
                        .detectStartDate(startDateTime)
                        .detectEndDate(endDateTime)
                        .createId(0L)
                        .userIdList(vo.getUserIdList())
                        .orderIdList(vo.getOrderIdList())
                        .build())
                .collect(Collectors.toList());
        systemLogBiz.addIllegalLogList(addList);
    }

}
