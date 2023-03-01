package kr.co.pulmuone.v1.promotion.point.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class PointExpiredForEmailVo {

    @ApiModelProperty(value = "유저 PK")
    private Long urUserId;

    @ApiModelProperty(value = "이름")
    private String userName;

    @ApiModelProperty(value = "이메일")
    private String mail;

    @ApiModelProperty(value = "모바일 전화번호")
    private String mobile;

    @ApiModelProperty(value = "문자수신여부")
    private String smsYn;

    @ApiModelProperty(value = "이메일수신여부")
    private String mailYn;

    @ApiModelProperty(value = "소멸예정일자")
    private String expiredDate;

    @ApiModelProperty(value = "소멸예정 적립금")
    private String pointSum;

    @ApiModelProperty(value = "소멸예정 월")
    private String expiredMonth;

    @ApiModelProperty(value = "소멸예정 정보")
    private List<PointExpiredListForEmailVo> list;


}
