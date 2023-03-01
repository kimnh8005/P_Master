package kr.co.pulmuone.v1.order.delivery.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.order.delivery.dto.vo.OrderTrackingNumberVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* <PRE>
* Forbiz Korea
* 일괄송장 엑셀업로드 Dto
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일				:  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 12. 24.        이규한          	  최초작성
* =======================================================================
* </PRE>
*/

@Getter
@Setter
@ToString
@ApiModel(description = "OrderBulkTrackingNumberExcelUploadRequestDto")
public class OrderBulkTrackingNumberExcelUploadRequestDto extends BaseRequestDto {

	@ApiModelProperty(value = "업로드")
	private String upload;

    @ApiModelProperty(value = "업로드 리스트")
    private List<OrderTrackingNumberVo> uploadList;

    @ApiModelProperty(value = "원본 파일명")
    private String originNm;

    @ApiModelProperty(value = "SMS 발송여부")
    private String sendSmsYn;
}
