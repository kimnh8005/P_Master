package kr.co.pulmuone.v1.order.create.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.shopping.cart.dto.UseGoodsCouponDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문생성  사용 가능 쿠폰 조회 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일                		:	작성자      :	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 04. 16.		이규한	최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문생성  사용 가능 쿠폰 조회 Request Dto")
public class OrderCreateCouponRequestDto {

	@ApiModelProperty(value = "회원 PK")
	private Long urUserId;

    @ApiModelProperty(value = "수령인 우편번호")
    private String receiverZipCode;

    @ApiModelProperty(value = "배송지 건물 관리 번호")
    private String buildingCode;

	private String spCartIdListData;

	@ApiModelProperty(value = "장바구니PK 리스트")
	private List<Long> spCartIdList;

	private String arrivalScheduledListData;

	@ApiModelProperty(value = "배송 스케줄 변경 정보 리스트")
	private List<OrderCreateScheduledDto> arrivalScheduledList;

	private String useGoodsCouponListData;

	@ApiModelProperty(value = "상품 쿠폰 사용 정보", hidden = true)
	private List<UseGoodsCouponDto> useGoodsCouponList;

	public void convertDataList() throws Exception {
		if (!StringUtil.isEmpty(this.spCartIdListData)) this.spCartIdList = BindUtil.convertJsonArrayToDtoList(this.spCartIdListData, Long.class);
		if (!StringUtil.isEmpty(this.arrivalScheduledListData)) this.arrivalScheduledList = BindUtil.convertJsonArrayToDtoList(this.arrivalScheduledListData, OrderCreateScheduledDto.class);
		if (!StringUtil.isEmpty(this.useGoodsCouponListData)) this.useGoodsCouponList = BindUtil.convertJsonArrayToDtoList(this.useGoodsCouponListData, UseGoodsCouponDto.class);
    }
}