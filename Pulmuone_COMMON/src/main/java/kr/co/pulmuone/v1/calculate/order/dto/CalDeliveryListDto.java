package kr.co.pulmuone.v1.calculate.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 택배비 내역 조회 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 03. 05.	이명수		최초작성
 * =======================================================================
 * </PRE>
 */
@Setter
@Getter
@ToString
@ApiModel(description = "택배비 내역 조회 Request Dto")
public class CalDeliveryListDto {

    @ApiModelProperty(value = "상품정산구분")
    private String divNm;

    @ApiModelProperty(value = "BOS 주문번호")
    private String odid;

    @ApiModelProperty(value = "외부몰 주문번호")
    private String outmallId;

    @ApiModelProperty(value = "수집몰 주문번호")
    private String collectionMallId;

    @ApiModelProperty(value = "판매처")
    private String sellersNm;

    @ApiModelProperty(value = "출고처 명")
    private String warehouseName;

    @ApiModelProperty(value = "매장명")
    private String storeName;

    @ApiModelProperty(value = "공급업체 명")
    private String compNm;

    @ApiModelProperty(value = "배송비번호")
    private String odShippingPriceId;

    @ApiModelProperty(value = "조건 배송비 구분")
    private String conditionTpNm;

    @ApiModelProperty(value = "귀책구분")
    private String targetTp;

    @ApiModelProperty(value = "배송비(클레임배송비)")
    private Long shippingPrice;

    @ApiModelProperty(value = "환불배송비")
    private Long returnShippingPrice;

    @ApiModelProperty(value = "배송비할인금액")
    private Long shippingDiscountPrice;

    @ApiModelProperty(value = "배송중일자(반품승인일자)")
    private String settleDt;

    @ApiModelProperty(value = "배송비 합계")
    private Long totalTaxablePrice;

    @ApiModelProperty(value = "PG구분")
    private String pgServiceNm;

}
