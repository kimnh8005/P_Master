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
@ApiModel(description = "신용카드혜택안내 VO")
public class PolicyPaymentCardBenefitVo extends BaseRequestDto{

	@ApiModelProperty(value = "신용카드혜택안내.SEQ")
	private String psCardBenefitId;

	@ApiModelProperty(value = "신용카드혜택안내.PG 코드 공통코드(PG_SERVICE)")
	private String psPgCd;

	@ApiModelProperty(value = "신용카드혜택안내.진행여부상태")
	private String benefitStatus;

	@ApiModelProperty(value = "신용카드혜택안내.PG 코드 공통코드명(PG_SERVICE)")
	private String pgName;

	@ApiModelProperty(value = "신용카드혜택안내.안내유형 공통코드(CARD_BENEFIT_INFO_TP)")
	private String cardBenefitTp;

	@ApiModelProperty(value = "신용카드혜택안내.안내유형 공통코드명(CARD_BENEFIT_INFO_TP)")
	private String cardBenefitTpName;

	@ApiModelProperty(value = "신용카드혜택안내.시작일")
	private String startDt;

	@ApiModelProperty(value = "신용카드혜택안내.종료일")
	private String endDt;

	@ApiModelProperty(value = "신용카드혜택안내.제목")
	private String title;

	@ApiModelProperty(value = "신용카드혜택안내.본문")
	private String information;

	@ApiModelProperty(value = "등록자ID")
	private String createId;

	@ApiModelProperty(value = "등록일시")
	private String createDt;

	@ApiModelProperty(value = "수정자ID")
	private String modifyId;

	@ApiModelProperty(value = "수정일시")
	private String modifyDt;
}
