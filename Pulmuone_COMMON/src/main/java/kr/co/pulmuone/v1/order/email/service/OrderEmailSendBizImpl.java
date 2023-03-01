package kr.co.pulmuone.v1.order.email.service;

import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.base.service.ComnBizImpl;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.ClaimEnums;
import kr.co.pulmuone.v1.comm.enums.SendEnums;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.order.email.dto.OrderInfoForEmailResultDto;
import kr.co.pulmuone.v1.order.email.dto.OrderInfoForSmsDto;
import kr.co.pulmuone.v1.order.email.dto.OrderRegularGoodsPriceChangeDto;
import kr.co.pulmuone.v1.order.email.dto.OrderRegularResultDto;
import kr.co.pulmuone.v1.order.email.dto.vo.BosOrderInfoForEmailVo;
import kr.co.pulmuone.v1.order.email.dto.vo.OrderInfoForEmailVo;
import kr.co.pulmuone.v1.order.email.dto.vo.OrderRegularReqInfoVo;
import kr.co.pulmuone.v1.order.order.dto.PgApprovalOrderDataDto;
import kr.co.pulmuone.v1.order.present.dto.OrderPresentDto;
import kr.co.pulmuone.v1.send.template.dto.AddEmailIssueSelectRequestDto;
import kr.co.pulmuone.v1.send.template.dto.AddSmsIssueSelectRequestDto;
import kr.co.pulmuone.v1.send.template.dto.GetEmailSendResponseDto;
import kr.co.pulmuone.v1.send.template.dto.vo.GetEmailSendResultVo;
import kr.co.pulmuone.v1.send.template.service.SendTemplateBiz;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class OrderEmailSendBizImpl implements OrderEmailSendBiz {

    @Autowired
	private SendTemplateBiz sendTemplateBiz;

	@Autowired
	private ComnBizImpl comnBizImpl;

	@Autowired
	private OrderEmailBiz orderEmailBiz;

	@Autowired
	private OrderEmailService orderEmailService;

	/**
	 * 주문 접수 완료
	 */
    @Override
	public void orderReceivedComplete(OrderInfoForEmailResultDto orderInfoForEmailResultDto) throws Exception{
    	OrderInfoForEmailVo orderInfoVo = orderInfoForEmailResultDto.getOrderInfoVo();
    	OrderInfoForSmsDto orderInfoForSmsDto = orderInfoForEmailResultDto.getOrderInfoForSmsDto();

    	if(orderInfoVo != null && CollectionUtils.isNotEmpty(orderInfoForEmailResultDto.getOrderDetailList())) {

			ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_RECEIVED_COMPLETED.getCode());
	    	GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

	    	//이메일 발송
	    	if("Y".equals(getEmailSendResultVo.getMailSendYn())) {
	        	//serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
	    		String content = sendTemplateBiz.getDomainManagement() + "/admin/system/emailtmplt/orderReceivedComplete?odOrderId="+orderInfoVo.getOdOrderId();
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
	                    .urUserId(String.valueOf(orderInfoVo.getUrUserId()))
	                    .mail(orderInfoVo.getMail())
	                    .build();

	            sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
	    	}

	    	//SMS 발송
			if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {
				String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, orderInfoForSmsDto);
				String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
				String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
				AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
		                .content(content)
		                .urUserId(String.valueOf(orderInfoVo.getUrUserId()))
		                .mobile(orderInfoVo.getMobile())
		                .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
		                .reserveYn(reserveYn)
		                .build();

				sendTemplateBiz.sendSmsApi(addSmsIssueSelectRequestDto);

			}
    	}
	}


	/**
	 * 주문 결제 완료
	 */
    @Override
	public void orderPaymentComplete(OrderInfoForEmailResultDto orderInfoForEmailResultDto) throws Exception{
    	OrderInfoForEmailVo orderInfoVo = orderInfoForEmailResultDto.getOrderInfoVo();
    	OrderInfoForSmsDto orderInfoForSmsDto = orderInfoForEmailResultDto.getOrderInfoForSmsDto();

    	if(orderInfoVo != null && CollectionUtils.isNotEmpty(orderInfoForEmailResultDto.getOrderDetailList())) {

			ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_PAYMENT_COMPLETED.getCode());
	    	GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

	    	//이메일 발송
	    	if("Y".equals(getEmailSendResultVo.getMailSendYn())) {
	        	//serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
	    		String content = sendTemplateBiz.getDomainManagement() + "/admin/system/emailtmplt/orderPaymentComplete?odOrderId="+orderInfoVo.getOdOrderId();
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
	                    .urUserId(String.valueOf(orderInfoVo.getUrUserId()))
	                    .mail(orderInfoVo.getMail())
	                    .build();

	            sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
	    	}

	    	//SMS 발송
			if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {
				String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, orderInfoForSmsDto);
				String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
				String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
				AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
		                .content(content)
		                .urUserId(String.valueOf(orderInfoVo.getUrUserId()))
		                .mobile(orderInfoVo.getMobile())
		                .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
		                .reserveYn(reserveYn)
		                .build();

				sendTemplateBiz.sendSmsApi(addSmsIssueSelectRequestDto);

			}
    	}
	}

    /**
	 * 선물하기 받는사람 보내기
	 */
    @Override
	public void orderPresentMassegeSend(OrderInfoForEmailResultDto orderInfoForEmailResultDto, PgApprovalOrderDataDto orderData) throws Exception{
    	OrderInfoForEmailVo orderInfoVo = orderInfoForEmailResultDto.getOrderInfoVo();
    	OrderInfoForSmsDto orderInfoForSmsDto = orderInfoForEmailResultDto.getOrderInfoForSmsDto();

    	if(orderInfoVo != null && CollectionUtils.isNotEmpty(orderInfoForEmailResultDto.getOrderDetailList())) {

			ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_PRESENT_MASSEGE_SEND.getCode());
	    	GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

	    	//SMS 발송
			if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {
				String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, orderInfoForSmsDto);
				content = comnBizImpl.getTmpltContext(content, orderData);
				String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
				String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
				AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
		                .content(content)
		                .urUserId(String.valueOf(orderInfoVo.getUrUserId()))
		                .mobile(orderData.getPresentReceiveHp())
		                .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
		                .reserveYn(reserveYn)
		                .build();

				sendTemplateBiz.sendSmsApi(addSmsIssueSelectRequestDto);

			}
    	}
	}
	
	/**
	 * 선물하기 거절 보낸사람에게 보내기
	 */
    @Override
	public void orderPresentRejectMassegeSend(OrderInfoForEmailResultDto orderInfoForEmailResultDto, OrderPresentDto orderPresentDto) throws Exception{
    	OrderInfoForEmailVo orderInfoVo = orderInfoForEmailResultDto.getOrderInfoVo();
    	OrderInfoForSmsDto orderInfoForSmsDto = orderInfoForEmailResultDto.getOrderInfoForSmsDto();

    	if(orderInfoVo != null && CollectionUtils.isNotEmpty(orderInfoForEmailResultDto.getOrderDetailList())) {

			ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_PRESENT_REJECT.getCode());
	    	GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();
	    	//SMS 발송
			if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {
				String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, orderInfoForSmsDto);
				content = comnBizImpl.getTmpltContext(content, orderPresentDto);
				String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
				String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
				AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
		                .content(content)
		                .urUserId(String.valueOf(orderInfoVo.getUrUserId()))
		                .mobile(orderPresentDto.getBuyerHp())
		                .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
		                .reserveYn(reserveYn)
		                .build();

				sendTemplateBiz.sendSmsApi(addSmsIssueSelectRequestDto);

			}
    	}
	}


	/**
	 * 주문 입금 완료
	 */
    @Override
	public void orderDepositComplete(OrderInfoForEmailResultDto orderInfoForEmailResultDto) throws Exception{
    	OrderInfoForEmailVo orderInfoVo = orderInfoForEmailResultDto.getOrderInfoVo();
    	OrderInfoForSmsDto orderInfoForSmsDto = orderInfoForEmailResultDto.getOrderInfoForSmsDto();

    	if(orderInfoVo != null && CollectionUtils.isNotEmpty(orderInfoForEmailResultDto.getOrderDetailList())) {

			ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_DEPOSIT_COMPLETED.getCode());
	    	GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

	    	//이메일 발송
	    	if("Y".equals(getEmailSendResultVo.getMailSendYn())) {
	        	//serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
	    		String content = sendTemplateBiz.getDomainManagement() + "/admin/system/emailtmplt/orderDepositComplete?odOrderId="+orderInfoVo.getOdOrderId();
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
	                    .urUserId(String.valueOf(orderInfoVo.getUrUserId()))
	                    .mail(orderInfoVo.getMail())
	                    .build();

	            sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
	    	}

	    	//SMS 발송
			if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {
				String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, orderInfoForSmsDto);
				String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
				String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
				AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
		                .content(content)
		                .urUserId(String.valueOf(orderInfoVo.getUrUserId()))
		                .mobile(orderInfoVo.getMobile())
		                .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
		                .reserveYn(reserveYn)
		                .build();

				sendTemplateBiz.sendSmsApi(addSmsIssueSelectRequestDto);

			}
    	}
	}


	/**
	 * 정기 주문 결제 완료
	 */
    @Override
	public void orderRegularPaymentComplete(OrderInfoForEmailResultDto orderInfoForEmailResultDto) {
    	OrderRegularResultDto orderRegularResultDto = orderInfoForEmailResultDto.getOrderRegularResultDto();
    	OrderInfoForSmsDto orderInfoForSmsDto = orderInfoForEmailResultDto.getOrderInfoForSmsDto();

    	if(orderRegularResultDto != null ) {

			ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_REGULAR_PAYMENT_COMPLETE.getCode());
	    	GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

	    	//이메일 발송
	    	if("Y".equals(getEmailSendResultVo.getMailSendYn())) {
	        	//serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
	    		String content = sendTemplateBiz.getDomainManagement() + "/admin/system/emailtmplt/orderRegularPaymentComplete?odOrderId="+orderRegularResultDto.getOdOrderId();
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
	                    .urUserId(String.valueOf(orderRegularResultDto.getUrUserId()))
	                    .mail(orderRegularResultDto.getBuyerMail())
	                    .build();

	            sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
	    	}

	    	//SMS 발송
			if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {
				String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, orderInfoForSmsDto);
				String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
				String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
				AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
		                .content(content)
		                .urUserId(String.valueOf(orderRegularResultDto.getUrUserId()))
		                .mobile(orderRegularResultDto.getBuyerHp())
		                .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
		                .reserveYn(reserveYn)
		                .build();

				sendTemplateBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);

			}
    	}
	}

	/**
	 * 상품 발송
	 */
    @Override
	public void orderGoodsDelivery(OrderInfoForEmailResultDto orderInfoForEmailResultDto) {
    	OrderInfoForEmailVo orderInfoVo = orderInfoForEmailResultDto.getOrderInfoVo();
    	OrderInfoForSmsDto orderInfoForSmsDto = orderInfoForEmailResultDto.getOrderInfoForSmsDto();

    	if(orderInfoVo != null && CollectionUtils.isNotEmpty(orderInfoForEmailResultDto.getOrderDetailList())) {

			ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_GOODS_DELIVERY.getCode());
	    	GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

	    	String orderDetailGoodsListStr = StringUtils.join(orderInfoVo.getOrderDetailGoodsList(), ',');

	    	//이메일 발송
	    	if("Y".equals(getEmailSendResultVo.getMailSendYn())) {
	        	//serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
	    		String content = sendTemplateBiz.getDomainManagement() + "/admin/system/emailtmplt/orderGoodsDelivery?orderDetailGoodsList="+orderDetailGoodsListStr;
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
	                    .urUserId(String.valueOf(orderInfoVo.getUrUserId()))
	                    .mail(orderInfoVo.getMail())
	                    .build();

				// 상품발송 자동메일 중복검사
				int overlapSendMailForGoodsDeliveryCount = sendTemplateBiz.isOverlapSendMailForGoodsDelivery(addEmailIssueSelectRequestDto);
				if(overlapSendMailForGoodsDeliveryCount == 0){
					sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
				}

	    	}

	    	//SMS 발송
			if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {

				String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, orderInfoForSmsDto);
				String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
				String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
				String reserveDate = ""; //예약일자

				// 현재시간 22:00~09:00인 경우 -> AM 9:00로 예약문자 발송
				LocalDateTime nowTime = LocalDateTime.now();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 09:00:00");
				// 22시~24시 사이 -> 다음날 9시 예약발송
				if(nowTime.getHour()>=22 && nowTime.getHour()<24){
					reserveYn = "Y";
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DATE,1);
					reserveDate = dateFormat.format(cal.getTime());
				}

				// 0시~9시 사이 -> 당일 9시 예약발송
				if(nowTime.getHour()>=0 && nowTime.getHour()<9){
					reserveYn = "Y";
					Date today    = new Date();
					reserveDate = dateFormat.format(today);
				}

				AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
		                .content(content)
		                .urUserId(String.valueOf(orderInfoVo.getUrUserId()))
		                .mobile(orderInfoVo.getMobile())
		                .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
		                .reserveYn(reserveYn)
						.reserveDate(reserveDate)
		                .build();

				// 상품발송 SMS 중복검사
				int overlapSendSmsForGoodsDeliveryCount = sendTemplateBiz.isOverlapSendSmsForGoodsDelivery(addSmsIssueSelectRequestDto);
				if(overlapSendSmsForGoodsDeliveryCount == 0){
					sendTemplateBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);
				}

			}
    	}
	}


	/**
	 * 주문 취소 완료
	 */
    @Override
	public void orderCancelComplete(OrderInfoForEmailResultDto orderInfoForEmailResultDto, List<Long> odOrderDetlIdList) throws Exception{
    	OrderInfoForEmailVo orderInfoVo = orderInfoForEmailResultDto.getOrderInfoVo();
    	OrderInfoForSmsDto orderInfoForSmsDto = orderInfoForEmailResultDto.getOrderInfoForSmsDto();

    	if(orderInfoVo != null && CollectionUtils.isNotEmpty(orderInfoForEmailResultDto.getOrderDetailList())) {

			ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_CANCEL_COMPLETED.getCode());
	    	GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

			String orderDetailGoodsListStr = StringUtils.join(odOrderDetlIdList, ',');

	    	//이메일 발송
	    	if("Y".equals(getEmailSendResultVo.getMailSendYn())) {
	        	//serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
	    		String content = sendTemplateBiz.getDomainManagement() + "/admin/system/emailtmplt/orderCancelComplete?odClaimId="+orderInfoVo.getOdClaimId() + "&odOrderDetlIdList="+orderDetailGoodsListStr;
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
	                    .urUserId(String.valueOf(orderInfoVo.getUrUserId()))
	                    .mail(orderInfoVo.getMail())
	                    .build();

	            sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
	    	}

	    	//SMS 발송
			if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {
				String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, orderInfoForSmsDto);
				String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
				String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
				AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
		                .content(content)
		                .urUserId(String.valueOf(orderInfoVo.getUrUserId()))
		                .mobile(orderInfoVo.getMobile())
		                .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
		                .reserveYn(reserveYn)
		                .build();

				sendTemplateBiz.sendSmsApi(addSmsIssueSelectRequestDto);

			}
    	}
	}

	/**
	 * 주문 취소(입금 전 취소)
	 */
    @Override
	public void orderCancelBeforeDeposit(OrderInfoForEmailResultDto orderInfoForEmailResultDto) throws Exception{
    	OrderInfoForEmailVo orderInfoVo = orderInfoForEmailResultDto.getOrderInfoVo();
    	OrderInfoForSmsDto orderInfoForSmsDto = orderInfoForEmailResultDto.getOrderInfoForSmsDto();

    	if(orderInfoVo != null && CollectionUtils.isNotEmpty(orderInfoForEmailResultDto.getOrderDetailList())) {

			ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_CANCEL_BEFORE_DEPOSIT.getCode());
	    	GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

	    	//SMS 발송
			if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {
				String reserveDate = ""; // 예약발송일자
				String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)

				// BATCH 주문취소인 경우 -> 발송당일 AM 09:00 예약발송
				if(Integer.parseInt(ClaimEnums.ClaimFrontTp.BATCH.getCode()) == orderInfoForEmailResultDto.getFrontTp()){
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 09:00:00");
					Date today    = new Date();
					reserveDate = dateFormat.format(today);
					reserveYn = "Y";
				}

				String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, orderInfoForSmsDto);
				String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
				AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
		                .content(content)
		                .urUserId(String.valueOf(orderInfoVo.getUrUserId()))
		                .mobile(orderInfoVo.getMobile())
		                .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
		                .reserveYn(reserveYn)
						.reserveDate(reserveDate)
		                .build();

				sendTemplateBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);

			}
    	}
	}


	/**
	 * 주문 반품 완료
	 */
    @Override
	public void orderReturnCompleted(OrderInfoForEmailResultDto orderInfoForEmailResultDto, List<Long> odOrderDetlIdList) {
    	OrderInfoForEmailVo orderInfoVo = orderInfoForEmailResultDto.getOrderInfoVo();
    	OrderInfoForSmsDto orderInfoForSmsDto = orderInfoForEmailResultDto.getOrderInfoForSmsDto();

    	if(orderInfoVo != null && CollectionUtils.isNotEmpty(orderInfoForEmailResultDto.getOrderDetailList())) {

			ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_RETURN_COMPLETED.getCode());
	    	GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

			String orderDetailGoodsListStr = StringUtils.join(odOrderDetlIdList, ',');

	    	//이메일 발송
	    	if("Y".equals(getEmailSendResultVo.getMailSendYn())) {
	        	//serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
	    		String content = sendTemplateBiz.getDomainManagement() + "/admin/system/emailtmplt/orderReturnComplete?odClaimId="+orderInfoVo.getOdClaimId()  + "&odOrderDetlIdList="+orderDetailGoodsListStr;
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
	                    .urUserId(String.valueOf(orderInfoVo.getUrUserId()))
	                    .mail(orderInfoVo.getMail())
	                    .build();

	            sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
	    	}

	    	//SMS 발송
			if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {
				String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, orderInfoForSmsDto);
				String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
				String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
				AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
		                .content(content)
		                .urUserId(String.valueOf(orderInfoVo.getUrUserId()))
		                .mobile(orderInfoVo.getMobile())
		                .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
		                .reserveYn(reserveYn)
		                .build();

				sendTemplateBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);

			}
    	}
	}


	/**
	 * 정기배송 신청 완료
	 */
    @Override
	public void orderRegularApplyCompleted(OrderInfoForEmailResultDto orderInfoForEmailResultDto , String firstOrderYn) {
    	OrderRegularReqInfoVo orderRegularReqInfoVo = orderInfoForEmailResultDto.getOrderRegularReqInfoVo();
    	OrderInfoForSmsDto orderInfoForSmsDto = orderInfoForEmailResultDto.getOrderInfoForSmsDto();

    	if(orderRegularReqInfoVo != null && CollectionUtils.isNotEmpty(orderInfoForEmailResultDto.getOrderDetailList())) {

			ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_REGULAR_APPLY_COMPLETED.getCode());
	    	GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

	    	//이메일 발송
	    	if("Y".equals(getEmailSendResultVo.getMailSendYn())) {
	        	//serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
	    		String content = sendTemplateBiz.getDomainManagement() + "/admin/system/emailtmplt/orderRegularApplyCompleted?odRegularReqId="+orderRegularReqInfoVo.getOdRegularReqId()+"&firstOrderYn="+firstOrderYn;
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
	                    .urUserId(String.valueOf(orderRegularReqInfoVo.getUrUserId()))
	                    .mail(orderRegularReqInfoVo.getMail())
	                    .build();

	            sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
	    	}

	    	//SMS 발송
			if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {
				String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, orderInfoForSmsDto);
				String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
				String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
				AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
		                .content(content)
		                .urUserId(String.valueOf(orderRegularReqInfoVo.getUrUserId()))
		                .mobile(orderRegularReqInfoVo.getMobile())
		                .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
		                .reserveYn(reserveYn)
		                .build();

				sendTemplateBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);

			}
    	}
	}

	/**
	 * 정기배송 결제 실패(1차)
	 * 1차는 SMS만 발송
	 */
    @Override
	public void orderRegularPaymentFailFirst(OrderInfoForEmailResultDto orderInfoForEmailResultDto) {
    	OrderRegularResultDto orderRegularResultDto = orderInfoForEmailResultDto.getOrderRegularResultDto();
    	OrderInfoForSmsDto orderInfoForSmsDto = orderInfoForEmailResultDto.getOrderInfoForSmsDto();

    	if(orderRegularResultDto != null && orderInfoForSmsDto != null) {

			ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_REGULAR_PAYMENT_FAIL_FIRST.getCode());
	    	GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

	    	//SMS 발송
			if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {
				String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, orderInfoForSmsDto);
				String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
				String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
				AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
		                .content(content)
		                .urUserId(String.valueOf(orderRegularResultDto.getUrUserId()))
		                .mobile(orderRegularResultDto.getBuyerHp())
		                .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
		                .reserveYn(reserveYn)
		                .build();

				sendTemplateBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);

			}
    	}
	}

	/**
	 * 정기배송 결제 실패(2차)
	 * 2차는 EMAIL, SMS 모두 발송
	 */
    @Override
	public void orderRegularPaymentFailSecond(OrderInfoForEmailResultDto orderInfoForEmailResultDto) {
    	OrderRegularResultDto orderRegularResultDto = orderInfoForEmailResultDto.getOrderRegularResultDto();
    	OrderInfoForSmsDto orderInfoForSmsDto = orderInfoForEmailResultDto.getOrderInfoForSmsDto();

    	if(orderRegularResultDto != null && orderInfoForSmsDto != null) {

			ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_REGULAR_PAYMENT_FAIL_SECOND.getCode());
	    	GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();


	    	//이메일 발송
	    	if("Y".equals(getEmailSendResultVo.getMailSendYn())) {
	        	//serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
	    		String content = sendTemplateBiz.getDomainManagement() + "/admin/system/emailtmplt/orderRegualrPaymentFailSecond?odRegularResultId="+orderRegularResultDto.getOdRegularResultId();
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
	                    .urUserId(String.valueOf(orderRegularResultDto.getUrUserId()))
	                    .mail(orderRegularResultDto.getBuyerMail())
	                    .build();

	            sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
	    	}

	    	//SMS 발송
			if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {
				String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, orderInfoForSmsDto);
				String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
				String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
				AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
		                .content(content)
		                .urUserId(String.valueOf(orderRegularResultDto.getUrUserId()))
		                .mobile(orderRegularResultDto.getBuyerHp())
		                .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
		                .reserveYn(reserveYn)
		                .build();

				sendTemplateBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);

			}
    	}
	}

	/**
	 * 정기배송 결제 실패(4차) -> 정기배송 해지
	 * 4차는 SMS만 발송
	 */
	@Override
	public void orderRegularPaymentFailFourth(OrderInfoForEmailResultDto orderInfoForEmailResultDto) {
		OrderRegularResultDto orderRegularResultDto = orderInfoForEmailResultDto.getOrderRegularResultDto();
		OrderInfoForSmsDto orderInfoForSmsDto = orderInfoForEmailResultDto.getOrderInfoForSmsDto();

		if(orderRegularResultDto != null && orderInfoForSmsDto != null) {

			ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_REGULAR_PAYMENT_FAIL_FOURTH.getCode());
			GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

			//SMS 발송
			if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {
				String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, orderInfoForSmsDto);
				String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
				String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
				AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
						.content(content)
						.urUserId(String.valueOf(orderRegularResultDto.getUrUserId()))
						.mobile(orderRegularResultDto.getBuyerHp())
						.senderTelephone(senderTelephone) // SEND_SMS_NUMBER
						.reserveYn(reserveYn)
						.build();

				sendTemplateBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);

			}
		}
	}

	/**
	 * 정기배송 취소 완료
	 */
    @Override
	public void orderRegularCancelCompleted(OrderInfoForEmailResultDto orderInfoForEmailResultDto) {
    	OrderRegularResultDto orderRegularResultDto = orderInfoForEmailResultDto.getOrderRegularResultDto();
    	OrderInfoForSmsDto orderInfoForSmsDto = orderInfoForEmailResultDto.getOrderInfoForSmsDto();

    	if(orderRegularResultDto != null) {

			ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_REGULAR_CANCEL_COMPLETED.getCode());
	    	GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();


	    	//이메일 발송
	    	if("Y".equals(getEmailSendResultVo.getMailSendYn())) {
	        	//serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
	    		String content = sendTemplateBiz.getDomainManagement() + "/admin/system/emailtmplt/orderRegularCancelCompleted?odRegularResultDetlId="+orderRegularResultDto.getOdRegularResultDetlId();
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
	                    .urUserId(String.valueOf(orderRegularResultDto.getUrUserId()))
	                    .mail(orderRegularResultDto.getBuyerMail())
	                    .build();

	            sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
	    	}

	    	//SMS 발송
			if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {
				String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, orderInfoForSmsDto);
				String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
				String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
				AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
		                .content(content)
		                .urUserId(String.valueOf(orderRegularResultDto.getUrUserId()))
		                .mobile(orderRegularResultDto.getBuyerHp())
		                .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
		                .reserveYn(reserveYn)
		                .build();

				sendTemplateBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);

			}
    	}
	}

	/**
	 * 정기배송 상품 건너뛰기 완료
	 */
    @Override
    public void orderRegularGoodsSkipCompleted(OrderInfoForEmailResultDto orderInfoForEmailResultDto) throws Exception{
    	OrderRegularResultDto orderRegularResultDto = orderInfoForEmailResultDto.getOrderRegularResultDto();
    	OrderInfoForSmsDto orderInfoForSmsDto = orderInfoForEmailResultDto.getOrderInfoForSmsDto();

    	if(orderRegularResultDto != null) {

			ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_REGULAR_GOODS_SKIP_COMPLETED.getCode());
	    	GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();


	    	//이메일 발송
	    	if("Y".equals(getEmailSendResultVo.getMailSendYn())) {
	        	//serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
	    		String content = sendTemplateBiz.getDomainManagement() + "/admin/system/emailtmplt/orderRegularGoodsSkipCompleted?odRegularResultDetlId="+orderRegularResultDto.getOdRegularResultDetlId();
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
	                    .urUserId(String.valueOf(orderRegularResultDto.getUrUserId()))
	                    .mail(orderRegularResultDto.getBuyerMail())
	                    .build();

	            sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
	    	}

	    	//SMS 발송
			if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {
				String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, orderInfoForSmsDto);
				String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
				String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
				AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
		                .content(content)
		                .urUserId(String.valueOf(orderRegularResultDto.getUrUserId()))
		                .mobile(orderRegularResultDto.getBuyerHp())
		                .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
		                .reserveYn(reserveYn)
		                .build();

				sendTemplateBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);

			}
    	}
    }


	/**
	 * 정기배송 회차 건너뛰기 완료
	 */
    @Override
    public void orderRegularReqRoundSkipCompleted(OrderInfoForEmailResultDto orderInfoForEmailResultDto) throws Exception{
    	OrderRegularResultDto orderRegularResultDto = orderInfoForEmailResultDto.getOrderRegularResultDto();
    	OrderInfoForSmsDto orderInfoForSmsDto = orderInfoForEmailResultDto.getOrderInfoForSmsDto();

    	if(orderRegularResultDto != null) {

			ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_REGULAR_REQ_ROUND_SKIP_COMPLETED.getCode());
	    	GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();


	    	//이메일 발송
	    	if("Y".equals(getEmailSendResultVo.getMailSendYn())) {
	        	//serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
	    		String content = sendTemplateBiz.getDomainManagement() + "/admin/system/emailtmplt/orderRegularReqRoundSkipCompleted?odRegularResultId="+orderRegularResultDto.getOdRegularResultId();
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
	                    .urUserId(String.valueOf(orderRegularResultDto.getUrUserId()))
	                    .mail(orderRegularResultDto.getBuyerMail())
	                    .build();

	            sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
	    	}

	    	//SMS 발송
			if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {
				String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, orderInfoForSmsDto);
				String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
				String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
				AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
		                .content(content)
		                .urUserId(String.valueOf(orderRegularResultDto.getUrUserId()))
		                .mobile(orderRegularResultDto.getBuyerHp())
		                .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
		                .reserveYn(reserveYn)
		                .build();

				sendTemplateBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);

			}
    	}
    }

	/**
	 * 정기배송 주문 생성 완료
	 */
    @Override
	public void orderRegularCreationCompleted(OrderInfoForEmailResultDto orderInfoForEmailResultDto) {
		OrderRegularResultDto orderInfoVo = orderInfoForEmailResultDto.getOrderRegularResultDto();
    	OrderInfoForSmsDto orderInfoForSmsDto = orderInfoForEmailResultDto.getOrderInfoForSmsDto();

    	if(orderInfoForSmsDto != null) {

			ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_REGULAR_CREATION_COMPLETED.getCode());
	    	GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

	    	//SMS 발송
			if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {
				String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, orderInfoForSmsDto);
				String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
				String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
				AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
		                .content(content)
		                .urUserId(String.valueOf(orderInfoVo.getUrUserId()))
		                .mobile(orderInfoVo.getBuyerHp())
		                .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
		                .reserveYn(reserveYn)
		                .build();

				sendTemplateBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);

			}
    	}
	}

	/**
	 * 정기배송 만료 예정
	 */
    @Override
	public void orderRegularExpireExpected(OrderInfoForEmailResultDto orderInfoForEmailResultDto) {
		OrderRegularResultDto orderInfoVo = orderInfoForEmailResultDto.getOrderRegularResultDto();
    	OrderInfoForSmsDto orderInfoForSmsDto = orderInfoForEmailResultDto.getOrderInfoForSmsDto();

    	if(orderInfoForSmsDto != null) {

			ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_REGULAR_EXPIRE_EXPECTED.getCode());
	    	GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

	    	//SMS 발송
			if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {
				String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, orderInfoForSmsDto);
				String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
				String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
				AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
		                .content(content)
		                .urUserId(String.valueOf(orderInfoVo.getUrUserId()))
		                .mobile(orderInfoVo.getBuyerHp())
		                .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
		                .reserveYn(reserveYn)
		                .build();

				sendTemplateBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);

			}
    	}
	}


	/**
	 * 정기배송 만료
	 */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public void orderRegularExpired(OrderInfoForEmailResultDto orderInfoForEmailResultDto) {
    	OrderRegularResultDto orderRegularResultDto = orderInfoForEmailResultDto.getOrderRegularResultDto();
    	OrderInfoForSmsDto orderInfoForSmsDto = orderInfoForEmailResultDto.getOrderInfoForSmsDto();

    	if(orderInfoForSmsDto != null) {

			ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_REGULAR_EXPIRED.getCode());
	    	GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

	    	//SMS 발송
			if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {
				String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, orderInfoForSmsDto);
				String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
				String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
				AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
		                .content(content)
		                .urUserId(String.valueOf(orderRegularResultDto.getUrUserId()))
		                .mobile(orderRegularResultDto.getBuyerHp())
		                .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
		                .reserveYn(reserveYn)
		                .build();

				sendTemplateBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);

			}
    	}
	}


	/**
	 * 녹즙 일일배송 종료
	 */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public void orderDailyGreenJuiceEnd(OrderInfoForEmailResultDto orderInfoForEmailResultDto) {
    	OrderInfoForEmailVo orderInfoForEmailVo = orderInfoForEmailResultDto.getOrderInfoVo();
    	OrderInfoForSmsDto orderInfoForSmsDto = orderInfoForEmailResultDto.getOrderInfoForSmsDto();

    	if(orderInfoForSmsDto != null) {

			ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_DAILY_GREENJUICE_END.getCode());
	    	GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

	    	//SMS 발송
			if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {
				String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, orderInfoForSmsDto);
				String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
				String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
				AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
		                .content(content)
		                .urUserId(String.valueOf(orderInfoForEmailVo.getUrUserId()))
		                .mobile(orderInfoForEmailVo.getMobile())
		                .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
		                .reserveYn(reserveYn)
		                .build();

				sendTemplateBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);

			}
    	}
	}

	/**
	 * 정기배송 상품금액 변동 안내
	 */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public void orderRegularGoodsPriceChange(OrderInfoForEmailResultDto orderInfoForEmailResultDto) {
    	OrderRegularReqInfoVo orderRegularReqInfoVo = orderInfoForEmailResultDto.getOrderRegularReqInfoVo();
    	OrderRegularGoodsPriceChangeDto goodsPriceChangeDto = orderInfoForEmailResultDto.getOrderRegularGoodsPriceChangeDto();
    	OrderInfoForSmsDto orderInfoForSmsDto = orderInfoForEmailResultDto.getOrderInfoForSmsDto();

    	if(orderRegularReqInfoVo != null && goodsPriceChangeDto != null) {

			ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_REGULAR_GOODS_PRICE_CHANGE.getCode());
	    	GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

	    	//이메일 발송
	    	if("Y".equals(getEmailSendResultVo.getMailSendYn())) {
	        	//serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
	    		String content = sendTemplateBiz.getDomainManagement()
	    				+ "/admin/system/emailtmplt/orderRegularGoodsPriceChange?odRegularReqId="+orderRegularReqInfoVo.getOdRegularReqId()+"&ilGoodsId="+goodsPriceChangeDto.getIlGoodsId();
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
	                    .urUserId(String.valueOf(orderRegularReqInfoVo.getUrUserId()))
	                    .mail(orderRegularReqInfoVo.getMail())
	                    .build();

	            sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
	    	}

	    	//SMS 발송
			if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {
				String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, orderInfoForSmsDto);
				String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
				String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
				AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
		                .content(content)
		                .urUserId(String.valueOf(orderRegularReqInfoVo.getUrUserId()))
		                .mobile(orderRegularReqInfoVo.getMobile())
		                .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
		                .reserveYn(reserveYn)
		                .build();

				sendTemplateBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);
			}
			// 정기배송 상품 금액변동 알림 발송 이력 등록
			orderEmailService.putOdRegularInfoSendHist(goodsPriceChangeDto);
    	}
	}

	/**
	 * BOS 주문 상태 알림
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public void bosOrderStatusNotification(OrderInfoForEmailResultDto orderInfoForEmailResultDto) {
		BosOrderInfoForEmailVo bosOrderInfoForEmailVo = orderInfoForEmailResultDto.getBosOrderInfoForEmailVo();

		if(ObjectUtils.isNotEmpty(bosOrderInfoForEmailVo)) {

			ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.BOS_ORDER_STATUS_NOTIFICATION.getCode());
			GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

			//이메일 발송
			if("Y".equals(getEmailSendResultVo.getMailSendYn())) {
				//serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
				String content = sendTemplateBiz.getDomainManagement() + "/admin/system/emailtmplt/bosOrderStatusNotification?urClientId=" + bosOrderInfoForEmailVo.getUrClientId();
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
						.urUserId("0")
						.mail(bosOrderInfoForEmailVo.getMail())
						.build();

				sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
			}
		}
	}

	/**
	 * 직접배송 미등록 송장 알림
	 * @throws Exception
	 */
	@Override
	public void directShippingUnregisteredInvoiceNotification() throws Exception {
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.DIRECT_SHIPPING_UNREGISTERED_INVOICE_NOTIFICATION.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

		//이메일 발송
		if(!"Y".equals(getEmailSendResultVo.getMailSendYn())) {
			return;
		}

		//풀필먼트팀 대상 메일 전송
		String mailTitle =getEmailSendResultVo.getMailTitle()+ "("+ DateUtil.getDate("yyyy/MM/dd") +")";
		String mailContent = sendTemplateBiz.getDomainManagement() + "/admin/system/emailtmplt/getDirectShippingUnregisteredInvoice";
		String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
		sendTemplateBiz.sendMailToTarget(SendEnums.SendTargetType.FULFILLMENT, mailTitle, mailContent, reserveYn);
	}

	/**
	 * BOS 수집몰 연동 실패 알림
	 */

	@Override
	public void bosCollectionMallInterfaceFailNotification() {
		// BOS 수집몰 연동 실패 알림 발송 대상자 조회
		List<GetCodeListResultVo> codeListResultVoList = sendTemplateBiz.getSendTargetList(SendEnums.SendTargetType.FULFILLMENT);

		if(CollectionUtils.isNotEmpty(codeListResultVoList)){
			for(GetCodeListResultVo target : codeListResultVoList){
				// BOS 수집몰 연동 실패 알림 자동메일 발송
				this.bosCollectionMallInterfaceFailNotification(target);
			}
		}

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = {Exception.class })
	protected void bosCollectionMallInterfaceFailNotification(GetCodeListResultVo targetVo) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date time = new Date();
		String sendDate = dateFormat.format(time);

		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.BOS_COLLECTIONMALL_INTERFACE_FAIL_NOTIFICATION.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto) result.getData()).getRows();

		BosOrderInfoForEmailVo bosOrderInfoForEmailVo = new BosOrderInfoForEmailVo();
		bosOrderInfoForEmailVo.setSendDate(sendDate);
		bosOrderInfoForEmailVo.setMail(targetVo.getAttr1());
		bosOrderInfoForEmailVo.setMobile(targetVo.getAttr2());

		//이메일 발송
		if ("Y".equals(getEmailSendResultVo.getMailSendYn())) {
			String content = sendTemplateBiz.getDomainManagement() + "/admin/system/emailtmplt/bosCollectionMallInterfaceFailNotification";
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
					.urUserId("0")
					.mail(bosOrderInfoForEmailVo.getMail())
					.build();

			sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
		}

		//SMS 발송
		if ("Y".equals(getEmailSendResultVo.getSmsSendYn())) {
			String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, bosOrderInfoForEmailVo);
			String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
			String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
			AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
					.content(content)
					.urUserId("0")
					.mobile(bosOrderInfoForEmailVo.getMobile())
					.senderTelephone(senderTelephone) // SEND_SMS_NUMBER
					.reserveYn(reserveYn)
					.build();

			sendTemplateBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);
		}

	}

	/**
	 * BOS 올가 식품안전팀 주의주문 발생 알림
	 */
	@Override
	public void bosOrgaCautionOrderNotification(OrderInfoForEmailResultDto orderInfoForEmailResultDto) throws Exception {

		// 자동메일 발송 대상자 조회
		List<GetCodeListResultVo> codeListResultVoList = sendTemplateBiz.getSendTargetList(SendEnums.SendTargetType.CAUTION_ORDER_MAIL_LIST);

		if(CollectionUtils.isNotEmpty(codeListResultVoList)){
			for(GetCodeListResultVo target : codeListResultVoList){
				// 자동메일 발송
				this.sendBosOrgaCautionOrderNotification(orderInfoForEmailResultDto, target);
			}
		}
	}

	/**
	 * BOS 올가 식품안전팀 주의주문 발생 알림 발송
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = {Exception.class })
	protected void sendBosOrgaCautionOrderNotification(OrderInfoForEmailResultDto orderInfoForEmailResultDto, GetCodeListResultVo targetVo) throws Exception {
		OrderInfoForEmailVo orderInfoVo = orderInfoForEmailResultDto.getOrderInfoVo();
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.BOS_ORGA_CAUTION_ORDER_NOTIFICATION.getCode());
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto) result.getData()).getRows();

		//이메일 발송
		if ("Y".equals(getEmailSendResultVo.getMailSendYn())) {
			String content = sendTemplateBiz.getDomainManagement() + "/admin/system/emailtmplt/bosOrgaCautionOrderNotification?odOrderId=" + orderInfoVo.getOdOrderId();
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
					.urUserId("0")
					.mail(targetVo.getAttr1())
					.build();

			sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
		}
	}

	/**
	 * 부분취소 배송비 추가결제 가상계좌 발급
	 */
	@Override
	public void payAdditionalShippingPrice(OrderInfoForEmailResultDto orderInfoForEmailResultDto) throws Exception{
		OrderInfoForEmailVo orderInfoVo = orderInfoForEmailResultDto.getOrderInfoVo();
		OrderInfoForSmsDto orderInfoForSmsDto = orderInfoForEmailResultDto.getOrderInfoForSmsDto();

		if(orderInfoVo != null) {
			ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.PAY_ADDITIONAL_SHIPPING_PRICE.getCode());
			GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();

			//SMS 발송
			if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {
				String reserveDate = ""; // 예약발송일자
				String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)

				String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, orderInfoForSmsDto);
				String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
				AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
						.content(content)
						.urUserId(String.valueOf(orderInfoVo.getUrUserId()))
						.mobile(orderInfoVo.getMobile())
						.senderTelephone(senderTelephone) // SEND_SMS_NUMBER
						.reserveYn(reserveYn)
						.reserveDate(reserveDate)
						.build();

				sendTemplateBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);
			}
		}
	}

	/**
	 * 매장픽업 상품 준비
	 */
    @Override
	public void orderShopPickupGoodsDelivery(OrderInfoForEmailResultDto orderInfoForEmailResultDto) {
    	OrderInfoForEmailVo orderInfoVo = orderInfoForEmailResultDto.getOrderInfoVo();
    	OrderInfoForSmsDto orderInfoForSmsDto = orderInfoForEmailResultDto.getOrderInfoForSmsDto();

    	if(orderInfoVo != null && CollectionUtils.isNotEmpty(orderInfoForEmailResultDto.getOrderDetailList())) {

    		//ORDER_SHOP_PICKUP_GOODS_DELIVERY
			ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_SHOP_PICKUP_GOODS_DELIVERY.getCode());
	    	GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();


	    	String orderDetailGoodsListStr = StringUtils.join(orderInfoVo.getOrderDetailGoodsList(), ',');

	    	//이메일 발송
	    	if("Y".equals(getEmailSendResultVo.getMailSendYn())) {
	        	//serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
	    		String content = sendTemplateBiz.getDomainManagement() + "/admin/system/emailtmplt/orderShopPickupGoodsDelivery?orderDetailGoodsList="+orderDetailGoodsListStr;
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
	                    .urUserId(String.valueOf(orderInfoVo.getUrUserId()))
	                    .mail(orderInfoVo.getMail())
	                    .build();

	            sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
	    	}

	    	//SMS 발송
			if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {
				String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, orderInfoForSmsDto);
				String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
				String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
				AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
		                .content(content)
		                .urUserId(String.valueOf(orderInfoVo.getUrUserId()))
		                .mobile(orderInfoVo.getMobile())
		                .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
		                .reserveYn(reserveYn)
		                .build();

				sendTemplateBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);

			}
    	}
	}

}
