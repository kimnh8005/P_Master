package kr.co.pulmuone.v1.order.create.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 생성 정보 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 18.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "카드 결제 dto")
public class OrderInfoDto {
    @ApiModelProperty(value = "구매자명")
    private String buyerNm;

    @ApiModelProperty(value = "구매자메일")
    private String buyerMail;

    @ApiModelProperty(value = "구매자연락처")
    private String buyerHp;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "로그인ID")
    private String loginId;

    @ApiModelProperty(value = "주문번호")
    private String odid;
}
