package kr.co.pulmuone.v1.batch.order.etc.dto.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.api.constant.SourceServerTypes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * I/F 미출 정보 Vo
 * </PRE>
 */

@Getter
@Setter
@ToString
public class UnreleasedInfoVo {

	@JsonAlias({ "ifSeq" })
	@ApiModelProperty(value = "중계시스템 고유번호")
    private String ifSeq;

	@JsonAlias({ "line" })
    @ApiModelProperty(value = "I/F 미출 상세 정보 리스트")
    private List<UnreleasedDetailVo> unreleasedDetailList;

	@JsonAlias({ "hrdSeq" })
    @ApiModelProperty(value = "Header와 Line의 join key. orderSeqNo(통합몰에서 주문번호와 별도로 구분하기 위한 고유값으로 생성함)")
    private String hrdSeq;

	@JsonAlias({ "oriSysSeq" })
    @ApiModelProperty(value = "ERP 전용 key 값. 온라인 order key값")
    private String oriSysSeq;

	@JsonAlias({ "ordNum" })
    @ApiModelProperty(value = "통합몰 주문번호")
    private String odid;

	@JsonAlias({ "rn" })
    @ApiModelProperty(value = "조회결과 순번")
    private String rowNum;

	@JsonAlias({ SourceServerTypes.CODE_KEY })
    @ApiModelProperty(value = "입력 시스템 코드값")
    private String srcSvr;
}