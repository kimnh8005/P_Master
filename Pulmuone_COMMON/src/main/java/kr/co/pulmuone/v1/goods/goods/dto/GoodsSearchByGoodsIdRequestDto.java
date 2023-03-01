package kr.co.pulmuone.v1.goods.goods.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "상품ID로 상품리스트 검색 RequestDto")
public class GoodsSearchByGoodsIdRequestDto {

	@ApiModelProperty(value = "몰구분")
	private String mallDiv;

	@ApiModelProperty(value = "상품 PK 리스트")
	private List<Long> goodsIdList;

	@ApiModelProperty(value = "디바이스 정보")
	private String deviceInfo;

	@ApiModelProperty(value = "회원 여부")
	private boolean isMember;

	@ApiModelProperty(value = "임직원 여부")
	private boolean isEmployee;

	@ApiModelProperty(value = "상품 정렬순서")
	private String goodsSortCode;

	@ApiModelProperty(value = "비회원 회원대상 상품 조회 가능 여부 Y: 비회원 회원상품 조회")
	private boolean needNonMemberShowMemberGoods;

	@ApiModelProperty(value = "매장PK")
	private String urStoreId;

	@ApiModelProperty(value = "구매허용범위 조회여부")
	private boolean isPurchaseSearch;

	@ApiModelProperty(value = "매장전용상품 제외여부")
	private boolean exceptShopOnly;

	@ApiModelProperty(value = "전시 브랜드 리스트")
	private List<String> dpBrandIdList;

	@ApiModelProperty(value = "page")
	private int page;

	@ApiModelProperty(value = "limit")
	private int limit;

	@ApiModelProperty(value = "sPage")
	private int sPage;

	@ApiModelProperty(value = "ePage")
	private int ePage;

	public int getsPage() {
		if(StringUtil.nvlInt(this.page) > 1) {
			this.sPage = ( StringUtil.nvlInt(this.page) - 1 ) * StringUtil.nvlInt(this.limit);
		}
		return sPage;
	}
	public int getePage() {
		this.ePage = StringUtil.nvlInt(this.limit);
		return ePage;
	}

}
