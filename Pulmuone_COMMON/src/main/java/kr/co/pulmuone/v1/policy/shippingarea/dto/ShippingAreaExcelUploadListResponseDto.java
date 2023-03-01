package kr.co.pulmuone.v1.policy.shippingarea.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.policy.shippingarea.dto.vo.ShippingAreaExcelUploadInfoVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@ApiModel(description = "도서산간/배송불가 엑셀업로드 내역 Response Dto")
public class ShippingAreaExcelUploadListResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "count")
    private int total;

    @ApiModelProperty(value = "리스트")
    private List<ShippingAreaExcelUploadInfoVo> rows;
}
