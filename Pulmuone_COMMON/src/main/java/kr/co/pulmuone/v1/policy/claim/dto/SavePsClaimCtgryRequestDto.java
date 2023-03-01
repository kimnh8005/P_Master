package kr.co.pulmuone.v1.policy.claim.dto;

import java.util.ArrayList;
import java.util.List;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "BOS 클레임 사유 카테고리 저장 요청 DTO")
public class SavePsClaimCtgryRequestDto extends BaseRequestDto {

	@ApiModelProperty(value = "클레임 사유 카테고리",  required = true)
	String categoryCode;

	@ApiModelProperty(value = "등록데이터", required = false)
    String insertData;

    @ApiModelProperty(value = "변경데이터", required = false)
    String updateData;

    @ApiModelProperty(value = "삭제데이터", required = false)
    String deleteData;

    @ApiModelProperty(value = "", hidden = true)
    List<SavePolicyClaimCtgryRequestSaveDto> insertRequestDtoList = new ArrayList<SavePolicyClaimCtgryRequestSaveDto>();

    @ApiModelProperty(value = "", hidden = true)
    List<SavePolicyClaimCtgryRequestSaveDto> updateRequestDtoList = new ArrayList<SavePolicyClaimCtgryRequestSaveDto>();

    @ApiModelProperty(value = "", hidden = true)
    List<SavePolicyClaimCtgryRequestSaveDto> deleteRequestDtoList = new ArrayList<SavePolicyClaimCtgryRequestSaveDto>();

}
