package kr.co.pulmuone.v1.order.order.dto.mall;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 정보
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 03. 14.	이명수		최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@ToString
@ApiModel(description = "주문클레임 정보")
public class MallClaimInfoDto {


    @ApiModelProperty(value = "클레임 상태구분")
    private String claimStatusTp;

    @ApiModelProperty(value = "클레임 상태구분 명")
    private String claimStatusTpNm;

    @ApiModelProperty(value = "사유")
    private String reasonMsg;

    @ApiModelProperty(value = "상세사유")
    private String claimReasonMsg;

}
