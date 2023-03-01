package kr.co.pulmuone.v1.promotion.coupon.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetCoverageVo")
public class CoverageVo {

	@ApiModelProperty(value = "적용범위 구분")
	private String includeYn;

	@ApiModelProperty(value = "적용범위 항목")
	private String coverageId;

	@ApiModelProperty(value = "쿠폰ID")
	private String pmCouponId;

	@ApiModelProperty(value = "적용범위")
	private String coverageType;

	@ApiModelProperty(value = "사용자 ID")
	private String userId;

	@ApiModelProperty(value = "적용범위 명")
	private String coverageName;

	@ApiModelProperty(value = "쿠폰 상태")
	private String apprStat;

	@ApiModelProperty(value = "판매 상태")
	private String goodsDisplayYn;

	@ApiModelProperty(value = "전시 상태")
	private String saleStatusCodeName;

	@ApiModelProperty(value = "전시카테고리명")
	private String ilCategoryName;


}

