package kr.co.pulmuone.v1.user.warehouse.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "배송정책 출고처정보 조회 팝업 Request")
public class WarehouseTemplateRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "출고처 ID")
    private Long urWarehouseId;


}
