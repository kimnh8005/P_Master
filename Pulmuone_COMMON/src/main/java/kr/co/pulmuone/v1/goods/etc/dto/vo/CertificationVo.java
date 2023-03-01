package kr.co.pulmuone.v1.goods.etc.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품인증정보 Vo")
public class CertificationVo {

	@ApiModelProperty(value = "PK")
	private long ilCertificationId;

	@ApiModelProperty(value = "인증정보명")
	private String certificationName;

	@ApiModelProperty(value = "사용여부")
	private String useYn;

	@ApiModelProperty(value = "상세정보")
	private String defaultCertificationDescribe;

	@ApiModelProperty(value = "인증이미지 경로")
	private String imagePath;

	@ApiModelProperty(value = "인증이미지 파일명")
	private String imageName;

	@ApiModelProperty(value = "인증이미지 원본파일명")
	private String imageOriginName;

	@ApiModelProperty(value = "등록자")
	private String createLoginId;

	@ApiModelProperty(value = "등록자명")
	@UserMaskingUserName
	private String createLoginName;

	@ApiModelProperty(value = "등록일")
	private String createDate;

	@ApiModelProperty(value = "수정자")
	private String modifyLoginId;

	@ApiModelProperty(value = "수정자명")
	@UserMaskingUserName
	private String modifyLoginName;

	@ApiModelProperty(value = "수정일")
	private String modifyDate;

}