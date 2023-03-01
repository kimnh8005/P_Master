package kr.co.pulmuone.v1.user.brand.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.user.brand.dto.vo.DisplayBrandListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "브랜드 조회 Response")
public class DisplayBrandListResponseDto extends BaseResponseDto{

    @ApiModelProperty(value = "전시브랜드 목록")
	private	List<DisplayBrandListResultVo>	rows;

    @ApiModelProperty(value = "전시 브랜드 조회 건수")
	private	long	total;
}
