package kr.co.pulmuone.v1.user.buyer.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "회원정보조회 Result")
public class BuyerFromMypageResultVo {

    @ApiModelProperty(value = "이름")
    private String userName;

    @ApiModelProperty(value = "휴대폰번호")
    private String mobile;

    @ApiModelProperty(value = "ID")
    private String loginId;

    @ApiModelProperty(value = "이메일")
    private String mail;

    @ApiModelProperty(value = "추천인아이디")
    private String recommendationUserId;

    @ApiModelProperty(value = "네이버 연결 PK")
    private String urSocialIdNaver;

    @ApiModelProperty(value = "카카오 연결 PK")
    private String urSocialIdKakao;

    @ApiModelProperty(value = "구글 연결 PK")
    private String urSocialIdGoogle;

    @ApiModelProperty(value = "페이스북 연결 PK")
    private String urSocialIdFacebook;

    @ApiModelProperty(value = "애플 연결 PK")
    private String urSocialIdApple;

    @ApiModelProperty(value = "최근본상품 로그 누적 여부")
    private String recentlyViewYn;

    @ApiModelProperty(value = "이메일 수신 동의 여부")
    private String mailYn;

    @ApiModelProperty(value = "SMS 수신 동의 여부")
    private String smsYn;

	@ApiModelProperty(value = "마케팅 활용 동의 여부")
	private String marketingYn;

    @ApiModelProperty(value = "법인명")
    private String erpRegalName;

    @ApiModelProperty(value = "부서명")
    private String erOrganizationName;

    @ApiModelProperty(value = "주문 건수")
    private int orderCount;

}
