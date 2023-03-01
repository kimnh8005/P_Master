package kr.co.pulmuone.v1.api.babymeal.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "베이비밀 배송 스케줄정보 Dto")
public class BabymealOrderDeliveryListDto {

	@JsonAlias({ "goodsOrderNo" })
	@ApiModelProperty(value = "베이비밀 주문번호")
	private String goodsOrderNo;

	@JsonAlias({ "goodsDeliverySeq" })
	@ApiModelProperty(value = "베이비밀 배송스케줄 ID(PK)")
	private String id;

	@JsonAlias({ "goodsKindCD" })
	@ApiModelProperty(value = "주문구분  0001(정상주문), 0002(취소), 0003~0007(증정)")
	private String goodsKindCD;

	@JsonAlias({ "goodsKind" })
	@ApiModelProperty(value = "증정구분 (0001 일반, 0002 증정)")
	private String goodsKind;

	@JsonAlias({ "goodsStatus" })
	@ApiModelProperty(value = "goodsStatus")
	private String goodsStatus;

	@JsonAlias({ "goodsGoodsSetID" })
	@ApiModelProperty(value = "I/F 상품 코드")
	private String ilGoodsId;

	@JsonAlias({ "goodsGoodsSetNm" })
	@ApiModelProperty(value = "상품명")
	private String goodsNm;

	@JsonAlias({ "goodsGoodsNm1" })
	@ApiModelProperty(value = "상품명1")
	private String goodsGoodsNm1;

	@JsonAlias({ "goodsGoodsNm2" })
	@ApiModelProperty(value = "상품명2")
	private String goodsGoodsNm2;

	@JsonAlias({ "goodsGoodsNm3" })
	@ApiModelProperty(value = "상품명3")
	private String goodsGoodsNm3;

	@JsonAlias({ "goodsGoodsGroupID" })
	@ApiModelProperty(value = "상품그룹코드")
	private String goodsGoodsGroupID;

	@JsonAlias({ "goodsDay" })
	@ApiModelProperty(value = "배송요일")
	private String delvDateWeekDay;

	@JsonAlias({ "goodsDeliveryDate" })
	@ApiModelProperty(value = "배송일자")
	private String delvDate;

	@JsonAlias({ "goodsGoodsSetCnt" })
	@ApiModelProperty(value = "주문수량")
	private String orderCnt;

	@JsonAlias({ "goodsJisaCD" })
	@ApiModelProperty(value = "가맹점코드")
	private String urStoreId;

	@JsonAlias({ "goodsJisaNm" })
	@ApiModelProperty(value = "가맹점명")
	private String urStoreNm;

	@JsonAlias({ "goodsOrdZipCd" })
	@ApiModelProperty(value = "수령인 우편번호")
	private String recvZipCd;

	@JsonAlias({ "tempAddr1" })
	@ApiModelProperty(value = "수령인 주소1")
	private String recvAddr1;

	@JsonAlias({ "tempAddr2" })
	@ApiModelProperty(value = "수령인 주소2(상세주소)")
	private String recvAddr2;

	@JsonAlias({ "goodsOrdAddr1Do" })
	@ApiModelProperty(value = "수령인 도로명주소")
	private String recvAddr1Do;

	@JsonAlias({ "goodsAllergyType" })
	@ApiModelProperty(value = "제외식품타입")
	private String goodsAllergyType;

	@ApiModelProperty(value = "저장구분 S(저장 기본값), I(추가), U(수정)")
	private String activeFlg;

	@ApiModelProperty(value = "변경 요청 배송일자")
	private String deliveryDate;

	@ApiModelProperty(value = "상품이미지")
	private String goodsImgNm;
}