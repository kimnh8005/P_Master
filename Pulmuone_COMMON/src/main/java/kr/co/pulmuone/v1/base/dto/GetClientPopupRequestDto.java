package kr.co.pulmuone.v1.base.dto;

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
@ApiModel(description = "거래처 팝업 Request")
public class GetClientPopupRequestDto extends BaseRequestDto{

	@ApiModelProperty(value = "거래처코드")
	private String clientCode;

	@ApiModelProperty(value = "거래처명")
	private String clientName;

	@ApiModelProperty(value = "거래처유형")
	private String clientType;

	@ApiModelProperty(value = "거래처유형 리스트")
	private List<String> clientTypeList;
}
