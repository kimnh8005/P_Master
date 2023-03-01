package kr.co.pulmuone.v1.user.warehouse.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.user.warehouse.dto.vo.ShippingTemplateVo;
import kr.co.pulmuone.v1.user.warehouse.dto.vo.WarehouseResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "배송정책 설정 Response")
public class WarehouseTemplateResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "출고처 탬플릿")
	private WarehouseResultVo  warehouseResultTemplate;

    @ApiModelProperty(value = "배송정책 정보 리스트")
    private List<ShippingTemplateVo>  shippingTemplateList;
}
