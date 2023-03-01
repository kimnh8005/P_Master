package kr.co.pulmuone.v1.user.brand.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.user.brand.dto.vo.GetUrSupplierIdListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetUrSupplierIdListResponseDto")
public class GetUrSupplierIdListResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "공급업체 목록")
	private	List<GetUrSupplierIdListResultVo>	rows;

    @ApiModelProperty(value = "공급업체 조회 건수")
	private	long	total;

}
