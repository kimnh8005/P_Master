package kr.co.pulmuone.v1.outmall.order.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.policy.fee.dto.OmBasicFeeListDto;
import lombok.*;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OutMallOrderSellersDto {

	@ApiModelProperty(value = "판매처PK")
	private long omSellersId;

	@ApiModelProperty(value = "판매처그룹코드")
	private String sellersGroupCd;

	@ApiModelProperty(value = "판매처에 등록된 공급업체 리스트")
	private List<OmBasicFeeListDto> supplierList;
}
