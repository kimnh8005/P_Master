package kr.co.pulmuone.v1.outmall.sellers.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.outmall.sellers.dto.vo.SellersListVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "외부몰리스트 응답 Response Dto")
public class SellersCodeListResponseDto extends BaseResponseDto {
	@ApiModelProperty(value = "")
	private	List<SellersCodeListDto> rows;

	@ApiModelProperty(value = "")
	private int total;

	@Builder
	public SellersCodeListResponseDto(int total, List<SellersCodeListDto> rows) {
		this.total = total;
		this.rows = rows;
	}
}
