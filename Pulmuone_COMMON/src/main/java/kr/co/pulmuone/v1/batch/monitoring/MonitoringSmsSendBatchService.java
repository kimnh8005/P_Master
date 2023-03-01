package kr.co.pulmuone.v1.batch.monitoring;

import kr.co.pulmuone.v1.batch.monitoring.dto.vo.MonitoringSmsSendResultVo;
import kr.co.pulmuone.v1.batch.send.template.service.SendTemplateBatchBiz;
import kr.co.pulmuone.v1.batch.send.template.service.dto.AddSmsIssueSelectBatchRequestDto;
import kr.co.pulmuone.v1.comm.mappers.batch.master.monitoring.MonitoringSmsSendBatchMapper;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.system.code.dto.GetCodeListRequestDto;
import kr.co.pulmuone.v1.system.code.dto.GetCodeListResponseDto;
import kr.co.pulmuone.v1.system.code.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.system.code.service.SystemCodeBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MonitoringSmsSendBatchService {

    private final MonitoringSmsSendBatchMapper monitoringSmsSendBatchMapper;

    @Autowired
    SystemCodeBiz systemCodeBiz;

    @Autowired
    private SendTemplateBatchBiz sendTemplateBatchBiz;



    protected void addUserOrderPaymentInfo() {
        monitoringSmsSendBatchMapper.addUserOrderPaymentInfo();
    }

    protected void execMonitoringInfoSend(String stCommonCodeMasterId) throws Exception {

        //09시 배치실행시에만 전일 마감 문자내용 추가
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH")); //현재 시간
        String yesterdayContentAll = "";
        if("09".equals(dateTime)) {
            MonitoringSmsSendResultVo yesterdayResultVo = monitoringSmsSendBatchMapper.getUserOrderPaymentInfoYesterdayAll();

            if(ObjectUtils.isNotEmpty(yesterdayResultVo)) {
                yesterdayContentAll = "[" + yesterdayResultVo.getExecYmd() + " 합계]"
                        + "\r\n■ 가입회원수 : " + yesterdayResultVo.getTotUserCnt() + " 명"
                        + "\r\n■ 자사몰 주문건수 : " + yesterdayResultVo.getTotOrderCnt() + " 건"
                        + "\r\n■ 자사몰 결제금액 : " + StringUtil.numberFormat(yesterdayResultVo.getTotPaymentPrice()) + "원"
                        + "\r\n■ 외부몰 주문건수 : " + yesterdayResultVo.getTotOrderCntOutmall() + " 건"
                        + "\r\n■ 외부몰 결제금액 : " + StringUtil.numberFormat(yesterdayResultVo.getTotPaymentPriceOutmall()) + "원\n\n";
            }
        }

        //SMS발송할 정보를 가져온다
        MonitoringSmsSendResultVo resultVo = monitoringSmsSendBatchMapper.getUserOrderPaymentInfo();

        //SMS발송 데이터가 존재하면 발송
        if(ObjectUtils.isNotEmpty(resultVo)) {
            String content = "[" + resultVo.getExecYmd() + " " + resultVo.getExecTime() + "]"
                    + "\r\n■ 가입회원수 : " + resultVo.getTotUserCnt() + " 명"
                    + "\r\n■ 자사몰 주문건수 : " + resultVo.getTotOrderCnt() + " 건"
                    + "\r\n■ 자사몰 결제금액 : " + StringUtil.numberFormat(resultVo.getTotPaymentPrice()) + "원"
                    + "\r\n■ 외부몰 주문건수 : " + resultVo.getTotOrderCntOutmall() + " 건"
                    + "\r\n■ 외부몰 결제금액 : " + StringUtil.numberFormat(resultVo.getTotPaymentPriceOutmall()) + "원";
            if(!"".equals(yesterdayContentAll)) content = yesterdayContentAll + content;

            String senderTelephone = sendTemplateBatchBiz.getPsValue("SEND_SMS_NUMBER");
            String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)

            //SMS수신자 대상으로 문자 발송
            GetCodeListRequestDto getCodeListRequestDto = GetCodeListRequestDto.builder()
                    .stCommonCodeMasterId(stCommonCodeMasterId)
                    .useYn("Y")
                    .build();

            GetCodeListResponseDto getCodeListResponseDto = systemCodeBiz.getCodeList(getCodeListRequestDto);

            List<GetCodeListResultVo> codeListResultVoList = getCodeListResponseDto.getRows();

            if (CollectionUtils.isNotEmpty(codeListResultVoList)) {
                for (kr.co.pulmuone.v1.system.code.dto.vo.GetCodeListResultVo target : codeListResultVoList) {
                    // 모니터링 대상자 SMS 발송
                    AddSmsIssueSelectBatchRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectBatchRequestDto.builder()
                            .content(content)
                            .mobile(target.getAttribute2())
                            .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
                            .reserveYn(reserveYn)
                            .build();

                    sendMonitoringInfo(addSmsIssueSelectRequestDto);
                }

                upUserOrderPaymentInfo(); //전송된 모니터링 데이터 전송여부 Y 업데이트
            }
        }
    }

    /**
     * @Desc SMS 발송
     * @param addSmsIssueSelectRequestDto
     * @return void
     */
    private void sendMonitoringInfo(AddSmsIssueSelectBatchRequestDto addSmsIssueSelectRequestDto) {
        sendTemplateBatchBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);
    }

    /**
     * @Desc 전송된 데이터 전송여부 Y 업데이트
     * @param
     * @return void
     */
    private void upUserOrderPaymentInfo() {
        monitoringSmsSendBatchMapper.upUserOrderPaymentInfo();
    }
}
