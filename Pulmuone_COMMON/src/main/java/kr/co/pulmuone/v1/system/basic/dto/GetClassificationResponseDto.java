package kr.co.pulmuone.v1.system.basic.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " GetClassificationListResponseDto")
public class GetClassificationResponseDto extends BaseResponseDto {

    private GetClassificationResultVo rows;

}