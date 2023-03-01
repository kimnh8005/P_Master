package kr.co.pulmuone.v1.outmall.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallTrackingNumberHistVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "외부몰 배송정보 송장등록이력 response Dto")
public class OutMallTrackingNumberHistResponseDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "")
	private	List<OutMallTrackingNumberHistVo> rows;

	@ApiModelProperty(value = "")
	private int total;

	@Builder
	public OutMallTrackingNumberHistResponseDto(int total, List<OutMallTrackingNumberHistVo> rows) {
		this.total = total;
		this.rows = rows;
	}

}

