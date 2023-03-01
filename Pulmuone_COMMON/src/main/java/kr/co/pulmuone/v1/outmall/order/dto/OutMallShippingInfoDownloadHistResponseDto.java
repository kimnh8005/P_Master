package kr.co.pulmuone.v1.outmall.order.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallShippingExceldownHistVo;
import kr.co.pulmuone.v1.user.buyer.dto.GetBuyerListResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetBuyerListResultVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "외부몰 배송정보 내역 다운로드 내역 팝업 response Dto")
public class OutMallShippingInfoDownloadHistResponseDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "")
	private	List<OutMallShippingExceldownHistVo> rows;

	@ApiModelProperty(value = "")
	private int total;

	@Builder
	public OutMallShippingInfoDownloadHistResponseDto(int total, List<OutMallShippingExceldownHistVo> rows) {
		this.total = total;
		this.rows = rows;
	}

}

