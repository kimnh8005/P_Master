package kr.co.pulmuone.v1.promotion.coupon.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponApprovalResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "CouponApprovalResponseDto")
public class CouponApprovalResponseDto  extends BaseResponseDto {

	@ApiModelProperty(value = "쿠폰승인 목록 조회 리스트")
	private	List<CouponApprovalResultVo> rows;

	@ApiModelProperty(value = "쿠폰승인 목록 조회 총 Count")
	private long total;

}
