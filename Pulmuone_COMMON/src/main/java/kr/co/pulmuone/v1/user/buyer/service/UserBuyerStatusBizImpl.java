package kr.co.pulmuone.v1.user.buyer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.base.service.ComnBizImpl;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.SendEnums;
import kr.co.pulmuone.v1.send.template.dto.AddEmailIssueSelectRequestDto;
import kr.co.pulmuone.v1.send.template.dto.AddSmsIssueSelectRequestDto;
import kr.co.pulmuone.v1.send.template.dto.GetEmailSendResponseDto;
import kr.co.pulmuone.v1.send.template.dto.vo.GetEmailSendResultVo;
import kr.co.pulmuone.v1.send.template.service.SendTemplateBiz;
import kr.co.pulmuone.v1.user.buyer.dto.GetBuyerStatusHistoryListRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.GetBuyerStatusHistoryListResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.GetUserStopHistoryListRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.GetUserStopHistoryListResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.GetUserStopHistoryRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.GetUserStopHistoryResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.GetUserStopListRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.GetUserStopListResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.MarketingInfoDto;
import kr.co.pulmuone.v1.user.buyer.dto.PutBuyerNormalRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.PutBuyerNormalResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.PutBuyerStopRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.PutBuyerStopResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.BuyerStatusResultVo;

@Service
public class UserBuyerStatusBizImpl implements UserBuyerStatusBiz {

    @Autowired
    private UserBuyerStatusService userBuyerStatusService;

    @Autowired
	private ComnBizImpl comnBizImpl;

    @Autowired
	private SendTemplateBiz sendTemplateBiz;


    @Override
    public GetUserStopListResponseDto getBuyerStopList(GetUserStopListRequestDto dto) throws Exception {
        return userBuyerStatusService.getBuyerStopList(dto);
    }

    @Override
    public GetUserStopHistoryResponseDto getBuyerStopLog(GetUserStopHistoryRequestDto dto) throws Exception {
        return userBuyerStatusService.getBuyerStopLog(dto);
    }

    @Override
    public GetUserStopHistoryListResponseDto getBuyerStopHistoryList(GetUserStopHistoryListRequestDto dto) throws Exception {
        return userBuyerStatusService.getBuyerStopHistoryList(dto);
    }

    @Override
    public GetBuyerStatusHistoryListResponseDto getBuyerStatusHistoryList(GetBuyerStatusHistoryListRequestDto dto) throws Exception {
        return userBuyerStatusService.getBuyerStatusHistoryList(dto);
    }

    @Override
    public PutBuyerStopResponseDto putBuyerStop(PutBuyerStopRequestDto dto) throws Exception {
    	PutBuyerStopResponseDto result = userBuyerStatusService.putBuyerStop(dto);

    	if(result != null) {
        	BuyerStatusResultVo buyerStatusResultVo = getBuyerStatusConvertInfo(dto.getUrUserId());
        	getBuyerStopConvertCompleted(buyerStatusResultVo);
        }
		return result;
    }

    /**
     * @Desc 회원상태 결과 조회 (정상 <-> 정지)
     * @param urUserId
     */
    @Override
    public BuyerStatusResultVo getBuyerStatusConvertInfo(String urUserId) {
    	return userBuyerStatusService.getBuyerStatusConvertInfo(urUserId);
    }

    /**
	 * @Desc 정지회원 전환 시 자동메일 발송
	 * @param buyerStatusResultVo
	 * @return void
	 */
    @Override
    public void getBuyerStopConvertCompleted(BuyerStatusResultVo buyerStatusResultVo) {

		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.BUYER_STOP_CONVERT_INFO.getCode());
    	GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

    	//이메일 발송
    	if("Y".equals(getEmailSendResultVo.getMailSendYn())) {
        	//serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
    		String content = sendTemplateBiz.getDomainManagement() + "/admin/system/emailtmplt/getBuyerStopInfoEmailTmplt?urUserId="+buyerStatusResultVo.getUrUserId();
        	String title = getEmailSendResultVo.getMailTitle();
            String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
            String senderName = sendTemplateBiz.getPsValue("SEND_EMAIL_SENDER");
            String senderMail = sendTemplateBiz.getPsValue("SEND_EMAIL_ADDRESS");

            AddEmailIssueSelectRequestDto addEmailIssueSelectRequestDto = AddEmailIssueSelectRequestDto.builder()
            		.senderName(senderName) // SEND_EMAIL_SENDER
                    .senderMail(senderMail) // SEND_EMAIL_ADDRESS
            		.reserveYn(reserveYn)
                    .content(content)
                    .title(title)
                    .urUserId(String.valueOf(buyerStatusResultVo.getUrUserId()))
                    .mail(buyerStatusResultVo.getMail())
                    .build();

            sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
    	}

    	//SMS 발송
		if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {

			String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, buyerStatusResultVo);
			String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
			String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
			AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
	                .content(content)
                    .urUserId(String.valueOf(buyerStatusResultVo.getUrUserId()))
	                .mobile(buyerStatusResultVo.getMobile())
	                .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
	                .reserveYn(reserveYn)
	                .build();

			sendTemplateBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);

		}
	}

    @Override
    public PutBuyerNormalResponseDto putBuyerNormal(PutBuyerNormalRequestDto dto) throws Exception {

    	PutBuyerNormalResponseDto result = userBuyerStatusService.putBuyerNormal(dto);

    	if(result != null) {
        	BuyerStatusResultVo buyerStatusResultVo = getBuyerStatusConvertInfo(dto.getUrUserId());
        	getBuyerNormalConvertCompleted(buyerStatusResultVo);
        }
		return result;
    }

    /**
     * @Desc 정상회원 전환 시 자동메일 발송
     * @param buyerStatusResultVo
     * @return void
     */
    @Override
    public void getBuyerNormalConvertCompleted(BuyerStatusResultVo buyerStatusResultVo) {

    	ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.BUYER_NORMAL_CONVERT_INFO.getCode());
    	GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

    	//이메일 발송
    	if("Y".equals(getEmailSendResultVo.getMailSendYn())) {
    		//serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
    		String content = sendTemplateBiz.getDomainManagement() + "/admin/system/emailtmplt/getBuyerNormalInfoEmailTmplt?urUserId="+buyerStatusResultVo.getUrUserId();
    		String title = getEmailSendResultVo.getMailTitle();
    		String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
    		String senderName = sendTemplateBiz.getPsValue("SEND_EMAIL_SENDER");
    		String senderMail = sendTemplateBiz.getPsValue("SEND_EMAIL_ADDRESS");

    		AddEmailIssueSelectRequestDto addEmailIssueSelectRequestDto = AddEmailIssueSelectRequestDto.builder()
    				.senderName(senderName) // SEND_EMAIL_SENDER
    				.senderMail(senderMail) // SEND_EMAIL_ADDRESS
    				.reserveYn(reserveYn)
    				.content(content)
    				.title(title)
    				.urUserId(String.valueOf(buyerStatusResultVo.getUrUserId()))
    				.mail(buyerStatusResultVo.getMail())
    				.build();

    		sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
    	}

    	//SMS 발송
    	if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {

    		String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, buyerStatusResultVo);
    		String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
    		String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
    		AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
    				.content(content)
    				.urUserId(String.valueOf(buyerStatusResultVo.getUrUserId()))
    				.mobile(buyerStatusResultVo.getMobile())
    				.senderTelephone(senderTelephone) // SEND_SMS_NUMBER
    				.reserveYn(reserveYn)
    				.build();

    		sendTemplateBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);

    	}
    }

	/**
	 * 마케팅 정보 수신동의 안내 대상자 조회
	 * @return MarketingInfoDto
	 * @throws Exception
	 */
    @Override
    public MarketingInfoDto getMarketingInfo(Long urUserId) throws Exception{
    	return userBuyerStatusService.getMarketingInfo(urUserId);
    }


}
