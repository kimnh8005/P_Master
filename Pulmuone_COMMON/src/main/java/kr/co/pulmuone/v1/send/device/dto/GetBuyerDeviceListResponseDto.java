package kr.co.pulmuone.v1.send.device.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.send.device.dto.vo.GetBuyerDeviceListResultVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@ApiModel(description = "푸시 가능 회원 조회 Response")
public class GetBuyerDeviceListResponseDto extends BaseResponseDto{

    @ApiModelProperty(value = "푸시 가능 회원 조회 리스트")
	private	List<GetBuyerDeviceListResultVo>	rows;

    @ApiModelProperty(value = "푸시 가능 회원 조회 카운트")
	private	int	total;

}
