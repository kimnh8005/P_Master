package kr.co.pulmuone.v1.user.noti.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "회원 알림 DTO")
public class UserNotiDto {

	@ApiModelProperty(value = "알림 PK")
	private Long urNotiId;

	@ApiModelProperty(value = "회원 PK")
	private Long urUserId;

	@ApiModelProperty(value = "알림타입 - 공통코드")
	private String userNotiType;

	@ApiModelProperty(value = "알림타입 - 공통코드")
	private String userNotiTypeName;

	@ApiModelProperty(value = "알림내용")
	private String notiMsg;

	@ApiModelProperty(value = "알림 읽음 여부 - 알람")
	private String readYn;

	@ApiModelProperty(value = "알림 읽음 여부 - 내용")
	private String clickYn;

	@ApiModelProperty(value = "알림 대상 유형")
	private String targetType;

	@ApiModelProperty(value = "알림 대상 PK")
	private String targetPk;

	@ApiModelProperty(value = "등록일자")
	private String createDate;
}
