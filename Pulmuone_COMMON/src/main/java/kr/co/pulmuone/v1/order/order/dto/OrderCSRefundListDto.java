package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "OrderCSRefundListDto")
public class OrderCSRefundListDto {

    @ApiModelProperty(value = "CS환불정보PK")
    private long odCsId;

    @ApiModelProperty(value = "CS환불정보상세PK")
    private long odCsDetlId;

    @ApiModelProperty(value = "주문 PK")
    private long odOrderId;

    @ApiModelProperty(value = "승인요청자ID")
    private long apprReqUserId;

    @ApiModelProperty(value = "승인요청일")
    private String apprReqDt;

    @ApiModelProperty(value = "승인2차처리일")
    private String apprChgDt;

    @ApiModelProperty(value = "판매처명")
    private String omSellersNm;

    @ApiModelProperty(value = "주문 번호")
    private String odid;

    @ApiModelProperty(value = "주문 상세 SEQ")
    private String odOrderDetlSeq;

    @ApiModelProperty(value = "마스터품목코드")
    private String ilItemCd;

    @ApiModelProperty(value = "품목바코드")
    private String itemBarcode;

    @ApiModelProperty(value = "상품 PK")
    private long ilGoodsId;

    @ApiModelProperty(value = "공급업체명")
    private String compNm;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "매출여부")
    private String salesSettleYn;

    @ApiModelProperty(value = "과세구분")
    private String taxYn;

    @ApiModelProperty(value = "미출여부")
    private String unreleasedYn;

    @ApiModelProperty(value = "출고처이름")
    private String warehouseNm;

    @ApiModelProperty(value = "CS환불구분")
    private String csRefundTp;

    @ApiModelProperty(value = "CS환불구분명")
    private String csRefundTpNm;

    @ApiModelProperty(value = "환불금액")
    private int refundPrice;

    @ApiModelProperty(value = "BOS 클레임 대분류 ID")
    private long bosClaimLargeId;

    @ApiModelProperty(value = "BOS 클레임 대분류 명")
    private String bosClaimLargeNm;

    @ApiModelProperty(value = "BOS 클레임 중분류 ID")
    private long bosClaimMiddleId;

    @ApiModelProperty(value = "BOS 클레임 중분류 명")
    private String bosClaimMiddleNm;

    @ApiModelProperty(value = "BOS 클레임 소분류 ID")
    private long bosClaimSmallId;

    @ApiModelProperty(value = "BOS 클레임 소분류 명")
    private String bosClaimSmallNm;

    @ApiModelProperty(value = "상세사유")
    private String csReasonMsg;

    @ApiModelProperty(value = "은행코드")
    private String bankCd;

    @ApiModelProperty(value = "은행명")
    private String bankNm;

    @ApiModelProperty(value = "예금주명")
    @UserMaskingUserName
    private String accountHolder;

    @ApiModelProperty(value = "계좌번호")
    @UserMaskingAccountNumber
    private String accountNumber;

    @ApiModelProperty(value = "처리상태")
    private String csRefundApproveCd;

    @ApiModelProperty(value = "처리상태명")
    private String csRefundApproveCdNm;

    @ApiModelProperty(value = "응답데이터")
    private String responseData;
}
