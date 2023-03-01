package kr.co.pulmuone.v1.statics.pm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.statics.pm.dto.vo.PromotionStaticsVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "PromotionStaticsResponseDto")
public class PromotionStaticsResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "결과코드")
    private String resultCode;

    @ApiModelProperty(value = "결과메시지")
    private String resultMessage;

    @ApiModelProperty(value = "리스트전체건수")
    private long total;

    @ApiModelProperty(value = "리스트")
    private List<PromotionStaticsVo> rows;
}
