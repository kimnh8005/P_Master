package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 쿠폰 할인 리스트 관련 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 11.  석세동         최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "주문 상세 쿠폰 할인 리스트 관련 Dto")
public class OrderDetailDiscountListDto {

    @ApiModelProperty(value = "쿠폰 종류명 ")
    private String discountTypeName;

    @ApiModelProperty(value = "쿠폰 종류")
    private String discountTp;

    @ApiModelProperty(value = "쿠폰번호")
    private Long couponId;

    @ApiModelProperty(value = "쿠폰명")
    private String couponNm;

    @ApiModelProperty(value = "외 N건")
    private String goodsNmCnt;

	@ApiModelProperty(value = "할인기준")
	private String discountValue;

    @ApiModelProperty(value = "할인금액")
    private String discountPrice;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "주문 결제 마스터 PK")
    private long odClaimId;

}
