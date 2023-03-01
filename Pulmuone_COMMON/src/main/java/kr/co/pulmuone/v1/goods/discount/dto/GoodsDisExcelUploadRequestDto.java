package kr.co.pulmuone.v1.goods.discount.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.goods.discount.dto.vo.GoodsDiscountVo;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistApprRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GoodsDisExcelUploadRequestDto")
public class GoodsDisExcelUploadRequestDto extends BaseRequestDto{

	@ApiModelProperty(value = "업로드")
	private String upload;

    @ApiModelProperty(value = "업로드 리스트")
    private List<GoodsDiscountVo> uploadList;
	
	@ApiModelProperty(value = "승인 관리자", required = false)
	private List<GoodsRegistApprRequestDto> goodsDiscountApproList;
}
