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
@ApiModel(description = "PG 결제수단 관리 VO")
public class PolicyPaymentGatewayMethodVo extends BaseRequestDto{

	@ApiModelProperty(value = "PG 결제수단.PG 결제수단_고유값")
	private String psPaymentMethodId;

	@ApiModelProperty(value = "PG사 설정 정보.이중화비율.SEQ")
	private String psPgPayId;

	@ApiModelProperty(value = "PG 결제수단.PG사 은행/신용카드 코드")
	private String pgBankCd;

	@ApiModelProperty(value = "PG 결제수단.은행/신용카드사명 공통코드(CARD_CODE)")
	private String bankNmCd;

	@ApiModelProperty(value = "PG 결제수단.은행/신용카드사명 공통코드명(CARD_CODE)")
	private String bankNmCdNm;

	@ApiModelProperty(value = "PG 결제수단.사용여부")
	private String useYn;

	@ApiModelProperty(value = "PG 결제수단.사용여부 KCP")
	private String useYnKcp;

	@ApiModelProperty(value = "PG 결제수단.사용여부 이니시스")
	private String useYnInicis;

	@ApiModelProperty(value = "PG 결제수단.PG 결제수단_고유값 KCP")
	private String psPaymentMethodIdKcp;

	@ApiModelProperty(value = "PG 결제수단.PG 결제수단_고유값 이니시스")
	private String psPaymentMethodIdInicis;

	@ApiModelProperty(value = "등록자ID")
	private String createId;

	@ApiModelProperty(value = "등록일시")
	private String createDt;

	@ApiModelProperty(value = "수정자ID")
	private String modifyId;

	@ApiModelProperty(value = "수정일시")
	private String modifyDt;
}
