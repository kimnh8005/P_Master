package kr.co.pulmuone.v1.order.create.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "주문생성 엑셀 업로드 상품 정보  리스트 Response")
public class OrderExcelResponseDto {

	@ApiModelProperty(value = "번호")
    private int rnum;

	@ApiModelProperty(value = "상품코드")
    private String goodsId;

	@ApiModelProperty(value = "마스터품목코드")
    private String ilItemCd;

	@ApiModelProperty(value = "품목바코드")
    private String itemBarcode;

	@ApiModelProperty(value = "상품명")
	private String goodsName;

    @ApiModelProperty(value = "보관방법코드")
    private String storageMethodTypeCode;

    @ApiModelProperty(value = "보관방법명")
    private String storageMethodTypeName;

    @ApiModelProperty(value = "정상가")
    private int recommendedPrice;

    @ApiModelProperty(value = "엑셀업로드-판매가")
    private int salePrice;

    @ApiModelProperty(value = "상품정보-판매가")
    private int orgSalePrice;

	@ApiModelProperty(value = "판매가 에러 메세지")
	private String errSalePrice;

    @ApiModelProperty(value = "판매가 메세지")
    private String salePriceRemark;

	@ApiModelProperty(value = "수취인명")
    private String recvNm;

	@ApiModelProperty(value = "수취인명 에러 메세지")
	private String errRecvNm;

	@ApiModelProperty(value = "수취인연락처")
    private String recvHp;

	@ApiModelProperty(value = "수취인연락처 에러 메세지")
	private String errRecvHp;

	@ApiModelProperty(value = "우편번호")
    private String recvZipCd;

	@ApiModelProperty(value = "우편번호 에러 메세지")
	private String errRecvZipCd;

	@ApiModelProperty(value = "주소1")
    private String recvAddr1;

	@ApiModelProperty(value = "주소1 에러 메세지")
	private String errRecvAddr1;

	@ApiModelProperty(value = "주소2")
    private String recvAddr2;

	@ApiModelProperty(value = "주소2 에러 메세지")
	private String errRecvAddr2;

	@ApiModelProperty(value = "주소검색유무")
	private String zipSrchYn;

	@ApiModelProperty(value = "상품코드")
    private String ilGoodsId;

	@ApiModelProperty(value = "상품코드 에러 메세지")
	private String errIlGoodsId;

	@ApiModelProperty(value = "상품검색유무")
	private String goodsSrchYn;

	@ApiModelProperty(value = "수량")
    private String orderCnt;

	@ApiModelProperty(value = "수량 에러 메세지")
    private String errOrderCnt;

	@ApiModelProperty(value = "판매상태 공통코드(SALE_STATUS)")
	private String saleStatus;

	@ApiModelProperty(value = "판매상태명 공통코드(SALE_STATUS)")
	private String saleStatusNm;

	@ApiModelProperty(value = "출고처_배송정책")
	private String grpShippingId;

	@ApiModelProperty(value = "배송정책 PK")
	private String ilShippingTmplId;

	@ApiModelProperty(value = "출고처 정보")
	private String urWarehouseId;

	@ApiModelProperty(value = "판매상태 에러 메세지")
	private String errSaleStatus;

	@ApiModelProperty(value = "주문금액")
	private int orderAmt;

	@ApiModelProperty(value = "성공여부")
	private boolean success;

	@ApiModelProperty(value = "실패사유")
	private String failMessage;

	@ApiModelProperty(value = "상품타입")
	private String goodsTp;

	@ApiModelProperty(value = "배송비")
	private int shippingPrice;

	@ApiModelProperty(value = "주문 생성 구분자")
	public String getKey() {
		return StringUtil.nvl(getRecvNm()) + StringUtil.nvl(getRecvHp()) + StringUtil.nvl(getRecvZipCd()) + StringUtil.nvl(getRecvAddr1()) + StringUtil.nvl(getRecvAddr2());
	}
}
