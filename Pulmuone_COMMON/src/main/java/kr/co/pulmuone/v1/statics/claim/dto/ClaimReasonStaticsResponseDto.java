package kr.co.pulmuone.v1.statics.claim.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.statics.claim.dto.vo.ClaimReasonStaticsVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class ClaimReasonStaticsResponseDto {

    @ApiModelProperty(value = "리스트전체건수")
    private long total;

    @ApiModelProperty(value = "리스트")
    private List<ClaimReasonStaticsVo> rows;

}
