package kr.co.pulmuone.v1.promotion.coupon.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.OrganizationPopupListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "OrganizationPopupListResponseDto")
public class OrganizationPopupListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "조직 조회 리스트")
	private	List<OrganizationPopupListResultVo> rows;

	@ApiModelProperty(value = "조직 조회 총 Count")
	private long total;
}
