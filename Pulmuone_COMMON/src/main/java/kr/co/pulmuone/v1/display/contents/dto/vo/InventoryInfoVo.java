package kr.co.pulmuone.v1.display.contents.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "인벤토리 정보 Result")
public class InventoryInfoVo {

    @ApiModelProperty(value = "인벤토리 PK")
    private Long dpInventoryId;

    @ApiModelProperty(value = "인벤토리 코드")
    private String inventoryCode;

    @ApiModelProperty(value = "인벤토리 명")
    private String inventoryName;

    @ApiModelProperty(value = "그룹 카테고리 PK")
    private String groupCategoryId;

    @ApiModelProperty(value = "컨텐츠 레벨1 유형")
    private String contentsLevel1Type;

    @ApiModelProperty(value = "컨텐츠 레벨2 유형")
    private String contentsLevel2Type;

    @ApiModelProperty(value = "컨텐츠 레벨3 유형")
    private String contentsLevel3Type;

    @ApiModelProperty(value = "컨텐츠 레벨1 정보")
    private List<ContentsDetailVo> contentsLevel1List;

}
