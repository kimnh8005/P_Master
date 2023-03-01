package kr.co.pulmuone.v1.promotion.coupon.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.IssueListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "CouponResponseDto")
public class CouponResponseDto  extends BaseResponseDto {

	@ApiModelProperty(value = "쿠폰지급 내역  조회 리스트")
	private	List<IssueListResultVo> rows;

	@ApiModelProperty(value = "쿠폰지급 내역 조회 총 Count")
	private long total;


	@ApiModelProperty(value = "Update Count")
	private int updateCount;
}
