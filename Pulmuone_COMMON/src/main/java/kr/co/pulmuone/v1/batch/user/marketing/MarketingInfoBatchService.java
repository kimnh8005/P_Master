package kr.co.pulmuone.v1.batch.user.marketing;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.batch.send.template.service.SendTemplateBatchBiz;
import kr.co.pulmuone.v1.batch.send.template.service.dto.AddEmailIssueSelectBatchRequestDto;
import kr.co.pulmuone.v1.batch.send.template.service.dto.AddSmsIssueSelectBatchRequestDto;
import kr.co.pulmuone.v1.batch.send.template.service.dto.GetEmailSendBatchResponseDto;
import kr.co.pulmuone.v1.batch.send.template.service.dto.vo.GetEmailSendBatchResultVo;
import kr.co.pulmuone.v1.batch.user.marketing.dto.MarketingInfoBatchDto;
import kr.co.pulmuone.v1.comm.enums.SendEnums;
import kr.co.pulmuone.v1.comm.mappers.batch.master.user.marketing.MarketingInfoBatchMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MarketingInfoBatchService {

    @Autowired
    @Qualifier("masterSqlSessionTemplateBatch")
    private SqlSessionTemplate masterSqlSession;

    private MarketingInfoBatchMapper marketingInfoBatchMapper;

    @Autowired
    private SendTemplateBatchBiz sendTemplateBatchBiz;

    /**
     * 마케팅 정보 수신동의확인 Batch 실행
     */
    public void runMarketingInfo() {
        this.marketingInfoBatchMapper = masterSqlSession.getMapper(MarketingInfoBatchMapper.class);

        // 대상자 조회
        List<MarketingInfoBatchDto> marketingInfoList = marketingInfoBatchMapper.getTargetMarketingInfo();
        for(MarketingInfoBatchDto dto : marketingInfoList) {
        	sendMarketingInfo(dto);
        }
    }


    /**
     * @Desc 자동메일 발송
     * @param MarketingInfoBatchDto
     * @return void
     */
    private void sendMarketingInfo(MarketingInfoBatchDto marketingInfoBatchDto) {

    	GetEmailSendBatchResponseDto result = sendTemplateBatchBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.MARKETING_INFO.getCode());
    	GetEmailSendBatchResultVo getEmailSendResultVo = result.getRows();

    	//이메일 발송
    	if("Y".equals(getEmailSendResultVo.getMailSendYn())) {
    		//serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
    		String content = sendTemplateBatchBiz.getDomainManagement() + "/admin/system/emailtmplt/getMarketingInfo?urUserId=" + marketingInfoBatchDto.getUrUserId();
    		String title = getEmailSendResultVo.getMailTitle();
    		String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
    		String senderName = sendTemplateBatchBiz.getPsValue("SEND_EMAIL_SENDER");
    		String senderMail = sendTemplateBatchBiz.getPsValue("SEND_EMAIL_ADDRESS");

    		AddEmailIssueSelectBatchRequestDto addEmailIssueSelectRequestDto = AddEmailIssueSelectBatchRequestDto.builder()
    				.senderName(senderName) // SEND_EMAIL_SENDER
    				.senderMail(senderMail) // SEND_EMAIL_ADDRESS
    				.reserveYn(reserveYn)
    				.content(content)
    				.title(title)
    				.urUserId(String.valueOf(marketingInfoBatchDto.getUrUserId()))
    				.mail(marketingInfoBatchDto.getMail())
    				.build();

    		sendTemplateBatchBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
    	}

    	//SMS 발송
    	if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {

    		String content = sendTemplateBatchBiz.getSMSTmplt(getEmailSendResultVo, marketingInfoBatchDto);
    		String senderTelephone = sendTemplateBatchBiz.getPsValue("SEND_SMS_NUMBER");
    		String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)

    		AddSmsIssueSelectBatchRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectBatchRequestDto.builder()
    				.content(content)
    				.urUserId(String.valueOf(marketingInfoBatchDto.getUrUserId()))
    				.mobile(marketingInfoBatchDto.getMobile())
    				.senderTelephone(senderTelephone) // SEND_SMS_NUMBER
    				.reserveYn(reserveYn)
    				.build();

    		sendTemplateBatchBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);

    	}
    }


}
