package kr.co.pulmuone.v1.promotion.manage.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EvUserGroupVo {

  @ApiModelProperty(value = "이벤트 PK")
  private String evEventId;

  @ApiModelProperty(value = "유저등급 PK")
  private Long urGroupId;

  @ApiModelProperty(value = "유저 등급 마스터 명")
  private String groupMasterName;

  @ApiModelProperty(value = "유저 등급 명")
  private String groupName;

  @ApiModelProperty(value = "이벤트접근권한정보PK")
  private String evEventUserGroupId;

}
