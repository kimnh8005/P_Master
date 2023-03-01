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
@ApiModel(description = "PutNoticeSetRequestDto")
public class PutNoticeSetRequestDto extends BaseRequestDto {

	@ApiModelProperty(value = "")
    String notificationYn;

	@ApiModelProperty(value = "")
    String updateData;

	@ApiModelProperty(value = "")
    List<PutNoticeSetRequestSaveDto> updateRequestDtoList;

}
