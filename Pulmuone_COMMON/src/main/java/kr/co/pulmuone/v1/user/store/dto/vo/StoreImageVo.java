package kr.co.pulmuone.v1.user.store.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@ApiModel(description = "매장 이미지 Vo")
public class StoreImageVo {

	@ApiModelProperty(value="매장 ID")
	private String urStoreId;

	@ApiModelProperty(value = "이미지 타입")
	private String storeImageType;

	@ApiModelProperty(value = "파일명")
	private String imageName;

	@ApiModelProperty(value = "이미지 원본 파일 경로")
	private String imagePath;

	@ApiModelProperty(value = "원본 파일명")
	private String imageOriginalName;

	@ApiModelProperty(value = "이미지 타입")
	private String imageType;

	@ApiModelProperty(value = "등록자 ID")
	private String createUserId;



}
