package kr.co.pulmuone.v1.display.qrcode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " AddQrcodeRequestDto")
public class AddQrcodeRequestDto {

	@ApiModelProperty(value = "URL")
	private String qrCodeUrl;

	@ApiModelProperty(value = "이미지 원본명")
	private String qrCodeImageName;

	@ApiModelProperty(value = "이미지 경로 (RootStorage)")
	private String qrCodeRootStoragePath;

	@ApiModelProperty(value = "이미지 경로 (FullSubStorage)")
	private String qrCodeFullSubStoragePath;

	@ApiModelProperty(value = "이미지 가로 사이즈")
	private int width;

	@ApiModelProperty(value = "이미지 세로 사이즈")
	private int height;


}
