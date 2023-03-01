package kr.co.pulmuone.v1.base.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "PrivacyMenuOperLogRequestDto")
public class PrivacyMenuOperLogRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "ST_MENU_URL PK")
	String systemMenuUrlId;

	@ApiModelProperty(value = "URL명(시스템명)")
	String urlName;

	@ApiModelProperty(value = "URL")
	String url;

	@ApiModelProperty(value = "CRUD 유형(CRUD_TP : C(CREATE), R(READ), U(UPDATE), D(DELETE), DL(DOWNLOAD), UNDEFINED(미정의))")
	String crudType;

	@ApiModelProperty(value = "parameter value")
	String paramValue;

	@ApiModelProperty(value = "IP")
	String ip;

	@ApiModelProperty(value = "검색구분")
	String searchType;

	@ApiModelProperty(value = "검색값")
	String searchValue;

	@ApiModelProperty(value = "시작일")
	String startCreateDate;

	@ApiModelProperty(value = "종료일")
	String endCreateDate;

	@ApiModelProperty(value = "CRUD 유형 리스트 (파싱 O)")
	private List<String> crudTypeList;

}
