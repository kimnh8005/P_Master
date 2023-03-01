package kr.co.pulmuone.v1.order.order.dto.mall;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.MallBaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 일일배송 주문 리스트 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 29.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "Mall 일일배송 주문 리스트 Request Dto")
public class MallOrderDailyListRequestDto extends MallBaseRequestPageDto {

	@ApiModelProperty(value = "조회시작일(yyyy-MM-dd)")
    private String startDate;

    @ApiModelProperty(value = "조회종료일(yyyy-MM-dd)")
    private String endDate;

    @ApiModelProperty(value = "일일상품타입")
    private String goodsDailyTp;

    @ApiModelProperty(value = "주문상태코드")
    private String orderStatusCd;

    @ApiModelProperty(value = "상품배송타입")
    private String goodsDeliveryType;

    @ApiModelProperty(value = "페이지번호")
    private int page;

    @ApiModelProperty(value = "페이지갯수")
    private int limit;

    @ApiModelProperty(value = "페이지 시작 번호")
    private int startPage;

    @ApiModelProperty(value = "페이지 사이즈")
    private int pageSize;

    @ApiModelProperty(value = "주문자ID", hidden = true)
    private long urUserId;

    @ApiModelProperty(value = "비회원 CI", hidden = true)
    private String guestCi;
}