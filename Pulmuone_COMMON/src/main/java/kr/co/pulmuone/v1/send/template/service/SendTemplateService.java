package kr.co.pulmuone.v1.send.template.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.SendEnums.Email;
import kr.co.pulmuone.v1.comm.mapper.send.template.SendTemplateMapper;
import kr.co.pulmuone.v1.send.template.dto.*;
import kr.co.pulmuone.v1.send.template.dto.vo.BatchSmsTargetVo;
import kr.co.pulmuone.v1.send.template.dto.vo.GetEmailSendListResultVo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("sendTemplateServiceHangaram")
@RequiredArgsConstructor
public class SendTemplateService {

    @NotNull
    private final SendTemplateMapper sendTemplateMapper;

    /**
     * Email,SMS 템플릿 설정
     * @param getEmailSendListRequestDto
     * @return GetEmailSendListResponseDto
     */
    protected GetEmailSendListResponseDto getEmailSendList(GetEmailSendListRequestDto getEmailSendListRequestDto) {
        GetEmailSendListResponseDto result = new GetEmailSendListResponseDto();

        int total = sendTemplateMapper.getEmailSendListCount(getEmailSendListRequestDto);
        List<GetEmailSendListResultVo> rows = sendTemplateMapper.getEmailSendList(getEmailSendListRequestDto);

        result.setTotal(total);
        result.setRows(rows);

        return result;
    }


    /**
     * Email,SMS 템플릿 설정 상세조회
     * @param snAutoSendId
     * @return GetEmailSendResponseDto
     */
    protected GetEmailSendResponseDto getEmailSend(Long snAutoSendId)  {
        GetEmailSendResponseDto result = new GetEmailSendResponseDto();
        result.setRows(sendTemplateMapper.getEmailSend(snAutoSendId));

        return result;
    }

    /**
     * 중복 데이터 체크
     * @param parameterMap
     * @return SendEnums.Email
     */
    protected Email checkEmailSendDuplicate(Map<String, Object> parameterMap) {
        int count = 0;

        if(!parameterMap.isEmpty()) {
            count = sendTemplateMapper.checkEmailSendDuplicate(parameterMap);

            if(count > 0) {
                return Email.DUPLICATE_DATA;
            }
        }

        return Email.SUCCESS;
    }

    /**
     * Email,SMS 템플릿 설정 삽입
     * @param addEmailSendRequestDto
     * @return ApiResult<?>
     */
    protected ApiResult<?> addEmailSend(AddEmailSendRequestDto addEmailSendRequestDto) {
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("templateCode", addEmailSendRequestDto.getTemplateCode());

        Email emailEnum = this.checkEmailSendDuplicate(parameterMap);

        // 템플릿코드, 템플릿이름이 없는경우 반환
        if(addEmailSendRequestDto.getTemplateCode().isEmpty() || addEmailSendRequestDto.getTemplateName().isEmpty()) {
            return ApiResult.result(Email.MANDATORY_MISSING);
        }

        if(Email.DUPLICATE_DATA == emailEnum) {
            return ApiResult.result(Email.DUPLICATE_DATA);
        }
        sendTemplateMapper.addEmailSend(addEmailSendRequestDto);
        return ApiResult.success();
    }

    /**
     * Email,SMS 템플릿 설정 수정
     * @param putEmailSendRequestDto
     * @return ApiResult<?>
     */
    protected ApiResult<?> putEmailSend(PutEmailSendRequestDto putEmailSendRequestDto) {
        Map<String, Object> parameterMap 	= new HashMap<>();
        parameterMap.put("snAutoSendId", putEmailSendRequestDto.getSnAutoSendId());
        parameterMap.put("templateCode", putEmailSendRequestDto.getTemplateCode());

        Email emailEnum  = this.checkEmailSendDuplicate(parameterMap);

        if(Email.DUPLICATE_DATA == emailEnum) {
            return ApiResult.result(Email.DUPLICATE_DATA);
        }

        sendTemplateMapper.putEmailSend(putEmailSendRequestDto);

        return ApiResult.success();
    }

    /**
     * Email,SMS 템플릿 설정 삭제
     * @param snAutoSendId
     * @return ApiResult
     */
    protected ApiResult<?> delEmailSend(Long snAutoSendId) {
        sendTemplateMapper.delEmailSend(snAutoSendId);
        return ApiResult.success();
    }

    /**
     * 선택한 회원 Email 발송
     * @param   addEmailIssueSelectRequestDto
     * @return  ApiResult<?>
     */
    protected ApiResult<?> addEmailIssueSelect(AddEmailIssueSelectRequestDto addEmailIssueSelectRequestDto) {
        if(addEmailIssueSelectRequestDto.getMail() != null && !addEmailIssueSelectRequestDto.getMail().isEmpty()) {
            AddEmailManualRequestDto addEmailManualRequestDto = AddEmailManualRequestDto.builder()
                                                            .senderMail(addEmailIssueSelectRequestDto.getSenderMail())
                                                            .senderName(addEmailIssueSelectRequestDto.getSenderName())
                                                            .title(addEmailIssueSelectRequestDto.getTitle())
                                                            .content(addEmailIssueSelectRequestDto.getContent())
                                                            .attachment(addEmailIssueSelectRequestDto.getAttachment())
                                                            .originFileName(addEmailIssueSelectRequestDto.getOriginFileName())
                                                            .bcc(addEmailIssueSelectRequestDto.getBcc())
                                                            .build();

            sendTemplateMapper.addEmailManual(addEmailManualRequestDto);                   // Email 수동등록
            sendTemplateMapper.addEmailIssue(addEmailIssueSelectRequestDto);               // Email 발송대상 등록

            return ApiResult.success();
        } else {
            return ApiResult.success();
        }
    }

    /**
     * 선택한 회원 SMS 보내기
     * @param addSmsIssueSelectRequestDto
     * @return ApiResult<?>
     */
    protected ApiResult<?> addSmsIssueSelect(AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto) {

    	if(StringUtils.isNotEmpty(addSmsIssueSelectRequestDto.getMobile())) {
    		String mobile = addSmsIssueSelectRequestDto.getMobile().replaceAll("-", "");
    		addSmsIssueSelectRequestDto.setMobile(mobile);
    	}

        AddSmsManualRequestDto addSmsManualRequestDto = AddSmsManualRequestDto.builder()
                .content(addSmsIssueSelectRequestDto.getContent())
                .reserveDate(addSmsIssueSelectRequestDto.getReserveDate())
                .senderTelephone(addSmsIssueSelectRequestDto.getSenderTelephone())
                .attachment(addSmsIssueSelectRequestDto.getAttachment())
                .snManualSmsId(addSmsIssueSelectRequestDto.getSnManualSmsId())
                .build();

        sendTemplateMapper.addSmsManual(addSmsManualRequestDto);
        sendTemplateMapper.addSmsIssue(addSmsIssueSelectRequestDto);

        return ApiResult.success();
    }



    /**
     * Email,SMS 템플릿 코드로 상세조회
     * @param templateCode
     * @return GetEmailSendResponseDto
     */
    protected GetEmailSendResponseDto getSendTemplateByCode(String templateCode)  {
        GetEmailSendResponseDto result = new GetEmailSendResponseDto();
        result.setRows(sendTemplateMapper.getSendTemplateByCode(templateCode));

        return result;
    }


	/**
	 * @Desc Email,SMS psKey로 psValue조회
	 * @param psKey
	 * @return String
	 */
	protected String getPsValue(String psKey) {
		return sendTemplateMapper.getPsValue(psKey);
	}


    /**
     * Batch SMS 전송 대상자 조회
     *
     * @param batchNo Long
     * @return List<BatchSmsTargetVo>
     */
    protected List<BatchSmsTargetVo> getBatchSmsTargetList(Long batchNo) {
        return sendTemplateMapper.getBatchSmsTargetList(batchNo);
    }

    /**
     * 상품발송 자동메일 중복검사
     *
     * @param addEmailIssueSelectRequestDto
     * @return int
     */
    protected int isOverlapSendMailForGoodsDelivery(AddEmailIssueSelectRequestDto addEmailIssueSelectRequestDto){
        return sendTemplateMapper.isOverlapSendMailForGoodsDelivery(addEmailIssueSelectRequestDto);
    }

    /**
     * 상품발송 SMS 중복검사
     *
     * @param addSmsIssueSelectRequestDto
     * @return int
     */
    protected int isOverlapSendSmsForGoodsDelivery(AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto){
        return sendTemplateMapper.isOverlapSendSmsForGoodsDelivery(addSmsIssueSelectRequestDto);
    }




}
