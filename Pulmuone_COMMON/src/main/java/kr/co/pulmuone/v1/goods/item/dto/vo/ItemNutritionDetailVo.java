package kr.co.pulmuone.v1.goods.item.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@ApiModel(description = "품목 영양정보 세부항목")
public class ItemNutritionDetailVo {

    /*
     * IL_NUTRITION 테이블에서 조회된 영양정보 세부항목 VO
     */

	@ApiModelProperty(value="품목코드 PK")
    private String ilItemCode;

	@ApiModelProperty(value="영양정보 분류코드 PK")
    private String nutritionCode;

	@ApiModelProperty(value="삭제 가능 여부 : 최초 항목 생성시 ERP 관련 데이터 있는 경우 false ( 삭제 불가능 )")
    private boolean canDeleted;

	@ApiModelProperty(value="ERP 영양성분량")
    private Double erpNutritionQuantity;

	@ApiModelProperty(value="ERP 영양성분 기준치대비 함량")
    private Double erpNutritionPercent;

	@ApiModelProperty(value="BOS 영양성분량")
    private Double nutritionQuantity;

	@ApiModelProperty(value="BOS 영양성분 기준치대비 함량")
    private Double nutritionPercent;

	@ApiModelProperty(value="정렬순서")
    private int sort;

	@ApiModelProperty(value="등록자 ID")
    private Long createId;

	@ApiModelProperty(value="수정자 ID")
    private Long modifyId;

	@ApiModelProperty(value="영양성분명")
	private String nutritionName;

	@ApiModelProperty(value="영양성분단위")
	private String nutritionUnit;

	@ApiModelProperty(value="영양소 기준치 사용여부(Y:사용)")
	private boolean nutritionPercentYn;

	@ApiModelProperty(value="영양성분 수정여부")
	private boolean changedNutritionCode;

}
