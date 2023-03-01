package kr.co.pulmuone.v1.promotion.point.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "PointUserGradeVo")
public class PointUserGradeVo {

	@ApiModelProperty(value = "일반후기 적립금 금액")
	private int normalAmount;

	@ApiModelProperty(value = "포토후기 적립금 금액")
	private int photoAmount;

	@ApiModelProperty(value = "프리미엄 후기 적립금 금액")
	private int premiumAmount;

	@ApiModelProperty(value = "일반후기 적립금 제한일")
	private String normalValidityDay;

	@ApiModelProperty(value = "포토후기 적립금 제한일")
	private String photoValidityDay;

	@ApiModelProperty(value = "프리미엄 후기 적립금 제한일")
	private String premiumValidityDay;

	@ApiModelProperty(value = "적립금 설정 Id")
	private String pmPointId;

	@ApiModelProperty(value = "적립금 회원등급설정 타입")
	private String reviewType;

	@ApiModelProperty(value = "적립금 회원등급설정 타입명")
	private String reviewTypeName;

	@ApiModelProperty(value = "회원등급 세부 타입")
	private String reviewDetailType;

	@ApiModelProperty(value = "회원등급명")
	private String userGradeName;

	@ApiModelProperty(value = "회원그룹명")
	private String userGroupName;

	@ApiModelProperty(value = "사용자 Login Id")
	private String userId;

	@ApiModelProperty(value = "적립금 상태")
	private String pointStatus;

	@ApiModelProperty(value = "회원그룹마스터 ID")
	private long userMaster;

	@ApiModelProperty(value = "복사타입")
	private String copyType;

}
