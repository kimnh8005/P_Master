package kr.co.pulmuone.v1.goods.item.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "식단 패턴 MealPatternListVo")
public class MealPatternListVo {

    @ApiModelProperty(value = "식단패턴기본정보 코드")
    private String patternCd;

	@ApiModelProperty(value = "식단패턴 명")
    private String patternNm;

	@ApiModelProperty(value = "식단분류 명(몰 구분 : MALL_DIV.BABYMEAL, MALL_DIV.EATSLIM)")
    private String mallDivNm;

	@ApiModelProperty(value = "식단분류 코드(몰 구분 : MALL_DIV.BABYMEAL, MALL_DIV.EATSLIM)")
    private String mallDiv;

	@ApiModelProperty(value = "패턴 시작일")
    private String patternStartDt;

	@ApiModelProperty(value = "패턴 종료일")
    private String patternEndDt;

	@ApiModelProperty(value = "등록자 ID")
    private long createId;

	@ApiModelProperty(value = "등록일")
    private String createDt;
	
	@ApiModelProperty(value = "등록자 명")
    private String createNm;
	
	@ApiModelProperty(value = "등록자 로그인ID")
    private String createLoginId;

	@ApiModelProperty(value = "수정자 ID")
    private long modifyId;

	@ApiModelProperty(value = "수정일")
    private String modifyDt;
	
	@ApiModelProperty(value = "수정자 명")
    private String modifyNm;
	
	@ApiModelProperty(value = "수정자 로그인ID")
    private String modifyLoginId;
	
	@ApiModelProperty(value = "연결상품 정보 (상품아이디, 상품명)")
    private String ilGoodsIdInfo;

    @ApiModelProperty(value = "상품아이디")
    private String ilGoodsId;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;
  
}
