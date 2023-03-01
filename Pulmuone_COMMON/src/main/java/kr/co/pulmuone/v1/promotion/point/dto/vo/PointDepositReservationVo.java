package kr.co.pulmuone.v1.promotion.point.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.PointEnums;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "PointVo 포인트 설정")
public class PointDepositReservationVo {

	@ApiModelProperty(value = "적립금 예약 적립 고유값")
	private Long depositReservationId;

	@ApiModelProperty(value = "적립금 설정 고유값")
	private Long pmPointId;

	@ApiModelProperty(value = "후기 적립금 유형 설정 고유값")
	private Long pmPointUserGradeId;

	@ApiModelProperty(value = "회원 고유번호")
	private Long urUserId;

	@ApiModelProperty(value = "적립/차감액")
	private Long amount;

	@ApiModelProperty(value = "포인트 적립일")
	private String depositDt;

	@ApiModelProperty(value = "포인트 만료일")
	private String expirationDt;

	@ApiModelProperty(value = "포인 상세 유형")
	private PointEnums.PointProcessType pointProcessTp;

	@ApiModelProperty(value = "적립금 정산 유형")
	private PointEnums.PointSettlementType pointSettlementTp;

	@ApiModelProperty(value = "적립금 정산 법인 코드")
	private String issueDeptCd;

	@ApiModelProperty(value = "적립금 설정 사용 여부")
	private String useYn;
}
