package kr.co.pulmuone.v1.goods.etc.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "상품인증정보관리상세 RequestDto")
public class CertificationRequestDto extends BaseRequestDto {

	@ApiModelProperty(value = "PK")
	private String ilCertificationId;

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

	@ApiModelProperty(value = "업로드 파일 정보(json형식)")
	private String addFile;

	@ApiModelProperty(value = "업로드 파일 목록")
	List<FileVo> addFileList;
}
