package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문상세 상담 관련 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 13.            석세동         최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "주문상세 상담 Request Dto")
public class OrderDetailConsultRequestDto{

    @ApiModelProperty(value = "주문 PK")
    private String odOrderId;

    @ApiModelProperty(value = "주문상세 상담 PK")
    private String odConsultId;

    @ApiModelProperty(value = "주문상담 구분")
    private String odConsultType;

    @ApiModelProperty(value = "주문상담 내용")
    private String odConsultMsg;

    @ApiModelProperty(value = "등록자")
    private String createId;
}

