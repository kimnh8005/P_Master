package kr.co.pulmuone.v1.goods.discount.dto;

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
@ApiModel(description = "GoodsDiscountUploadRequestDto")
public class GoodsDiscountUploadRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "상품할인 일괄 업로드 로그 아이디(SEQ)")
	private String logId;

	@ApiModelProperty(value = "상품할인 일괄 업로드 유형(GOODS_DISP(상품할인 일괄업로드), EMPLOYEE_DISC(임직원할인 일괄업로드))")
	private String uploadTp;

	@ApiModelProperty(value = "검색 시작일")
	private String startDate;

	@ApiModelProperty(value = "검색 종료일")
	private String endDate;

	@ApiModelProperty(value = "단일_복수구분값")
	private String searchType;

	@ApiModelProperty(value = "코드검색타입")
	private String codeType;

	@ApiModelProperty(value = "코드검색값")
	private String goodsCodes;

	@ApiModelProperty(value = "상품코드 Array", required = false)
    private ArrayList<String> goodsCodeArray;

	@ApiModelProperty(value = "상품명")
	private String goodsNm;


}
