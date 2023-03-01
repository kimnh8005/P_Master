package kr.co.pulmuone.v1.send.template.service;

import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.SendEnums;
import kr.co.pulmuone.v1.send.template.dto.*;
import kr.co.pulmuone.v1.send.template.dto.vo.BatchSmsTargetVo;

import java.util.List;

public interface SendTemplateBiz {

    // Email,SMS 템플릿 설정
    ApiResult<?> getEmailSendList(GetEmailSendListRequestDto dto);

    // Email,SMS 템플릿 설정 상세조회
    ApiResult<?> getEmailSend(Long snAutoSendId);

    // Email,SMS 템플릿 설정 삽입
    ApiResult<?> addEmailSend(AddEmailSendRequestDto dto);

    // Email,SMS 템플릿 설정 수정
    ApiResult<?> putEmailSend(PutEmailSendRequestDto dto);

    // Email,SMS 템플릿 설정 삭제
    ApiResult<?> delEmailSend(Long snAutoSendId);

    // 선택한 회원 Email 발송
    ApiResult<?> addEmailIssueSelect(AddEmailIssueSelectRequestDto dto);

    // 선택한 회원 SMS 발송
    ApiResult<?> addSmsIssueSelect(AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto);

    /**
     * 대상으로 메일 전송
     * @param sendTargetType
     * @param mailTitle
     * @param mailContent
     * @param reserveYn
     * @return
     */
    void sendMailToTarget(SendEnums.SendTargetType sendTargetType, String mailTitle, String mailContent, String reserveYn);

    /**
     * 발송 대상 리스트 조회
     * ST_COMN_CODE.ST_COMN_CODE_MST_CD
     * @param sendTargetType
     * @return
     */
    List<GetCodeListResultVo> getSendTargetList(SendEnums.SendTargetType sendTargetType);

    // Email,SMS 템플릿 코드로 상세조회
    ApiResult<?> getSendTemplateByCode(String templateCode);

    //psKey값으로 psValue값 조회
    String getPsValue(String psKey);

 	//도메인 관리
	String getDomainManagement();
	
	//SMS 즉시 발송 및 이력 저장
    Boolean sendSmsApi(AddSmsIssueSelectRequestDto dto) throws Exception;

    List<BatchSmsTargetVo> getBatchSmsTargetList(Long batchNo);

    // 상품발송 자동메일 중복검사
    int isOverlapSendMailForGoodsDelivery(AddEmailIssueSelectRequestDto addEmailIssueSelectRequestDto);

    // 상품발송 SMS 중복검사
    int isOverlapSendSmsForGoodsDelivery(AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto);

}
