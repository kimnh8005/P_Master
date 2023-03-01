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
public class GetNoticeListResultVo {

	@ApiModelProperty(value = "")
	String no;

	@ApiModelProperty(value = "")
	String csCompanyBbsId;

	@ApiModelProperty(value = "")
	String companyBbsType;

	@ApiModelProperty(value = "")
	String title;

	@ApiModelProperty(value = "")
	String notificationYn;

	@ApiModelProperty(value = "")
	String notificationModeYn;

	@ApiModelProperty(value = "")
	String popupYn;

	@ApiModelProperty(value = "")
	String useYn;

	@ApiModelProperty(value = "")
	String createName;

	@ApiModelProperty(value = "")
	String createDate;

	@ApiModelProperty(value = "")
	String physicalAttachment;

	@ApiModelProperty(value = "")
	String views;


  @ApiModelProperty(value = "")
  String content;

  @ApiModelProperty(value = "")
  String popupDispStartDt;

  @ApiModelProperty(value = "")
  String popupDispEndDt;

  @ApiModelProperty(value = "")
  String popupCoordX;

  @ApiModelProperty(value = "")
  String popupCoordY;

  @ApiModelProperty(value = "")
  String popupDispTodayYn;

  @ApiModelProperty(value = "")
  String createId;

  @ApiModelProperty(value = "")
  String modifyId;

  @ApiModelProperty(value = "")
  String modifyDt;

}
