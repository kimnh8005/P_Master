package kr.co.pulmuone.v1.batch.order.etc.dto.vo;

import java.util.List;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * I/F 송장 정보 조회완료 Vo
 * </PRE>
 */

@Builder
@Getter
@ToString
public class UnreleasedInfoUpdateVo {

	@ApiModelProperty(value = "송장정보 조회완료 기본 정보")
    private UnreleasedHeaderConditionVo condition;

	@ApiModelProperty(value = "송장정보 조회완료 상세 정보")
    private List<UnreleasedDetailConditionVo> line;
}
