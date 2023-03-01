package kr.co.pulmuone.v1.company.notice.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "DelNoticeRequestDto")
public class DelNoticeRequestDto extends BaseRequestDto {

	@ApiModelProperty(value = "")
    String deleteData;

	@ApiModelProperty(value = "")
    List<DelNoticeRequestSaveDto> deleteRequestDtoList;

}
