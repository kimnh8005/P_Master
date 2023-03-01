package kr.co.pulmuone.v1.goods.goods.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@ApiModel(description = "상품이미지 Dto")
public class GoodsImageDto
{

	@ApiModelProperty(value = "기본이미지 여부")
	private String basicYn;

	@ApiModelProperty(value = "파일명")
	private String imageName;

	@ApiModelProperty(value = "원본 파일명")
	private String imageOriginName;

	@ApiModelProperty(value = "640*640")
	private String bImage;

	@ApiModelProperty(value = "320*320")
	private String mImage;

	@ApiModelProperty(value = "216*216")
	private String msImage;

	@ApiModelProperty(value = "180*180")
	private String sImage;

	@ApiModelProperty(value = "75*75")
	private String cImage;

	@ApiModelProperty(value = "정렬")
	private int sort;

}