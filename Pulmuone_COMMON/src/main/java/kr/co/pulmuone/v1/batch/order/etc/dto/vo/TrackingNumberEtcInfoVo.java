package kr.co.pulmuone.v1.batch.order.etc.dto.vo;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * I/F 기타 송장 정보 Vo
 * </PRE>
 */

@Getter
@Setter
@ToString
public class TrackingNumberEtcInfoVo {

	@JsonAlias({ "ifSeq" })
	@ApiModelProperty(value = "중계시스템 고유번호")
    private String ifSeq;

	@JsonAlias({ "ordNum" })
    @ApiModelProperty(value = "통합몰 주문번호")
    private String odid;

	@JsonAlias({ "ordNoDtl" })
    @ApiModelProperty(value = "주문상품 순번")
    private String odOrderDetlSeq;

	@JsonAlias({ "dlvTakCd" })
    @ApiModelProperty(value = "택배사코드")
    private String psShippingCompId;

    @JsonAlias({ "dlvNo" })
    @ApiModelProperty(value = "송장번호")
    private String trackingNumber;

	@JsonAlias({ "rn" })
    @ApiModelProperty(value = "조회결과 순번")
    private String rowNum;

    @ApiModelProperty(value = "주문상세번호")
    private String odOrderDetlId;
}