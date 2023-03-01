package kr.co.pulmuone.v1.user.buyer.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "PutBuyerNormalRequestDto")
public class PutBuyerNormalRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "회원 ID")
	private String urUserId;

	@ApiModelProperty(value = "정상전환 사유")
	private String reason;

    @ApiModelProperty(value = "파일정보")
    private String addFile;

    @ApiModelProperty(value = "파일 리스트")
    private List<FileVo> addFileList;

	@ApiModelProperty(value = "회원상태변경 PK")
	private String urBuyerStatusLogId;

	@ApiModelProperty(value = "첨부파일 삭제여부")
	private String uploadFileDeleteYn;

}
