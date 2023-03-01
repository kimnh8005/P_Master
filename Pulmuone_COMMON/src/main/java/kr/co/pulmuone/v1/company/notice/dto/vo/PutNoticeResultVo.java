package kr.co.pulmuone.v1.company.notice.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetNoticeResultVo")
public class PutNoticeResultVo {

	@ApiModelProperty(value = "")
	String csCompanyBbsId;

}
