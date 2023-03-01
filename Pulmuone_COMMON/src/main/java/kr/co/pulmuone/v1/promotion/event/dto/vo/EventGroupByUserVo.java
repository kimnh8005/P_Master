package kr.co.pulmuone.v1.promotion.event.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class EventGroupByUserVo {

    @ApiModelProperty(value = "이벤트 그룹 PK")
    private Long evEventGroupId;

    @ApiModelProperty(value = "이벤트 그룹 명")
    private String groupName;

    @ApiModelProperty(value = "텍스트 색")
    private String textColor;

    @ApiModelProperty(value = "이벤트 그룹 배경 유형")
    private String eventImageType;

    @ApiModelProperty(value = "배경 컬러 코드")
    private String backgroundCode;

    @ApiModelProperty(value = "설명")
    private String description;
    
    @ApiModelProperty(value = "이벤트상품수")
    private int displayCount;

    @ApiModelProperty(value = "상품정보")
    private List<GoodsSearchResultDto> goods;

}
