package kr.co.pulmuone.v1.user.buyer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "사용자 탈퇴 RequestDto")
public class UserDropRequestDto {

    @ApiModelProperty(value = "유저 PK", hidden = true)
    private Long urUserId;

    @ApiModelProperty(value = "탈퇴사유 PK")
    private Long code;

    @ApiModelProperty(value = "탈퇴 상세")
    private String comment;

    @ApiModelProperty(value = "생성자 PK", hidden = true)
    private Long createId;

    @ApiModelProperty(value = "탈퇴회원정보 PK", hidden = true)
    private Long urUserDropId;

    @ApiModelProperty(value = "탈퇴시점의 회원상태")
    private String status;

}