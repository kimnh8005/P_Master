package kr.co.pulmuone.mall.display.layout.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " LogoDto")
public class LogoDto {

	private String shopLogoImage;

	private String shopLogoDetailImage;
}
