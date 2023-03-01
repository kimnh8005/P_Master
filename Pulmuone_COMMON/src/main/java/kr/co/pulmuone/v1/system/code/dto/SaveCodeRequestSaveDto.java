package kr.co.pulmuone.v1.system.code.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = " SaveCodeRequestSaveDto")
public class SaveCodeRequestSaveDto extends BaseRequestDto {

	@ApiModelProperty(value = "공통 코드 마스터 시퀀스 아이디")
	String stCommonCodeMasterId;

	@ApiModelProperty(value = "공통 마스터 코드")
    String commonMasterCode;

	@ApiModelProperty(value = "공통 코드 시퀀스 아이디")
	String stCommonCodeId;

	@ApiModelProperty(value = "공통코드")
    String commonCode;

	@ApiModelProperty(value = "사전명")
    String dictionaryName;

	@ApiModelProperty(value = "글로벌사전마스터아이디")
	String gbDictionaryMasterId;

	@ApiModelProperty(value = "순번")
	String sort;

	@ApiModelProperty(value = "사용여부")
    String useYn;

	@ApiModelProperty(value = "설명")
    String comment;

	@ApiModelProperty(value = "임시값")
    String attribute1;

	@ApiModelProperty(value = "임시값2")
    String attribute2;

	@ApiModelProperty(value = "임시값3")
    String attribute3;
}
