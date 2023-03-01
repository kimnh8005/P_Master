package kr.co.pulmuone.v1.system.basic.dto;

import java.util.ArrayList;
import java.util.List;

import kr.co.pulmuone.v1.system.basic.dto.vo.GetClassificationListResultVo;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " GetClassificationListResponseDto")
public class GetClassificationListResponseDto extends BaseResponseDto {

    private List<GetClassificationListResultVo> rows = new ArrayList<>();
    private int total;

}