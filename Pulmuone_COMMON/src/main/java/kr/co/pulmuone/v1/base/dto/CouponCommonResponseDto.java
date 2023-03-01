package kr.co.pulmuone.v1.base.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.base.dto.vo.PmCouponCommonResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "CouponCommonResponseDto")
public class CouponCommonResponseDto  extends BaseResponseDto {

	@ApiModelProperty(value = "적립금 내역 조회 리스트")
	private	List<PmCouponCommonResultVo> rows;


}
