package kr.co.pulmuone.v1.batch.promotion.point;

import kr.co.pulmuone.v1.batch.promotion.point.dto.PointExpiredListResponseDto;
import kr.co.pulmuone.v1.batch.promotion.point.dto.vo.PointExpiredListVo;
import kr.co.pulmuone.v1.batch.promotion.point.dto.vo.PointExpiredVo;
import kr.co.pulmuone.v1.batch.send.template.service.SendTemplateBatchBiz;
import kr.co.pulmuone.v1.batch.send.template.service.dto.AddEmailIssueSelectBatchRequestDto;
import kr.co.pulmuone.v1.batch.send.template.service.dto.AddSmsIssueSelectBatchRequestDto;
import kr.co.pulmuone.v1.batch.send.template.service.dto.GetEmailSendBatchResponseDto;
import kr.co.pulmuone.v1.batch.send.template.service.dto.vo.GetEmailSendBatchResultVo;
import kr.co.pulmuone.v1.comm.constants.PromotionConstants;
import kr.co.pulmuone.v1.comm.enums.SendEnums;
import kr.co.pulmuone.v1.comm.mappers.batch.master.promotion.PromotionPointBatchMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class PromotionPointBatchService {

    private final PromotionPointBatchMapper promotionPointBatchMapper;

    @Autowired
    private SendTemplateBatchBiz sendTemplateBatchBiz;

    /**
     * 적립금 소멸 예정 안내 Batch 실행
     */
    public void runPointExpectExpired() {

        // 대상자 조회
        List<PointExpiredVo> pointExpiredList = getPointExpectExpired();

        for(PointExpiredVo vo : pointExpiredList){
        	// 자동메일 & SMS 발송
        	sendMarketingInfo(vo);
        }
    }

    /**
     * 적립금 소멸 예정 대상자 조회
     */
    protected List<PointExpiredVo> getPointExpectExpired() {

    	// 매월 1일에 그 달 소멸예정인 적립금 모두 조회
        // 시작일자, 종료일자 설정
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDay = LocalDate.now().format(dateTimeFormatter);
        String endDay = LocalDate.now().plusDays(PromotionConstants.EXPIRED_DATE).format(dateTimeFormatter);

        // 미사용 적립금 회원 조회
        List<PointExpiredVo> pointUserList = promotionPointBatchMapper.getPointExpectExpired(startDay, endDay);
        if (pointUserList.isEmpty()) return pointUserList;

        // 미사용 적립금 리스트 조회
        List<PointExpiredListVo> expiredList = promotionPointBatchMapper.getPointExpectExpireList(pointUserList, startDay, endDay);
        Map<Long, List<PointExpiredListResponseDto>> expiredMap = new HashMap<>();
        expiredList.forEach(vo -> {
            List<PointExpiredListResponseDto> list = expiredMap.getOrDefault(vo.getUrUserId(), new ArrayList<>());
            list.add(new PointExpiredListResponseDto(vo));
            expiredMap.put(vo.getUrUserId(), list);
        });

        // 리턴값 설정
        pointUserList.forEach(vo -> {
            vo.setExpiredDate(PromotionConstants.EXPIRED_DATE);
            vo.setList(expiredMap.get(vo.getUrUserId()));
        });

        return pointUserList;
    }

    /**
     * @Desc 자동메일 발송
     * @param PointExpiredVo
     * @return void
     */
    private void sendMarketingInfo(PointExpiredVo pointExpiredVo) {

    	GetEmailSendBatchResponseDto result = sendTemplateBatchBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.POINT_EXPECT_EXPIRED.getCode());
    	GetEmailSendBatchResultVo getEmailSendResultVo = result.getRows();

    	//이메일 발송
    	if("Y".equals(getEmailSendResultVo.getMailSendYn())) {
    		//serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
    		String content = sendTemplateBatchBiz.getDomainManagement() + "/admin/system/emailtmplt/getPointExpectExpired?urUserId=" + pointExpiredVo.getUrUserId();
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
    				.urUserId(String.valueOf(pointExpiredVo.getUrUserId()))
    				.mail(pointExpiredVo.getMail())
    				.build();

    		sendTemplateBatchBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
    	}

    	//SMS 발송
    	if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {

    		String content = sendTemplateBatchBiz.getSMSTmplt(getEmailSendResultVo, pointExpiredVo);
    		String senderTelephone = sendTemplateBatchBiz.getPsValue("SEND_SMS_NUMBER");
    		String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)

    		AddSmsIssueSelectBatchRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectBatchRequestDto.builder()
    				.content(content)
    				.urUserId(String.valueOf(pointExpiredVo.getUrUserId()))
    				.mobile(pointExpiredVo.getMobile())
    				.senderTelephone(senderTelephone) // SEND_SMS_NUMBER
    				.reserveYn(reserveYn)
    				.build();

    		sendTemplateBatchBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);

    	}
    }

}
