package kr.co.pulmuone.v1.order.order.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 상담 VO
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
@ApiModel(description = "주문 상담 VO")
public class OrderConsultVo {

    @ApiModelProperty(value = "주문상세 상담 PK")
    private long odConsultId;

    @ApiModelProperty(value = "주문 PK")
    private long odOrderId;

    @ApiModelProperty(value = "주문상담 구분")
    private String odConsultType;

    @ApiModelProperty(value = "주문상담 내용")
    private String odConsultMsg;

    @ApiModelProperty(value = "등록자")
    private long createId;
}

