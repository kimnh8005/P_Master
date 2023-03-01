package kr.co.pulmuone.bos.system.help;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "DictionaryMasterResponseDto")
@NoArgsConstructor
public class DictionaryMasterResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "표준용어 상세 조회 데이터")
	private DictionarySaveRequestDto rows;

	public DictionaryMasterResponseDto(DictionarySaveRequestDto dto) {
		this.rows = dto;
	}
}
