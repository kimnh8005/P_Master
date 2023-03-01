package kr.co.pulmuone.v1.pg.service.kcp.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.kcp.J_PP_CLI_N;

import kr.co.kcp.net.connection.ConnectionKCP;
import kr.co.kcp.net.connection.dto.ParamData;
import kr.co.kcp.net.connection.util.HttpJsonXml;
import kr.co.kcp.net.connection.util.OpenHash;
import kr.co.pulmuone.v1.comm.constants.AppConstants;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.DeviceType;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.CashReceipt;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.PaymentType;
import kr.co.pulmuone.v1.comm.enums.PgEnums;
import kr.co.pulmuone.v1.comm.enums.PgEnums.KcpCode;
import kr.co.pulmuone.v1.comm.enums.PgEnums.PgServiceType;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import kr.co.pulmuone.v1.comm.util.SystemUtil;
import kr.co.pulmuone.v1.pg.dto.BasicDataRequestDto;
import kr.co.pulmuone.v1.pg.dto.BasicDataResponseDto;
import kr.co.pulmuone.v1.pg.dto.CancelRequestDto;
import kr.co.pulmuone.v1.pg.dto.CancelResponseDto;
import kr.co.pulmuone.v1.pg.dto.EscrowRegistDeliveryDataRequestDto;
import kr.co.pulmuone.v1.pg.dto.EtcDataCartDto;
import kr.co.pulmuone.v1.pg.dto.PaymentFormDto;
import kr.co.pulmuone.v1.pg.dto.ReceiptCancelResponseDto;
import kr.co.pulmuone.v1.pg.dto.ReceiptIssueRequestDto;
import kr.co.pulmuone.v1.pg.dto.ReceiptIssueResponseDto;
import kr.co.pulmuone.v1.pg.dto.VirtualAccountDataResponseDto;
import kr.co.pulmuone.v1.pg.service.PgAbstractService;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpApplyRegularBatchKeyDataRequestDto;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpApplyRegularBatchKeyDataResponseDto;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpApplyRegularBatchKeyRequestDto;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpApplyRegularBatchKeyResponseDto;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpApprovalRequestDto;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpApprovalResponseDto;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpPaymentRegularBatchKeyRequestDto;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpPaymentRegularBatchKeyResponseDto;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpRemitRequestDto;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpRemitResponseDto;
import kr.co.pulmuone.v1.shopping.cart.dto.EtcDataClaimDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KcpPgService extends PgAbstractService<KcpApprovalRequestDto, KcpApprovalResponseDto> {

	@Autowired
	KcpConfig baiscConfig;

	@Override
	public PgServiceType getServiceType() throws Exception {
		return PgServiceType.KCP;
	}

	@Override
	public BasicDataResponseDto getBasicData(BasicDataRequestDto reqDto) throws Exception {
		BasicDataResponseDto resDto = new BasicDataResponseDto();

		KcpConfig config = null;
		if (PaymentType.isSimplePay(reqDto.getPaymentType().getCode())) {
			config = baiscConfig.getKcpConfig(PgEnums.PgAccountType.KCP_SIMPLE.getCode());
		} else {
			config = baiscConfig.getKcpConfig(PgEnums.PgAccountType.KCP_BASIC.getCode());
		}

		List<PaymentFormDto> formDtoList = new ArrayList<PaymentFormDto>();

		String exeScriptType = "";

		DeviceType deviceType = DeviceUtil.getGoodsEnumDeviceTypeByUserDevice();

		// PC 일떄
		if (deviceType.getCode().equals(DeviceType.PC.getCode())) {

			exeScriptType = PgEnums.ExeScriptType.PC_KCP_BASIC.getCode();

			// 화폐단위 원화 : WON / 달러 : USD
			formDtoList.add(PaymentFormDto.builder().name("currency").value("WON").build());
			// 결제수단코드 신용카드, 계좌이체, 가상계좌를 하나의 결제 창에 같이 나타나게 하는 경우의 pay_method 는 `111000000000`
			if (PaymentType.CARD.equals(reqDto.getPaymentType())
					|| PaymentType.KAKAOPAY.equals(reqDto.getPaymentType())
					|| PaymentType.PAYCO.equals(reqDto.getPaymentType())
					|| PaymentType.NAVERPAY.equals(reqDto.getPaymentType())
					|| PaymentType.SSPAY.equals(reqDto.getPaymentType())) {
				formDtoList.add(PaymentFormDto.builder().name("pay_method").value("100000000000").build());
			} else if (PaymentType.BANK.equals(reqDto.getPaymentType())) {
				formDtoList.add(PaymentFormDto.builder().name("pay_method").value("010000000000").build());
			} else if (PaymentType.VIRTUAL_BANK.equals(reqDto.getPaymentType())) {
				formDtoList.add(PaymentFormDto.builder().name("pay_method").value("001000000000").build());

				SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

				Date FirstDate = format.parse(DateUtil.getCurrentDate());
				Date SecondDate = format.parse(reqDto.getVirtualAccountDateTime().substring(0, 8));

				// Date로 변환된 두 날짜를 계산한 뒤 그 리턴값으로 long type 변수를 초기화 하고 있다.
				// 연산결과 -950400000. long type 으로 return 된다.
				long calDate = FirstDate.getTime() - SecondDate.getTime();

				// Date.getTime() 은 해당날짜를 기준으로1970년 00:00:00 부터 몇 초가 흘렀는지를 반환해준다.
				// 이제 24*60*60*1000(각 시간값에 따른 차이점) 을 나눠주면 일수가 나온다.
				long calDateDays = calDate / (24 * 60 * 60 * 1000);

				calDateDays = Math.abs(calDateDays);

				formDtoList.add(
						PaymentFormDto.builder().name("vcnt_expire_term").value(String.valueOf(calDateDays)).build());
			}

			formDtoList.add(PaymentFormDto.builder().name("module_type").value(config.getModule_type()).build());
			formDtoList.add(PaymentFormDto.builder().name("res_cd").value("").build());
			formDtoList.add(PaymentFormDto.builder().name("res_msg").value("").build());
			formDtoList.add(PaymentFormDto.builder().name("enc_info").value("").build());
			formDtoList.add(PaymentFormDto.builder().name("enc_data").value("").build());
			formDtoList.add(PaymentFormDto.builder().name("ret_pay_method").value("").build());
			formDtoList.add(PaymentFormDto.builder().name("tran_cd").value("").build());
			formDtoList.add(PaymentFormDto.builder().name("use_pay_method").value("").build());

			// 주문정보 검증 관련 정보 : 표준웹 에서 설정하는 정보입니다
			formDtoList.add(PaymentFormDto.builder().name("ordr_chk").value("").build());

			// - Start 기타 옵션
			// 결제 창 왼쪽 상단에 가맹점 사이트의 로고를 띄움. 업체의 로고가 있는 URL을 정확히 입력해야 하며 해당 변수 생략 시에는 로고가
			// 뜨지않고 site_name 값이 표시됨 로고 파일은 GIF, JPG 파일만 지원 최대 사이즈 : 150 X 50 미만 이미지 파일을 150
			// X 50 이상으로 설정 시 site_name 값이 표시됨
//				formDtoList.add(PaymentFormDto.builder().name("site_logo").value("").build());
			// 결제 창 한글/영문 변환 신용카드, 계좌이체, 가상계좌, 휴대폰소액결제에 적용
//				formDtoList.add(PaymentFormDto.builder().name("eng_flag").value("").build());
			// 결제 창 스킨 변경. 1~11까지 설정 가능
//				formDtoList.add(PaymentFormDto.builder().name("skin_indx").value("").build());
			// 상품코드 : 주문상품명으로 구분이 어려운 경우 상품군을 따로 구분하여 처리할 수 있는 옵션기능
//				formDtoList.add(PaymentFormDto.builder().name("good_cd").value("").build());
			// 결제창의 상단문구를 변경할 수 있는 파라미터 입니다.
//				formDtoList.add(PaymentFormDto.builder().name("kcp_pay_title").value("").build());
			// - ENd 기타 옵션
		} else {

			exeScriptType = PgEnums.ExeScriptType.MOBILE_KCP_BASIC.getCode();

			// 인코딩 설정
			formDtoList.add(PaymentFormDto.builder().name("encoding_trans").value("UTF-8").build());// 인코딩 네임은 대문자
			// 결제수단코드 (영문 대문자)
			if (PaymentType.CARD.equals(reqDto.getPaymentType())
					|| PaymentType.KAKAOPAY.equals(reqDto.getPaymentType())
					|| PaymentType.PAYCO.equals(reqDto.getPaymentType())
					|| PaymentType.NAVERPAY.equals(reqDto.getPaymentType())
					|| PaymentType.SSPAY.equals(reqDto.getPaymentType())) {
				formDtoList.add(PaymentFormDto.builder().name("pay_method").value("CARD").build());
				formDtoList.add(PaymentFormDto.builder().name("ActionResult").value("card").build());
			} else if (PaymentType.BANK.equals(reqDto.getPaymentType())) {
				formDtoList.add(PaymentFormDto.builder().name("pay_method").value("BANK").build());
				formDtoList.add(PaymentFormDto.builder().name("ActionResult").value("acnt").build());
			} else if (PaymentType.VIRTUAL_BANK.equals(reqDto.getPaymentType())) {
				formDtoList.add(PaymentFormDto.builder().name("pay_method").value("VCNT").build());
				formDtoList.add(PaymentFormDto.builder().name("ActionResult").value("vcnt").build());
				formDtoList.add(
						PaymentFormDto.builder().name("ipgm_date").value(reqDto.getVirtualAccountDateTime()).build());
			}
			formDtoList.add(PaymentFormDto.builder().name("van_code").value("").build());
			// 상점이름(영문으로작성권장)
			formDtoList.add(PaymentFormDto.builder().name("shop_name").value(config.getG_conf_site_name()).build());
			// 거래 화폐 단위 원화 : 410
			formDtoList.add(PaymentFormDto.builder().name("currency").value("410").build());

			String stringJoson = getApprovalKey(config, reqDto);

			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonode = null;
			try {
				jsonode = mapper.readTree(stringJoson);
			} catch (Exception e) {
				e.printStackTrace();
			}
			formDtoList.add(PaymentFormDto.builder().name("approvalKeyResCD")
					.value(jsonode.findValue("Code").getTextValue()).build());
			formDtoList.add(PaymentFormDto.builder().name("approvalKeyresMsg")
					.value(jsonode.findValue("Message").getTextValue()).build());
			formDtoList.add(PaymentFormDto.builder().name("approval_key")
					.value(jsonode.findValue("approvalKey").getTextValue()).build());
			formDtoList.add(
					PaymentFormDto.builder().name("PayUrl").value(jsonode.findValue("PayUrl").getTextValue()).build());

			if (deviceType.getCode().equals("APP")) {
				formDtoList.add(PaymentFormDto.builder().name("AppUrl").value(DeviceUtil.isIos() ? AppConstants.APP_SCHEME : "").build());
			}
			// 스마트폰 결제 창에서 인증완료 후 인증 데이터를 리턴 받을 업체 페이지
			formDtoList.add(PaymentFormDto.builder().name("Ret_URL").value(getRetUrl(reqDto.isAddPay())).build());
		}

		// 간편결제 파라미터 세팅
		if (PaymentType.KAKAOPAY.equals(reqDto.getPaymentType())) {
			formDtoList.add(PaymentFormDto.builder().name("kakaopay_direct").value("Y").build());
		} else if (PaymentType.PAYCO.equals(reqDto.getPaymentType())) {
			formDtoList.add(PaymentFormDto.builder().name("payco_direct").value("Y").build());
		} else if (PaymentType.NAVERPAY.equals(reqDto.getPaymentType())) {
			formDtoList.add(PaymentFormDto.builder().name("naverpay_direct").value("Y").build());
			// 네이버페이 포인트 사용여부
//			formDtoList.add(PaymentFormDto.builder().name("naverpay_point_direct").value("Y").build());
		} else if (PaymentType.SSPAY.equals(reqDto.getPaymentType())) {
			formDtoList.add(PaymentFormDto.builder().name("sspay_direct").value("Y").build());
		}

		if (PaymentType.BANK.equals(reqDto.getPaymentType())
				|| PaymentType.VIRTUAL_BANK.equals(reqDto.getPaymentType())) {
			// 에스크로 서비스를 이용할 경우, 반드시 Y로 설정
			formDtoList.add(PaymentFormDto.builder().name("escw_used").value("Y").build());
			// 에스크로 결제 처리 : ‘Y’
			formDtoList.add(PaymentFormDto.builder().name("pay_mod").value("Y").build());
			// 예상되는 배송 소요 일을 입력
			formDtoList.add(PaymentFormDto.builder().name("deli_term").value("2").build());
			// 장바구니에 담겨있는 상품의 개수
			formDtoList.add(PaymentFormDto.builder().name("bask_cntx").value("1").build());

			formDtoList.add(PaymentFormDto.builder().name("good_info")
					.value("seq=1" + fromCharCode(31) + "ordr_numb=" + reqDto.getOdid() + fromCharCode(31)
							+ "good_name=" + reqDto.getGoodsName() + fromCharCode(31) + "good_cntx=1" + fromCharCode(31)
							+ "good_amtx=" + reqDto.getPaymentPrice())
					.build());
		} else {
			formDtoList.add(PaymentFormDto.builder().name("escw_used").value("N").build());
		}

		// - Start 가맹점 필수 정보 설정
		// 요청종류 : 승인(pay)/취소,매입(mod) 요청시 사용
		formDtoList.add(PaymentFormDto.builder().name("req_tx").value("pay").build());
		// 상점이름(영문으로 작성권장)
		formDtoList.add(PaymentFormDto.builder().name("site_name").value(config.getG_conf_site_name()).build());
		// 상점코드
		formDtoList.add(PaymentFormDto.builder().name("site_cd").value(config.getG_conf_site_cd()).build());
		// - End 가맹점 필수 정보 설정

		// - Start 주문 정보 입력
		// 상점 관리 주문번호
		formDtoList.add(PaymentFormDto.builder().name("ordr_idxx").value(reqDto.getOdid()).build());
		// 상품명
		formDtoList.add(PaymentFormDto.builder().name("good_name").value(reqDto.getGoodsName()).build());
		// 주문요청금액
		formDtoList
				.add(PaymentFormDto.builder().name("good_mny").value(String.valueOf(reqDto.getPaymentPrice())).build());
		// 주문자 이름
		formDtoList.add(PaymentFormDto.builder().name("buyr_name").value(reqDto.getBuyerName()).build());
		// 주문자 이메일
		formDtoList.add(PaymentFormDto.builder().name("buyr_mail").value(reqDto.getBuyerEmail()).build());
		// 주문자 전화번호
		formDtoList.add(PaymentFormDto.builder().name("buyr_tel1").value("").build());
		// 주문자 휴대폰번호
		formDtoList.add(PaymentFormDto.builder().name("buyr_tel2").value(reqDto.getBuyerMobile()).build());
		// 쇼핑몰회원ID
		formDtoList.add(PaymentFormDto.builder().name("shop_user_id").value(reqDto.getLoginId()).build());
		// - End 주문 정보 입력

		// - Start 신용카드 옵션
		// 50,000원 이상 거래에 대한 할부 옵션. 기본값은 12개월. 0~12의 값을 설정하면 결제 창에 할부 개월 수가 최대값까지 표기됨
		formDtoList.add(PaymentFormDto.builder().name("quotaopt").value("12").build());
		// 무이자할부 표시기능“” : 상점관리자 설정에 따름“Y” : kcp_noint_quota 값에 따라 무이자표시(단, 상점관리자에 설정이
		// 되어야 함)“N” : 상점관리자 값을 무시하고 일반할부로 처리됨
		formDtoList.add(PaymentFormDto.builder().name("kcp_noint").value("").build());
		// 무이자할부 표시기능이 Y 일 경우 무이자 설정 값을 결제창에 표기 무이자 설정은 카드사 별로 설정 가능
		formDtoList.add(PaymentFormDto.builder().name("kcp_noint_quota").value("").build());
		// 결제 요청 시 원하는 신용카드사 확인 해당 변수 값을 Y로 설정 후 used_card 변수 값에 원하는 신용카드사의 코드를 입력하면 입력한
		// 신용카드사만 결제창에 노출
		formDtoList.add(PaymentFormDto.builder().name("used_card_YN").value("Y").build());
		// used_card_YN 변수 값을 Y로 설정한 후 사용하길 원하는 신용카드사의 코드 입력
		formDtoList.add(PaymentFormDto.builder().name("used_card").value(reqDto.getPgBankCode()).build());
		// 결제금액이 50,000원 이상일 경우 결제 창에서 선택 할 수 있는 할부 개월 수를 0~12 의 값 중 하나로 고정
		formDtoList.add(PaymentFormDto.builder().name("fix_inst").value(reqDto.getQuota()).build());
		// 해외카드 구분하는 파라미터 입니다.(해외비자, 해외마스터, 해외JCB로 구분하여 표시)
		formDtoList.add(PaymentFormDto.builder().name("used_card_CCXX").value("Y").build());
		// 신용카드 결제시 OK캐쉬백 적립 여부를 묻는 창을 설정하는 파라미터 입니다. 포인트 가맹점의 경우에만 창이 보여집니다.
		formDtoList.add(PaymentFormDto.builder().name("save_ocb").value("N").build());
		// - End 신용카드 옵션

		// - Start 현금영수증 옵션
//		formDtoList.add(PaymentFormDto.builder().name("cash_yn").value("Y").build());
//		String cash_tr_code = "0"; // 소득공제용(개인) : 0 / 지출증빙용(기업) : 1
//		String cash_id_info = reqDto.getCashReceiptNumber();
//		if (reqDto.isCashReceipt()) {
//			cash_tr_code = reqDto.getCashReceiptEnum().getKcpCode();
//		} else {
//			cash_id_info = "0100001234";
//		}
//		formDtoList.add(PaymentFormDto.builder().name("cash_tr_code").value(cash_tr_code).build());
//		formDtoList.add(PaymentFormDto.builder().name("cash_id_info").value(cash_id_info).build());
//		// 제공 기간 설정 0:일회성 1:기간설정(ex 1:2012010120120131)
//		formDtoList.add(PaymentFormDto.builder().name("good_expr").value("0").build());
//		// 계좌이체, 가상계좌를 이용한 현금 결제 시 결제 금액이 1원 이상인 경우, 결제 창에 현금영수증 등록여부를 보여줌 현금영수증 자동등록을
//		// 원할 경우 해당 변수를 Y 로 설정. 가상계좌의 경우 입금 완료 후 현금영수증 등록됨 Y : 소득공제용, 지출증빙용 노출 N : 현금영수증
//		// 숨기기 R : 소득공제용만 노출 E : 지출증빙용만 노출
//		formDtoList.add(PaymentFormDto.builder().name("disp_tax_yn").value("Y").build());
		// - End 현금영수증 옵션

		// - Start 복함과세
		// ※ tax_flag 변수 추가 안내 사항
		// ① NHN KCP 운영팀(1544 - 8660)으로 복합과세 신청이 된, 복합과세 전용 사이트코드로 계약한 가맹점에만 해당 (스마트폰의
		// 경우 신서버 smpay 모듈에만 적용)
		// ② 상품별이 아니라 금액으로 구분하여 요청
		// ③ 복합과세 구분 파라미터를 보내지 않으면 기본적으로 과세 금액으로 처리되니 복합과세로 처리를 원하시면 반드시 tax_flag,
		// tax_mny, free_mny, vat_mny 값을 전송
		// ④ 복합과세 이용 시 OCB 포인트 사용 및 적립, 베네피아 복지포인트 사용을 신용카드와 함께 진행할 경우 복합과세로 처리되지 않으니 유의
		// 복합 과세 구문 TG01 : 과세 TG02 : 비과세 TG03 : 복합과세
		formDtoList.add(PaymentFormDto.builder().name("tax_flag").value("TG03").build());
		// 과세 승인금액 (공급가액) 과세 금액에 해당하는 공급가액 설정 과세 금액 = good_mny / 1.1
		int comm_tax_mny = Math.round(Float.valueOf(reqDto.getTaxPaymentPrice()) / new Float(1.1));
		formDtoList.add(PaymentFormDto.builder().name("comm_tax_mny").value(String.valueOf(comm_tax_mny)).build());
		// 비과세 승인금액 비과세 금액에 해당하는 공급가액 설정 비과세 금액 = good_mny – 과세금액 – 부가가치세
		formDtoList.add(PaymentFormDto.builder().name("comm_free_mny")
				.value(String.valueOf(reqDto.getTaxFreePaymentPrice())).build());
		// 부가가치세 부가가치세는 과세금액 공금가액의 10 % 부가가치세 = good_mny – 과세금액
		formDtoList.add(PaymentFormDto.builder().name("comm_vat_mny")
				.value(String.valueOf(reqDto.getTaxPaymentPrice() - comm_tax_mny)).build());
		// - End 복함과세

		// NHN KCP 기본 파라미터 외 업체 추가 파라미터
		formDtoList.add(PaymentFormDto.builder().name("param_opt_1").value(reqDto.getEtcData()).build());
		// NHN KCP 기본 파라미터 외 업체 추가 파라미터
		formDtoList.add(PaymentFormDto.builder().name("param_opt_2").value("").build());
		// NHN KCP 기본 파라미터 외 업체 추가 파라미터
		formDtoList.add(PaymentFormDto.builder().name("param_opt_3").value("").build());

		resDto.setPgFormDataList(formDtoList);
		resDto.setExeScriptType(exeScriptType);
		resDto.setOdid(reqDto.getOdid());
		resDto.setPaymentPrice(reqDto.getPaymentPrice());
		return resDto;
	}

	private String getApprovalKey(KcpConfig config, BasicDataRequestDto reqDto) {
		// KCP 인증 정보를 저장하기 위한 Object ( 통신의 기본이 되는 객체 ) - 필수
		ConnectionKCP suc = new ConnectionKCP();
		// KCP 와 통신시 데이터 위변조를 확인 하기 위한 Hash Object ( 업체와 KCP 간의 통신시 데이터 위변조를 확인 하기 위해필요.
		// 미설정시 통신 구간만 라이브러리에서 자체적으로 hash 처리 )
		OpenHash oh = new OpenHash();
		// 응답값 get value 형식으로 가져올수 있는 Object ( Java 또는 JSP 내에서 데이터를 파싱할때 필요 - XML 또는
		// JSON )
		HttpJsonXml hjx = new HttpJsonXml();
		// 파라메타 값을 세팅할수 있는 bean Object ( String, HashMap 등으로 대체 가능 )
		ParamData pd = new ParamData();

		pd.setGood_mny(String.valueOf(reqDto.getPaymentPrice()));
		pd.setGood_name(reqDto.getGoodsName());
		pd.setOrdr_idxx(reqDto.getOdid());

		if (PaymentType.CARD.equals(reqDto.getPaymentType())
				|| PaymentType.KAKAOPAY.equals(reqDto.getPaymentType())
				|| PaymentType.PAYCO.equals(reqDto.getPaymentType())
				|| PaymentType.NAVERPAY.equals(reqDto.getPaymentType())
				|| PaymentType.SSPAY.equals(reqDto.getPaymentType())) {
			pd.setPay_method("CARD");
			pd.setEscw_used("N");
		} else if (PaymentType.BANK.equals(reqDto.getPaymentType())) {
			pd.setPay_method("BANK");
			pd.setEscw_used("Y");
		} else if (PaymentType.VIRTUAL_BANK.equals(reqDto.getPaymentType())) {
			pd.setPay_method("VCNT");
			pd.setEscw_used("Y");
		}
		pd.setRet_URL(getRetUrl(reqDto.isAddPay()));
		pd.setSite_cd(config.getG_conf_site_cd());

		pd.setResponse_type("JSON");

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();

		return suc.kcpPaymentSmartPhone(request, config.isG_conf_server(), pd, config.getG_conf_log_dir());
	}

	/**
	 * 정기 결제 결제 키 발급
	 *
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	public KcpApplyRegularBatchKeyDataResponseDto applyRegularBatchKeyData(
			KcpApplyRegularBatchKeyDataRequestDto reqDto) throws Exception {

		KcpConfig config = baiscConfig.getKcpConfig(PgEnums.PgAccountType.KCP_REGULAR.getCode());

		List<PaymentFormDto> formDtoList = new ArrayList<PaymentFormDto>();

		KcpApplyRegularBatchKeyDataResponseDto resDto = new KcpApplyRegularBatchKeyDataResponseDto();
		DeviceType deviceType = DeviceUtil.getGoodsEnumDeviceTypeByUserDevice();

		String exeScriptType = "";

		// PC 일떄
		if (deviceType.getCode().equals(DeviceType.PC.getCode())) {

			exeScriptType = PgEnums.ExeScriptType.PC_KCP_BATCH_KEY.getCode();

			// 그룹 아이디
			formDtoList.add(PaymentFormDto.builder().name("kcpgroup_id").value(config.getKcpgroup_id()).build());
			// 인증키 요청 서비스 종류
			formDtoList.add(PaymentFormDto.builder().name("pay_method").value("AUTH:CARD").build());
			// 인증 방식 : 공인인증(BCERT)
			formDtoList.add(PaymentFormDto.builder().name("card_cert_type").value("BATCH").build());
			// PLUGIN 설정 정보 (변경불가)
			formDtoList.add(PaymentFormDto.builder().name("module_type").value(config.getModule_type()).build());
			// KCP 결제창 내에서 주민번호 입력 받는 변수
			formDtoList.add(PaymentFormDto.builder().name("batch_soc").value("Y").build());
			// KCP 결제창에 노출되는 제공기간 설정 변수
			formDtoList.add(PaymentFormDto.builder().name("good_expr").value("").build());

			// 필수 항목 : PLUGIN에서 값을 설정하는 부분으로 반드시 포함되어야함.
			formDtoList.add(PaymentFormDto.builder().name("res_cd").value("").build());
			formDtoList.add(PaymentFormDto.builder().name("res_msg").value("").build());
			formDtoList.add(PaymentFormDto.builder().name("trace_no").value("").build());
			formDtoList.add(PaymentFormDto.builder().name("enc_info").value("").build());
			formDtoList.add(PaymentFormDto.builder().name("enc_data").value("").build());
			formDtoList.add(PaymentFormDto.builder().name("tran_cd").value("").build());
		} else {

			exeScriptType = PgEnums.ExeScriptType.MOBILE_KCP_BATCH_KEY.getCode();

			// 인코딩 설정
			formDtoList.add(PaymentFormDto.builder().name("encoding_trans").value("UTF-8").build());// 인코딩 네임은 대문자
			// 그룹 아이디
			formDtoList.add(PaymentFormDto.builder().name("kcp_group_id").value(config.getKcpgroup_id()).build());
			// 상점이름(영문으로작성권장)
			formDtoList.add(PaymentFormDto.builder().name("shop_name").value(config.getG_conf_site_name()).build());
			// 거래 화폐 단위 원화 : 410
			formDtoList.add(PaymentFormDto.builder().name("currency").value("410").build());
			// 결제창 내에서 상품명 표기를 할 것인지에 대한 옵션입니다.
			formDtoList.add(PaymentFormDto.builder().name("kcp_bath_info_view").value("N").build());

			String stringJoson = getApprovalKey(config, reqDto);

			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonode = null;
			try {
				jsonode = mapper.readTree(stringJoson);
			} catch (Exception e) {
				e.printStackTrace();
			}
			formDtoList.add(PaymentFormDto.builder().name("approvalKeyResCD")
					.value(jsonode.findValue("Code").getTextValue()).build());
			formDtoList.add(PaymentFormDto.builder().name("approvalKeyresMsg")
					.value(jsonode.findValue("Message").getTextValue()).build());
			formDtoList.add(PaymentFormDto.builder().name("approval_key")
					.value(jsonode.findValue("approvalKey").getTextValue()).build());
			formDtoList.add(
					PaymentFormDto.builder().name("PayUrl").value(jsonode.findValue("PayUrl").getTextValue()).build());

			// 결제인증 방법 설정 변수
			formDtoList.add(PaymentFormDto.builder().name("pay_method").value("AUTH").build());
			// 인증수단 설정 변수
			formDtoList.add(PaymentFormDto.builder().name("ActionResult").value("batch").build());
			// 스마트폰 결제 창에서 인증완료 후 인증 데이터를 리턴 받을 업체 페이지
			formDtoList.add(PaymentFormDto.builder().name("Ret_URL").value(getApplyRegularBatchKeyRetUrl()).build());

			formDtoList.add(PaymentFormDto.builder().name("escw_used").value("N").build());
		}

		// 상점코드
		formDtoList.add(PaymentFormDto.builder().name("site_cd").value(config.getG_conf_site_cd()).build());

		// 주문 번호
		formDtoList.add(PaymentFormDto.builder().name("ordr_idxx").value(reqDto.getOdid()).build());
		// 주문자명
		formDtoList.add(PaymentFormDto.builder().name("buyr_name").value(reqDto.getBuyerName()).build());
		// 주문요청금액
		formDtoList
				.add(PaymentFormDto.builder().name("good_mny").value(String.valueOf(reqDto.getPaymentPrice())).build());
		// 상품명
		formDtoList.add(PaymentFormDto.builder().name("good_name").value(reqDto.getGoodsName()).build());
		// 프로세스 요청 종류 구분 변수
		formDtoList.add(PaymentFormDto.builder().name("req_tx").value("pay").build());

		// NHN KCP 기본 파라미터 외 업체 추가 파라미터
		formDtoList.add(PaymentFormDto.builder().name("param_opt_1").value(reqDto.getOrderInputUrl()).build());
		// NHN KCP 기본 파라미터 외 업체 추가 파라미터
		formDtoList.add(PaymentFormDto.builder().name("param_opt_2").value(reqDto.getUrUserId()).build());
		// NHN KCP 기본 파라미터 외 업체 추가 파라미터
		formDtoList.add(PaymentFormDto.builder().name("param_opt_3").value("").build());

		// 배치키 발급시 카드번호 리턴 여부 설정 Y : 1234-4567-****-8910 형식, L : 8910 형식(카드번호 끝 4자리)
		formDtoList.add(PaymentFormDto.builder().name("batch_cardno_return_yn").value("Y").build());
		// batch_cardno_return_yn 설정시 결제창에서 리턴
		formDtoList.add(PaymentFormDto.builder().name("card_mask_no").value("").build());

		resDto.setPgFormDataList(formDtoList);
		resDto.setExeScriptType(exeScriptType);

		return resDto;
	}

	private String getApprovalKey(KcpConfig config, KcpApplyRegularBatchKeyDataRequestDto reqDto) {
		// KCP 인증 정보를 저장하기 위한 Object ( 통신의 기본이 되는 객체 ) - 필수
		ConnectionKCP suc = new ConnectionKCP();
		// KCP 와 통신시 데이터 위변조를 확인 하기 위한 Hash Object ( 업체와 KCP 간의 통신시 데이터 위변조를 확인 하기 위해필요.
		// 미설정시 통신 구간만 라이브러리에서 자체적으로 hash 처리 )
		OpenHash oh = new OpenHash();
		// 응답값 get value 형식으로 가져올수 있는 Object ( Java 또는 JSP 내에서 데이터를 파싱할때 필요 - XML 또는
		// JSON )
		HttpJsonXml hjx = new HttpJsonXml();
		// 파라메타 값을 세팅할수 있는 bean Object ( String, HashMap 등으로 대체 가능 )
		ParamData pd = new ParamData();

		pd.setGood_mny(String.valueOf(reqDto.getPaymentPrice()));
		pd.setGood_name(reqDto.getGoodsName());
		pd.setOrdr_idxx(reqDto.getOdid());

		pd.setPay_method("AUTH");
		pd.setRet_URL(getApplyRegularBatchKeyRetUrl());
		pd.setSite_cd(config.getG_conf_site_cd());
		pd.setEscw_used("N");
		pd.setResponse_type("JSON");

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();

		return suc.kcpPaymentSmartPhone(request, config.isG_conf_server(), pd, config.getG_conf_log_dir());
	}

	private String getRetUrl(boolean isAddPay) {
		return baiscConfig.getAppDomain() + (isAddPay ? "/pg/kcp/addPayApproval" : "/pg/kcp/approval");
	}

	private String getApplyRegularBatchKeyRetUrl() {
		return baiscConfig.getAppDomain() + "/pg/kcp/applyRegularBatchKey";
	}

	@Override
	public KcpApprovalResponseDto approval(KcpApprovalRequestDto reqDto) throws Exception {
		KcpApprovalResponseDto resDto = new KcpApprovalResponseDto();
		String paymentType = null;
		if(reqDto.isAddApproval()) {
			EtcDataClaimDto etcDataClaimDto = toDtoEtcData(reqDto.getParam_opt_1(), EtcDataClaimDto.class);
			paymentType = etcDataClaimDto.getPaymentType();
		} else {
			EtcDataCartDto etcDataCartDto = toDtoEtcData(reqDto.getParam_opt_1(), EtcDataCartDto.class);
			paymentType = etcDataCartDto.getPaymentType();
		}

		KcpConfig config = null;
		if (PaymentType.isSimplePay(paymentType)) {
			config = baiscConfig.getKcpConfig(PgEnums.PgAccountType.KCP_SIMPLE.getCode());
		} else {
			config = baiscConfig.getKcpConfig(PgEnums.PgAccountType.KCP_BASIC.getCode());
		}

		J_PP_CLI_N c_PayPlus = setCPayPlusSet(config);

		// PG 승인시 req_tx = pay 로 넘어옴 그외에는 X
		String reqTx = "pay";

		if (reqTx.equals(reqDto.getReq_tx())) {
			c_PayPlus.mf_set_enc_data(f_get_parm(reqDto.getEnc_data()), f_get_parm(reqDto.getEnc_info()));

			/* 1 원은 실제로 업체에서 결제하셔야 될 원 금액을 넣어주셔야 합니다. 결제금액 유효성 검증 */

			int ordr_data_set_no;

			ordr_data_set_no = c_PayPlus.mf_add_set("ordr_data");

			c_PayPlus.mf_set_us(ordr_data_set_no, "ordr_mony", String.valueOf(reqDto.getPaymentPrice()));

		}

		if (reqDto.getTran_cd().length() > 0) {
			c_PayPlus.mf_do_tx(config.getG_conf_site_cd(), config.getG_conf_site_key(), reqDto.getTran_cd(), "",
					reqDto.getOrdr_idxx(), config.getG_conf_log_level(), "1");
		} else {
			resDto.setRes_cd("9562");
			resDto.setRes_msg("연동 오류|tran_cd값이 설정되지 않았습니다.");
			return resDto;
		}

		// 결과 코드
		resDto.setRes_cd(c_PayPlus.m_res_cd);
		// 결과 메시지
		resDto.setRes_msg(c_PayPlus.m_res_msg);

		if (reqTx.equals(reqDto.getReq_tx())) {
			if (KcpCode.SUCCESS.getCode().equals(resDto.getRes_cd())) {
				// 쇼핑몰 주문번호
				resDto.setOrdr_idxx(reqDto.getOrdr_idxx());
				// KCP 거래 고유 번호
				resDto.setTno(c_PayPlus.mf_get_res("tno"));
				// KCP 실제 거래 금액
				resDto.setAmount(c_PayPlus.mf_get_res("amount"));
				// 결제 포인트사 코드
				resDto.setPnt_issue(c_PayPlus.mf_get_res("pnt_issue"));
				// 쿠폰금액
				resDto.setCoupon_mny(c_PayPlus.mf_get_res("coupon_mny"));
				// 에스크로 결제 여부
				resDto.setEscw_yn(c_PayPlus.mf_get_res("escw_yn"));

				/*
				 * = --------------------------------------------------------------------------
				 * =
				 */
				/* = 06-1. 신용카드 승인 결과 처리 = */
				/*
				 * = --------------------------------------------------------------------------
				 * =
				 */
				if ("100000000000".equals(reqDto.getUse_pay_method())) {
					// 카드사 코드
					resDto.setCard_cd(c_PayPlus.mf_get_res("card_cd"));
					// 카드사 명
					resDto.setCard_name(c_PayPlus.mf_get_res("card_name"));
					// 카드 번호
					resDto.setCard_no(c_PayPlus.mf_get_res("card_no"));
					// 승인시간
					resDto.setApp_time(c_PayPlus.mf_get_res("app_time"));
					// 승인번호
					resDto.setApp_no(c_PayPlus.mf_get_res("app_no"));
					// 무이자 여부
					resDto.setNoinf(c_PayPlus.mf_get_res("noinf"));
					// 할부 개월 수
					resDto.setQuota(c_PayPlus.mf_get_res("quota"));
					// 부분취소 가능유무
					resDto.setPartcanc_yn(c_PayPlus.mf_get_res("partcanc_yn"));
					// 카드구분1
					resDto.setCard_bin_type_01(c_PayPlus.mf_get_res("card_bin_type_01"));
					// 카드구분2
					resDto.setCard_bin_type_02(c_PayPlus.mf_get_res("card_bin_type_02"));
					// 카드결제금액
					resDto.setCard_mny(c_PayPlus.mf_get_res("card_mny"));

					/* = -------------------------------------------------------------- = */
					/* = 06-1.1. 복합결제(포인트+신용카드) 승인 결과 처리 = */
					/* = -------------------------------------------------------------- = */
					if ("SCSK".equals(resDto.getPnt_issue()) || "SCWB".equals(resDto.getPnt_issue())) {
						// 적립금액 or 사용금액
						resDto.setPnt_amount(c_PayPlus.mf_get_res("pnt_amount"));
						// 승인시간
						resDto.setPnt_app_time(c_PayPlus.mf_get_res("pnt_app_time"));
						// 승인번호
						resDto.setPnt_app_no(c_PayPlus.mf_get_res("pnt_app_no"));
						// 발생 포인트
						resDto.setAdd_pnt(c_PayPlus.mf_get_res("add_pnt"));
						// 사용가능 포인트
						resDto.setUse_pnt(c_PayPlus.mf_get_res("use_pnt"));
						resDto.setRsv_pnt(c_PayPlus.mf_get_res("rsv_pnt")); // 총 누적 포인트
						// 복합결제시 총 거래금액
						resDto.setTotal_amount(String.valueOf(
								Integer.valueOf(resDto.getAmount()) + Integer.valueOf(resDto.getPnt_amount())));
					}
				}

				/*
				 * = --------------------------------------------------------------------------
				 * =
				 */
				/* = 06-2. 계좌이체 승인 결과 처리 = */
				/*
				 * = --------------------------------------------------------------------------
				 * =
				 */
				if ("010000000000".equals(reqDto.getUse_pay_method())) {
					// 승인시간
					resDto.setApp_time(c_PayPlus.mf_get_res("app_time"));
					// 은행명
					resDto.setBank_name(c_PayPlus.mf_get_res("bank_name"));
					// 은행코드
					resDto.setBank_code(c_PayPlus.mf_get_res("bank_code"));
					// 계좌이체결제금액
					resDto.setBk_mny(c_PayPlus.mf_get_res("bk_mny"));
				}

				/*
				 * = --------------------------------------------------------------------------
				 * =
				 */
				/* = 06-3. 가상계좌 승인 결과 처리 = */
				/*
				 * = --------------------------------------------------------------------------
				 * =
				 */
				if ("001000000000".equals(reqDto.getUse_pay_method())) {
					// 입금할 은행 이름
					resDto.setBankname(c_PayPlus.mf_get_res("bankname"));
					// 입금할 계좌 예금주
					resDto.setDepositor(c_PayPlus.mf_get_res("depositor"));
					// 입금할 계좌 번호
					resDto.setAccount(c_PayPlus.mf_get_res("account"));
					// 가상계좌 입금마감시간
					resDto.setVa_date(c_PayPlus.mf_get_res("va_date"));
				}

				/*
				 * = --------------------------------------------------------------------------
				 * =
				 */
				/* = 06-7. 현금영수증 승인 결과 처리 = */
				/*
				 * = --------------------------------------------------------------------------
				 * =
				 */

				// 현금영수증 선택 시 식별코드 소득공제용(개인) : 0 / 지출증빙용(기업) : 1
				resDto.setCash_tr_code(reqDto.getCash_tr_code());
				// 현금영수증 승인번호
				resDto.setCash_authno(c_PayPlus.mf_get_res("cash_authno"));
				// 현금영수증 거래번호
				resDto.setCash_no(c_PayPlus.mf_get_res("cash_no"));
				// 승인단계에서 callback 받을 기타 정보
				resDto.setEtcData(reqDto.getParam_opt_1());
			}
		}

		return resDto;
	}

	public KcpApplyRegularBatchKeyResponseDto applyRegularBatchKey(KcpApplyRegularBatchKeyRequestDto reqDto)
			throws Exception {

		KcpConfig config = baiscConfig.getKcpConfig(PgEnums.PgAccountType.KCP_REGULAR.getCode());

		KcpApplyRegularBatchKeyResponseDto resDto = new KcpApplyRegularBatchKeyResponseDto();

		J_PP_CLI_N c_PayPlus = setCPayPlusSet(config);

		// PG 승인시 req_tx = pay 로 넘어옴 그외에는 X
		String reqTx = "pay";

		if (reqTx.equals(reqDto.getReq_tx())) {
			c_PayPlus.mf_set_enc_data(f_get_parm(reqDto.getEnc_data()), f_get_parm(reqDto.getEnc_info()));

			/* 1 원은 실제로 업체에서 결제하셔야 될 원 금액을 넣어주셔야 합니다. 결제금액 유효성 검증 */

			int ordr_data_set_no;

			ordr_data_set_no = c_PayPlus.mf_add_set("ordr_data");

			c_PayPlus.mf_set_us(ordr_data_set_no, "ordr_mony", String.valueOf(reqDto.getGood_mny()));

		}

		if (reqDto.getTran_cd().length() > 0) {
			c_PayPlus.mf_do_tx(config.getG_conf_site_cd(), config.getG_conf_site_key(), reqDto.getTran_cd(),
					reqDto.getIp(), reqDto.getOrdr_idxx(), config.getG_conf_log_level(), "1");
		} else {
			resDto.setRes_cd("9562");
			resDto.setRes_msg("연동 오류|tran_cd값이 설정되지 않았습니다.");
			return resDto;
		}

		// 결과 코드
		resDto.setRes_cd(c_PayPlus.m_res_cd);
		// 결과 메시지
		resDto.setRes_msg(c_PayPlus.m_res_msg);

		if (reqTx.equals(reqDto.getReq_tx())) {
			if (KcpCode.SUCCESS.getCode().equals(resDto.getRes_cd())) {
				// 카드 코드
				resDto.setCard_cd(c_PayPlus.mf_get_res("card_cd"));
				// 카드명
				resDto.setCard_name(c_PayPlus.mf_get_res("card_name"));
				// 배치 인증키
				resDto.setBatch_key(c_PayPlus.mf_get_res("batch_key"));
				// 추가 옵션 1
				resDto.setParam_opt_1(reqDto.getParam_opt_1());
				// 추가 옵션 2
				resDto.setParam_opt_2(reqDto.getParam_opt_2());
				// 추가 옵션 3
				resDto.setParam_opt_3(reqDto.getParam_opt_3());
				// 카드 마스킹 정보
				resDto.setCard_mask_no(reqDto.getCard_mask_no());
			}
		}

		return resDto;
	}

	@Override
	public VirtualAccountDataResponseDto getVirtualAccountData(BasicDataRequestDto reqDto) throws Exception {

		KcpConfig config = baiscConfig.getKcpConfig(PgEnums.PgAccountType.KCP_BASIC.getCode());

		VirtualAccountDataResponseDto resDto = new VirtualAccountDataResponseDto();
		int payx_data_set;
		int common_data_set;
		String tx_cd = "00100000";

		J_PP_CLI_N c_PayPlus = setCPayPlusSet(config);

		payx_data_set = c_PayPlus.mf_add_set("payx_data");
		common_data_set = c_PayPlus.mf_add_set("common");

		c_PayPlus.mf_set_us(common_data_set, "amount", String.valueOf(reqDto.getPaymentPrice()));
		c_PayPlus.mf_set_us(common_data_set, "currency", "WON");
		c_PayPlus.mf_set_us(common_data_set, "cust_ip", reqDto.getIp());
		c_PayPlus.mf_set_us(common_data_set, "escw_mod", "N");

		// 복합 과세 구문 TG01 : 과세 TG02 : 비과세 TG03 : 복합과세
		c_PayPlus.mf_set_us(common_data_set, "tax_flag", "TG03");
		// 과세 승인금액 (공급가액) 과세 금액에 해당하는 공급가액 설정 과세 금액 = good_mny / 1.1
		int comm_tax_mny = Math.round(Float.valueOf(reqDto.getTaxPaymentPrice()) / new Float(1.1));
		c_PayPlus.mf_set_us(common_data_set, "comm_tax_mny", String.valueOf(comm_tax_mny));
		// 비과세 승인금액 비과세 금액에 해당하는 공급가액 설정 비과세 금액 = good_mny – 과세금액 – 부가가치세
		c_PayPlus.mf_set_us(common_data_set, "comm_free_mny", String.valueOf(reqDto.getTaxFreePaymentPrice()));
		// 부가가치세 부가가치세는 과세금액 공금가액의 10 % 부가가치세 = good_mny – 과세금액
		c_PayPlus.mf_set_us(common_data_set, "comm_vat_mny",
				String.valueOf(reqDto.getTaxPaymentPrice() - comm_tax_mny));

		c_PayPlus.mf_add_rs(payx_data_set, common_data_set);

		// 주문 정보
		int ordr_data_set;
		ordr_data_set = c_PayPlus.mf_add_set("ordr_data");

		c_PayPlus.mf_set_us(ordr_data_set, "ordr_idxx", reqDto.getOdid());
		c_PayPlus.mf_set_us(ordr_data_set, "good_name", reqDto.getGoodsName());
		// 결제
		c_PayPlus.mf_set_us(ordr_data_set, "good_mny", String.valueOf(reqDto.getPaymentPrice()));
		c_PayPlus.mf_set_us(ordr_data_set, "buyr_name", reqDto.getBuyerName());
		c_PayPlus.mf_set_us(ordr_data_set, "buyr_tel1", "");
		c_PayPlus.mf_set_us(ordr_data_set, "buyr_tel2", reqDto.getBuyerMobile());
		c_PayPlus.mf_set_us(ordr_data_set, "buyr_mail", reqDto.getBuyerEmail());

		// 가상계좌 정보
		int vcnt_data_set;
		vcnt_data_set = c_PayPlus.mf_add_set("va");

		c_PayPlus.mf_set_us(vcnt_data_set, "va_txtype", "41100000"); // 지불 타입 (가상계좌)
		c_PayPlus.mf_set_us(vcnt_data_set, "va_mny", String.valueOf(reqDto.getPaymentPrice())); // 결제 금액
		c_PayPlus.mf_set_us(vcnt_data_set, "va_bankcode", reqDto.getPgBankCode()); // 입금은행
		c_PayPlus.mf_set_us(vcnt_data_set, "va_name", reqDto.getBuyerName()); // 입금자명
		c_PayPlus.mf_set_us(vcnt_data_set, "va_date", reqDto.getVirtualAccountDateTime()); // 입금예정일자

		// 현금영수증
//		String va_receipt_gubn = "0"; // 소득공제용(개인) : 0 / 지출증빙용(기업) : 1
//		String va_taxno = reqDto.getCashReceiptNumber();
//		if (reqDto.isCashReceipt()) {
//			va_receipt_gubn = reqDto.getCashReceiptEnum().getKcpCode();
//		} else {
//			va_taxno = "0100001234";
//		}
//		c_PayPlus.mf_set_us(vcnt_data_set, "va_receipt_gubn", va_receipt_gubn); // 영수증 용도 구분
//		c_PayPlus.mf_set_us(vcnt_data_set, "va_taxno", va_taxno); // 주민(사업자 혹은 휴대폰)번호

		c_PayPlus.mf_add_rs(payx_data_set, vcnt_data_set);

		if (tx_cd.length() > 0) {
			c_PayPlus.mf_do_tx(config.getG_conf_site_cd(), config.getG_conf_site_key(), tx_cd, reqDto.getIp(),
					reqDto.getOdid(), "3", "1");
		} else {
			c_PayPlus.m_res_cd = "9562";
			c_PayPlus.m_res_msg = "연동 오류";
		}

		// 결과 코드
		resDto.setSuccess(KcpCode.SUCCESS.getCode().equals(c_PayPlus.m_res_cd));
		// 결과 메시지
		resDto.setMessage(c_PayPlus.m_res_msg);

		if (KcpCode.SUCCESS.getCode().equals(c_PayPlus.m_res_cd)) {
			// KCP 거래 고유 번호
			resDto.setTid(c_PayPlus.mf_get_res("tno"));
			// 실제 결제금액 (반드시 결제완료 후 리턴받은 amount와 가맹점 결제금액을 확인하시기 바랍니다.)
			resDto.setPaymentPrice(c_PayPlus.mf_get_res("amount"));
			// 가상계좌 은행명
			resDto.setBankName(c_PayPlus.mf_get_res("bankname"));
			// 가상계좌 은행코드
			resDto.setBankCode(c_PayPlus.mf_get_res("bankcode"));
			// 가상계좌 예금주명
			resDto.setDepositor(c_PayPlus.mf_get_res("depositor"));
			// 가상계좌번호
			resDto.setAccount(c_PayPlus.mf_get_res("account"));
			// 승인시간
			resDto.setAuthDate(c_PayPlus.mf_get_res("app_time"));
			// 가상계좌 입금마감시간
			resDto.setValidDate(c_PayPlus.mf_get_res("va_date"));
		}

		return resDto;
	}

	public KcpPaymentRegularBatchKeyResponseDto paymentRegularBatchKey(KcpPaymentRegularBatchKeyRequestDto reqDto)
			throws Exception {

		KcpConfig config = baiscConfig.getKcpConfig(PgEnums.PgAccountType.KCP_REGULAR.getCode());

		KcpPaymentRegularBatchKeyResponseDto resDto = new KcpPaymentRegularBatchKeyResponseDto();

		J_PP_CLI_N c_PayPlus = setCPayPlusSet(config);

		String tran_cd = "00100000";

		int payx_data_set;
		int common_data_set;

		payx_data_set = c_PayPlus.mf_add_set("payx_data");
		common_data_set = c_PayPlus.mf_add_set("common");

		c_PayPlus.mf_set_us(common_data_set, "amount", String.valueOf(reqDto.getPaymentPrice()));
		c_PayPlus.mf_set_us(common_data_set, "currency", "410");
		c_PayPlus.mf_set_us(common_data_set, "cust_ip", reqDto.getIp());
		c_PayPlus.mf_set_us(common_data_set, "escw_mod", "N");

		// - Start 복함과세
		// ※ tax_flag 변수 추가 안내 사항
		// ① NHN KCP 운영팀(1544 - 8660)으로 복합과세 신청이 된, 복합과세 전용 사이트코드로 계약한 가맹점에만 해당 (스마트폰의
		// 경우 신서버 smpay 모듈에만 적용)
		// ② 상품별이 아니라 금액으로 구분하여 요청
		// ③ 복합과세 구분 파라미터를 보내지 않으면 기본적으로 과세 금액으로 처리되니 복합과세로 처리를 원하시면 반드시 tax_flag,
		// tax_mny, free_mny, vat_mny 값을 전송
		// ④ 복합과세 이용 시 OCB 포인트 사용 및 적립, 베네피아 복지포인트 사용을 신용카드와 함께 진행할 경우 복합과세로 처리되지 않으니 유의
		// 복합 과세 구문 TG01 : 과세 TG02 : 비과세 TG03 : 복합과세
		c_PayPlus.mf_set_us(common_data_set, "tax_flag", "TG03");
		// 과세 승인금액 (공급가액) 과세 금액에 해당하는 공급가액 설정 과세 금액 = good_mny / 1.1
		int comm_tax_mny = Math.round(Float.valueOf(reqDto.getTaxPaymentPrice()) / new Float(1.1));
		c_PayPlus.mf_set_us(common_data_set, "comm_tax_mny", String.valueOf(comm_tax_mny));
		// 비과세 승인금액 비과세 금액에 해당하는 공급가액 설정 비과세 금액 = good_mny – 과세금액 – 부가가치세
		c_PayPlus.mf_set_us(common_data_set, "comm_free_mny", String.valueOf(reqDto.getTaxFreePaymentPrice()));
		// 부가가치세 부가가치세는 과세금액 공금가액의 10 % 부가가치세 = good_mny – 과세금액
		c_PayPlus.mf_set_us(common_data_set, "comm_vat_mny",
				String.valueOf(reqDto.getTaxPaymentPrice() - comm_tax_mny));
		// - End 복함과세

		c_PayPlus.mf_add_rs(payx_data_set, common_data_set);

		// 주문 정보
		int ordr_data_set;

		ordr_data_set = c_PayPlus.mf_add_set("ordr_data");

		c_PayPlus.mf_set_us(ordr_data_set, "ordr_idxx", reqDto.getOdid());
		c_PayPlus.mf_set_us(ordr_data_set, "good_name", reqDto.getGoodsName());
		c_PayPlus.mf_set_us(ordr_data_set, "good_mny", String.valueOf(reqDto.getPaymentPrice()));
		c_PayPlus.mf_set_us(ordr_data_set, "buyr_name", reqDto.getBuyerName());
		c_PayPlus.mf_set_us(ordr_data_set, "buyr_tel1", "");
		c_PayPlus.mf_set_us(ordr_data_set, "buyr_tel2", reqDto.getBuyerMobile());
		c_PayPlus.mf_set_us(ordr_data_set, "buyr_mail", reqDto.getBuyerEmail());

		int card_data_set;

		card_data_set = c_PayPlus.mf_add_set("card");

		c_PayPlus.mf_set_us(card_data_set, "card_mny", String.valueOf(reqDto.getPaymentPrice())); // 결제 금액

		// TODO 복합과세 여부 확인
		c_PayPlus.mf_set_us(card_data_set, "card_tx_type", "11511000");
		c_PayPlus.mf_set_us(card_data_set, "quota", "00");
		c_PayPlus.mf_set_us(card_data_set, "bt_group_id", config.getKcpgroup_id());
		c_PayPlus.mf_set_us(card_data_set, "bt_batch_key", reqDto.getBatchKey());

		c_PayPlus.mf_add_rs(payx_data_set, card_data_set);

		if (tran_cd.length() > 0) {
			c_PayPlus.mf_do_tx(config.getG_conf_site_cd(), config.getG_conf_site_key(), tran_cd, reqDto.getIp(),
					reqDto.getOdid(), config.getG_conf_log_level(), "1");
		} else {
			resDto.setRes_cd("9562");
			resDto.setRes_msg("연동 오류|tran_cd값이 설정되지 않았습니다.");
			return resDto;
		}

		// 결과 코드
		resDto.setRes_cd(c_PayPlus.m_res_cd);
		// 결과 메시지
		resDto.setRes_msg(c_PayPlus.m_res_msg);

		if (KcpCode.SUCCESS.getCode().equals(resDto.getRes_cd())) {
			// KCP 거래 고유 번호
			resDto.setTno(c_PayPlus.mf_get_res("tno"));
			// 카드사 코드
			resDto.setCard_cd(c_PayPlus.mf_get_res("card_cd"));
			// 카드사 명
			resDto.setCard_name(c_PayPlus.mf_get_res("card_name"));
			// 승인시간
			resDto.setApp_time(c_PayPlus.mf_get_res("app_time"));
			// 승인번호
			resDto.setApp_no(c_PayPlus.mf_get_res("app_no"));
			// 무이자 여부
			resDto.setNoinf(c_PayPlus.mf_get_res("noinf"));
			// 할부 개월 수
			resDto.setQuota(c_PayPlus.mf_get_res("quota"));
		}

		return resDto;
	}

	/**
	 * 송금 처리
	 *
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	public KcpRemitResponseDto remit(KcpRemitRequestDto reqDto) throws Exception {

		// 로컬, 개발, QA 환경은 테스트 계정으로 처리
		KcpConfig config = null;
		if (SystemUtil.DEFAULT_PROFILE.equals(System.getProperty(SystemUtil.SPRING_PROFILES_ACTIVE))
				|| SystemUtil.DEV_PROFILE.equals(System.getProperty(SystemUtil.SPRING_PROFILES_ACTIVE))
				|| SystemUtil.QA_PROFILE.equals(System.getProperty(SystemUtil.SPRING_PROFILES_ACTIVE))) {
			// 송금은 테스트계정이 별도로 있기때문에 예외처리..
			config = baiscConfig.getKcpConfigRemitTest();
			// 테스트 송금은 5000 으로만 가능하여 강제 수정 처리
			reqDto.setPaymentPrice(5000);
		} else {
			config = baiscConfig.getKcpConfig(PgEnums.PgAccountType.KCP_BASIC.getCode());
		}

		KcpRemitResponseDto resDto = new KcpRemitResponseDto();

		J_PP_CLI_N c_PayPlus = setCPayPlusSet(config);

		String tran_cd = "00100000";

		int payx_data_set;
		int common_data_set;

		payx_data_set = c_PayPlus.mf_add_set("payx_data");
		common_data_set = c_PayPlus.mf_add_set("common");

		c_PayPlus.mf_set_us(common_data_set, "amount", String.valueOf(reqDto.getPaymentPrice()));
		c_PayPlus.mf_set_us(common_data_set, "currency", "410");
		c_PayPlus.mf_set_us(common_data_set, "cust_ip", reqDto.getIp());
		c_PayPlus.mf_set_us(common_data_set, "escw_mod", "N");

		c_PayPlus.mf_add_rs(payx_data_set, common_data_set);

		// 송금 전문
	    int vcnt_data_set;

	    vcnt_data_set = c_PayPlus.mf_add_set( "va" );

		c_PayPlus.mf_set_us(vcnt_data_set, "va_txtype", "48200000"); // 거래 종류(대량이체 송금)
		c_PayPlus.mf_set_us(vcnt_data_set, "va_mny", String.valueOf(reqDto.getPaymentPrice())); // 거래금액
		c_PayPlus.mf_set_us(vcnt_data_set, "va_bankcode", reqDto.getRefundBankCode()); // 은행코드
		c_PayPlus.mf_set_us(vcnt_data_set, "va_account", reqDto.getRefundBankNumber()); // 계좌번호
		c_PayPlus.mf_set_us(vcnt_data_set, "va_name", reqDto.getSendName()); // 입금 의뢰인명

		c_PayPlus.mf_add_rs(payx_data_set, vcnt_data_set);

		if (tran_cd.length() > 0) {
			c_PayPlus.mf_do_tx(config.getG_conf_site_cd(), config.getG_conf_site_key(), tran_cd, reqDto.getIp(), "", config.getG_conf_log_level(), "1");
		} else {
			resDto.setRes_cd("9562");
			resDto.setMessage("연동 오류|tran_cd값이 설정되지 않았습니다.");
			return resDto;
		}

		// 결과 코드
		resDto.setSuccess(KcpCode.SUCCESS.getCode().equals(c_PayPlus.m_res_cd));
		resDto.setRes_cd(c_PayPlus.m_res_cd);

		// 결과 메시지
		resDto.setMessage(c_PayPlus.m_res_msg);

		if (KcpCode.SUCCESS.getCode().equals(resDto.getRes_cd())) {
		     // 처리일자
			resDto.setApp_time(c_PayPlus.mf_get_res("app_time"));
			// 거래일자
			resDto.setTrade_date(c_PayPlus.mf_get_res("trade_date"));
			// 거래일련번호
			resDto.setTrade_seq(c_PayPlus.mf_get_res("trade_seq"));
			// 남은금액
			resDto.setBal_amount(c_PayPlus.mf_get_res("bal_amount"));
		}

		return resDto;
	}

	@Override
	public CancelResponseDto cancel(String pgAccountType, CancelRequestDto reqDto) throws Exception {

		KcpConfig config = baiscConfig.getKcpConfig(pgAccountType);

		CancelResponseDto resDto = new CancelResponseDto();
		J_PP_CLI_N c_PayPlus = setCPayPlusSet(config);

		int mod_data_set_no = c_PayPlus.mf_add_set("mod_data");
		String tran_cd = "00200000";

		// KCP 원거래 거래번호
		c_PayPlus.mf_set_us(mod_data_set_no, "tno", reqDto.getTid());
		// 변경 요청자 IP
		c_PayPlus.mf_set_us(mod_data_set_no, "mod_ip", reqDto.getIp());
		// 변경 사유
		c_PayPlus.mf_set_us(mod_data_set_no, "mod_desc", reqDto.getCancelMessage());

		// 부분 취소시
		if (reqDto.isPartial()) {
			if (PaymentType.VIRTUAL_BANK.equals(reqDto.getPaymentType())) {
				// 가상계좌 부분취소 STPD
				c_PayPlus.mf_set_us(mod_data_set_no, "mod_type", "STPD");
				c_PayPlus.mf_set_us(mod_data_set_no, "mod_sub_type", "MDSC04");
			} else {
				// 부분취소 STPC
				c_PayPlus.mf_set_us(mod_data_set_no, "mod_type", "STPC");
			}

			// 취소요청 금액
			c_PayPlus.mf_set_us(mod_data_set_no, "mod_mny", String.valueOf(reqDto.getCancelPrice()));
			// 부분취소 이전에 남은 금액
			c_PayPlus.mf_set_us(mod_data_set_no, "rem_mny",
					String.valueOf(reqDto.getCancelPrice() + reqDto.getExpectedRestPrice()));
			// 복합과세 거래 구분 값입니다. (복합과세)
			c_PayPlus.mf_set_us(mod_data_set_no, "tax_flag", "TG03");

			int mod_tax_mny = Math.round(Float.valueOf(reqDto.getTaxCancelPrice()) / new Float(1.1));
			// 공급가 부분취소 요청금액(복합과세)
			c_PayPlus.mf_set_us(mod_data_set_no, "mod_tax_mny", String.valueOf(mod_tax_mny));
			// 부가세 부분취소 요청금액 (복합과세)
			c_PayPlus.mf_set_us(mod_data_set_no, "mod_vat_mny",
					String.valueOf(reqDto.getTaxCancelPrice() - mod_tax_mny));
			// 비과세 부분취소 요청금액 (복합과세)
			c_PayPlus.mf_set_us(mod_data_set_no, "mod_free_mny", String.valueOf(reqDto.getTaxFreecancelPrice()));
		} else {
			if (PaymentType.VIRTUAL_BANK.equals(reqDto.getPaymentType())) {
				// 가상계좌 전체 환불 STHD
				c_PayPlus.mf_set_us(mod_data_set_no, "mod_type", "STHD");
				c_PayPlus.mf_set_us(mod_data_set_no, "mod_sub_type", "MDSC00");
			} else {
				// 원거래 변경 요청 종류
				c_PayPlus.mf_set_us(mod_data_set_no, "mod_type", "STSC");
			}
		}

		if (PaymentType.VIRTUAL_BANK.equals(reqDto.getPaymentType())) {
			// 인증타입: 계좌인증 + 환불등록 - MDCP01
			c_PayPlus.mf_set_us(mod_data_set_no, "mod_comp_type", "MDCP01");
			// 계좌인증 및 환불 받을 계좌번호
			c_PayPlus.mf_set_us(mod_data_set_no, "mod_account", reqDto.getRefundBankNumber());
			// 계좌인증 및 환불 받을 계좌 예금주의 이름
			c_PayPlus.mf_set_us(mod_data_set_no, "mod_depositor", reqDto.getRefundBankName());
			// 환불 요청하는 은행코드
			c_PayPlus.mf_set_us(mod_data_set_no, "mod_bankcode", reqDto.getRefundBankCode());
		}

		c_PayPlus.mf_do_tx(config.getG_conf_site_cd(), config.getG_conf_site_key(), tran_cd, "", reqDto.getOdid(),
				config.getG_conf_log_level(), "1");

		// 결과 코드
		resDto.setSuccess(KcpCode.SUCCESS.getCode().equals(c_PayPlus.m_res_cd));
		// 결과 메시지
		resDto.setMessage(c_PayPlus.m_res_msg);

		return resDto;
	}

	@Override
	public ReceiptIssueResponseDto receiptIssue(ReceiptIssueRequestDto reqDto) throws Exception {

		KcpConfig config = baiscConfig.getKcpConfig(PgEnums.PgAccountType.KCP_BASIC.getCode());

		ReceiptIssueResponseDto resDto = new ReceiptIssueResponseDto();
		J_PP_CLI_N c_PayPlus = setCPayPlusSet(config);

		String tran_cd = "07010000"; // 현금영수증 등록 요청

		int rcpt_data_set;
		int ordr_data_set;
		int corp_data_set;

		rcpt_data_set = c_PayPlus.mf_add_set("rcpt_data");
		ordr_data_set = c_PayPlus.mf_add_set("ordr_data");
		corp_data_set = c_PayPlus.mf_add_set("corp_data");

		c_PayPlus.mf_set_us(rcpt_data_set, "user_type", "PGNW");
		// 원거래 시간 20071201011030
		c_PayPlus.mf_set_us(rcpt_data_set, "trad_time", DateUtil.getCurrentDate("yyyyMMddHHmmss"));
		// 발행 용도 (0:소득공제용, 1:지출증빙)
		c_PayPlus.mf_set_us(rcpt_data_set, "tr_code", (reqDto.getReceiptType().equals(CashReceipt.PROOF) ? "1" : "0"));
		c_PayPlus.mf_set_us(rcpt_data_set, "id_info", reqDto.getRegNumber()); // 주민(휴대폰)번호/현금영수증 카드
		c_PayPlus.mf_set_us(rcpt_data_set, "amt_tot", String.valueOf(reqDto.getTotalPrice())); // 결제금액
		c_PayPlus.mf_set_us(rcpt_data_set, "amt_sup", String.valueOf(reqDto.getSupPrice())); // 공금가액
		c_PayPlus.mf_set_us(rcpt_data_set, "amt_svc", String.valueOf(reqDto.getSrcvPrice())); // 봉사액
		c_PayPlus.mf_set_us(rcpt_data_set, "amt_tax", String.valueOf(reqDto.getTax())); // 부가세
		c_PayPlus.mf_set_us(rcpt_data_set, "pay_type", "PAXX"); // 고정

		// 주문 정보
		c_PayPlus.mf_set_us(ordr_data_set, "ordr_idxx", reqDto.getOdid()); // 주문번호
		c_PayPlus.mf_set_us(ordr_data_set, "good_name", reqDto.getGoodsName()); // 상품명
		c_PayPlus.mf_set_us(ordr_data_set, "buyr_name", reqDto.getBuyerName()); // 구매자명
		c_PayPlus.mf_set_us(ordr_data_set, "buyr_tel1", reqDto.getBuyerMobile()); // 전화번호
		c_PayPlus.mf_set_us(ordr_data_set, "buyr_mail", reqDto.getBuyerEmail()); // 이메일
		c_PayPlus.mf_set_us(ordr_data_set, "comment", ""); // 비고

		// 가맹점 정보
		c_PayPlus.mf_set_us(corp_data_set, "corp_type", "0"); // "0" 고정
//		if( "1".equals( corp_type ) )
//        {
//            c_PayPlus.mf_set_us( corp_data_set, "corp_tax_type"   , corp_tax_type  ) ; // 과세/면세 구분 TG01: 과세, TG02: 면세
//            c_PayPlus.mf_set_us( corp_data_set, "corp_tax_no"     , corp_tax_no    ) ; // 발행 사업자 번호
//            c_PayPlus.mf_set_us( corp_data_set, "corp_sell_tax_no", corp_tax_no    ) ;
//            c_PayPlus.mf_set_us( corp_data_set, "corp_nm"         , corp_nm        ) ; // 상호
//            c_PayPlus.mf_set_us( corp_data_set, "corp_owner_nm"   , corp_owner_nm  ) ; // 대표자명
//            c_PayPlus.mf_set_us( corp_data_set, "corp_addr"       , corp_addr      ) ; // 사업장 주소
//            c_PayPlus.mf_set_us( corp_data_set, "corp_telno"      , corp_telno     ) ; // 사업장 대표자 연락처
//        }

		c_PayPlus.mf_do_tx(config.getG_conf_site_cd(), "", tran_cd, reqDto.getIp(), reqDto.getOdid(),
				config.getG_conf_log_level(), "1");

		// 결과 코드
		resDto.setSuccess(KcpCode.SUCCESS.getCode().equals(c_PayPlus.m_res_cd));
		// 결과 메시지
		resDto.setMessage(c_PayPlus.m_res_msg);

		if (KcpCode.SUCCESS.getCode().equals(c_PayPlus.m_res_cd)) {
			resDto.setTid(c_PayPlus.mf_get_res("cash_no")); // 현금영수증 거래번호
			resDto.setReceiptNo(c_PayPlus.mf_get_res("receipt_no")); // 현금영수증 승인번호
			resDto.setAuthDateTime(c_PayPlus.mf_get_res("app_time")); // 승인시간(YYYYMMDDhhmmss)
		}
		return resDto;
	}

	@Override
	public ReceiptCancelResponseDto receiptCancel(String tid) throws Exception {
		return receiptCancel(tid, DeviceUtil.getServerIp());
	}

	@Override
	public ReceiptCancelResponseDto receiptCancel(String tid, String ip) throws Exception {

		KcpConfig config = baiscConfig.getKcpConfig(PgEnums.PgAccountType.KCP_BASIC.getCode());

		ReceiptCancelResponseDto resDto = new ReceiptCancelResponseDto();
		J_PP_CLI_N c_PayPlus = setCPayPlusSet(config);

		int mod_data_set_no = c_PayPlus.mf_add_set("mod_data");
		String tran_cd = "07020000"; // 취소 요청

		c_PayPlus.mf_set_us(mod_data_set_no, "mod_type", "STSC");// 고정
		c_PayPlus.mf_set_us(mod_data_set_no, "mod_value", tid); // 현금영수증 거래번호
		c_PayPlus.mf_set_us(mod_data_set_no, "mod_gubn", "MG01"); // 고정
		c_PayPlus.mf_set_us(mod_data_set_no, "trad_time", DateUtil.getCurrentDate("yyyyMMddHHmmss"));

		// KCP 원거래 거래번호
		c_PayPlus.mf_set_us(mod_data_set_no, "tno", tid);
		// 변경 요청자 IP
		c_PayPlus.mf_set_us(mod_data_set_no, "mod_ip", ip);

		c_PayPlus.mf_do_tx(config.getG_conf_site_cd(), "", tran_cd, ip, "", config.getG_conf_log_level(), "1");

		// 결과 코드
		resDto.setSuccess(KcpCode.SUCCESS.getCode().equals(c_PayPlus.m_res_cd));
		// 결과 메시지
		resDto.setMessage(c_PayPlus.m_res_msg);

		return resDto;
	}

	@Override
	public boolean escrowRegistDelivery(EscrowRegistDeliveryDataRequestDto reqDto) throws Exception {
		KcpConfig config = baiscConfig.getKcpConfig(PgEnums.PgAccountType.KCP_BASIC.getCode());

		J_PP_CLI_N c_PayPlus = setCPayPlusSet(config);

		int mod_data_set_no = c_PayPlus.mf_add_set("mod_data");
		String tran_cd = "00200000";

		c_PayPlus.mf_set_us(mod_data_set_no, "tno", reqDto.getTid()); // KCP 원거래 거래번호
		c_PayPlus.mf_set_us(mod_data_set_no, "mod_ip", reqDto.getIp()); // 변경 요청자 IP
		c_PayPlus.mf_set_us(mod_data_set_no, "mod_desc", ""); // 변경 사유

		c_PayPlus.mf_set_us(mod_data_set_no, "mod_type", "STE1");// 고정
		c_PayPlus.mf_set_us(mod_data_set_no, "deli_numb", reqDto.getTrackingNo()); // 운송장 번호
		c_PayPlus.mf_set_us(mod_data_set_no, "deli_corp", reqDto.getShippingCompanyName()); // 택배 업체명

		c_PayPlus.mf_do_tx(config.getG_conf_site_cd(), config.getG_conf_site_key(), tran_cd, "", "", config.getG_conf_log_level(), "1");

//		System.out.println(c_PayPlus.m_res_msg);

		return KcpCode.SUCCESS.getCode().equals(c_PayPlus.m_res_cd);
	}

	private J_PP_CLI_N setCPayPlusSet(KcpConfig config) {
		J_PP_CLI_N c_PayPlus = new J_PP_CLI_N();
		c_PayPlus.mf_init("", config.getG_conf_gw_url(), config.getG_conf_gw_port(), config.getG_conf_tx_mode(),
				config.getG_conf_log_dir());
		c_PayPlus.mf_init_set();
		return c_PayPlus;
	}

	private String f_get_parm(String val) {
		if (val == null)
			val = "";
		return val;
	}

	private String fromCharCode(int... codePoints) {
		return new String(codePoints, 0, codePoints.length);
	}
}
