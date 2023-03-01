package kr.co.pulmuone.v1.promotion.shoplive.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ShopliveInfoRequestDto {

    @ApiModelProperty(value = "샵라이브 PK")
    private Long evShopliveId;

    @ApiModelProperty(value = "유저 PK", hidden = true)
    private Long urUserId;

    @ApiModelProperty(value = "유저 명", hidden = true)
    private String userNm;

    @ApiModelProperty(value = "유저 상태", hidden = true)
    private String userStatus;

    @ApiModelProperty(value = "유저 등급", hidden = true)
    private Long urGroupId;

    @ApiModelProperty(value = "회원 로그인 ID", hidden = true)
    private String loginId;
}
