package kr.co.pulmuone.v1.policy.shippingtemplate.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.policy.shippingtemplate.dto.vo.ShippingTemplateVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "배송정책설정 목록 Response")
public class ShippingTemplateResponseDto{

    @ApiModelProperty(value = "배송정책 설정 조회 리스트")
	private	List<ShippingTemplateVo> rows;

    @ApiModelProperty(value = "배송정책 설정 조회 카운트")
	private	long total;
}
