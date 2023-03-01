package kr.co.pulmuone.v1.promotion.exhibit.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ExhibitUserGroupByUserVo {

    @ApiModelProperty(value = "유저 그룹 PK")
    private Long urGroupId;

    @ApiModelProperty(value = "그룹 마스터 명")
    private String groupMasterName;

    @ApiModelProperty(value = "그룹 명")
    private String groupName;

}