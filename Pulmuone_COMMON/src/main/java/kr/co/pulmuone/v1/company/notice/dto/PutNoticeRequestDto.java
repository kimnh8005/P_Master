package kr.co.pulmuone.v1.company.notice.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "PutNoticeRequestDto")
public class PutNoticeRequestDto extends BaseRequestDto {

	@ApiModelProperty(value = "")
	String csCompanyBbsId;

	@ApiModelProperty(value = "")
	String companyBbsType;

	@ApiModelProperty(value = "")
	String title;

	@ApiModelProperty(value = "")
	String content;

	@ApiModelProperty(value = "")
	String notificationYn;

	@ApiModelProperty(value = "")
	String popupYn;

	@ApiModelProperty(value = "")
	String popupDisplayStartDate;

	@ApiModelProperty(value = "")
	String popupDisplayEndDate;

	@ApiModelProperty(value = "")
	String popupCoordinateX;

	@ApiModelProperty(value = "")
	String popupCoordinateY;

	@ApiModelProperty(value = "")
	String popupDisplayTodayYn;

	@ApiModelProperty(value = "")
	String useYn;

	@ApiModelProperty(value = "")
    String addFile;

	@ApiModelProperty(value = "")
	List<FileVo> addFileList;
}
