package kr.co.pulmuone.v1.outmall.sellers.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "판매처 리스트 검색조건 Request Dto")
public class SellersListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "검색조건", required = false)
	private String searchCondition;

	@ApiModelProperty(value = "검색어", required = false)
	private String findKeyword;

	@ApiModelProperty(value = "판매처그룹코드검색", required = false)
	private String findSellersGroupCd;

	@ApiModelProperty(value = "연동여부검색", required = false)
	private String findInterfaceYn;

	@ApiModelProperty(value = "사용여부검색", required = false)
	private String findUseYn;
}

