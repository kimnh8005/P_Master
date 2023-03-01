package kr.co.pulmuone.v1.statics.outbound.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.statics.outbound.dto.vo.OutboundStaticsVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "OutboundStaticsResponseDto")
public class OutboundStaticsResponseDto {

    @ApiModelProperty(value = "리스트")
    private List<OutboundStaticsVo> rows;

    @ApiModelProperty(value = "건수")
    private long total;

}
