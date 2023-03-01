package kr.co.pulmuone.v1.user.employee.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.user.employee.dto.vo.ErpEmployeeVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ERP 임직원정보 Response")
public class ErpEmployeeResponseDto{

	@ApiModelProperty(value = "임직원정보 리스트")
	private	List<ErpEmployeeVo> rows;

	@ApiModelProperty(value = "임직원정보 총 갯수")
	private long total;

    @ApiModelProperty(value = "최근 업데이트 일자")
    private String lastUpdateDate;

}
