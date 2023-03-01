package kr.co.pulmuone.v1.system.log.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "메뉴사용이력 로그 Result Vo")
public class MenuOperLogResultVo {

	@ApiModelProperty(value = "메뉴명")
	String menuName;

	@ApiModelProperty(value = "URL명(시스템명)")
	String urlName;

	@ApiModelProperty(value = "아이디")
	String loginId;

	@ApiModelProperty(value = "관리자명")
	String loginName;

	@ApiModelProperty(value = "IP")
	String ip;

	@ApiModelProperty(value = "등록일자")
	String createDate;


}
