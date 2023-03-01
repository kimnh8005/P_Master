package kr.co.pulmuone.v1.send.template.service;

import com.google.gson.JsonObject;
import kr.co.pulmuone.v1.api.ncp.dto.NcpSmsRequestDto;
import kr.co.pulmuone.v1.api.ncp.service.NCPApiSmsBiz;
import kr.co.pulmuone.v1.base.dto.GetCodeListRequestDto;
import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.base.service.StComnBiz;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.ApiConstants;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.SendEnums;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.send.template.dto.*;
import kr.co.pulmuone.v1.send.template.dto.vo.BatchSmsTargetVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Service
public class SendTemplateBizImpl implements SendTemplateBiz {

    @Value("${resource.server.url.bos}")
    private String serverUrlBos;

    @Autowired
    private SendTemplateService sendTemplateService;

    @Autowired
    private NCPApiSmsBiz ncpApiSmsBiz;

    @Autowired
    private StComnBiz stComnBiz;

    @Override
    public ApiResult<?> getEmailSendList(GetEmailSendListRequestDto dto) {
        return ApiResult.success(sendTemplateService.getEmailSendList(dto));
    }

    @Override
    public ApiResult<?> getEmailSend(Long snAutoSendId) {
        return ApiResult.success(sendTemplateService.getEmailSend(snAutoSendId));
    }

    @Override
    public ApiResult<?> addEmailSend(AddEmailSendRequestDto dto) {
        return sendTemplateService.addEmailSend(dto);
    }

    @Override
    public ApiResult<?> putEmailSend(PutEmailSendRequestDto dto) {
        return sendTemplateService.putEmailSend(dto);
    }

    @Override
    public ApiResult<?> delEmailSend(Long snAutoSendId) {
        return sendTemplateService.delEmailSend(snAutoSendId);
    }

    @Override
    public ApiResult<?> addEmailIssueSelect(AddEmailIssueSelectRequestDto dto) {
        return sendTemplateService.addEmailIssueSelect(dto);
    }

    @Override
    public ApiResult<?> addSmsIssueSelect(AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto) {
        return sendTemplateService.addSmsIssueSelect(addSmsIssueSelectRequestDto);
    }

    @Override
    public void sendMailToTarget(SendEnums.SendTargetType sendTargetType, String mailTitle, String mailContent, String reserveYn) {

        // 대상 조회
        List<GetCodeListResultVo> list = getSendTargetList(sendTargetType);

        for (GetCodeListResultVo target : list){
            AddEmailIssueSelectRequestDto addEmailIssueSelectRequestDto = AddEmailIssueSelectRequestDto.builder()
					.senderName(getPsValue("SEND_EMAIL_SENDER")) // SEND_EMAIL_SENDER
					.senderMail(getPsValue("SEND_EMAIL_ADDRESS")) // SEND_EMAIL_ADDRESS
					.reserveYn(reserveYn)
					.content(mailContent)
					.title(mailTitle)
					.urUserId("0")
					.mail(target.getAttr1())
					.build();
            sendTemplateService.addEmailIssueSelect(addEmailIssueSelectRequestDto);
        }
    }

    /**
     * 발송 대상 리스트 조회
     * @param sendTargetType
     * @return
     */
    @Override
    public List<GetCodeListResultVo> getSendTargetList(SendEnums.SendTargetType sendTargetType) {
        GetCodeListRequestDto dto = new GetCodeListRequestDto();
        dto.setStCommonCodeMasterCode(sendTargetType.getCode());
        dto.setUseYn("Y");
        dto.setGbLangId(Constants.GB_LANG_ID);

        return stComnBiz.getCommonCodeList(dto);
    }

    @Override
    public ApiResult<?> getSendTemplateByCode(String templateCode) {
        return ApiResult.success(sendTemplateService.getSendTemplateByCode(templateCode));
    }

    @Override
    public String getPsValue(String psKey) {
        return sendTemplateService.getPsValue(psKey);
    }

    public String getDomainManagement() {
        return serverUrlBos.replace("https://", "http://");
    }

    @Override
    public Boolean sendSmsApi(AddSmsIssueSelectRequestDto dto) throws Exception {
		if (StringUtil.isEmpty(dto.getMobile())) {
			return false;
		}
        dto.setMobile(dto.getMobile().replaceAll("-", ""));
        if (dto.getSenderTelephone() == null || StringUtil.isEmpty(dto.getSenderTelephone())) {
            dto.setSenderTelephone(sendTemplateService.getPsValue("SEND_SMS_NUMBER"));
        }
        dto.setSenderTelephone(dto.getSenderTelephone().replaceAll("-", ""));
        JsonObject jsonObject = ncpApiSmsBiz.getJSONObject(
                Collections.singletonList(NcpSmsRequestDto.builder()
                        .content(dto.getContent())
                        .mobile(dto.getMobile())
                        .senderTelephone(dto.getSenderTelephone())
                        .build()
                ),
                (dto.getContent().getBytes(StandardCharsets.UTF_8).length <= ApiConstants.NCP_SMS_LENGTH) ? SendEnums.SendNcpSmsType.SMS : SendEnums.SendNcpSmsType.LMS
        );

        if (jsonObject.size() == 0) return false;

        String responseValue = ncpApiSmsBiz.sendMessage(jsonObject);
        if (responseValue.equals(SendEnums.SendNcpSmsResponseCode.ACCEPT.getCode())) {
            dto.setSendYn("Y");
        }
        sendTemplateService.addSmsIssueSelect(dto);
        return responseValue.equals(SendEnums.SendNcpSmsResponseCode.ACCEPT.getCode());
    }

    @Override
    public List<BatchSmsTargetVo> getBatchSmsTargetList(Long batchNo) {
        return sendTemplateService.getBatchSmsTargetList(batchNo);
    }

    @Override
    public int isOverlapSendMailForGoodsDelivery(AddEmailIssueSelectRequestDto addEmailIssueSelectRequestDto){
        return sendTemplateService.isOverlapSendMailForGoodsDelivery(addEmailIssueSelectRequestDto);
    }

    @Override
    public int isOverlapSendSmsForGoodsDelivery(AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto){
        return sendTemplateService.isOverlapSendSmsForGoodsDelivery(addSmsIssueSelectRequestDto);
    }

}