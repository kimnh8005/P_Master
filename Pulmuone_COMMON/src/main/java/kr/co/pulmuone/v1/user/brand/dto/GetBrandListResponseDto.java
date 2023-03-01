package kr.co.pulmuone.v1.user.brand.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.user.brand.dto.vo.GetBrandListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "브랜드 조회 Response")
public class GetBrandListResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "브랜드 목록")
	private	List<GetBrandListResultVo>	rows;

    @ApiModelProperty(value = "브랜드 조회 건수")
	private	long	total;

}
