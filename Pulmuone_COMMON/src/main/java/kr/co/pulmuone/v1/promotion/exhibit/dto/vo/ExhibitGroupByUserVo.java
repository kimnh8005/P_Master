package kr.co.pulmuone.v1.promotion.exhibit.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ExhibitGroupByUserVo {

    @ApiModelProperty(value = "전시 그룹 PK")
    private Long evExhibitGroupId;

    @ApiModelProperty(value = "전시 그룹 명")
    private String groupName;

    @ApiModelProperty(value = "텍스트 색")
    private String textColor;

    @ApiModelProperty(value = "전시 그룹 배경 유형")
    private String exhibitImageType;

    @ApiModelProperty(value = "배경 컬러 코드")
    private String backgroundCode;

    @ApiModelProperty(value = "설명")
    private String description;
    
    @ApiModelProperty(value = "전시상품수")
    private int displayCount;

    @ApiModelProperty(value = "상품정보")
    private List<GoodsSearchResultDto> goods;

}
