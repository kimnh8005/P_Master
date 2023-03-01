package kr.co.pulmuone.v1.promotion.adminpointpaymentuse.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.promotion.adminpointpaymentuse.dto.vo.AdminPointPaymentUseVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@ApiModel(description = "관리자 적립금 설정 지급/차감 내역 리스트 조회 ResponseDto")
public class AdminPointPaymentUseListResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "관리자 적립금 설정 지급/차감 내역 리스트")
	private	List<AdminPointPaymentUseVo> rows;

	@ApiModelProperty(value = "관리자 적립금 설정 지급/차감 내역 리스트 총 Count")
	private long total;
}
