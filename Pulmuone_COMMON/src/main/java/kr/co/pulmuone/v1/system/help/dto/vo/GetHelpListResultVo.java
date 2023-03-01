package kr.co.pulmuone.v1.system.help.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetHelpListResultVo")
public class GetHelpListResultVo {

	@ApiModelProperty(value = "번호")
	private int no;

	@ApiModelProperty(value = "아이디")
	private String id;

	@ApiModelProperty(value = "타이틀")
	private String title;

	@ApiModelProperty(value = "컨텐츠")
	private String content;

	@ApiModelProperty(value = "컨텐츠(태그제외)")
	private String contentPlain;

	@ApiModelProperty(value = "구분코드")
	private String divisionCode;

	@ApiModelProperty(value = "구분코드명")
	private String divisionCodeName;

	@ApiModelProperty(value = "사용여부")
	private String useYn;

	@ApiModelProperty(value = "생성일자")
	private String createDate;

	@ApiModelProperty(value = "수정일자")
	private String modifyDate;

}
