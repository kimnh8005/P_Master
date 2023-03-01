package kr.co.pulmuone.v1.goods.goods.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.goods.discount.dto.vo.GoodsDiscountVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemInfoVo;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@ApiModel(description = "상품할인 Request")
public class GoodsDiscountRequestDto extends BaseResponseDto{

	@ApiModelProperty(value = "상품 ID")
	private String goodsId;

	@ApiModelProperty(value = "상품할인 ID")
	private String goodsDiscountId;

	@ApiModelProperty(value = "상품할인코드")
	private String discountTypeCode;

	@ApiModelProperty(value = "승인상태")
	private String approvalStatusCode;

	@ApiModelProperty(value = "승인상태명")
	private String approvalStatusCodeName;

	@ApiModelProperty(value = "상품할인 시작일자")
	private String discountStartDateTime;

	@ApiModelProperty(value = "상품할인 종료일자")
	private String discountEndDateTime;

	@ApiModelProperty(value = "상품할인 유형코드")
	private String discountMethodTypeCode;

	@ApiModelProperty(value = "할인율")
	private double discountRatio;

	@ApiModelProperty(value = "할인 판매가")
	private int discountSalePrice;

	@ApiModelProperty(value = "원가")
	private int itemStandardPrice;

	@ApiModelProperty(value = "정상가")
	private int itemRecommendedPrice;

	@ApiModelProperty(value = "묶음상품 원가")
	private int standardPrice;

	@ApiModelProperty(value = "묶음상품 정상가")
	private int recommendedPrice;

	@ApiModelProperty(value = "묶음 상품 총 구성 갯수")
	private int rowCount;

	@ApiModelProperty(value = "가격정보 변동내용 추적용")
	private int rowNum;
	
	/**
	 * 묶음상품 > 임직원 할인 정보 > 임직원 개별할인 정보 관련
	 * 임직원 개별할인 정보는 IL_GOODS_DISCOUNT, IL_GOODS_PACKAGE_ITEM_FIXED_DISCOUNT_PRICE 테이블을 구성하는 정보가 함께 있으므로 해당 내역을 추가 함
	*/
	@ApiModelProperty(value = "묶음상품 관리 ID")
	private String ilGoodsPackageGoodsMappingId;

	@ApiModelProperty(value = "등록자")
	private String createId;

	@ApiModelProperty(value = "수정자")
	private String modifyId;
}
