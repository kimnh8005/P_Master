package kr.co.pulmuone.v1.promotion.coupon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponDetailVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "CouponDetailResponseDto")
public class CouponDetailResponseDto   extends BaseResponseDto {


	@ApiModelProperty(value = "쿠폰 상세 Vo")
	private	CouponDetailVo rows;

}
