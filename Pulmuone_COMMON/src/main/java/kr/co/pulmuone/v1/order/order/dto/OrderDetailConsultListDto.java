package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 상담 리스트 관련 Dto
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
@ApiModel(description = "주문 상세 상담 리스트 관련 Dto")
public class OrderDetailConsultListDto {

    @ApiModelProperty(value = "주문상세 상담 PK")
    private long odConsultId;

    @ApiModelProperty(value = "등록자")
    private String modifyNm;

    @ApiModelProperty(value = "등록자ID")
    private String modifyId;

    @ApiModelProperty(value = "등록일자")
    private String modifyDt;

    @ApiModelProperty(value = "주문상담 구분")
    private String odConsultType;

    @ApiModelProperty(value = "주문상담 내용")
    private String odConsultMsg;

    @ApiModelProperty(value = "작성자 아이디")
    private String loginId;
}
