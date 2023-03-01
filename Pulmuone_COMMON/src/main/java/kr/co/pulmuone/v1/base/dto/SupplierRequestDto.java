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
@ApiModel(description = "공급처 Request")
public class SupplierRequestDto extends BaseRequestDto{

    @ApiModelProperty(value = "공급처ID")
    private Long supplierId;

    @ApiModelProperty(value = "클레임사유 사용여부")
    private String claimReasonYn;

    @ApiModelProperty(value = "접근권한 공급업체 ID 리스트")
    private List<String> listAuthSupplierId;

}
