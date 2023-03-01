package kr.co.pulmuone.v1.goods.category.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.search.searcher.constants.SortCode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "카테고리별 상품 리스트 requestDto")
public class GetCategoryGoodsListRequestDto
{

	@ApiModelProperty(value = "카테고리 PK")
	private String ilCategoryId;

	@ApiModelProperty(value = "첫검색 여부")
	private boolean isFirstSearch = true;

	@ApiModelProperty(value = "offset 에서 시작하여 반환할 문서 개수")
	private int limit = 50;

	@ApiModelProperty(value = "리스팅 페이지")
	private int page = 0;

	@ApiModelProperty(value = "일시품절상품 제외 여부")
	private boolean excludeSoldOutGoods = false;

	@ApiModelProperty(value = "혜택유형코드 리스트")
	private List<String> benefitTypeIdList;

	@ApiModelProperty(value = "브랜드 코드 리스트")
	private List<String> brandIdList;

	@ApiModelProperty(value = "배송유형코드 리스트")
	private List<String> deliveryTypeIdList;

	@ApiModelProperty(value = "인증유형코드 리스트")
	private List<String> certificationTypeIdList;

	@ApiModelProperty(value = "보관방법코드 리스트")
	private List<String> storageMethodIdList;

	@ApiModelProperty(value = "최소 가격")
	private int minimumPrice = 0;

	@ApiModelProperty(value = "최대 가격")
	private int maximumPrice = 0;

	@ApiModelProperty(value = "정렬코드")
	private SortCode sortCode = SortCode.NEW;

	@Builder
	public GetCategoryGoodsListRequestDto(String ilCategoryId, Boolean isFirstSearch, Integer limit, Integer page, Boolean excludeSoldOutGoods, List<String> benefitTypeIdList, List<String> brandIdList, List<String> deliveryTypeIdList, List<String> certificationTypeIdList, List<String> storageMethodIdList, Integer minimumPrice, Integer maximumPrice, SortCode sortCode)
	{

		this.ilCategoryId = ilCategoryId;
		this.benefitTypeIdList = benefitTypeIdList;
		this.brandIdList = brandIdList;
		this.deliveryTypeIdList = deliveryTypeIdList;
		this.certificationTypeIdList = certificationTypeIdList;
		this.storageMethodIdList = storageMethodIdList;
		this.isFirstSearch = isFirstSearch == null ? true : isFirstSearch;
		this.excludeSoldOutGoods = excludeSoldOutGoods == null ? false : excludeSoldOutGoods;
		this.minimumPrice = minimumPrice == null ? this.minimumPrice : minimumPrice;
		this.maximumPrice = maximumPrice == null ? this.maximumPrice : maximumPrice;
		this.sortCode = sortCode == null ? this.sortCode : sortCode;
		this.limit = limit == null ? this.limit : limit;
		this.page = page == null ? this.page : page;
	}

}
