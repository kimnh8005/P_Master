package kr.co.pulmuone.v1.goods.stock.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "StockExcelUploadRequestDto")
public class StockExcelUploadRequestDto extends BaseRequestDto {

    @ApiModelProperty(value = "업로드")
	private String upload;

    @ApiModelProperty(value = "업로드 리스트")
    private List<StockExcelUploadListRequestDto> uploadList;

}