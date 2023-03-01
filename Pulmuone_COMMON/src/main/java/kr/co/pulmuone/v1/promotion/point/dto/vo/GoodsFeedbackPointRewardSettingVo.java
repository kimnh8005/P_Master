package kr.co.pulmuone.v1.promotion.point.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.PointEnums;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@ApiModel(description = "GoodsFeedbackPointRewardSettingVo 상품 후기 포인트 설정")
public class GoodsFeedbackPointRewardSettingVo {

	@ApiModelProperty(value = "적립금 회원등급 고유값")
	private Long pmPointUserGradeId;

	@ApiModelProperty(value = "적립금 고유값")
	private Long pmPointId;

	@ApiModelProperty(value = "적립금 처리 유형")
	private PointEnums.PointProcessType pointProcessTp;

	@ApiModelProperty(value = "적립금 정산 유형")
	private PointEnums.PointSettlementType pointSettlementTp;

	@ApiModelProperty(value = "일반 후기 적립금")
	private Long nomalAmount;

	@ApiModelProperty(value = "포토 후기 적립금")
	private Long photoAmount;

	@ApiModelProperty(value = "프리미엄 후기 적립금")
	private Long premiumAmount;

	@ApiModelProperty(value = "일반 후기 유효일")
	private Long nomalValidityDay;

	@ApiModelProperty(value = "포토 후기 유효일")
	private Long photoValidityDay;

	@ApiModelProperty(value = "프리미엄 후기 유효일")
	private Long premiumValidityDay;

	@ApiModelProperty(value = "지급 기준일")
	private int paymentStandardDetlVal;

	@ApiModelProperty(value = "적립금 발급 부서 코드")
	private String issueDeptCd;

}
