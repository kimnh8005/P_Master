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
public class UnreleasedDetailVo {

    @JsonAlias({ "ifSeq" })
    @ApiModelProperty(value = "중계시스템 고유번호")
    private String ifSeq;

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

	@JsonAlias({ "misRsn" })
    @ApiModelProperty(value = "미출사유")
    private String misRsn;

    @JsonAlias({ "itfMisDat" })
    @ApiModelProperty(value = "미출 정보 등록 일시 (YYYYMMDDHHMISS)")
    private String itfMisDat;

    @JsonAlias({ "misCnt" })
    @ApiModelProperty(value = "미출 수량")
    private String misCnt;

	@JsonAlias({ "itfMisFlg" })
    @ApiModelProperty(value = "미출 인터페이스 수신여부(통합몰에서 가져간 후 Y로 업데이트, 최초값은 N)")
    private String itfMisFlg;

    @JsonAlias({ "misYn" })
    @ApiModelProperty(value = "미출 여부")
    private String misYn;

    @JsonAlias({ "misRepDat" })
    @ApiModelProperty(value = "미출대체일자")
    private String misRepDat;

    @JsonAlias({ "misSndMsg" })
    @ApiModelProperty(value = "미출 상세사유")
    private String misSndMsg;

    @JsonAlias({ "schLinNo" })
    @ApiModelProperty(value = "주문상품 스케줄라인 순번")
    private String schLinNo;
}