package kr.co.pulmuone.v1.batch.order.etc.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * I/F 기타송장 정보 조회완료 Vo
 * </PRE>
 */

@Builder
@Getter
@ToString
public class TrackingNumberEtcInfoUpdateVo {

	@ApiModelProperty(value = "기타송장정보 조회완료 정보")
    private TrackingNumberEtcHeaderConditionVo condition;
}