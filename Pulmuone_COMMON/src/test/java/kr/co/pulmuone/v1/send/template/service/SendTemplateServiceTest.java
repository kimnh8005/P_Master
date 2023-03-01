package kr.co.pulmuone.v1.send.template.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.SendEnums.Email;
import kr.co.pulmuone.v1.send.template.dto.*;

import kr.co.pulmuone.v1.send.template.dto.vo.BatchSmsTargetVo;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class SendTemplateServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private SendTemplateService sendTemplateService;

    @BeforeEach
    void setUp() {
        preLogin();
    }

    @Test
    void EMAIL_SMS_템플릿설정_리스트_조회() {
        GetEmailSendListRequestDto dto = new GetEmailSendListRequestDto();
        GetEmailSendListResponseDto result = sendTemplateService.getEmailSendList(dto);

        assertTrue(CollectionUtils.isNotEmpty(result.getRows()));
        assertTrue(result.getTotal() > 0);
    }

    @Test
    void EMAIL_SMS_템플릿설정_상세조회_성공() {
        //given
        Long snAutoSendId = 320L;

        //when
        GetEmailSendResponseDto result = sendTemplateService.getEmailSend(snAutoSendId);

        //then
        assertFalse(ObjectUtils.isEmpty(result.getRows()));
    }

    @Test
    void EMAIL_SMS_템플릿설정_상세조회_실패() {
        Long snAutoSendId = 100L;
        GetEmailSendResponseDto result = sendTemplateService.getEmailSend(snAutoSendId);
        assertFalse(!ObjectUtils.isEmpty(result.getRows()));
    }

    @Test
    void 템플릿_중복체크_통과() {
        Map<String, Object> param = new HashMap<>();
        param.put("templateCode","신규템플릿");

        Email email = sendTemplateService.checkEmailSendDuplicate(param);

        assertTrue(email.getCode().equals(Email.SUCCESS.getCode()));
    }

    @Test
    void 템플릿_데이터체크_중복() {
        //given
        Map<String, Object> param = new HashMap<>();
        param.put("templateCode","goodsRestockInfo");

        //when
        Email email = sendTemplateService.checkEmailSendDuplicate(param);

        //then
        assertEquals(Email.DUPLICATE_DATA.getCode(), email.getCode());
    }

    @Test
    void EMAIL_SMS_템플릿설정_추가_성공() {
        AddEmailSendRequestDto addEmailSendRequestDto = AddEmailSendRequestDto.builder()
                .templateCode("템플릿 추가")
                .templateName("TEST 템플릿추가")
                .mailSendYn("Y")
                .smsSendYn("Y")
                .smsBody("SMS 템플릿")
                .mailTitle("EMAIL 제목")
                .mailBody("EMAIL 템플릿")
                .build();

        ApiResult<?> apiResult = sendTemplateService.addEmailSend(addEmailSendRequestDto);

        assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());
    }

    @Test
    void EMAIL_SMS_템플릿설정_추가_중복() {
        //given
        AddEmailSendRequestDto addEmailSendRequestDto = AddEmailSendRequestDto.builder()
                .templateCode("goodsRestockInfo")
                .templateName("TEST 템플릿추가")
                .mailSendYn("Y")
                .smsSendYn("Y")
                .smsBody("SMS 템플릿")
                .mailTitle("EMAIL 제목")
                .mailBody("EMAIL 템플릿")
                .build();

        //when
        ApiResult<?> apiResult = sendTemplateService.addEmailSend(addEmailSendRequestDto);

        //then
        assertSame(Email.DUPLICATE_DATA.getCode(), apiResult.getCode());
    }

    @Test
    void EMAIL_SMS_템플릿설정_수정() {
        PutEmailSendRequestDto putEmailSendRequestDto = PutEmailSendRequestDto.builder()
                .snAutoSendId("253")
                .templateCode("test")
                .templateName("TEST 템플릿추가")
                .mailSendYn("Y")
                .smsSendYn("N")
                .smsBody("SMS 템플릿")
                .mailTitle("EMAIL 제목")
                .mailBody("EMAIL 템플릿")
                .build();

        ApiResult<?> apiResult = sendTemplateService.putEmailSend(putEmailSendRequestDto);

        assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());
    }

    @Test
    void EMAIL_SMS_템플릿_삭제() {
        Long snAutoSendId = 253L;
        ApiResult<?> apiResult = sendTemplateService.delEmailSend(snAutoSendId);

        assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());
    }

    @Test
    void 선택한_회원_EMAIL_발송() {
        AddEmailIssueSelectRequestDto addEmailIssueSelectRequestDto = AddEmailIssueSelectRequestDto.builder()
                .senderMail("sender@gmail.com")
                .senderName("보내는이")
                .title("메일제목")
                .content("메일내용입니다.")
                .attachment(null)
                .originFileName(null)
                .bcc(null)
                .mail("receiver@gmail.com")
                .reserveYn("N")
                .urUserId("178224")
                .build();

        ApiResult<?> apiResult = sendTemplateService.addEmailIssueSelect(addEmailIssueSelectRequestDto);

        assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());
    }

    @Test
    void 선택한_회원_SMS_발송() {
        AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
                .content("테스트")
                .urUserId("178224")
                .mobile("010-1234-5678")
                .senderTelephone("010-4567-8900")
                .attachment(null)
                .snManualSmsId(null)
                .snAutoSendId(null)
                .build();

        ApiResult<?> apiResult = sendTemplateService.addSmsIssueSelect(addSmsIssueSelectRequestDto);

        assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());
    }

    @Test
    void EMAIL_SMS_상세조회_BY템플릿코드_성공() {
        GetEmailSendResponseDto result = sendTemplateService.getSendTemplateByCode("bosFindPassword");
        assertTrue(!ObjectUtils.isEmpty(result.getRows()));
    }

    @Test
    void EMAIL_SMS_상세조회_BY템플릿코드_실패() {
        GetEmailSendResponseDto result = sendTemplateService.getSendTemplateByCode("abcdtest");
        assertFalse(!ObjectUtils.isEmpty(result.getRows()));
    }

    @Test
    void EMAIL_SMS_psKey로_psValue조회() {
    	//given
    	String psKey = "SEND_EMAIL_SENDER";

    	//when
    	String psValue = sendTemplateService.getPsValue(psKey);

    	//then
    	assertEquals("풀무원샵", psValue);
    }

    @Test
    void getBatchSmsTargetList_조회_성공() {
        //given
        Long batchNo = 9992L;

        //when
        List<BatchSmsTargetVo> result = sendTemplateService.getBatchSmsTargetList(batchNo);

        //then
        assertTrue(result.size() > 0);
    }

}