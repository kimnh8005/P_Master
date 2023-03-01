package kr.co.pulmuone.v1.goods.goods.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@ApiModel(description = "묶음 상품 전용 이미지 Vo")
public class GoodsRegistImageVo {

	@ApiModelProperty(value="상품 ID")
	String ilGoodsId;

	@ApiModelProperty(value="기본 이미지 여부")
	String basicYn;

	@ApiModelProperty(value="이미지 파일 원본 경로 ( 물리적 파일명 포함 )")
	String imagePath;

	@ApiModelProperty(value="이미지 물리적 파일명")
	String imagePhysicalName;

	@ApiModelProperty(value="이미지 원본 파일명")
	String imageOriginalName;

	@ApiModelProperty(value="640*640 이미지 파일 경로 ( 물리적 파일명 포함 )")
	String size640ImagePath;

	@ApiModelProperty(value="640*640 이미지 물리적 파일명")
	String size640ImagePhysicalName;

	@ApiModelProperty(value="320*320 이미지 파일 경로 ( 물리적 파일명 포함 )")
	String size320ImagePath;

	@ApiModelProperty(value="320*320 이미지 물리적 파일명")
	String size320ImagePhysicalName;

	@ApiModelProperty(value="216*216 이미지 파일 경로 ( 물리적 파일명 포함 )")
	String size216ImagePath;

	@ApiModelProperty(value="216*216 이미지 물리적 파일명")
	String size216ImagePhysicalName;

	@ApiModelProperty(value="180*180 이미지 파일 경로 ( 물리적 파일명 포함 )")
	String size180ImagePath;

	@ApiModelProperty(value="180*180 이미지 물리적 파일명")
	String size180ImagePhysicalName;

	@ApiModelProperty(value="75*75 이미지 파일 경로 ( 물리적 파일명 포함 )")
	String size75ImagePath;

	@ApiModelProperty(value="75*75 이미지 물리적 파일명")
	String size75ImagePhysicalName;

	@ApiModelProperty(value="정렬 순서")
	int sort;

	@ApiModelProperty(value="등록자 ID")
	String createId;

	@ApiModelProperty(value="수정자 ID")
	String modifyId;
}
