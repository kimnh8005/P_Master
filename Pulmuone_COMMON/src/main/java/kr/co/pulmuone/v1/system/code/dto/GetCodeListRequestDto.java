package kr.co.pulmuone.v1.system.code.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@ApiModel(description = "GetCodeListRequestDto")
public class GetCodeListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "공통 코드 마스터 시퀀스 아이디")
	private String stCommonCodeMasterId;

    @ApiModelProperty(value = "사용여부")
    private String useYn;

    @Builder
    public GetCodeListRequestDto(String stCommonCodeMasterId, String useYn) {
        this.stCommonCodeMasterId = stCommonCodeMasterId;
        this.useYn = useYn;
    }
}
