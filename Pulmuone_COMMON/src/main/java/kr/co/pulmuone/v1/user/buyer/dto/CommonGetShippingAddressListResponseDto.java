package kr.co.pulmuone.v1.user.buyer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CommonGetShippingAddressListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "공통 배송지 리스트 조회 ResponseDto")
public class CommonGetShippingAddressListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "배송지 리스트 resultVo")
	private	List<CommonGetShippingAddressListResultVo> rows;

	@ApiModelProperty(value = "배송지 리스트 총 갯수")
	private	int total;

	@ApiModelProperty(value = "주문 상세 배송가능일자리스트")
	private	List<String> scheduleDelvDateList;

	@ApiModelProperty(value = "주문 상세 배송가능일자요일 리스트")
	private List<String> scheduleDelvDayOfWeekList;
}
