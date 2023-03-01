package kr.co.pulmuone.v1.promotion.coupon.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.UploadInfoVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "CouponUploadResponseDto")
public class CouponUploadResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "파일업로드 리스트")
	private	List<CouponIssueParamDto> rows;

	@ApiModelProperty(value = "파일업로드 조회 총 Count")
	private long total;

}
