package kr.co.pulmuone.v1.user.urcompany.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "GetStoreListRequestDto")
public class GetStoreListRequestDto extends BaseRequestDto{

	@ApiModelProperty(value = "거래처 ID")
	private String urClientId;

	@ApiModelProperty(value = "공급처 ID")
	private String urSupplierId;

	@ApiModelProperty(value = "접근권한 매장 ID 리스트")
    private List<String> listAuthStoreId;

}
