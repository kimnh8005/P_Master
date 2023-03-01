package kr.co.pulmuone.v1.goods.goods.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ExhibitGiftResultVo")
public class ExhibitGiftResultVo
{
	/**
	 * 증정행사 관련
	 **/
	@ApiModelProperty(value = "기획전 증정대상 상품 ID or 기획전 증정대상 브랜드 ID")
	private String evExhibitGiftTargetId;

	@ApiModelProperty(value = "적용대상 유형")
	private String giftTargetType;

	@ApiModelProperty(value = "기획전 ID")
	private String evExhibitId;

	@ApiModelProperty(value = "기획전결제상태")
	private String exhibitStatus;

	@ApiModelProperty(value = "증정조건 유형")
	private String giftType;

	@ApiModelProperty(value = "증정조건 유형명")
	private String giftTypeName;

	@ApiModelProperty(value = "증정행사 제목")
	private String title;

	@ApiModelProperty(value = "진행기간 시작일시")
	private String startDate;

	@ApiModelProperty(value = "진행기간 종료일시")
	private String endDate;

	@ApiModelProperty(value = "상시진행 여부")
	private String alwaysYn;
}
