package kr.co.pulmuone.v1.send.device.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.send.device.dto.vo.GetDeviceListResultVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@ApiModel(description = "APP 설치 단말기 목록 조회 Response")
public class GetDeviceListResponseDto extends BaseResponseDto{

    @ApiModelProperty(value = "APP 설치 단말기 목록 조회 리스트")
	private	List<GetDeviceListResultVo>	rows;

    @ApiModelProperty(value = "APP 설치 단말기 목록 조회 카운트")
	private	int	total;

}
