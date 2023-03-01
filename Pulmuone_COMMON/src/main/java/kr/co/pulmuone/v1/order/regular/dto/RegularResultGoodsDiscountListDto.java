package kr.co.pulmuone.v1.order.regular.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.MallBaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주문 결과 상품 할인정보 목록 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 18.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 주문 결과 상품 할인정보 목록 Dto")
public class RegularResultGoodsDiscountListDto {

	@ApiModelProperty(value = "상품할인코드명")
	private String discountTpNm;

	@ApiModelProperty(value = "상품할인금액")
	private int discountPrice;
}
