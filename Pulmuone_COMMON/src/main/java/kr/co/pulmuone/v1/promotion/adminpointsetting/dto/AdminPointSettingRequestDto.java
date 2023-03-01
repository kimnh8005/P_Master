package kr.co.pulmuone.v1.promotion.adminpointsetting.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "관리자 지급한도 설정 목록 조회 RequestDto")
public class AdminPointSettingRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "관리자 적립금 사용설정 ID")
	private String pmPointAdminSettingId;

	@ApiModelProperty(value = "관리자 ID")
	private String grantAuthEmployeeNumber;

	@ApiModelProperty(value = "역할그룹 Id")
	private String roleGroup;

	@ApiModelProperty(value = "수정일자 시작일")
	private String startCreateDate;

	@ApiModelProperty(value = "수정일자 종료일")
	private String endCreateDate;

	@ApiModelProperty(value = "적립금 설정 금액")
	private String amount;

	@ApiModelProperty(value = "기본유효기간 설정일")
	private String validityDay;

	@ApiModelProperty(value = "관리자별 적립금 설정 금액")
	private String amountIndividual;

	@ApiModelProperty(value = "관리자별 설정")
	private String settingType;

	@ApiModelProperty(value = "삭제 구분")
	private String delYn;

	@ApiModelProperty(value = "사용자 ID")
	private String urUserId;

	@ApiModelProperty(value = "등록자 ID")
	private String createUserId;

}
