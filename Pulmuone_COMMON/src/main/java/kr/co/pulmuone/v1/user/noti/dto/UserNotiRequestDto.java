package kr.co.pulmuone.v1.user.noti.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@ApiModel(description = "회원 알림 요청 DTO")
public class UserNotiRequestDto {

    @ApiModelProperty(value = "유저 PK List")
    private List<Long> urUserIdList;

    @ApiModelProperty(value = "알림 유형")
    private String userNotiType;

    @ApiModelProperty(value = "알림 내용")
    private String notiMessage;

    @ApiModelProperty(value = "알림 대상 유형")
    private String targetType;

    @ApiModelProperty(value = "알림 대상 PK")
    private String targetPk;

}
