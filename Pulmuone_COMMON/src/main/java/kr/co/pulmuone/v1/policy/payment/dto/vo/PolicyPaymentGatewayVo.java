package kr.co.pulmuone.v1.policy.payment.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "PG사 설정 정보 VO")
public class PolicyPaymentGatewayVo extends BaseRequestDto {

	@ApiModelProperty(value = "PG사 설정 정보.이중화비율.SEQ")
	private String psPgPayId;

	@ApiModelProperty(value = "PG사 설정 정보.PG 코드 공통코드(PG_SERVICE)")
	private String psPgCd;

	@ApiModelProperty(value = "PG사 설정 정보.PG 코드 공통코드명(PG_SERVICE)")
	private String pgName;

	@ApiModelProperty(value = "PG사 설정 정보.가맹점로고")
	private String pgLogo;

	@ApiModelProperty(value = "PG사 설정 정보.가맹점사이트코드")
	private String pgSiteCode;

	@ApiModelProperty(value = "PG사 설정 정보.가맹점사이트키")
	private String pgSiteKey;

	@ApiModelProperty(value = "PG사 설정 정보.배치결제정보")
	private String pgBatchPayInfo;

	@ApiModelProperty(value = "PG사 설정 정보.무통장결제 입금예정 기한설정 Day")
	private String depositScheduled;

	@ApiModelProperty(value = "PG사 설정 정보.무통장결제 자동입금확인 URL")
	private String automaticDepositUrl;

	@ApiModelProperty(value = "PG사 설정 정보.PG 결제방법 공통코드(PAY_TP)")
	private String psPayCd;

	@ApiModelProperty(value = "PG사 설정 정보.PG 결제방법 공통코드명(PAY_TP)")
	private String psPayCdName;

	@ApiModelProperty(value = "PG사 설정 정보.이중화 비율")
	private String useRatio;

	@ApiModelProperty(value = "PG사 설정 정보.이중화 비율 KCP")
	private String useRatioKcp;

	@ApiModelProperty(value = "PG사 설정 정보.이중화 비율 이니시스")
	private String useRatioInicis;

	@ApiModelProperty(value = "등록자ID")
	private String createId;

	@ApiModelProperty(value = "등록일시")
	private String createDt;

	@ApiModelProperty(value = "수정자ID")
	private String modifyId;

	@ApiModelProperty(value = "수정일시")
	private String modifyDt;

	@ApiModelProperty(value = "PG 결제수단.은행/신용카드사명 공통코드(CARD_CODE)")
	private String bankNmCd;

	@ApiModelProperty(value = "PG 결제수단.은행/신용카드사명 공통코드명(CARD_CODE)")
	private String bankNmCdNm;
}
