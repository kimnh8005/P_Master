package kr.co.pulmuone.v1.display.contents.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "메인 컨텐츠 조회 ResponseDto")
public class InventoryContentsInfoResponseDto {
    @ApiModelProperty(value = "인벤토리 PK")
    private Long dpInventoryId;

    @ApiModelProperty(value = "인벤토리 코드")
    private String inventoryCode;

    @ApiModelProperty(value = "인벤토리 명")
    private String inventoryName;

    @ApiModelProperty(value = "level1 유형")
    private String level1ContentsType;

    @ApiModelProperty(value = "level1 수량")
    private int level1TotalCount = 0;

    @ApiModelProperty(value = "level1 정보")
    private List<?> level1 = new ArrayList<>();

    @ApiModelProperty(value = "level2 유형")
    private String level2ContentsType;

    @ApiModelProperty(value = "level2 수량")
    private int level2TotalCount = 0;

    @ApiModelProperty(value = "level2 정보")
    private List<?> level2;

    @ApiModelProperty(value = "level3 유형")
    private String level3ContentsType;

    @ApiModelProperty(value = "level3 수량")
    private int level3TotalCount = 0;

    @ApiModelProperty(value = "level3 정보")
    private List<?> level3;
}
