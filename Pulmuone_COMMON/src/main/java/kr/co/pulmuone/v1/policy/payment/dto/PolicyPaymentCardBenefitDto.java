package kr.co.pulmuone.v1.policy.payment.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.policy.payment.dto.vo.PolicyPaymentCardBenefitVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "신용카드혜택안내 Dto")
public class PolicyPaymentCardBenefitDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "신용카드혜택안내 조회 리스트")
	private	List<PolicyPaymentCardBenefitVo> rows;

	@ApiModelProperty(value = "신용카드혜택안내 조회 카운트")
	private	int	total;

	@ApiModelProperty(value = "신용카드혜택안내.SEQ")
	private String psCardBenefitId;

	@ApiModelProperty(value = "신용카드혜택안내.PG 코드 공통코드(PG_SERVICE)")
	private String psPgCd;

	@ApiModelProperty(value = "신용카드혜택안내.안내유형 공통코드(CARD_BENEFIT_INFO_TP)")
	private String cardBenefitTp;

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

	@ApiModelProperty(value = "제휴구분")
	private String psPayCode;

}
