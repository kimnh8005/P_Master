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
public class GetNoticeResultVo {

	@ApiModelProperty(value = "")
	String csCompanyBbsId;

	@ApiModelProperty(value = "")
	String companyBbsType;

	@ApiModelProperty(value = "")
	String companyBbsTypeName;

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
	String createName;

	@ApiModelProperty(value = "")
	String createDate;

	@ApiModelProperty(value = "")
	String modifyName;

	@ApiModelProperty(value = "")
	String modifyDate;
}
