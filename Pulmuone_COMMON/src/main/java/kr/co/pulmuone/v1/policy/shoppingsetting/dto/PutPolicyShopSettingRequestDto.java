package kr.co.pulmuone.v1.policy.shoppingsetting.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = " PutShopSettingRequestDto")
public class PutPolicyShopSettingRequestDto extends BaseRequestDto {

    @ApiModelProperty(value = "", required = false)
    String updateData;

    @ApiModelProperty(value = "이미지파일")
    String addFile;

	@ApiModelProperty(value = "이미지파일 리스트")
	List<FileVo> addFileList;

    @ApiModelProperty(value = "", hidden = true)
    List<PutPolicyShopSettingRequestSaveDto> updateRequestDtoList = new ArrayList<>();

}
