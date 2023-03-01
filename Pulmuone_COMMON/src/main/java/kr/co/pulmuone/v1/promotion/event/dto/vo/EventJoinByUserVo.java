package kr.co.pulmuone.v1.promotion.event.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EventJoinByUserVo {

    @ApiModelProperty(value = "댓글구분")
    private String commentValue;

    @ApiModelProperty(value = "댓글내용")
    private String comment;

    @ApiModelProperty(value = "로그인아이디")
    @UserMaskingUserName
    private String loginId;

    @ApiModelProperty(value = "댓글입력일자")
    private String createDate;

    @ApiModelProperty(value = "관리자 댓글 규제여부")
    private String adminSecretYn;

}
