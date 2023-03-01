package kr.co.pulmuone.v1.send.device.dto;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "APP 플랫폼 유형 조회 Request")
public class GetDeviceRequestDto extends BaseRequestDto{

	@ApiModelProperty(value = "사용여부")
	private String useYn;

	@ApiModelProperty(value = "상위 디렉토리 위치")
	private String rootPath;

    @ApiModelProperty(value = "이미지파일")
    String addFile;

	@ApiModelProperty(value = "이미지파일 리스트")
	List<FileVo> addFileList;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "기기타입명")
    private String deviceType;

    @ApiModelProperty(value = "이미지 URL")
	private String imageUrl;

    @ApiModelProperty(value = "환경변수 값")
	private String envVal;

    @ApiModelProperty(value = "환경변수 ID")
	private String envKey;
}
