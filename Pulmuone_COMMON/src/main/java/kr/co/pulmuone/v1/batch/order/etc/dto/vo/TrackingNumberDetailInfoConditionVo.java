package kr.co.pulmuone.v1.batch.order.etc.dto.vo;

import com.fasterxml.jackson.annotation.JsonAlias;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * I/F 송장정보 조회완료 상세 정보 Vo
 * </PRE>
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
public class TrackingNumberDetailInfoConditionVo {

	@ApiModelProperty(value = "Header와 Line의 join key. orderSeqNo(통합몰에서 주문번호와 별도로 구분하기 위한 고유값으로 생성함)")
    private String hrdSeq;

    @ApiModelProperty(value = "ERP 전용 key 값. 온라인 order key값")
    private String oriSysSeq;

    @ApiModelProperty(value = "송장번호")
    private String dlvNo;

    @ApiModelProperty(value = "주문상품 순번")
    private String ordNoDtl;

    @JsonAlias({ "schLinNo" })
    @ApiModelProperty(value = "스케줄라인번호")
    private String schLinNo;
}