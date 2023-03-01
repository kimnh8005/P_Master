package kr.co.pulmuone.v1.batch.order.etc.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * I/F 송장정보 조회완료 상세 정보 Vo
 * </PRE>
 */

@Builder
@Getter
@ToString
public class UnreleasedDetailConditionVo {

	@ApiModelProperty(value = "송장정보 조회완료 상세 정보")
    private UnreleasedDetailInfoConditionVo condition;
}
