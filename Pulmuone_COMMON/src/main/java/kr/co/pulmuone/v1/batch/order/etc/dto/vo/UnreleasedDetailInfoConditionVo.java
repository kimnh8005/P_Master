package kr.co.pulmuone.v1.batch.order.etc.dto.vo;

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
public class UnreleasedDetailInfoConditionVo {

	@ApiModelProperty(value = "Header와 Line의 join key. orderSeqNo(통합몰에서 주문번호와 별도로 구분하기 위한 고유값으로 생성함)")
    private String hrdSeq;

    @ApiModelProperty(value = "ERP 전용 key 값. 온라인 order key값")
    private String oriSysSeq;

    @ApiModelProperty(value = "통합몰 주문번호")
    private String ordNum;

    @ApiModelProperty(value = "주문상품 순번")
    private String ordNoDtl;

    @ApiModelProperty(value = "주문상품 스케줄라인 순번")
    private String schLinNo;
}