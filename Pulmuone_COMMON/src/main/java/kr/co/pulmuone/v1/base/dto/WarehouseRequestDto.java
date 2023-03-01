package kr.co.pulmuone.v1.base.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "출고처 Request")
public class WarehouseRequestDto extends BaseRequestDto{

    @ApiModelProperty(value = "출고처 ID")
    private Long warehouseId;

    @ApiModelProperty(value = "공급처ID")
    private Long supplierId;

    @ApiModelProperty(value = "출고처그룹 코드")
    private String warehouseGroupCode;

	@ApiModelProperty(value = "재고발주여부")
	private String stockOrderYn;

    @ApiModelProperty(value = "접근권한 공급업체 ID 리스트")
    private List<String> listAuthSupplierId;

	@ApiModelProperty(value = "접근권한 출고처 ID 리스트")
    private List<String> listAuthWarehouseId;

}
