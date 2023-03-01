package kr.co.pulmuone.v1.goods.stock.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "StockExprRequestDto")
public class StockExprRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "품목 출고처 PK")
	private long ilItemWarehouseId;

	@ApiModelProperty(value = "품목 출고처 PK(팝업)")
	private long popIlItemWarehouseId;

	@ApiModelProperty(value = "미연동 재고 수량(팝업)")
	private long popStockCnt;

	@ApiModelProperty(value = "재고미연동시 재고무제한 사용여부(Y: 재고 무제한)-팝업")
	private String popUnlimitStockYn;

	@ApiModelProperty(value = "품목코드 PK")
	private String ilItemCd;

	@ApiModelProperty(value = "코드검색")
	private String itemCodes;

	@ApiModelProperty(value = "기준일자(단일조건)")
	private String baseDt;

	@ApiModelProperty(value = "품목코드Array")
	private ArrayList<String> ilItemCodeArray;

	@ApiModelProperty(value = "검색조건 종류")
    private String selectConditionType;

	@ApiModelProperty(value = "공급업체")
    private String urSupplierId;

	@ApiModelProperty(value = "출고처")
    private String urWarehouseId;

	@ApiModelProperty(value = "출고처그룹의 출고처")
    private String warehouseGroupByWarehouseId;

	@ApiModelProperty(value = "마스터품목명")
    private String itemName;

	@ApiModelProperty(value = "품목재고SEQ")
    private String ilItemStockId;

	@ApiModelProperty(value = "연동시작일자")
    private String startCreateDate;

	@ApiModelProperty(value = "연동종료일자")
    private String endCreateDate;

	@ApiModelProperty(value = "재고유형")
    private String stockTp;

	@ApiModelProperty(value = "표준카테고리(대분류)")
    private String bigCategory;

	@ApiModelProperty(value = "보관방법")
    private String keepMethod;

	@ApiModelProperty(value = "상품 판매상태")
    private String saleStatus;

	@ApiModelProperty(value = "상품유형")
    private String goodsType;

	@ApiModelProperty(value = "판매상태 리스트")
    private List<String> saleStatusList;

	@ApiModelProperty(value = "상품유형 리스트")
    private List<String> goodsTypeList;

	@ApiModelProperty(value = "상품 미생성 품목 여부")
    private String isNoGoodsItem;

	@ApiModelProperty(value = "보관방법 리스트")
    private List<String> storageMethodTypeList;

	@ApiModelProperty(value = "재고운영타입")
    private String stockOperType;

	@ApiModelProperty(value = "출고처그룹 코드")
    private String warehouseGroupCode;

	@ApiModelProperty(value = "접근권한 출고처 ID 리스트")
    private List<String> listAuthWarehouseId;

	@ApiModelProperty(value = "접근권한 공급처 ID 리스트")
    private List<String> listAuthSupplierId;

}
