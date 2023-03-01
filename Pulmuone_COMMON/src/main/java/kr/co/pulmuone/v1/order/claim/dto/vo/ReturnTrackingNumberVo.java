package kr.co.pulmuone.v1.order.claim.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * <PRE>
 * Forbiz Korea
 * 회수 송장 번호 VO
 * </PRE>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "회수 송장 번호 VO")
public class ReturnTrackingNumberVo {

    @ApiModelProperty(value = "PK")
    private String odReturnTrackingNumberId;

    @ApiModelProperty(value = "주문 클레임 상세 PK")
    private String odClaimDetlId;

    @ApiModelProperty(value = "택배사설정 PK")
    private String psShippingCompId;

    @ApiModelProperty(value = "개별송장번호")
    private String trackingNo;

    @ApiModelProperty(value = "ERP 연동 택배사코드")
    private String takCode;

    @ApiModelProperty(value = "순서")
    private String sort;

    @ApiModelProperty(value = "등록자")
    private String createId;

    @ApiModelProperty(value = "등록일")
    private LocalDate createDt;

    @ApiModelProperty(value = "택배사코드")
    private String logisticsCd;




}
