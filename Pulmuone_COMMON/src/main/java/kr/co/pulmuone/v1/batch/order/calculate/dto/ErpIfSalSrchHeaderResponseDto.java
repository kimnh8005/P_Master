package kr.co.pulmuone.v1.batch.order.calculate.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 매출 확정된 내역 조회 Header ResponseDto
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "ErpIfSalSrchHeaderResponseDto")
public class ErpIfSalSrchHeaderResponseDto {

    @ApiModelProperty(value = "중계시스템 고유번호")
    private String ifSeq;

    @ApiModelProperty(value = "입력시스템 코드값")
    private String srcSvr;

    @ApiModelProperty(value = "Header와 Line의 join Key")
    private String hrdSeq;

    @ApiModelProperty(value = "ERP 전용 key 값")
    private String oriSysSeq;

    @ApiModelProperty(value = "Header 유형 주문")
    private String hdrTyp;

    @ApiModelProperty(value = "통합몰 주문번호")
    private String ordNum;

    @ApiModelProperty(value = "순번")
    private String rn;

    @ApiModelProperty(value = "매출 확정된 내역 조회 Line ResponseDto 리스트")
    private List<ErpIfSalSrchLineResponseDto> line;

}
