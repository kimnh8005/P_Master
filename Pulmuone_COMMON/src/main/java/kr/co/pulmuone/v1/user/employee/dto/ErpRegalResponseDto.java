package kr.co.pulmuone.v1.user.employee.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.user.employee.dto.vo.ErpRegalVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "법인정보 Response")
public class ErpRegalResponseDto{

	@ApiModelProperty(value = "법인정보 리스트")
	private	List<ErpRegalVo> rows;

	@ApiModelProperty(value = "법인정보 총 갯수")
	private long total;

    @ApiModelProperty(value = "최근 업데이트 일자")
    private String lastUpdateDate;

}
