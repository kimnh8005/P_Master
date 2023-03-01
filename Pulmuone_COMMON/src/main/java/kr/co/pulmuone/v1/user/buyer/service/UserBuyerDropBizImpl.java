package kr.co.pulmuone.v1.user.buyer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.base.service.ComnBizImpl;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.SendEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.order.front.service.OrderFrontBiz;
import kr.co.pulmuone.v1.promotion.coupon.service.PromotionCouponBiz;
import kr.co.pulmuone.v1.promotion.point.service.PointBiz;
import kr.co.pulmuone.v1.send.template.dto.AddEmailIssueSelectRequestDto;
import kr.co.pulmuone.v1.send.template.dto.AddSmsIssueSelectRequestDto;
import kr.co.pulmuone.v1.send.template.dto.GetEmailSendResponseDto;
import kr.co.pulmuone.v1.send.template.dto.vo.GetEmailSendResultVo;
import kr.co.pulmuone.v1.send.template.service.SendTemplateBiz;
import kr.co.pulmuone.v1.user.buyer.dto.GetUserDropListRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.GetUserDropListResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.UserDropRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.UserDropResultVo;
import kr.co.pulmuone.v1.user.certification.service.UserCertificationBiz;
import kr.co.pulmuone.v1.user.join.dto.vo.JoinResultVo;
import kr.co.pulmuone.v1.user.join.service.UserJoinBiz;


@Service
public class UserBuyerDropBizImpl implements UserBuyerDropBiz {

    @Autowired
    private UserBuyerDropService userBuyerDropService;

    @Autowired
    private OrderFrontBiz orderFrontBiz;

    @Autowired
    private PointBiz pointBiz;

    @Autowired
    private PromotionCouponBiz promotionCouponBiz;

    @Autowired
    private UserJoinBiz userJoinBiz;

    @Autowired
  	private ComnBizImpl comnBizImpl;

    @Autowired
  	private SendTemplateBiz sendTemplateBiz;

    @Autowired
  	private UserCertificationBiz userCertificationBiz;

    @Override
    public GetUserDropListResponseDto getUserDropList(GetUserDropListRequestDto dto) throws Exception {
        return userBuyerDropService.getUserDropList(dto);
    }

    @Override
    @Transactional
    public ApiResult<?> progressUserDrop(UserDropRequestDto dto) throws Exception {
        // validation
        int orderCount = orderFrontBiz.getOrderCountFromUserDrop(dto.getUrUserId()); // 주문건수조회
        if(orderCount > 0){ // 진행중거래가 있는 경우
            return ApiResult.result(UserEnums.Drop.ORDER_EXIST);
        }

        // BACKUP
        // TODO : 백업대상 확정 필요함
        /*
        1. 임직원할인혜택 이용정보 -> 재가입시 기존 잔여 한도 적용
        2. 주문이력 분리 보관
         */
        JoinResultVo joinResultVo = userJoinBiz.getJoinCompletedInfo(String.valueOf(dto.getUrUserId()));
        String mobile = joinResultVo.getMobile(); //탈퇴 전 mobile 가져오기
        String status = joinResultVo.getStatus(); //탈퇴 전 status 가져오기
        dto.setStatus(status);

        // 탈퇴 진행
        pointBiz.expireWithdrawalMemberPoint(dto.getUrUserId()); // 적립금취소
        promotionCouponBiz.putWithdrawalMemberCoupon(dto.getUrUserId()); //쿠폰취소
        userBuyerDropService.progressUserDrop(dto); // 회원코드삭제
        userCertificationBiz.unlinkAllAccount(dto.getUrUserId()); //SNS 계정 연동 삭제

        UserDropResultVo userDropResultVo = getUserDropInfo(dto.getUrUserDropId()); //탈퇴결과 조회 VO
        getUserDropCompleted(userDropResultVo, mobile);

        return ApiResult.success();
    }

    /**
	 * @Desc 회원탈퇴 완료 시 회원탈퇴결과 조회
	 * @param urUserId
	 * @return UserDropResultVo
	 */
    public UserDropResultVo getUserDropInfo(Long urUserDropId) {
    	return userBuyerDropService.getUserDropInfo(urUserDropId);
    }


	/**
	 * @Desc 회원탈퇴 완료 시 자동메일 발송
	 * @param userDropResultVo
	 * @param mobile
	 * @return void
	 */
	public void getUserDropCompleted(UserDropResultVo userDropResultVo, String mobile) {

		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.USER_DROP_INFO.getCode());
    	GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

    	//이메일 발송
    	if("Y".equals(getEmailSendResultVo.getMailSendYn())) {
        	//serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
    		String content = sendTemplateBiz.getDomainManagement() + "/admin/system/emailtmplt/getUserDropInfoEmailTmplt?urUserDropId="+userDropResultVo.getUrUserDropId();
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
                    .urUserId(String.valueOf(userDropResultVo.getUrUserId()))
                    .mail(userDropResultVo.getMail())
                    .build();

            sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
    	}

    	//SMS 발송
		if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {

    		String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, userDropResultVo);
			String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
			String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
			AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
	                .content(content)
                    .urUserId(String.valueOf(userDropResultVo.getUrUserId()))
	                .mobile(mobile)
	                .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
	                .reserveYn(reserveYn)
	                .build();

			sendTemplateBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);

		}

	}


}
