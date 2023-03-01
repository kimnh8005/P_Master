package kr.co.pulmuone.v1.batch.order.etc.dto.vo;

import com.fasterxml.jackson.annotation.JsonAlias;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.api.constant.TransferServerTypes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * I/F 송장 상세 정보 Vo
 * </PRE>
 */

@Getter
@Setter
@ToString
public class TrackingNumberDetailVo {

	@JsonAlias({ "hrdSeq" })
    @ApiModelProperty(value = "Header와 Line의 join key. orderSeqNo(통합몰에서 주문번호와 별도로 구분하기 위한 고유값으로 생성함)")
    private String hrdSeq;

	@JsonAlias({ TransferServerTypes.CODE_KEY })
	@ApiModelProperty(value = "전송 대상 시스템 구분 코드값")
    private String crpCd;

	@JsonAlias({ "oriSysSeq" })
    @ApiModelProperty(value = "ERP 전용 key 값.온라인 order key값")
    private String oriSysSeq;

	@JsonAlias({ "ordNum" })
    @ApiModelProperty(value = "통합몰 주문번호")
    private String odid;

	@JsonAlias({ "ordNoDtl" })
    @ApiModelProperty(value = "주문상품 순번")
    private String odOrderDetlSeq;

	@JsonAlias({ "takCd" })
    @ApiModelProperty(value = "택배사코드")
    private String psShippingCompId;

    @JsonAlias({ "dlvNo" })
    @ApiModelProperty(value = "송장번호")
    private String trackingNumber;

    @JsonAlias({ "schLinNo" })
    @ApiModelProperty(value = "스케줄라인번호")
    private String odOrderDetlDailySchSeq;

    @ApiModelProperty(value = "주문상세번호")
    private String odOrderDetlId;

    @ApiModelProperty(value = "가맹점코드")
    private String stoCd;
}
