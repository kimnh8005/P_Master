package kr.co.pulmuone.v1.user.certification.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetCertificationResultVo")
public class GetCertificationResultVo
{
 	@ApiModelProperty(value = "회원이름")
 	private String userName;

 	@ApiModelProperty(value = "핸드폰번호")
 	private String mobile;

 	@ApiModelProperty(value = "생년월일")
 	private String birth;

 	@ApiModelProperty(value = "기존 탈퇴 회원 여부(Y)")
 	private String beforeUserDropYn;

 	@ApiModelProperty(value = "기존회원 아이디 ")
 	private String asisLoginId;

 	@ApiModelProperty(value = "기존회원 배송지 우편번호")
 	private String asisReceiverZipCode;

 	@ApiModelProperty(value = "기존회원 배송지 주소1")
 	private String asisReceiverAddress1;

 	@ApiModelProperty(value = "기존회원 배송지 주소2")
 	private String asisReceiverAddress2;

 	@ApiModelProperty(value = "기존회원 배송지 건물 관리 번호")
 	private String asisBuildingCode;
}
