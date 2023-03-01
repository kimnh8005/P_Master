package kr.co.pulmuone.v1.outmall.order.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OutMallExcelFailVo {

    @ApiModelProperty(value = "수집몰 주문 번호")
    private String collectionMallId;

    @ApiModelProperty(value = "수집몰 상세 번호")
    private String collectionMallDetailId;

    @ApiModelProperty(value = "판매처 코드")
    private String omSellersId;

    @ApiModelProperty(value = "품목 PK")
    private String ilItemCd;

    @ApiModelProperty(value = "상품 PK")
    private Long ilGoodsId;

    @ApiModelProperty(value = "상품명")
    private String goodsName;

    @ApiModelProperty(value = "주문수량")
    private int orderCount;

    @ApiModelProperty(value = "상품 총 금액")
    private int paidPrice;

    @ApiModelProperty(value = "주문자 명")
    private String buyerName;

    @ApiModelProperty(value = "주문자 연락처")
    private String buyerTel;

    @ApiModelProperty(value = "주문자 핸드폰")
    private String buyerMobile;

    @ApiModelProperty(value = "수령인 명")
    private String receiverName;

    @ApiModelProperty(value = "수령인 연락처")
    private String receiverTel;

    @ApiModelProperty(value = "수령인 핸드폰")
    private String receiverMobile;

    @ApiModelProperty(value = "수령인 이메일")
    private String receiverMail;

    @ApiModelProperty(value = "수령인 우편번호")
    private String receiverZipCode;

    @ApiModelProperty(value = "수령인 주소 1")
    private String receiverAddress1;

    @ApiModelProperty(value = "수령인 주소 2")
    private String receiverAddress2;

    @ApiModelProperty(value = "배송비")
    private int shippingPrice;

    @ApiModelProperty(value = "배송요청사항")
    private String deliveryMessage;

    @ApiModelProperty(value = "택배사")
    private String logisticsCd;

    @ApiModelProperty(value = "송장번호")
    private String trackingNo;

    @ApiModelProperty(value = "외부몰주문번호")
    private String outMallId;

    @ApiModelProperty(value = "외부몰주문 상세번호1")
    private String outMallIdSeq1;

    @ApiModelProperty(value = "외부몰주문 상세번호2")
    private String outMallIdSeq2;

    @ApiModelProperty(value = "고객상담메모")
    private String memo;

    @ApiModelProperty(value = "외부몰주문일")
    private String outmallOrderDt;

    @ApiModelProperty(value = "외부몰상품명")
    private String outmallGoodsNm;

    @ApiModelProperty(value = "외부몰옵션명")
    private String outmallOptNm;

    @ApiModelProperty(value = "실패사유")
    private String failMessage;

    @ApiModelProperty(value = "실패타입 U:업로드 B:배치")
    private String failType;

    @ApiModelProperty(value = "풀샵상품코드")
    private String goodsNo;

    public OutMallExcelFailVo() {
    }

    public OutMallExcelFailVo(OutMallOrderDto dto) {
        this.collectionMallId           = dto.getCollectionMallId();
        this.collectionMallDetailId     = dto.getCollectionMallDetailId();
        this.omSellersId                = dto.getOmSellersId();
        this.ilItemCd                   = dto.getIlItemCd();
        this.ilGoodsId                  = StringUtil.nvlLong(dto.getIlGoodsId());
        this.goodsName                  = dto.getGoodsName();
        this.orderCount                 = StringUtil.nvlInt(dto.getOrderCount());
        this.paidPrice                  = StringUtil.nvlInt(dto.getPaidPrice());
        this.buyerName                  = dto.getBuyerName();
        this.buyerTel                   = dto.getBuyerTel();
        this.buyerMobile                = dto.getBuyerMobile();
        this.receiverName               = dto.getReceiverName();
        this.receiverTel                = dto.getReceiverTel();
        this.receiverMobile             = dto.getReceiverMobile();
        this.receiverMail               = dto.getReceiverMail();
        this.receiverZipCode            = dto.getReceiverZipCode();
        this.receiverAddress1           = dto.getReceiverAddress1();
        this.receiverAddress2           = dto.getReceiverAddress2();
        this.shippingPrice              = StringUtil.nvlInt(dto.getShippingPrice());
        this.deliveryMessage            = dto.getDeliveryMessage();
        this.outMallId                  = dto.getOutMallId();
        this.outMallIdSeq1              = dto.getOutMallIdSeq1();
        this.outMallIdSeq2              = dto.getOutMallIdSeq2();
        this.memo                       = dto.getMemo();
        this.outmallOrderDt             = dto.getOutmallOrderDt();
        this.outmallGoodsNm             = dto.getOutmallGoodsNm();
        this.outmallOptNm               = dto.getOutmallOptNm();
        this.failMessage                = dto.getFailMessage();
        this.goodsNo                    = dto.getGoodsNo();
    }
    public OutMallExcelFailVo(OutMallExcelSuccessVo dto) {
        this.collectionMallId           = dto.getCollectionMallId();
        this.collectionMallDetailId     = dto.getCollectionMallDetailId();
        this.omSellersId                = dto.getOmSellersId();
        this.ilItemCd                   = dto.getIlItemCd();
        this.ilGoodsId                  = StringUtil.nvlLong(dto.getIlGoodsId());
        this.goodsName                  = dto.getGoodsName();
        this.orderCount                 = StringUtil.nvlInt(dto.getOrderCount());
        this.paidPrice                  = StringUtil.nvlInt(dto.getPaidPrice());
        this.buyerName                  = dto.getBuyerName();
        this.buyerTel                   = dto.getBuyerTel();
        this.buyerMobile                = dto.getBuyerMobile();
        this.receiverName               = dto.getReceiverName();
        this.receiverTel                = dto.getReceiverTel();
        this.receiverMobile             = dto.getReceiverMobile();
        this.receiverMail               = dto.getReceiverMail();
        this.receiverZipCode            = dto.getReceiverZipCode();
        this.receiverAddress1           = dto.getReceiverAddress1();
        this.receiverAddress2           = dto.getReceiverAddress2();
        this.shippingPrice              = StringUtil.nvlInt(dto.getShippingPrice());
        this.deliveryMessage            = dto.getDeliveryMessage();
        this.outMallId                  = dto.getOutMallId();
        this.outMallIdSeq1              = dto.getOutMallIdSeq1();
        this.outMallIdSeq2              = dto.getOutMallIdSeq2();
        this.memo                       = dto.getMemo();
        this.outmallOrderDt             = dto.getOutmallOrderDt();
        this.outmallGoodsNm             = dto.getOutmallGoodsNm();
        this.outmallOptNm               = dto.getOutmallOptNm();
        this.failMessage                = dto.getFailMessage();
        this.goodsNo                    = dto.getGoodsNo();
    }
}
