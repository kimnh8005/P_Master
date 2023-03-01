package kr.co.pulmuone.v1.display.popup.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PreviewPopupResponseDto {

	@ApiModelProperty(value = "html 내용", required = false)
	String html;

	@ApiModelProperty(value = "이미지", required = false)
	String popupImgPath;

	@ApiModelProperty(value = "오늘 그만보기", required = false)
	String todayStopYn;


}
