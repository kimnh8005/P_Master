package kr.co.pulmuone.v1.goods.goods.dto.vo;


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
@ApiModel(description = "상품이미지 Vo")
public class GoodsImageVo
{
	@ApiModelProperty(value="상품ID")
	private String ilGoodsId;

	@ApiModelProperty(value = "기본이미지 여부")
	private String basicYn;

	@ApiModelProperty(value = "파일명")
	private String imageName;

	@ApiModelProperty(value = "이미지 원본 파일 경로")
	private String imagePath;

	@ApiModelProperty(value = "원본 파일명")
	private String imageOriginalName;

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

	@ApiModelProperty(value = "등록자 ID")
	private String createId;

}