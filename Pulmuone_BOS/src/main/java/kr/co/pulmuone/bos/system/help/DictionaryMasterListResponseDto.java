package kr.co.pulmuone.bos.system.help;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description = "DictionaryMasterListResponseDto")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DictionaryMasterListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "표준용어 조회 리스트")
	private	List<DictionaryMasterDto> rows;

	@ApiModelProperty(value = "표준용어 조회 총 Count")
	private int total;

	@Builder
	public DictionaryMasterListResponseDto(int total, List<DictionaryMasterDto> rows) {
		this.total = total;
		this.rows = rows;
	}

}
