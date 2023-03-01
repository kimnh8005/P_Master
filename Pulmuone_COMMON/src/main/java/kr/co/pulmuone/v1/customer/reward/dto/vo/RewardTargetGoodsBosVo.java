package kr.co.pulmuone.v1.customer.reward.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
* <PRE>
* Forbiz Korea
* 고객 보상제 적용대상 상품 VO
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2021-06-24              최윤지         최초작성
* =======================================================================
* </PRE>
*/

@Getter
@Setter
@ApiModel(description = "고객 보상제 적용대상 상품 VO")
public class RewardTargetGoodsBosVo {

    @ApiModelProperty(value = "고객보상제 적용대상 상품 PK")
    private String csRewardTargetGoodsId;

	@ApiModelProperty(value = "고객보상제 관리 PK")
    private String csRewardId;

	@ApiModelProperty(value = "상품 pk")
    private Long ilGoodsId;

	@ApiModelProperty(value = "순서")
    private int goodsSort;

	@ApiModelProperty(value = "상품유형")
    private String goodsTpNm;

	@ApiModelProperty(value = "상품명")
    private String goodsNm;

	@ApiModelProperty(value = "상품 이미지 경로")
    private String goodsImagePath;

	@ApiModelProperty(value = "판매상태")
    private String saleStatusNm;

}
