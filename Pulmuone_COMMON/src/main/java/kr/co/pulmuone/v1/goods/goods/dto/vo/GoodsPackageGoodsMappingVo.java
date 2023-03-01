package kr.co.pulmuone.v1.goods.goods.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "묶음 상품 Vo")
public class GoodsPackageGoodsMappingVo {

	@ApiModelProperty(value="묶음상품맵핑 ID")
	String ilGoodsPackageGoodsMappingId;

	@ApiModelProperty(value="상품구분")
	String goodsPackageType;

	@ApiModelProperty(value="품목코드")
	String ilItemCode;

	@ApiModelProperty(value="품목바코드")
	String itemBarcode;

	@ApiModelProperty(value="상품코드")
	String targetGoodsId;

	@ApiModelProperty(value="상품유형")
	String goodsType;

	@ApiModelProperty(value="상품유형명")
	String goodsTypeName;

	@ApiModelProperty(value="상품명")
	String goodsName;

	@ApiModelProperty(value="과세구분")
	String taxYn;

	@ApiModelProperty(value="과세구분명")
	String taxName;

	@ApiModelProperty(value="기준상품여부")
	String baseGoodsYn;

	@ApiModelProperty(value="구성수량")
	int goodsQuantity;

	@ApiModelProperty(value="구성상품 판매가(개당)")
	int salePricePerUnit;

	@ApiModelProperty(value="판매가")
	int salePrice;

	@ApiModelProperty(value="원가")
	int standardPrice;

	@ApiModelProperty(value="정상가")
	int recommendedPrice;

	@ApiModelProperty(value="상품에 따른 품목 대표 이미지 75X75 사이즈")
	String size75Image;

	@ApiModelProperty(value="상품에 따른 품목 대표 이미지 180X180 사이즈")
	String size180Image;

	@ApiModelProperty(value="상품별 대표이미지 순서")
	int imageSort;

	@ApiModelProperty(value="단종여부")
	String extinctionYn;

	@ApiModelProperty(value="등록자")
	String createId;

	@ApiModelProperty(value="등록일")
	String createDate;

	@ApiModelProperty(value="수정자")
	String modifyId;

	@ApiModelProperty(value="수정일")
	String modifyDate;

	@ApiModelProperty(value = "판매상태")
	private String saleStatus;

	@ApiModelProperty(value = "판매상태코드")
	private String saleStatusCode;

	@ApiModelProperty(value = "전시상태")
	private String displayYn;

	@ApiModelProperty(value = "재고발주여부YN")
	private String stockOrderYn;

	@ApiModelProperty(value = "재고미연동시 재고무제한 사용여부(Y: 재고 무제한)")
	private String unlimitStockYn;

	@ApiModelProperty(value = "미연동 재고 수량")
	private long notIfStockCnt;

	@ApiModelProperty(value = "D0 주문가능 수량")
	private long d0OrderQty;

	@ApiModelProperty(value = "D1 주문가능 수량")
	private long d1OrderQty;

	@ApiModelProperty(value ="D0 폐기 임박 주문 가능 수량")
	private long d0OrderDiscardQty;

	@ApiModelProperty(value = "품목 출고처 PK")
	private long ilItemWarehouseId;

	@ApiModelProperty(value = "선물하기 허용 여부")
	private String presentYn;

}
