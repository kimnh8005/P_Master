package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 현금영수증 발급내역 Response Dto
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "현금영수증 발급내역 Response Dto")
public class CashReceiptIssuedListResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "현금영수증 발급리스트")
	private	List<CashReceiptIssuedDto> rows;

    @ApiModelProperty(value = "목록 total")
    private	long total;

}
