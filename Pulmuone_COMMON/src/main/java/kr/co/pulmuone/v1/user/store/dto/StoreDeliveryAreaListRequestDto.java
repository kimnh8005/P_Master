package kr.co.pulmuone.v1.user.store.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "배송권역 관리 목록 조회 Request")
public class StoreDeliveryAreaListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "검색유형")
	private String searchType;

	@ApiModelProperty(value = "매장코드 검색어")
	private String searchValue;

	@ApiModelProperty(value = "매장명 검색어")
	private String searchName;

	@ApiModelProperty(value = "우편번호 검색어")
	private String searchZip;

	@ApiModelProperty(value = "매장/가맹점 타입")
	private String storeType;

	@ApiModelProperty(value = "노출여부")
	private String searchDisplayYn;

	@ApiModelProperty(value = "매장유형")
	private String searchStoreType;

	@ApiModelProperty(value = "O2O 매장여부")
	private String searchO2oShopYn;

	@ApiModelProperty(value = "매장유형")
	private String searchShopType;

}
