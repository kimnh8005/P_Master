package kr.co.pulmuone.v1.user.store.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.user.store.dto.vo.StoreDeliveryAreaVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "배송권역 관리 목록 조회 Response")
public class StoreDeliveryAreaListResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "리스트")
	private	 List<StoreDeliveryAreaVo>	rows;

    @ApiModelProperty(value = "전체 데이터 건수")
	private	long	total;

}
