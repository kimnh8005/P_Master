package kr.co.pulmuone.v1.user.login.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
@ApiModel(description ="관리자 회원 로그인 최근 접속 정보 Vo")
public class RecentlyLoginResultVo {

    @ApiModelProperty(value = "회원명")
    private String loginName;

    @ApiModelProperty(value = "접속 IP")
    private String addressIp;

    @ApiModelProperty(value = "로그인 시간")
    private String loginDate;

    @ApiModelProperty(value = "회사구분코드")
    private String companyType;

}
