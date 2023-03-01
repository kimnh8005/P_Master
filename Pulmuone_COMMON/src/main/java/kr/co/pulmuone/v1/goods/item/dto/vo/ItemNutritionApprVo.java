package kr.co.pulmuone.v1.goods.item.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemNutritionApprVo {

	private String ilItemNutritionId;

	private String ilItemApprId;

	private String ilItemCd; // 품목코드 PK

    private String nutritionCode; // 영양정보 분류코드 PK

    private boolean canDeleted; // 삭제 가능 여부 : 최초 항목 생성시 ERP 관련 데이터 있는 경우 false ( 삭제 불가능 )

	private Double erpNutritionQuantity; // ERP 영양성분량

	private Double erpNutritionPercent; // ERP 영양성분 기준치대비 함량

	private Double nutritionQuantity; // BOS 영양성분량

	private Double nutritionPercent; // BOS 영양성분 기준치대비 함량

	private int sort; // 정렬순서

	private String createId; // 등록자 ID

	private String modifyId; // 수정자 ID

}
