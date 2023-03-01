package kr.co.pulmuone.v1.goods.item.dto;

import java.util.ArrayList;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ItemPoTypeListRequestDto")
public class ItemPoTypeListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "발주유형명")
	private String searchPoTpName;

	@ApiModelProperty(value = "공급업체")
	private String searchUrSupplierId;

	@ApiModelProperty(value = "발주유형구분")
	private String searchPoType;

	@ApiModelProperty(value = "ERP 발주유형")
	private String searchErpTotp;

	@ApiModelProperty(value = "발주일Array", required = false)
    private ArrayList<String> searchScheduledArray;

	@ApiModelProperty(value = "일주일Array", required = false)
    private ArrayList<String> scheduledArray;

	@ApiModelProperty(value = "발주일", required = false)
    private String searchScheduled;

	@ApiModelProperty(value = "발주일검색 타입", required = false)
	private String searchScType;

}
