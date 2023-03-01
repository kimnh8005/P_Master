package kr.co.pulmuone.v1.goods.fooditem.dto;

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
@ApiModel(description = "식단품목아이콘상세 RequestDto")
public class FooditemIconRequestDto extends BaseRequestDto {

	@ApiModelProperty(value = "PK")
	private String ilFooditemIconId;

	@ApiModelProperty(value = "아이콘명")
	private String fooditemIconName;

	@ApiModelProperty(value = "식단분류")
	private String fooditemType;

	@ApiModelProperty(value = "타이틀명")
	private String titleNm;

	@ApiModelProperty(value = "사용여부")
	private String useYn;

	@ApiModelProperty(value = "상세정보")
	private String defaultDesc;

	@ApiModelProperty(value = "아이콘이미지 경로")
	private String imagePath;

	@ApiModelProperty(value = "아이콘이미지 파일명")
	private String imageName;

	@ApiModelProperty(value = "아이콘이미지 원본파일명")
	private String imageOriginName;

	@ApiModelProperty(value = "업로드 파일 정보(json형식)")
	private String addFile;

	@ApiModelProperty(value = "업로드 파일 목록")
	List<FileVo> addFileList;
}
