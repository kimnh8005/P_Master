package kr.co.pulmuone.v1.pg.service.inicis.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import kr.co.pulmuone.v1.comm.constants.Constants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.util.Base64;
import com.inicis.std.util.HttpUtil;
import com.inicis.std.util.ParseUtil;
import com.inicis.std.util.SignatureUtil;

import kr.co.pulmuone.v1.comm.enums.GoodsEnums.DeviceType;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.CashReceipt;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.PaymentType;
import kr.co.pulmuone.v1.comm.constants.AppConstants;
import kr.co.pulmuone.v1.comm.enums.PgEnums;
import kr.co.pulmuone.v1.comm.enums.PgEnums.InicisBankCode;
import kr.co.pulmuone.v1.comm.enums.PgEnums.InicisCode;
import kr.co.pulmuone.v1.comm.enums.PgEnums.PgServiceType;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.pg.dto.BasicDataRequestDto;
import kr.co.pulmuone.v1.pg.dto.BasicDataResponseDto;
import kr.co.pulmuone.v1.pg.dto.CancelRequestDto;
import kr.co.pulmuone.v1.pg.dto.CancelResponseDto;
import kr.co.pulmuone.v1.pg.dto.EscrowRegistDeliveryDataRequestDto;
import kr.co.pulmuone.v1.pg.dto.PaymentFormDto;
import kr.co.pulmuone.v1.pg.dto.ReceiptCancelResponseDto;
import kr.co.pulmuone.v1.pg.dto.ReceiptIssueRequestDto;
import kr.co.pulmuone.v1.pg.dto.ReceiptIssueResponseDto;
import kr.co.pulmuone.v1.pg.dto.VirtualAccountDataResponseDto;
import kr.co.pulmuone.v1.pg.service.PgAbstractService;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisApprovalRequestDto;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisApprovalResponseDto;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisMobileApprovalRequestDto;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisMobileApprovalResponseDto;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisNonAuthenticationCartPayRequestDto;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisNonAuthenticationCartPayResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class InicisPgService extends PgAbstractService<InicisApprovalRequestDto, InicisApprovalResponseDto> {

	@Autowired
	InicisConfig basicConfig;

	@Override
	public PgServiceType getServiceType() throws Exception {
		return PgServiceType.INICIS;
	}

	@Override
	public BasicDataResponseDto getBasicData(BasicDataRequestDto reqDto) throws Exception {

		InicisConfig config = basicConfig.getInicisConfig(PgEnums.PgAccountType.INICIS_BASIC.getCode());

		BasicDataResponseDto resDto = new BasicDataResponseDto();
		List<PaymentFormDto> formDtoList = new ArrayList<PaymentFormDto>();

		String exeScriptType = "";

		DeviceType deviceType = DeviceUtil.getGoodsEnumDeviceTypeByUserDevice();

		// PC 일떄
		if (deviceType.getCode().equals(DeviceType.PC.getCode())) {
			exeScriptType = PgEnums.ExeScriptType.PC_INICIS_BASIC.getCode();

			String timestamp = SignatureUtil.getTimestamp();

			// 가맹점 확인을 위한 signKey를 해시값으로 변경 (SHA-256방식 사용)
			String mKey = SignatureUtil.hash(config.getSignKey(), "SHA-256");

			Map<String, String> signParam = new HashMap<String, String>();

			signParam.put("oid", reqDto.getOdid()); // 필수
			signParam.put("price", String.valueOf(reqDto.getPaymentPrice())); // 필수
			signParam.put("timestamp", timestamp); // 필수

			// signature 데이터 생성 (모듈에서 자동으로 signParam을 알파벳 순으로 정렬후 NVP 방식으로 나열해 hash)
			String signature = SignatureUtil.makeSignature(signParam);

			// 전문버전
			formDtoList.add(PaymentFormDto.builder().name("version").value("1.0").build());
			// 통화구분
			formDtoList.add(PaymentFormDto.builder().name("currency").value("WON").build());
			// 인코딩 설정
			formDtoList.add(PaymentFormDto.builder().name("charset").value("UTF-8").build());
			// TimeInMillis(Long형) 20byte 필수
			formDtoList.add(PaymentFormDto.builder().name("timestamp").value(timestamp).build());
			// 가맹점 확인을 위한 signKey를 해시값으로 변경 (SHA-256방식 사용)
			formDtoList.add(PaymentFormDto.builder().name("mKey").value(mKey).build());
			// 위변조 방지 SHA256 Hash 값 64byte 필수
			formDtoList.add(PaymentFormDto.builder().name("signature").value(signature).build());
			// 상점아이디
			formDtoList.add(PaymentFormDto.builder().name("mid").value(config.getMid()).build());
			// Return URL
			formDtoList.add(PaymentFormDto.builder().name("returnUrl")
					.value(config.getAppDomain() + (reqDto.isAddPay() ? "/pg/inicis/addPayResult" : "/pg/inicis/result")).build());
			// 결제창 닫기처리 close URL
			formDtoList.add(PaymentFormDto.builder().name("closeUrl").value(config.getAppDomain() + "/pg/inicis/close")
					.build());
			// 요청결제수단
			// 결제수단별 추가 옵션값
			if (PaymentType.CARD.equals(reqDto.getPaymentType())) {
				formDtoList.add(PaymentFormDto.builder().name("gopaymethod").value("Card").build());
				// 신용카드 결제 시 1000원 이하금액 결제가능 옵션
				formDtoList.add(PaymentFormDto.builder().name("acceptmethod").value("below1000").build());
			} else if (PaymentType.BANK.equals(reqDto.getPaymentType())) {
				formDtoList.add(PaymentFormDto.builder().name("gopaymethod").value("DirectBank").build());
				formDtoList.add(PaymentFormDto.builder().name("acceptmethod").value("useescrow").build());
			} else if (PaymentType.VIRTUAL_BANK.equals(reqDto.getPaymentType())) {
				formDtoList.add(PaymentFormDto.builder().name("gopaymethod").value("VBank").build());
				formDtoList.add(PaymentFormDto.builder().name("acceptmethod")
						.value("va_receipt:useescrow:vbank(" + reqDto.getVirtualAccountDateTime() + ")").build());
			}

			// 주문정보
			// 상품주문번호 64 byte
			formDtoList.add(PaymentFormDto.builder().name("oid").value(reqDto.getOdid()).build());
			// 상품명
			formDtoList.add(PaymentFormDto.builder().name("goodname").value(reqDto.getGoodsName()).build());
			// 결제금액
			formDtoList.add(
					PaymentFormDto.builder().name("price").value(String.valueOf(reqDto.getPaymentPrice())).build());
			// 부가세 대상: 부가세업체정함’ 설정업체에 한함 주의: 전체금액의 10%이하로 설정 가맹점에서 등록시 VAT가 총 상품가격의 10% 초과할
			// 경우는 거절됨 가맹점에서 등록시 VAT가 총상품가격의 10% 초과할 경우는 거절됨
			int comm_tax_mny = getCommTaxMny(reqDto.getTaxPaymentPrice());
			formDtoList.add(PaymentFormDto.builder().name("tax")
					.value(String.valueOf(reqDto.getTaxPaymentPrice() - comm_tax_mny)).build());
			// 비과세 대상: ‘부가세업체정함’ 설정업체에 한함 과세되지 않는 금액
			formDtoList.add(PaymentFormDto.builder().name("taxfree")
					.value(String.valueOf(reqDto.getTaxFreePaymentPrice())).build());

			// 구매자명
			formDtoList.add(PaymentFormDto.builder().name("buyername").value(reqDto.getBuyerName()).build());
			// 구매자Mobile번호 숫자와 "-"만 허용
			formDtoList.add(PaymentFormDto.builder().name("buyertel").value(reqDto.getBuyerMobile()).build());
			// 구매자Email
			formDtoList.add(PaymentFormDto.builder().name("buyeremail").value(reqDto.getBuyerEmail()).build());
			// 인증 성공시 가맹점으로 리턴
			formDtoList.add(PaymentFormDto.builder().name("merchantData").value(reqDto.getEtcData()).build());

			// 신용카드 옵션
			// 할부 개월수 “2:3:4”,“2:0” * 개월수를 : 로 구분된 값 일시불은 기본적을 표시, 생략시 일시불만 * 5만원 이상시에만 동작
			formDtoList.add(PaymentFormDto.builder().name("quotabase").value(reqDto.getQuota()).build());
			// 할부 선택
			formDtoList.add(PaymentFormDto.builder().name("ansim_quota").value(reqDto.getQuota()).build());

			// 결제 카드사 선택 노출옵션
			formDtoList.add(PaymentFormDto.builder().name("ini_onlycardcode").value(reqDto.getPgBankCode()).build());

		} else {
			exeScriptType = PgEnums.ExeScriptType.MOBILE_INICIS_BASIC.getCode();

			// 캐릭터셋 설정
			formDtoList.add(PaymentFormDto.builder().name("P_CHARSET").value("utf8").build());

			// 결제요청 지불수단
			String P_RESERVED = "";
			if (PaymentType.CARD.equals(reqDto.getPaymentType())) {
				formDtoList.add(PaymentFormDto.builder().name("P_INI_PAYMENT").value("CARD").build());
				// 신용카드 노출제한 옵션
				formDtoList.add(PaymentFormDto.builder().name("P_ONLY_CARDCODE").value(reqDto.getPgBankCode()).build());
				// 신용카드 할부기간 지정
				String P_QUOTABASE = reqDto.getQuota();
				if(StringUtil.isEmpty(P_QUOTABASE)) {
					P_QUOTABASE = "0";
				}
				if (!"0".equals(P_QUOTABASE)) {
					P_QUOTABASE = P_QUOTABASE + ":99"; // {할부개월수}:99를 해야 PG 창에서 선택되어 있음 (일시불은 제외)
				}
				formDtoList.add(PaymentFormDto.builder().name("P_QUOTABASE").value(P_QUOTABASE).build());
				P_RESERVED = "below1000=Y";
			} else if (PaymentType.BANK.equals(reqDto.getPaymentType())) {
				formDtoList.add(PaymentFormDto.builder().name("P_INI_PAYMENT").value("BANK").build());
				P_RESERVED = "twotrs_bank=Y&apprun_check=Y&useescrow=Y";
			} else if (PaymentType.VIRTUAL_BANK.equals(reqDto.getPaymentType())) {
				formDtoList.add(PaymentFormDto.builder().name("P_INI_PAYMENT").value("VBANK").build());
//				ex) 입금기한 설정 예시
//				P_VBANK_DT=20200102, P_VBANK_TM=0000 : 2020-01-02 23 시 59 분 59 초까지
//				P_VBANK_DT=20200101, P_VBANK_TM=2359 : 2020-01-01 23 시 59 분 00 초까지
//				P_VBANK_DT=20200102, P_VBANK_TM=0001 : 2020-01-02 00 시 01 분 00 초까지
				formDtoList.add(PaymentFormDto.builder().name("P_VBANK_DT").value(reqDto.getVirtualAccountDateTime().substring(0, 8)).build());
				formDtoList.add(PaymentFormDto.builder().name("P_VBANK_TM").value(reqDto.getVirtualAccountDateTime().substring(8, 12)).build());
				P_RESERVED = "vbank_receipt=Y&useescrow=Y";
			}
			if (deviceType.getCode().equals("APP")) {
				P_RESERVED = ("".equals(P_RESERVED) ? "" : P_RESERVED + "&") + "app_scheme=" + AppConstants.APP_SCHEME;
				if (DeviceUtil.isIos()) {
					P_RESERVED = P_RESERVED + "&iosapp=Y";
				}
				if (PaymentType.BANK.equals(reqDto.getPaymentType())) {
					formDtoList.add(PaymentFormDto.builder().name("P_RETURN_URL").value(AppConstants.APP_SCHEME).build());
				}
			}
			formDtoList.add(PaymentFormDto.builder().name("P_RESERVED").value(P_RESERVED).build());

			// 상점아이디
			formDtoList.add(PaymentFormDto.builder().name("P_MID").value(config.getMid()).build());
			// 주문번호
			formDtoList.add(PaymentFormDto.builder().name("P_OID").value(reqDto.getOdid()).build());
			// 거래금액
			formDtoList.add(
					PaymentFormDto.builder().name("P_AMT").value(String.valueOf(reqDto.getPaymentPrice())).build());
			int comm_tax_mny = getCommTaxMny(reqDto.getTaxPaymentPrice());
			formDtoList.add(PaymentFormDto.builder().name("P_TAX")
					.value(String.valueOf(reqDto.getTaxPaymentPrice() - comm_tax_mny)).build());
			// 비과세 대상: ‘부가세업체정함’ 설정업체에 한함 과세되지 않는 금액
			formDtoList.add(PaymentFormDto.builder().name("P_TAXFREE")
					.value(String.valueOf(reqDto.getTaxFreePaymentPrice())).build());
			// 고객성명
			formDtoList.add(PaymentFormDto.builder().name("P_UNAME").value(reqDto.getBuyerName()).build());
			// 기타주문정보
			formDtoList.add(PaymentFormDto.builder().name("P_NOTI").value(reqDto.getEtcData()).build());
			// 결제상품명
			formDtoList.add(PaymentFormDto.builder().name("P_GOODS").value(reqDto.getGoodsName()).build());
			// 구매자 휴대폰번호
			formDtoList.add(PaymentFormDto.builder().name("P_MOBILE").value(reqDto.getBuyerMobile()).build());
			// 구매자 E-mail
			formDtoList.add(PaymentFormDto.builder().name("P_EMAIL").value(reqDto.getBuyerEmail()).build());
			// 인증결과수신 Url
			formDtoList.add(PaymentFormDto.builder().name("P_NEXT_URL").value(config.getAppDomain() + (reqDto.isAddPay() ? "/pg/inicis/addPayNext" : "/pg/inicis/next"))
					.build());
			// 승인결과통보 Url
			formDtoList.add(PaymentFormDto.builder().name("P_NOTI_URL").value(config.getAppDomain() + "/pg/inicis/noti")
					.build());
		}

		resDto.setPgFormDataList(formDtoList);
		resDto.setExeScriptType(exeScriptType);
		resDto.setOdid(reqDto.getOdid());
		resDto.setPaymentPrice(reqDto.getPaymentPrice());

		return resDto;
	}

	@Override
	public VirtualAccountDataResponseDto getVirtualAccountData(BasicDataRequestDto reqDto) throws Exception {

		InicisConfig config = basicConfig.getInicisConfig(PgEnums.PgAccountType.INICIS_BASIC.getCode());

		VirtualAccountDataResponseDto resDto = new VirtualAccountDataResponseDto();

		// API 요청 전문 생성
		Map<String, String> authMap = new Hashtable<String, String>();

		authMap.put("type", "Pay"); // "Pay" 고정
		authMap.put("paymethod", "Vacct"); // "Vacct" 고정
		authMap.put("timestamp", DateUtil.getCurrentDate("yyyyMMddHHmmss")); // 전문생성시간(YYYYMMDDhhmmss)
		authMap.put("clientIp", reqDto.getIp()); // 가맹점 요청 서버IP (추후 거래 확인 등에 사용됨)
		authMap.put("mid", config.getMid()); // 가맹점 ID
		authMap.put("moid", reqDto.getOdid()); //
		authMap.put("url", config.getAppDomain()); // 가맹점 URL
		authMap.put("goodName", reqDto.getGoodsName()); // 상품명
		authMap.put("price", String.valueOf(reqDto.getPaymentPrice())); // 결제금액

		int comm_tax_mny = getCommTaxMny(reqDto.getTaxPaymentPrice());
		// 부과세
		authMap.put("tax", String.valueOf(reqDto.getTaxPaymentPrice() - comm_tax_mny));
		// 비과세
		authMap.put("taxFree", String.valueOf(reqDto.getTaxFreePaymentPrice()));

		authMap.put("currency", "WON"); // 통화코드(WON/USD)
		authMap.put("buyerName", reqDto.getBuyerName()); // 구매자명
		authMap.put("buyerEmail", reqDto.getBuyerEmail()); // 구매자이메일
		authMap.put("buyerTel", reqDto.getBuyerMobile()); // 구매자 연락처
		authMap.put("bankCode", reqDto.getPgBankCode()); // 은행코드

		authMap.put("dtInput", reqDto.getVirtualAccountDateTime().substring(0, 8)); // 입금예정일자(YYYYMMDD)
		authMap.put("tmInput", reqDto.getVirtualAccountDateTime().substring(8, 12)); // 입금예정시간 [hhmm]
		authMap.put("nmInput", reqDto.getBuyerName()); // 입금자명

		String flgCash = reqDto.getFlgCash(); // 현금영수증 발행여부(0: 미발행, 1: 소득공제 발행, 2: 지출증빙)
		if(StringUtils.isNotEmpty(flgCash) && !"0".equals(flgCash)) {
			// 현금영수증 발행정보(주민번호, 휴대폰번호, 사업장등록번호 등)
			String cashRegNo = aesEncrypt(config, reqDto.getCashReceiptNumber());
			authMap.put("flgCash", flgCash);
			authMap.put("cashRegNo", cashRegNo);
		}

		// 전문위변조 HASH
		authMap.put("hashData", SignatureUtil.hash(
				config.getKey() + authMap.get("type") + authMap.get("paymethod") + authMap.get("timestamp")
						+ authMap.get("clientIp") + authMap.get("mid") + authMap.get("moid") + authMap.get("price"),
				"SHA-512"));

		//HttpUtil httpUtil = new HttpUtil();
		InicisHttpClient httpUtil = new InicisHttpClient();

		// API 통신 시작
		String authResultString = "";

		authResultString = httpUtil.processHTTP(authMap, "https://iniapi.inicis.com/api/v1/formpay");

		Map<String, String> resultMap = ConverthttpResultStringToMap(authResultString);

		// 결과 코드
		resDto.setSuccess(InicisCode.SUCCESS_MOBILE.getCode().equals(resultMap.get("resultCode")));
		// 결과 메시지
		resDto.setMessage(resultMap.get("resultMsg"));
		// 승인일자
		resDto.setTid(resultMap.get("tid"));
		// 실제 결제금액
		resDto.setPaymentPrice(String.valueOf(reqDto.getPaymentPrice()));
		// 승인시간
		resDto.setAuthDate(resultMap.get("authDate") + resultMap.get("authTime"));
		// 가상계좌번호
		resDto.setAccount(resultMap.get("vacct"));
		// 예금주명
		resDto.setDepositor(resultMap.get("vacctName"));
		// 가상계좌 은행코드
		resDto.setBankCode(resultMap.get("vacctBankCode"));
		// 가상계좌 은행명
		resDto.setBankName(InicisBankCode.findByCode(resDto.getBankCode()).getCodeName());
		// 가상계좌 입금예정날짜
		resDto.setValidDate(resultMap.get("validDate") + resultMap.get("validTime"));

		return resDto;
	}

	@Override
	public InicisApprovalResponseDto approval(InicisApprovalRequestDto reqDto) throws Exception {

		InicisConfig config = basicConfig.getInicisConfig(PgEnums.PgAccountType.INICIS_BASIC.getCode());

		InicisApprovalResponseDto resDto = new InicisApprovalResponseDto();

		String timestamp = SignatureUtil.getTimestamp(); // util에 의해서 자동생성
		String charset = "UTF-8"; // 리턴형식[UTF-8,EUC-KR](가맹점 수정후 고정)
		String format = "JSON"; // 리턴형식[XML,JSON,NVP](가맹점 수정후 고정)

		// signature 생성
		Map<String, String> signParam = new HashMap<String, String>();

		signParam.put("authToken", reqDto.getAuthToken()); // 필수
		signParam.put("timestamp", timestamp); // 필수

		// signature 데이터 생성 (모듈에서 자동으로 signParam을 알파벳 순으로 정렬후 NVP 방식으로 나열해 hash)
		String signature = SignatureUtil.makeSignature(signParam);

		// API 요청 전문 생성
		Map<String, String> authMap = new Hashtable<String, String>();

		authMap.put("mid", reqDto.getMid()); // 필수
		authMap.put("authToken", reqDto.getAuthToken()); // 필수
		authMap.put("signature", signature); // 필수
		authMap.put("timestamp", timestamp); // 필수
		authMap.put("charset", charset); // default=UTF-8
		authMap.put("format", format); // default=XML
		authMap.put("price", String.valueOf(reqDto.getPaymentPrice())); // 가격위변조체크기능 (선택사용)

		//HttpUtil httpUtil = new HttpUtil();
		InicisHttpClient httpUtil = new InicisHttpClient();

		try {
			// API 통신 시작
			String authResultString = "";

			authResultString = httpUtil.processHTTP(authMap, reqDto.getAuthUrl());

			Map<String, String> resultMap = ConverthttpResultStringToMap(authResultString);

//			log.info("resultMap == " + resultMap);

			// 결제 보안 강화
			Map<String, String> secureMap = new HashMap<String, String>();
			secureMap.put("mid", reqDto.getMid()); // mid
			secureMap.put("tstamp", timestamp); // timestemp
			secureMap.put("MOID", resultMap.get("MOID")); // MOID
			secureMap.put("TotPrice", resultMap.get("TotPrice")); // TotPrice

			// signature 데이터 생성
			String secureSignature = SignatureUtil.makeSignatureAuth(secureMap);

			resDto.setResultCode(resultMap.get("resultCode"));
			resDto.setResultMsg(resultMap.get("resultMsg"));
			// 결제보안 강화 2016-05-18
			if (InicisCode.SUCCESS_PC.getCode().equals(resultMap.get("resultCode")) && !secureSignature.equals(resultMap.get("authSignature"))) {

				// 결과정보
				resDto.setResultCode("9999");
				resDto.setResultMsg("데이터 위변조 체크 실패");

				// 망취소
				if (InicisCode.SUCCESS_PC.getCode().equals(resultMap.get("resultCode"))) {
					throw new Exception("데이터 위변조 체크 실패");
				}
			}

			// 공통 부분만
			resDto.setTid(resultMap.get("tid"));
			resDto.setPayMethod(resultMap.get("payMethod"));
			resDto.setTotPrice(resultMap.get("TotPrice"));
			resDto.setMoid(resultMap.get("MOID"));
			resDto.setApplDate(resultMap.get("applDate"));
			resDto.setApplTime(resultMap.get("applTime"));

			if ("DirectBank".equals(resultMap.get("payMethod"))) { // 실시간계좌이체
				resDto.setBankCode(resultMap.get("ACCT_BankCode"));
				resDto.setCSHR_ResultCode(resultMap.get("CSHR_ResultCode"));
				resDto.setCSHR_Type(resultMap.get("CSHR_Type"));
			} else if ("iDirectBank".equals(resultMap.get("payMethod"))) { // 실시간계좌이체
				resDto.setBankCode(resultMap.get("ACCT_BankCode"));
				resDto.setCSHR_ResultCode(resultMap.get("CSHR_ResultCode"));
				resDto.setCSHR_Type(resultMap.get("CSHR_Type"));
			} else if ("VBank".equals(resultMap.get("payMethod"))) { // 가상계좌
				resDto.setBankCode(resultMap.get("VACT_BankCode"));
				resDto.setVACT_Num(resultMap.get("VACT_Num"));
				resDto.setVACT_Name(resultMap.get("VACT_Name"));
				resDto.setVACT_Date(resultMap.get("VACT_Date"));
				resDto.setVACT_Time(resultMap.get("VACT_Time"));
				resDto.setVactBankName(resultMap.get("vactBankName"));

			} else {// 카드
				resDto.setCardNum(resultMap.get("CARD_Num"));
				resDto.setApplNum(resultMap.get("applNum"));
				resDto.setCardCode(resultMap.get("CARD_Code"));
				resDto.setCARD_Interest(resultMap.get("CARD_Interest"));
				resDto.setCARD_Quota(resultMap.get("CARD_Quota"));
				resDto.setCARD_PRTC_CODE(resultMap.get("CARD_PRTC_CODE"));
			}
			resDto.setEtcData(reqDto.getMerchantData());
			resDto.setAuthMap(authMap);
			resDto.setNetCancelUrl(reqDto.getNetCancelUrl());
		} catch (Exception ex) {
			// 망취소 요청 API url(고정, 임의 세팅 금지)
			netCancel(reqDto.getNetCancelUrl(), authMap);
			resDto.setResultCode("APPROVAL_MAPPING_ERROR");
			resDto.setResultMsg("PG 승인 데이터 맵핑 실패");
		}
		return resDto;
	}

	public void netCancel(String netCancelUrl, Map<String, String> authMap) {
		try {
			InicisHttpClient httpUtil = new InicisHttpClient();
			String authResultString = httpUtil.processHTTP(authMap, netCancelUrl);
			Map<String, String> resultMap = ConverthttpResultStringToMap(authResultString);
			log.error("====== 이니시스 망취소 요청  => {}", authMap);
			log.error("====== 이니시스 망취소 결과  => {}", resultMap);
		} catch (Exception e) {
			log.error("ERROR ====== 이니시스 망취소 Exception", e);
		}
	}

	public InicisMobileApprovalResponseDto mobileApproval(InicisMobileApprovalRequestDto reqDto) throws Exception {

		InicisConfig config = basicConfig.getInicisConfig(PgEnums.PgAccountType.INICIS_BASIC.getCode());

		InicisMobileApprovalResponseDto resDto = new InicisMobileApprovalResponseDto();

		// API 요청 전문 생성
		Map<String, String> authMap = new Hashtable<String, String>();

		authMap.put("P_MID", config.getMid()); // 필수
		authMap.put("P_TID", reqDto.getP_TID()); // 필수

		//HttpUtil httpUtil = new HttpUtil();
		InicisHttpClient httpUtil = new InicisHttpClient();

		// API 통신 시작
		String authResultString = "";

		authResultString = httpUtil.processHTTP(authMap, reqDto.getP_REQ_URL());

		Map<String, String> resultMap = ConverthttpResultStringToMap(authResultString);

		// 거래상태 "00":성공, 이외 : 실패
		resDto.setP_STATUS(resultMap.get("P_STATUS"));

		// 지불결과메시지
		resDto.setP_RMESG1(resultMap.get("P_RMESG1"));
		// 승인거래번호
		resDto.setP_TID(resultMap.get("P_TID"));
		// 지불수단
		resDto.setP_TYPE(resultMap.get("P_TYPE"));
		// 승인일자
		resDto.setP_AUTH_DT(resultMap.get("P_AUTH_DT"));
		// 상점아이디
		resDto.setP_MID(resultMap.get("P_MID"));
		// 상점 주문번호
		resDto.setP_OID(resultMap.get("P_OID"));
		// 결제금액
		resDto.setP_AMT(resultMap.get("P_AMT"));
		// 주문자명
		resDto.setP_UNAME(resultMap.get("P_UNAME"));
		// 가맹점명
		resDto.setP_MNAME(resultMap.get("P_MNAME"));
		// 주문정보에 입력한 값 반환
		resDto.setEtcData(resultMap.get("P_NOTI"));
		// 가맹점 전달 NOTI_URL
		resDto.setP_NOTEURL(resultMap.get("P_NOTEURL"));
		// 가맹점 전달 NEXT_URL
		resDto.setP_NEXT_URL(resultMap.get("P_NEXT_URL"));

		// 신용카드
		// 발급사 코드
		resDto.setP_CARD_ISSUER_CODE(resultMap.get("P_CARD_ISSUER_CODE"));
		//무이자 할부여부 ["0":일반, "1":무이자]
		resDto.setP_CARD_INTEREST(resultMap.get("P_CARD_INTEREST"));
		// 부분취소 가능여부 ["1":가능 , "0":불가능]
		resDto.setP_CARD_PRTC_CODE(resultMap.get("P_CARD_PRTC_CODE"));
		// 신용카드할부개월
		resDto.setP_RMESG2(resultMap.get("P_RMESG2"));
		// 승인번호
		resDto.setP_AUTH_NO(resultMap.get("P_AUTH_NO"));


		// 앱연동구분
		// 앱연동여부
		resDto.setP_SRC_CODE(resultMap.get("P_SRC_CODE"));

		// 계좌이체 AND 신용카드
		// 은행코드 , 카드코드
		resDto.setP_FN_CD1(resultMap.get("P_FN_CD1"));
		// 결제은행 한글명 , 결제카드한글명
		resDto.setP_FN_NM(resultMap.get("P_FN_NM"));
		// 카드번호
		resDto.setP_CARD_NUM(resultMap.get("P_CARD_NUM"));

		// 가상계좌
		// 입금할 계좌번호
		resDto.setP_VACT_NUM(resultMap.get("P_VACT_NUM"));
		// 입금마감일자 [YYYYMMDD]
		resDto.setP_VACT_DATE(resultMap.get("P_VACT_DATE"));
		// 입금마감시간 [hhmmss]
		resDto.setP_VACT_TIME(resultMap.get("P_VACT_TIME"));
		// 계좌주명
		resDto.setP_VACT_NAME(resultMap.get("P_VACT_NAME"));
		// 은행코드
		resDto.setP_VACT_BANK_CODE(resultMap.get("P_VACT_BANK_CODE"));

		// 현금영수증
		// 결과코드 ["220000":정상, 그외 실패]
		resDto.setP_CSHR_CODE(resultMap.get("P_CSHR_CODE"));
		// 용도구분 ["0":소득공제, "1":지출증빙]
		resDto.setP_CSHR_TYPE(resultMap.get("P_CSHR_TYPE"));
		// 발행 승인번호
		resDto.setP_CSHR_AUTH_NO(resultMap.get("P_CSHR_AUTH_NO"));

		authMap.put("P_AMT", resDto.getP_AMT()); // 결제요청 시 설정한 P_AMT 값
		authMap.put("P_OID", resDto.getP_OID()); // 결제요청 시 설정한 P_OID 값

		resDto.setP_REQ_URL(reqDto.getP_REQ_URL());
		resDto.setNetCancelAuthMap(authMap);
		return resDto;
	}

	public void mobileNetCancel(String pReqUrl, Map<String, String> netCancelAuthMap) {
		try {
			InicisHttpClient httpUtil = new InicisHttpClient();
			// API 통신 시작
			URL url = new URL(pReqUrl);
			String netCancelUrl = url.getProtocol()+"://"+url.getAuthority() + "/smart/payNetCancel.ini";
			String authResultString = httpUtil.processHTTP(netCancelAuthMap, netCancelUrl);
			Map<String, String> resultMap = ConverthttpResultStringToMap(authResultString);
			log.error("====== 이니시스 모바일 망취소 요청  => {}", netCancelAuthMap);
			log.error("====== 이니시스 모바일 망취소 결과  => {}", resultMap);
		} catch (Exception e) {
			log.error("ERROR ====== 이니시스 모바일 망취소 Exception", e);
		}
	}

	@Override
	public CancelResponseDto cancel(String pgAccountType, CancelRequestDto reqDto) throws Exception {

		InicisConfig config = basicConfig.getInicisConfig(pgAccountType);

		CancelResponseDto resDto = new CancelResponseDto();

		// API 요청 전문 생성
		Map<String, String> cancelMap = new Hashtable<String, String>();

		// 지불수단 코드
		if (PaymentType.BANK.equals(reqDto.getPaymentType())) {
			cancelMap.put("paymethod", "Acct");
		} else if (PaymentType.VIRTUAL_BANK.equals(reqDto.getPaymentType())) {
			cancelMap.put("paymethod", "Vacct");
			if(reqDto.isRefundBank()) {
				cancelMap.put("refundAcctNum", aesEncrypt(config, reqDto.getRefundBankNumber()));
				cancelMap.put("refundBankCode", reqDto.getRefundBankCode());
				cancelMap.put("refundAcctName", reqDto.getRefundBankName());
			}
		} else {
			cancelMap.put("paymethod", "Card");
		}
		// 전문생성시간(YYYYMMDDhhmmss)
		cancelMap.put("timestamp", DateUtil.getCurrentDate("yyyyMMddHHmmss"));
		// 가맹점 요청 서버IP (추후 거래 확인 등에 사용됨)
		cancelMap.put("clientIp", reqDto.getIp());
		// 가맹점 ID
		cancelMap.put("mid", config.getMid());
		// 취소요청 TID
		cancelMap.put("tid", reqDto.getTid());
		// 취소요청사유
		cancelMap.put("msg", reqDto.getCancelMessage());

		String hashText = "";
		if (reqDto.isPartial()) {
			cancelMap.put("type", "PartialRefund"); // 고정

			// 취소요청금액
			cancelMap.put("price", String.valueOf(reqDto.getCancelPrice()));
			// 부분취소 후 남은금액
			cancelMap.put("confirmPrice", String.valueOf(reqDto.getExpectedRestPrice()));

			int comm_tax_mny = getCommTaxMny(reqDto.getTaxCancelPrice());
			// 부과세
			cancelMap.put("tax", String.valueOf(reqDto.getTaxCancelPrice() - comm_tax_mny));
			// 비과세
			cancelMap.put("taxFree", String.valueOf(reqDto.getTaxFreecancelPrice()));

			if (PaymentType.VIRTUAL_BANK.equals(reqDto.getPaymentType())) {
				hashText = config.getKey() + cancelMap.get("type") + cancelMap.get("paymethod")
						+ cancelMap.get("timestamp") + cancelMap.get("clientIp") + cancelMap.get("mid")
						+ cancelMap.get("tid") + cancelMap.get("price") + cancelMap.get("confirmPrice")
						+ cancelMap.get("refundAcctNum");
			} else {
				hashText = config.getKey() + cancelMap.get("type") + cancelMap.get("paymethod")
						+ cancelMap.get("timestamp") + cancelMap.get("clientIp") + cancelMap.get("mid")
						+ cancelMap.get("tid") + cancelMap.get("price") + cancelMap.get("confirmPrice");
			}
		} else {
			cancelMap.put("type", "Refund"); // 고정

			if (PaymentType.VIRTUAL_BANK.equals(reqDto.getPaymentType()) && reqDto.isRefundBank()) {
				hashText = config.getKey() + cancelMap.get("type") + cancelMap.get("paymethod")
						+ cancelMap.get("timestamp") + cancelMap.get("clientIp") + cancelMap.get("mid")
						+ cancelMap.get("tid") + cancelMap.get("refundAcctNum");
			} else {
				hashText = config.getKey() + cancelMap.get("type") + cancelMap.get("paymethod")
						+ cancelMap.get("timestamp") + cancelMap.get("clientIp") + cancelMap.get("mid")
						+ cancelMap.get("tid");
			}
		}

		log.info("=============== hashText = {}", hashText);
		// 전문위변조 HASH
		cancelMap.put("hashData", SignatureUtil.hash(hashText, "SHA-512"));

		//HttpUtil httpUtil = new HttpUtil();
		InicisHttpClient httpUtil = new InicisHttpClient();

		// API 통신 시작
		String cancelResultString = "";

		log.debug("------------------------------------------------------이니시스 요청값");
		log.debug(cancelMap.toString());

		cancelResultString = httpUtil.processHTTP(cancelMap, "https://iniapi.inicis.com/api/v1/refund");

		Map<String, String> resultMap = ConverthttpResultStringToMap(cancelResultString);
		log.debug("resultMap :: <{}>", resultMap.toString());

		// 결과 코드
		resDto.setSuccess(InicisCode.SUCCESS_MOBILE.getCode().equals(resultMap.get("resultCode")));
		// 결과 메시지
		resDto.setMessage(resultMap.get("resultMsg"));

		return resDto;
	}

	/**
	 * 신용카드 (비인증) 결제
	 *
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	public InicisNonAuthenticationCartPayResponseDto nonAuthenticationCartPay(
			InicisNonAuthenticationCartPayRequestDto reqDto) throws Exception {

		InicisConfig config = basicConfig.getInicisConfig(PgEnums.PgAccountType.INICIS_ADMIN.getCode());

		InicisNonAuthenticationCartPayResponseDto resDto = new InicisNonAuthenticationCartPayResponseDto();

		// API 요청 전문 생성
		Map<String, String> payMap = new Hashtable<String, String>();

		String timestamp = DateUtil.getCurrentDate("yyyyMMddHHmmss");

		payMap.put("type", "Pay"); // 고정
		payMap.put("paymethod", "Card"); // 고정
		payMap.put("timestamp", timestamp); // 필수
		payMap.put("clientIp", reqDto.getIp()); // 필수 가맹점 요청 서버IP (추후 거래 확인 등에 사용됨)
		payMap.put("mid", config.getMid()); // 필수 가맹점 ID
		payMap.put("url", config.getAppDomain()); // 필수 가맹점 URL
		payMap.put("moid", reqDto.getOdid()); // 필수 가맹점주문번호
		payMap.put("goodName", reqDto.getGoodsName()); // 필수 상품명
		payMap.put("price", String.valueOf(reqDto.getPaymentPrice())); // 필수 결제금액
		int comm_tax_mny = getCommTaxMny(reqDto.getTaxPaymentPrice());
		payMap.put("tax", String.valueOf(reqDto.getTaxPaymentPrice() - comm_tax_mny));// 선택 부과세
		payMap.put("taxFree", String.valueOf(reqDto.getTaxFreePaymentPrice())); // 선택 비과세
		payMap.put("currency", "WON"); // 선택 통화코드(WON/USD)
		payMap.put("buyerName", reqDto.getBuyerName()); // 필수 구매자명
		payMap.put("buyerEmail", StringUtil.nvl(reqDto.getBuyerEmail(), Constants.NOREPLY_EMAIL)); // 필수 구매자이메일
		payMap.put("buyerTel", reqDto.getBuyerMobile()); // 선택 구매자 연락처
//		payMap.put("quotaInterest", ""); //선택 무이자구분
		payMap.put("cardQuota", reqDto.getQuota()); // 필수 할부기간(일시불 : 00 / 그 외 : 02, 03 ....)
		payMap.put("cardNumber", aesEncrypt(config, reqDto.getCardNumber())); // 필수 카드번호
		payMap.put("cardExpire", aesEncrypt(config, reqDto.getCardExpire())); // 필수 카드유효기간(YYMM)
		payMap.put("regNo", aesEncrypt(config, reqDto.getRegNo())); // 선택 생년월일(YYMMDD)/사업자번호
		payMap.put("cardPw", aesEncrypt(config, reqDto.getCardPw())); // 선택 카드비밀번호 앞 2자리
		payMap.put("authentification", "99"); // 본인인증 안함 가맹점으로 별도계약된 경우 '99'로 세팅 / 이외는 '00' 고정
//		payMap.put("cardPoint", ""); //선택 카드포인트 사용유무
//		payMap.put("language", ""); //선택 언어설정(eng)
		payMap.put("hashData",
				SignatureUtil.hash(config.getKey() + payMap.get("type") + payMap.get("paymethod")
						+ payMap.get("timestamp") + payMap.get("clientIp") + payMap.get("mid") + payMap.get("moid")
						+ payMap.get("price") + payMap.get("cardNumber"), "SHA-512")); // 필수 전문위변조 HASH

		//HttpUtil httpUtil = new HttpUtil();
		InicisHttpClient httpUtil = new InicisHttpClient();

		// API 통신 시작
		String payResultString = "";

		payResultString = httpUtil.processHTTP(payMap, "https://iniapi.inicis.com/api/v1/formpay");

		Map<String, String> resultMap = ConverthttpResultStringToMap(payResultString);

		// 결과 코드
		resDto.setSuccess(InicisCode.SUCCESS_MOBILE.getCode().equals(resultMap.get("resultCode")));
		// 결과 메시지
		resDto.setMessage(resultMap.get("resultMsg"));
		// 승인일자
		resDto.setTid(resultMap.get("tid"));
		// 결제일자
		resDto.setPayDate(resultMap.get("payDate"));
		// 결제시간
		resDto.setPayTime(resultMap.get("payTime"));
		// 결제인증코드
		resDto.setPayAuthCode(resultMap.get("payAuthCode"));
		// 카드코드
		resDto.setCardCode(resultMap.get("cardCode"));
		// 카드종류구분
		resDto.setCheckFlg(resultMap.get("checkFlg"));
		// 할부기간
		resDto.setPayAuthQuota(resultMap.get("payAuthQuota"));
		// 결제금액
		resDto.setPrice(resultMap.get("price"));
		// 부분환불 가능여부(1 - 가능 / 0 - 불가능)
		resDto.setPrtcCode(resultMap.get("prtcCode"));

		return resDto;
	}

	@Override
	public ReceiptIssueResponseDto receiptIssue(ReceiptIssueRequestDto reqDto) throws Exception {

		InicisConfig config = basicConfig.getInicisConfig(PgEnums.PgAccountType.INICIS_BASIC.getCode());

		ReceiptIssueResponseDto resDto = new ReceiptIssueResponseDto();

		// API 요청 전문 생성
		Map<String, String> payMap = new Hashtable<String, String>();

		String timestamp = DateUtil.getCurrentDate("yyyyMMddHHmmss");

		payMap.put("type", "Issue"); // 고정
		payMap.put("paymethod", "Receipt"); // 고정
		payMap.put("timestamp", timestamp); // 필수
		payMap.put("clientIp", reqDto.getIp()); // 필수 가맹점 요청 서버IP (추후 거래 확인 등에 사용됨)
		payMap.put("mid", config.getMid()); // 필수 가맹점 ID
		payMap.put("goodName", reqDto.getGoodsName()); // 필수 상품명
		payMap.put("currency", "WON"); // 선택 통화코드(WON/USD)
		payMap.put("buyerName", reqDto.getBuyerName()); // 필수 구매자명
		payMap.put("buyerEmail", reqDto.getBuyerEmail()); // 필수 구매자이메일
		payMap.put("buyerTel", reqDto.getBuyerMobile()); // 선택 구매자 연락처
		payMap.put("crPrice", String.valueOf(reqDto.getTotalPrice())); // 필수 결제금액
		payMap.put("supPrice", String.valueOf(reqDto.getSupPrice()));// 필수 공급가액
		payMap.put("tax", String.valueOf(reqDto.getTax())); // 필수 부가세
		payMap.put("srcvPrice", String.valueOf(reqDto.getSrcvPrice())); // 필수 봉사료
		payMap.put("regNum", aesEncrypt(config, reqDto.getRegNumber())); // 필수 현금영수증 식별번호(주민번호,휴대폰번호,사업자번호)
		// 필수 현금영수증 발행용도(0:소득공제용, 1:지출증빙)
		payMap.put("useOpt", (reqDto.getReceiptType().equals(CashReceipt.PROOF) ? "1" : "0"));
//		payMap.put("compayNumber", ""); //선택 서브몰사업자번호 (서브몰가맹점 등록 요청 후 사용 가능합니다.)
		// 필수 전문위변조 HASH
		payMap.put("hashData",
				SignatureUtil.hash(config.getKey() + payMap.get("type") + payMap.get("paymethod")
						+ payMap.get("timestamp") + payMap.get("clientIp") + payMap.get("mid") + payMap.get("crPrice")
						+ payMap.get("supPrice") + payMap.get("srcvPrice") + payMap.get("regNum"), "SHA-512"));

		//HttpUtil httpUtil = new HttpUtil();
		InicisHttpClient httpUtil = new InicisHttpClient();

		// API 통신 시작
		String payResultString = "";

		payResultString = httpUtil.processHTTP(payMap, "https://iniapi.inicis.com/api/v1/receipt");

		Map<String, String> resultMap = ConverthttpResultStringToMap(payResultString);

		// 결과 코드
		resDto.setSuccess(InicisCode.SUCCESS_MOBILE.getCode().equals(resultMap.get("resultCode")));
		// 결과 메시지
		resDto.setMessage(resultMap.get("resultMsg"));
		// 현금영수증발급 거래번호 TID
		resDto.setTid(resultMap.get("tid"));
		// 현금영수증 승인번호
		resDto.setReceiptNo(resultMap.get("authNo"));
		// 승인/발급 시간
		resDto.setAuthDateTime(resultMap.get("authDate") + resultMap.get("authTime"));

		return resDto;
	}

	@Override
	public ReceiptCancelResponseDto receiptCancel(String tid) throws Exception {
		return receiptCancel(tid, DeviceUtil.getServerIp());
	}

	@Override
	public ReceiptCancelResponseDto receiptCancel(String tid, String ip) throws Exception {

		InicisConfig config = basicConfig.getInicisConfig(PgEnums.PgAccountType.INICIS_BASIC.getCode());

		ReceiptCancelResponseDto resDto = new ReceiptCancelResponseDto();

		// API 요청 전문 생성
		Map<String, String> cancelMap = new Hashtable<String, String>();

		cancelMap.put("type", "Refund"); // 고정
		// 지불수단 코드
		cancelMap.put("paymethod", "Receipt"); // 고정

		// 전문생성시간(YYYYMMDDhhmmss)
		cancelMap.put("timestamp", DateUtil.getCurrentDate("yyyyMMddHHmmss"));
		// 가맹점 요청 서버IP (추후 거래 확인 등에 사용됨)
		cancelMap.put("clientIp", ip);
		// 가맹점 ID
		cancelMap.put("mid", config.getMid());
		// 취소요청 TID
		cancelMap.put("tid", tid);
		// 취소요청사유
		cancelMap.put("msg", "현금영수증취소");

		// 전문위변조 HASH
		cancelMap.put("hashData",
				SignatureUtil.hash(config.getKey() + cancelMap.get("type") + cancelMap.get("paymethod")
						+ cancelMap.get("timestamp") + cancelMap.get("clientIp") + cancelMap.get("mid")
						+ cancelMap.get("tid"), "SHA-512"));

		//HttpUtil httpUtil = new HttpUtil();
		InicisHttpClient httpUtil = new InicisHttpClient();

		// API 통신 시작
		String cancelResultString = "";

		cancelResultString = httpUtil.processHTTP(cancelMap, "https://iniapi.inicis.com/api/v1/refund");

		Map<String, String> resultMap = ConverthttpResultStringToMap(cancelResultString);

		// 결과 코드
		resDto.setSuccess(InicisCode.SUCCESS_MOBILE.getCode().equals(resultMap.get("resultCode")));
		// 결과 메시지
		resDto.setMessage(resultMap.get("resultMsg"));

		return resDto;
	}

	@Override
	public boolean escrowRegistDelivery(EscrowRegistDeliveryDataRequestDto reqDto) throws Exception {
		InicisConfig config = basicConfig.getInicisConfig(PgEnums.PgAccountType.INICIS_BASIC.getCode());

		// API 요청 전문 생성
		Map<String, String> map = new Hashtable<String, String>();

		map.put("type", "Dlv"); // 고정
		map.put("mid", config.getMid()); // 가맹점 ID
		map.put("clientIp", reqDto.getIp()); // 가맹점 요청 서버IP (추후 거래 확인 등에 사용됨)
		map.put("timestamp", DateUtil.getCurrentDate("yyyyMMddHHmmss")); // 전문생성시간(YYYYMMDDhhmmss)
		map.put("tid", reqDto.getTid()); // 에스크로 결제 승인TID
		map.put("oid", reqDto.getOdid()); // 주문번호
		map.put("price", String.valueOf(reqDto.getPaymentPrice())); // 결제금액
		map.put("report", "I"); // 에스크로 등록형태 ["I":등록, "U":변경]
		map.put("invoice", reqDto.getTrackingNo()); // 운송장번호
		map.put("registName", reqDto.getRegistTrakingNoUserName()); // 배송등록자
		map.put("exCode", reqDto.getInicisSshippingCompanyCode()); // 택배사코드
		map.put("exName", reqDto.getShippingCompanyName()); // 택배사명
		map.put("charge", "BH"); // 배송비 지급형태 ("SH":판매자부담, "BH":구매자부담)
		map.put("invoiceDay", reqDto.getRegistTrackingNoDate().toString()); // 배송등록 확인일자
		map.put("sendName", reqDto.getSendName()); // 송신자 이름
		map.put("sendTel", reqDto.getSendTel()); // 송신자 전화번호
		map.put("sendPost", reqDto.getSendZipCode()); // 송신자 우편번호
		map.put("sendAddr1", reqDto.getSendAddress1()); // 송신자 주소 1
		map.put("recvName", reqDto.getReceiverName()); // 수신자 이름
		map.put("recvTel", reqDto.getReceiverMobile()); // 수신자 전화번호
		map.put("recvPost", reqDto.getReceiverZipCode()); // 수신자 우편번호(구분자 없이)
		map.put("recvAddr", reqDto.getReceiverAddress1()); // 수신자 주소 1

		// 전문위변조 HASH
		map.put("hashData", SignatureUtil.hash(config.getKey() + map.get("type") + map.get("timestamp")
		+ map.get("clientIp") + map.get("mid") + map.get("oid") + map.get("tid") + map.get("price"), "SHA-512"));

		//HttpUtil httpUtil = new HttpUtil();
		InicisHttpClient httpUtil = new InicisHttpClient();

		// API 통신 시작
		String cancelResultString = "";

		cancelResultString = httpUtil.processHTTP(map, "https://iniapi.inicis.com/api/v1/escrow");

		Map<String, String> resultMap = ConverthttpResultStringToMap(cancelResultString);

		System.out.println("홍진영 ====================="+resultMap.get("resultMsg"));

		return InicisCode.SUCCESS_MOBILE.getCode().equals(resultMap.get("resultCode"));
	}

	private int getCommTaxMny(int price) {
		return Math.round(Float.valueOf(price) / new Float(1.1));
	}

	private Map<String, String> ConverthttpResultStringToMap(String sesultString) throws Exception {
		// API 통신결과 처리(***가맹점 개발수정***)
		String test = sesultString.replace(",", "&").replace(":", "=").replace("\"", "").replace(" ", "")
				.replace("\n", "").replace("}", "").replace("{", "");

		return ParseUtil.parseStringToMap(test); // 문자열을 MAP형식으로 파싱
	}

	private String aesEncrypt(InicisConfig config, String plaintext) throws Exception {
		// Get Cipher Instance
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

		// Create SecretKeySpec
		SecretKeySpec keySpec = new SecretKeySpec(config.getKey().getBytes(), "AES");

		// Create IvParameterSpec
		IvParameterSpec ivSpec = new IvParameterSpec(config.getIv().getBytes());

		// Initialize Cipher for ENCRYPT_MODE
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

		// Perform Encryption
		byte[] cipherText = cipher.doFinal(plaintext.getBytes());

		return new String(Base64.encodeBase64(cipherText));
	}
}
