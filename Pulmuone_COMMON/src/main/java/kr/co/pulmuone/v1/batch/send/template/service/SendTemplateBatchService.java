package kr.co.pulmuone.v1.batch.send.template.service;

import com.google.gson.JsonObject;
import kr.co.pulmuone.v1.api.ncp.dto.NcpSmsRequestDto;
import kr.co.pulmuone.v1.api.ncp.service.NCPApiSmsBiz;
import kr.co.pulmuone.v1.batch.send.template.service.dto.*;
import kr.co.pulmuone.v1.batch.send.template.service.dto.vo.SmsSendBatchResultVo;
import kr.co.pulmuone.v1.comm.constants.ApiConstants;
import kr.co.pulmuone.v1.comm.enums.SendEnums;
import kr.co.pulmuone.v1.comm.mappers.batch.master.send.SendTemplateBatchMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SendTemplateBatchService {
    @NotNull
    private final SendTemplateBatchMapper sendTemplateBatchMapper;

    @Autowired
    private NCPApiSmsBiz ncpApiSmsBiz;

    /**
     * 선택한 회원 Email 발송
     *
     * @param addEmailIssueSelectRequestDto
     * @return ApiResult<?>
     */
    protected void addEmailIssueSelect(AddEmailIssueSelectBatchRequestDto addEmailIssueSelectBatchRequestDto) {
        AddEmailManualBatchRequestDto addEmailManualBatchRequestDto = AddEmailManualBatchRequestDto.builder()
                .senderMail(addEmailIssueSelectBatchRequestDto.getSenderMail())
                .senderName(addEmailIssueSelectBatchRequestDto.getSenderName())
                .title(addEmailIssueSelectBatchRequestDto.getTitle())
                .content(addEmailIssueSelectBatchRequestDto.getContent())
                .attachment(addEmailIssueSelectBatchRequestDto.getAttachment())
                .originFileName(addEmailIssueSelectBatchRequestDto.getOriginFileName())
                .bcc(addEmailIssueSelectBatchRequestDto.getBcc())
                .build();

        sendTemplateBatchMapper.addEmailManual(addEmailManualBatchRequestDto);                   // Email 수동등록
        sendTemplateBatchMapper.addEmailIssue(addEmailIssueSelectBatchRequestDto);               // Email 발송대상 등록
    }

    /**
     * 선택한 회원 SMS 보내기
     *
     * @param addSmsIssueSelectRequestDto
     * @return ApiResult<?>
     */
    protected void addSmsIssueSelect(AddSmsIssueSelectBatchRequestDto addSmsIssueSelectBatchRequestDto) {
        AddSmsManualBatchRequestDto addSmsManualBatchRequestDto = AddSmsManualBatchRequestDto.builder()
                .content(addSmsIssueSelectBatchRequestDto.getContent())
                .reserveDate(addSmsIssueSelectBatchRequestDto.getReserveDate())
                .reserveYn(addSmsIssueSelectBatchRequestDto.getReserveYn())
                .senderTelephone(addSmsIssueSelectBatchRequestDto.getSenderTelephone())
                .attachment(addSmsIssueSelectBatchRequestDto.getAttachment())
                .snManualSmsId(addSmsIssueSelectBatchRequestDto.getSnManualSmsId())
                .build();

        sendTemplateBatchMapper.addSmsManual(addSmsManualBatchRequestDto);
        sendTemplateBatchMapper.addSmsIssue(addSmsIssueSelectBatchRequestDto);
    }


    /**
     * Email,SMS 템플릿 코드로 상세조회
     *
     * @param templateCode
     * @return GetEmailSendResponseDto
     */
    protected GetEmailSendBatchResponseDto getSendTemplateByCode(String templateCode) {
        GetEmailSendBatchResponseDto result = new GetEmailSendBatchResponseDto();
        result.setRows(sendTemplateBatchMapper.getSendTemplateByCode(templateCode));

        return result;
    }


    protected String getPsValue(String psKey) {
        return sendTemplateBatchMapper.getPsValue(psKey);
    }


    protected void runSendSms() throws Exception {
        //대상조회
        List<SmsSendBatchResultVo> target = sendTemplateBatchMapper.getSmsSend();
        String phoneNumber = sendTemplateBatchMapper.getPsValue("SEND_SMS_NUMBER");
        if (target == null || target.size() == 0) return;

        //수신자 전화번호 유효성 체크
        List<SmsSendBatchResultVo> targetList = target.stream().filter(f-> StringUtils.isNumeric(f.getMobile().replaceAll("-", ""))).collect(Collectors.toList());

        //SMS 전송
        sendSms(targetList.stream()
                        .filter(vo -> vo.getContent().getBytes(StandardCharsets.UTF_8).length <= ApiConstants.NCP_SMS_LENGTH)
                        .collect(Collectors.toList())
                , phoneNumber, SendEnums.SendNcpSmsType.SMS);

        //LMS 전송
        sendSms(targetList.stream()
                        .filter(vo -> vo.getContent().getBytes(StandardCharsets.UTF_8).length > ApiConstants.NCP_SMS_LENGTH)
                        .collect(Collectors.toList())
                , phoneNumber, SendEnums.SendNcpSmsType.LMS);
    }

    private void sendSms(List<SmsSendBatchResultVo> target, String phoneNumber, SendEnums.SendNcpSmsType smsType) throws Exception {
        List<NcpSmsRequestDto> sendList = new ArrayList<>();
        for (SmsSendBatchResultVo vo : target) {
            sendList.add(
                    NcpSmsRequestDto.builder()
                            .content(vo.getContent())
                            .mobile(vo.getMobile())
                            .senderTelephone(phoneNumber)
                            .build()
            );
        }

        JsonObject jsonObject = ncpApiSmsBiz.getJSONObject(sendList, smsType);
        if (jsonObject.size() == 0) return;

        String result = ncpApiSmsBiz.sendMessage(jsonObject);

        //결과 업데이트
        if (result.equals(SendEnums.SendNcpSmsResponseCode.ACCEPT.getCode())) {
            sendTemplateBatchMapper.putPushSend(target, phoneNumber);
        }
    }

}
