package kr.co.pulmuone.v1.promotion.notissue.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingLoginId;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "PointNotIssueListVo")
public class PointNotIssueListVo {

	@ApiModelProperty(value = "회원명")
	@UserMaskingUserName
	private String userNm;

	@ApiModelProperty(value = "회원 ID")
	@UserMaskingLoginId
	private String loginId;

	@ApiModelProperty(value = "회원 고유 ID")
	private String urUserId;

	@ApiModelProperty(value = "지급구분")
	private String pointProcessTp;

	@ApiModelProperty(value = "지급구분 명")
	private String pointProcessTpName;

	@ApiModelProperty(value = "정산유형")
	private String pointSettlementTp;

	@ApiModelProperty(value = "정산유형 명")
	private String pointSettlementTpName;

	@ApiModelProperty(value = "잔여 적립금")
	private String sumAmount;

	@ApiModelProperty(value = "미지급 금액")
	private String issueVal;

	@ApiModelProperty(value = "부분지급 금액")
	private String partPointVal;

	@ApiModelProperty(value = "잔여지급 금액")
	private String redepositPointVal;

	@ApiModelProperty(value = "등록일자")
	private String createDt;

	@ApiModelProperty(value = "적립일")
	private String depositDt;

	@ApiModelProperty(value = "소멸일")
	private String expirationDt;

	@ApiModelProperty(value = "적립금 미지금 내역 ID")
	private Long pmPointNotIssueId;

	@ApiModelProperty(value = "최대 적립 적립금")
	private int maxPoint;

	@ApiModelProperty(value = "분담조직")
	private String organizationNm;


}
