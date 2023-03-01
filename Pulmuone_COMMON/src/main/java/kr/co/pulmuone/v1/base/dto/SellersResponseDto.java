package kr.co.pulmuone.v1.base.dto;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.base.dto.vo.SellersVo;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "SellersResponseDto")
public class SellersResponseDto extends BaseResponseDto {

	private List<SellersVo> rows;
}
