package kr.co.pulmuone.v1.promotion.coupon.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.BuyerInfoListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "BuyerInfoResponseDto")
public class BuyerInfoResponseDto  extends BaseResponseDto {

	@ApiModelProperty(value = "쿠폰지급 대상 조회 리스트")
	private	List<BuyerInfoListResultVo> rows;

	@ApiModelProperty(value = "쿠폰지급 조회 총 Count")
	private long total;

	@ApiModelProperty(value = "쿠폰지급 대상 총 Count")
	private int updateCount;

}
