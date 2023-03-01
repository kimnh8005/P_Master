package kr.co.pulmuone.v1.order.order.dto.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 관련 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 02.            홍진영         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "주문 상세 일일배송 패턴 OD_ORDER_DETL_DAILY VO")
public class OrderDetlDailyVo {

    @ApiModelProperty(value = "주문 상세 일일배송 패턴 PK")
    private long odOrderDetlDailyId;

    @ApiModelProperty(value = "주문 PK")
    private long odOrderId;

    @ApiModelProperty(value = "주문 상세 PK")
    private long odOrderDetlId;

    @ApiModelProperty(value = "주문상세 순번")
    private long odOrderDetlSeq;

    @ApiModelProperty(value = "상품 코드 PK")
    private long ilGoodsId;

    @ApiModelProperty(value = "배송주기 공통코드:GOODS_CYCLE_TP")
    private String goodsCycleTp;

    @ApiModelProperty(value = "배송기간 공통코드:GOODS_CYCLE_TERM_TP")
    private String goodsCycleTermTp;

    @ApiModelProperty(value = "월요일 수량")
    private int monCnt;

    @ApiModelProperty(value = "화요일 수량")
    private int tueCnt;

    @ApiModelProperty(value = "수요일 수량")
    private int wedCnt;

    @ApiModelProperty(value = "목요일 수량")
    private int thuCnt;

    @ApiModelProperty(value = "금요일 수량")
    private int friCnt;

    @ApiModelProperty(value = "세트수량")
    private int setCnt;

    @ApiModelProperty(value = "최초 구매 수량")
    private int orderCnt;

    @ApiModelProperty(value = "알러지식단 여부 Y:알러지식단")
    private String allergyYn;

    @ApiModelProperty(value = "일괄배송 여부 Y: 일괄배송")
    private String dailyBulkYn;

    @ApiModelProperty(value = "배송장소코드")
    private String storeDeliveryType;

    @ApiModelProperty(value = "스토어(매장/가맹점) PK")
    private Long urStoreId;

    @ApiModelProperty(value = "스케쥴 생성 여부 / 베이비밀은 I/F여부")
    private String scheduleYn;

    @ApiModelProperty(value = "일일 패키지 여부")
    private String dailyPackYn;
}
