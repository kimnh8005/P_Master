package kr.co.pulmuone.v1.api.eatsslim.dto;

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
@ApiModel(description = "잇슬림 배송 스케줄정보 Dto")
public class EatsslimOrderDeliveryListDto {

	@JsonAlias({ "kindCd" })
	@ApiModelProperty(value = "I/F 상품 코드")
	private String ilGoodsId;

	@JsonAlias({ "productName" })
	@ApiModelProperty(value = "상품명")
	private String goodsNm;

	@JsonAlias({ "delvDateWeekDay" })
	@ApiModelProperty(value = "배송요일")
	private String delvDateWeekDay;

	@JsonAlias({ "delvDate" })
	@ApiModelProperty(value = "배송일자")
	private String delvDate;

	@JsonAlias({ "orderCnt" })
	@ApiModelProperty(value = "주문수량")
	private String orderCnt;

	@JsonAlias({ "stateDetail" })
	@ApiModelProperty(value = "증정타입(021:마케팅, 022:제품불만, 023:배송관련, 024:휴먼오류, 025:시스템관련불편)")
	private String stateDetail;

	@JsonAlias({ "stateDetailName" })
	@ApiModelProperty(value = "증정타입명(021:마케팅, 022:제품불만, 023:배송관련, 024:휴먼오류, 025:시스템관련불편)")
	private String stateDetailNm;

	@JsonAlias({ "franchCd" })
	@ApiModelProperty(value = "가맹점코드")
	private String urStoreId;

	@JsonAlias({ "franchNm" })
	@ApiModelProperty(value = "가맹점명")
	private String urStoreNm;

	@JsonAlias({ "rcvZipCode" })
	@ApiModelProperty(value = "수령인 우편번호")
	private String recvZipCd;

	@JsonAlias({ "rcvAddr1" })
	@ApiModelProperty(value = "수령인 주소1")
	private String recvAddr1;

	@JsonAlias({ "rcvAddr2" })
	@ApiModelProperty(value = "수령인 주소2(상세주소)")
	private String recvAddr2;

	@JsonAlias({ "gubun1" })
	@ApiModelProperty(value = "구분1(01: 식사다이어트, 02: 프로그램다이어트, 03: 타입별다이어트)")
	private String gubun1;

	@JsonAlias({ "gubun2" })
	@ApiModelProperty(value = "구분2(11: 1식, 12: 2식, 13: 3식, 14: 2식+간식, 15: 3식+간식, 21: 감량, 22: 유지, 23: FULL-STEP, 31: 시크릿프(SS), 32: 밸런스쉐이크)")
	private String gubun2;

	@JsonAlias({ "id" })
	@ApiModelProperty(value = "잇슬림 주문상품 고유ID(PK)")
	private String id;

	@ApiModelProperty(value = "상품이미지")
	private String goodsImgNm;
}