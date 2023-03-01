package kr.co.pulmuone.v1.promotion.coupon.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "CouponListResponseDto")
public class CouponListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "쿠폰목록  조회 리스트")
	private	List<CouponListResultVo> rows;

	@ApiModelProperty(value = "쿠폰목록 조회 총 Count")
	private long total;

}
