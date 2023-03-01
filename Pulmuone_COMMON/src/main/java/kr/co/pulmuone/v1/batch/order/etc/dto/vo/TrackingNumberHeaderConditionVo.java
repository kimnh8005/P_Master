package kr.co.pulmuone.v1.batch.order.etc.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * I/F 송장정보 조회완료 기본 정보 Vo
 * </PRE>
 */

@Builder
@Getter
@ToString
public class TrackingNumberHeaderConditionVo {

    @ApiModelProperty(value = "Header와 Line의 join key. orderSeqNo(통합몰에서 주문번호와 별도로 구분하기 위한 고유값으로 생성함)")
    private String hrdSeq;

    @ApiModelProperty(value = "ERP 전용 key 값. 온라인 order key값")
    private String oriSysSeq;

    @ApiModelProperty(value = "통합몰 주문번호")
    private String ordNum;
}
