package kr.co.pulmuone.v1.goods.goods.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.base.dto.vo.GoodsSearchVo;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.comm.framework.dto.UploadFileDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsAllModifyVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품일괄수정 Request")
public class GoodsAllModifyRequestDto extends BaseRequestDto{

	@ApiModelProperty(value = "상품코드")
	private List<String> ilGoodsIds;

	@ApiModelProperty(value = "품목 코드")
	private String ilItemCd;

	@ApiModelProperty(value = "상품 코드")
	private String ilGoodsId;

	@ApiModelProperty(value = "상품 수정 유형")
	private String goodsSelectType;

	@ApiModelProperty(value = "프로모션 상품명")
	private String promotionNm;

	@ApiModelProperty(value = "프로모션 시작일자")
	private String promotionNameStartDate;

	@ApiModelProperty(value = "프로모션 종료일자")
	private String promotionNameEndDate;

	@ApiModelProperty(value = "상품 내역")
	private List<GoodsAllModifyVo> goodsGridList;

	@ApiModelProperty(value = "전시 카테고리")
	private List<GoodsRegistCategoryRequestDto> displayCategoryList;

	@ApiModelProperty(value = "판매허용범위")
	private List<String> purchaseTargetType;

	@ApiModelProperty(value = "쿠폰 사용여부")
	private String couponUseYn;

	@ApiModelProperty(value = "WEB PC 전시여부", required = false)
	private String displayWebPcYn;

	@ApiModelProperty(value = "WEB MOBILE 전시여부", required = false)
	private String displayWebMobilePcYn;

	@ApiModelProperty(value = "APP 전시여부", required = false)
	private String displayAppYn;

	/* 상품공지 시작*/
	@ApiModelProperty(value = "상세 하단공지1 이미지 파일 업로드 정보", required = false)
	List<UploadFileDto> noticeBelow1ImageUploadResultList;

	@ApiModelProperty(value = "상세 하단공지1 이미지 URL", required = false)
	private String noticeBelow1ImageUrl;

	@ApiModelProperty(value = "상세 하단공지1 시작일", required = false)
	private String noticeBelow1StartDate;

	@ApiModelProperty(value = "상세 하단공지1 종료일", required = false)
	private String noticeBelow1EndDate;

	@ApiModelProperty(value = "상세 하단공지2 이미지 파일 업로드 정보", required = false)
	List<UploadFileDto> noticeBelow2ImageUploadResultList;

	@ApiModelProperty(value = "상세 하단공지2 이미지 URL", required = false)
	private String noticeBelow2ImageUrl;

	@ApiModelProperty(value = "상세 하단공지2 시작일", required = false)
	private String noticeBelow2StartDate;

	@ApiModelProperty(value = "상세 하단공지2 종료일", required = false)
	private String noticeBelow2EndDate;
	/* 상품공지 끝*/

	@ApiModelProperty(value = "추가상품 목록", required = false)
	private List<GoodsSearchVo> goodsAdditionList;

	@ApiModelProperty(value = "추가상품 추가삭제유무", required = false)
	private String goodsAddType;

	@ApiModelProperty(value = "추가상품 ID", required = false)
	private String targetGoodsId;

	@ApiModelProperty(value = "판매가", required = false)
	private Integer salePrice;

	@ApiModelProperty(value = "출고처ID", required = false)
	private String urWarehouseId;

	@ApiModelProperty(value = "검색 상품ID", required = false)
	private String searchGoodsId;

	@ApiModelProperty(value = "검색 상품ID List", required = false)
	private List<String> searchGoodsIdList;
}
