package kr.co.pulmuone.v1.goods.goods.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품 할인 업데이트 리스트 Request")
public class GoodsDisPriceHisRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "상품코드 순으로 정렬")
	private String sort;

	@ApiModelProperty(value = "상품코드검색 타입")
	private String keywordType;

	@ApiModelProperty(value = "상품코드검색값")
	private String keywordVal;

	@ApiModelProperty(value = "승인요청자 구분값")
	private String userType1;

	@ApiModelProperty(value = "승인요청자 검색값")
	private String userTypeVal1;

	@ApiModelProperty(value = "승인완료자 구분값")
	private String userType2;

	@ApiModelProperty(value = "승인완료자 검색값")
	private String userTypeVal2;

	@ApiModelProperty(value = "공급업체 검색값")
	private String supplierId;

	@ApiModelProperty(value = "세금구분")
	private String taxYn;

	@ApiModelProperty(value = "할인구분")
	private String discountTp;

	@ApiModelProperty(value = "할인구분배열")
	private List<String> discountTpList;

	@ApiModelProperty(value = "할인기간검색 구분값")
	private String dateSearchType;

	@ApiModelProperty(value = "기간검색 시작일")
	private String startDate;

	@ApiModelProperty(value = "기간검색 종료일")
	private String endDate;

	@ApiModelProperty(value = "접근권한 출고처 ID 리스트")
    private List<String> listAuthWarehouseId;

	@ApiModelProperty(value = "접근권한 공급처 ID 리스트")
    private List<String> listAuthSupplierId;

}
