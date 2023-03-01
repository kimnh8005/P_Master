package kr.co.pulmuone.v1.order.schedule.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 스케줄 리스트 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일                		:	작성자      :	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 29.		이규한	최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 상세 스케줄 리스트 Request Dto")
public class OrderDetailScheduleListRequestDto {

	@ApiModelProperty(value = "주문상세 PK")
	private long odOrderDetlId;

	@ApiModelProperty(value = "주문 PK")
	private String odOrderId;

	@ApiModelProperty(value = "주문번호")
	private String odid;

    @ApiModelProperty(value = "I/F 상품ID")
	private	String ilGoodsId;

    @ApiModelProperty(value = "주문상세 상품ID")
	private	String orgIlGoodsId;

    @ApiModelProperty(value = "일일상품 유형(GOODS_DAILY_TP : GREENJUICE/BABYMEAL/EATSSLIM )")
    private String goodsDailyTp;

    @ApiModelProperty(value = "수령인 우편번호")
    private String recvZipCd;

    @ApiModelProperty(value = "건물번호")
    private String recvBldNo;

    @ApiModelProperty(value = "변경 적용일(YYYY-MM-DD)")
    private String changeDate;

    @ApiModelProperty(value = "리스트 TYPE")
    private String listType;

    @ApiModelProperty(value = "건너뛰기 주문 상세 일일배송 스케줄 PK")
    private String skipOdOrderDetlDailySchSeq;

    @ApiModelProperty(value = "주문자ID", hidden = true)
    private Long urUserId;

	@ApiModelProperty(value = "출고처 PK")
	private long urWarehouseId;

	@ApiModelProperty(value = "최초 주문 수량")
	private int orderCnt;

    @ApiModelProperty(value = "검색 리스트")
    private ArrayList<String> codeSearchList;

	@ApiModelProperty(value = "내맘대로 여부")
	private String promotionYn;

	@ApiModelProperty(value = "주문상세 순번(라인번호) 주문번호에 대한 순번")
	private int odOrderDetlSeq;

    @ApiModelProperty(value = "마감시간 여부")
    private String cutoffTimeYn;

    @ApiModelProperty(value = "마지막 배송일자")
    private String lastDeliveryDt;

    @ApiModelProperty(value = "배송기간")
    private String goodsCycleTermTp;
}