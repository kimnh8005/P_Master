package kr.co.pulmuone.v1.goods.item.dto;

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
@ApiModel(description = "ItemPoListRequestDto")
public class ItemPoListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "단일_복수조건검색")
	private String selectConditionType;

	@ApiModelProperty(value = "코드검색값List")
	private List<String> searchItemCdArray;

	@ApiModelProperty(value = "코드검색값")
	private String searchItemCd;

	@ApiModelProperty(value = "출고처Id")
    private String searchUrWarehouseId;

	@ApiModelProperty(value = "공급처Id")
    private String searchUrSupplierId;

	@ApiModelProperty(value = "발주유형구분")
    private String searchPoTypeGubun;

	@ApiModelProperty(value = "발주유형구분 목록")
    private List<String> searchPoTypeGubunList;

	@ApiModelProperty(value = "발주유형")
    private String searchPoType;

	@ApiModelProperty(value = "발주유형")
    private List<String> searchPoTypeList;

	@ApiModelProperty(value = "기준일자")
    private String searchBaseDt;

	@ApiModelProperty(value = "ERP 발주유형")
    private String searchErpPoTp;

	@ApiModelProperty(value = "표준카테고리(대분류)")
    private String searchBigCategory;

	@ApiModelProperty(value = "ERP 카테고리 (대분류)")
    private String searchErpCtgry;

	@ApiModelProperty(value = "ERP 카테고리 (대분류) 목록")
	private List<String> searchErpCtgryList;

	@ApiModelProperty(value = "보관방법")
    private String searchStorageMethod;

	@ApiModelProperty(value = "보관방법 목록")
    private List<String> searchStorageMethodList;

	@ApiModelProperty(value = "상품 판매상태")
    private String searchSaleStatus;

	@ApiModelProperty(value = "상품 판매상태목록")
    private List<String> searchSaleStatusList;

	@ApiModelProperty(value = "외부몰 판매상태")
	private String goodsOutMallSaleStatus;

	@ApiModelProperty(value = "외부몰 판매상태 리스트")
	private List<String> goodsOutMallSaleStatusList;

	@ApiModelProperty(value = "상품 전시상태")
    private String searchGoodsDisplayStatus;

	@ApiModelProperty(value = "상품 전시상태목록")
    private List<String> searchGoodsDisplayStatusList;

	@ApiModelProperty(value = "발주연동여부")
    private String searchPoIfYn;

	@ApiModelProperty(value = "품목별 상이여부")
    private String poPerItemYn;

	@ApiModelProperty(value = "발주목록")
    private List<String> poList;

	@ApiModelProperty(value = "발주내역여부")
	private String poResultYn;
}
