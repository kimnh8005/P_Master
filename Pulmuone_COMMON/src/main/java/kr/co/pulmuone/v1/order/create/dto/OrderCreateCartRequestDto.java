package kr.co.pulmuone.v1.order.create.dto;

import java.time.LocalDate;
import java.util.List;

import org.hsqldb.lib.StringUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.shopping.cart.dto.AddCartListGoodsRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 생성 상품정보 조회 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일                		:	작성자      :	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 04. 12.		이규한	최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문생성  장바구니 정보 생성 Request Dto")
public class OrderCreateCartRequestDto {

	@ApiModelProperty(value = "회원 PK")
	private Long urUserId;

	@ApiModelProperty(value = "배송 타입")
	private String deliveryType;

	private String spCartIdListData;

	@ApiModelProperty(value = "장바구니PK 리스트")
	private List<Long> spCartIdList;

	private String addGoodsListData;

	@ApiModelProperty(value = "장바구니 일괄 추가 상품 리스트")
	private List<AddCartListGoodsRequestDto> addGoodsList;

    @ApiModelProperty(value = "수령인 우편번호")
    private String receiverZipCode;

    @ApiModelProperty(value = "배송지 건물 관리 번호")
    private String buildingCode;

    @ApiModelProperty(value = "장바구니 타입")
 	private String cartType;

	@ApiModelProperty(value = "장바구니 PK")
	private Long spCartId;

	@ApiModelProperty(value = "수량")
	private int qty;

	@ApiModelProperty(value = "일일 배송주기코드")
	private String goodsDailyCycleType;

	@ApiModelProperty(value = "일일 배송기간코드")
	private String goodsDailyCycleTermType;

	@ApiModelProperty(value = "일일 배송 녹즙 요일 코드")
	private String goodsDailyCycleGreenJuiceWeekType[];

	@ApiModelProperty(value = "알러지 식단 여부")
	private String goodsDailyAllergyYn;

	@ApiModelProperty(value = "일괄 배송 여부")
	private String goodsDailyBulkYn;

	@ApiModelProperty(value = "일괄배송 배송 세트 코드")
	private String goodsBulkType;

	@ApiModelProperty(value = "도착예정일")
	private String deliveryDt;
	

	public void convertDataList() throws Exception {
		if (!StringUtil.isEmpty(this.addGoodsListData)) this.addGoodsList = BindUtil.convertJsonArrayToDtoList(this.addGoodsListData, AddCartListGoodsRequestDto.class);
		if (!StringUtil.isEmpty(this.spCartIdListData)) this.spCartIdList = BindUtil.convertJsonArrayToDtoList(this.spCartIdListData, Long.class);
    }
}