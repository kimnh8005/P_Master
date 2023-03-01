package kr.co.pulmuone.v1.system.menu.dto;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.system.menu.dto.vo.GetSystemUrlListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "GetSystemUrlListResponseDto")
public class GetSystemUrlListResponseDto {

	private List<GetSystemUrlListResultVo> rows;

	private int total;
}
