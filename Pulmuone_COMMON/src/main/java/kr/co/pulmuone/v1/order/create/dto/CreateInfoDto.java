package kr.co.pulmuone.v1.order.create.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


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
@ApiModel(description = "주문 생성 목록 dto")
public class CreateInfoDto {
	@ApiModelProperty(value = "No")
	private int rownum;

    @ApiModelProperty(value = "주문자명")
    private String buyerNm;

	@ApiModelProperty(value = "S:개별,T:단일")
	private String createType;

    @ApiModelProperty(value = "주문생성정보 PK")
    private long odCreateInfoId;

    @ApiModelProperty(value = "W:대기중,C:입금대기중,E:생성완료")
    private String createStatus;

    @ApiModelProperty(value = "결제수단:공통코드(ORDER_PAYMENT_TYPE)")
    private String orderPaymentType;

    @ApiModelProperty(value = "주문리스트검색용,로구분")
    private String odid;

    @ApiModelProperty(value = "성공 주문번호 건수")
    private int successOrderCnt;

    @ApiModelProperty(value = "성공 주문상세 건수")
    private int successOrderDetlCnt;

    @ApiModelProperty(value = "주문금액")
    private int orderPrice;

    @ApiModelProperty(value = "등록일")
    private String createDt;

    @ApiModelProperty(value = "주문결제 마스터 PK")
    private long odPaymentMasterId;
}
