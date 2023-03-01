package kr.co.pulmuone.v1.order.create.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;


/**
 * <PRE>
 * Forbiz Korea
 * 주문생성 관련 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "엑셀 업로드 데이터 VO")
public class OrderExeclDto {

	@ApiModelProperty(value = "수취인명")
    private String recvNm;

	@ApiModelProperty(value = "수취인연락처")
    private String recvHp;

	@ApiModelProperty(value = "우편번호")
    private String recvZipCd;

	@ApiModelProperty(value = "주소1")
    private String recvAddr1;

	@ApiModelProperty(value = "주소2")
    private String recvAddr2;

	@ApiModelProperty(value = "상품코드")
    private String ilGoodsId;

	@ApiModelProperty(value = "수량")
    private String orderCnt;

	@ApiModelProperty(value = "판매가")
    private String salePrice;

	@ApiModelProperty(value = "주문생성")
	private String orderStr;

	@ApiModelProperty(value = "품목코드")
	private String ilItemCd;

	@ApiModelProperty(value = "성공여부")
	private boolean success;

	@ApiModelProperty(value = "실패사유")
	private String failMessage;
}
