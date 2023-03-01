package kr.co.pulmuone.v1.batch.send.template.service;

import kr.co.pulmuone.v1.batch.send.template.service.dto.AddEmailIssueSelectBatchRequestDto;
import kr.co.pulmuone.v1.batch.send.template.service.dto.AddSmsIssueSelectBatchRequestDto;
import kr.co.pulmuone.v1.batch.send.template.service.dto.GetEmailSendBatchResponseDto;
import kr.co.pulmuone.v1.batch.send.template.service.dto.vo.GetEmailSendBatchResultVo;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface SendTemplateBatchBiz {

	// 선택한 회원 Email 발송
    void addEmailIssueSelect(AddEmailIssueSelectBatchRequestDto dto);

    // 선택한 회원 SMS 발송
    void addSmsIssueSelect(AddSmsIssueSelectBatchRequestDto addSmsIssueSelectBatchRequestDto);

    // Email,SMS 템플릿 코드로 상세조회
    GetEmailSendBatchResponseDto getSendTemplateByCode(String templateCode);

    //psKey값으로 psValue값 조회
    String getPsValue(String psKey);

    String getSMSTmplt(GetEmailSendBatchResultVo getEmailSendBatchResultVo, Object vo);

    //도메인 관리
  	String getDomainManagement();

    void runSendSms() throws Exception;

}
