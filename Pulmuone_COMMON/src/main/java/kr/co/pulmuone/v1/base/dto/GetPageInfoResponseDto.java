package kr.co.pulmuone.v1.base.dto;

import java.util.HashMap;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetPageInfoResponseDto")
public class GetPageInfoResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "")
	private	String[] auth;

	@ApiModelProperty(value = "")
	private UserVo session;

	@ApiModelProperty(value = "")
	private String navi;

	@ApiModelProperty(value = "")
	private String title;

	@ApiModelProperty(value = "")
	private HashMap lang;

	@ApiModelProperty(value = "매뉴 도움말 설정 PK")
	private Long stHelpId;
}
