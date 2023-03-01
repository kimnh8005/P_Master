package kr.co.pulmuone.v1.order.create.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문생성 관련 DTO
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
@Getter
@Setter
@ToString
@ApiModel(description = "주문생성 DTO")
public class OrderCreateDto {

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
    private long ilGoodsId;

	@ApiModelProperty(value = "품목코드")
	private String itemCode;

	@ApiModelProperty(value = "품목바코드")
	private String itemBarcode;

	@ApiModelProperty(value = "상품코드")
	private String goodsId;

	@ApiModelProperty(value = "상품명")
	private String goodsName;

	@ApiModelProperty(value = "보관방법")
	private String storageMethodTypeName;

	@ApiModelProperty(value = "보관방법")
	private String storageMethodTypeCode;

	@ApiModelProperty(value = "수량")
    private String orderCnt;

	@ApiModelProperty(value = "엑셀정보-판매가")
    private String salePrice;

	@ApiModelProperty(value = "상품정보-판매가")
    private int orgSalePrice;

	@ApiModelProperty(value = "정상가")
	private int recommendedPrice;

	@ApiModelProperty(value = "주문금액")
	private int orderAmt;

	@ApiModelProperty(value = "출고처_배송정책")
	private String grpShippingId;

	@ApiModelProperty(value = "배송정책 PK")
	private Long ilShippingTmplId;

	@ApiModelProperty(value = "출고처 정보")
	private String urWarehouseId;

	@ApiModelProperty(value = "상품타입")
	private String goodsTp;

	@ApiModelProperty(value = "주문 생성 구분자")
    public String getKey() {
        return StringUtil.nvl(getRecvNm()) + StringUtil.nvl(getRecvHp()) + StringUtil.nvl(getRecvZipCd()) + StringUtil.nvl(getRecvAddr1()) + StringUtil.nvl(getRecvAddr2());
    }
}
