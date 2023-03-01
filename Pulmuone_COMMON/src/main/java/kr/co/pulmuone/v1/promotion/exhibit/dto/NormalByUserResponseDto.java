package kr.co.pulmuone.v1.promotion.exhibit.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.promotion.exhibit.dto.vo.ExhibitGroupByUserVo;
import kr.co.pulmuone.v1.promotion.exhibit.dto.vo.NormalByUserVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class NormalByUserResponseDto {

    @ApiModelProperty(value = "제목")
    private String title;

    @ApiModelProperty(value = "설명")
    private String description;

    @ApiModelProperty(value = "상시진행여부")
    private String alwaysYn;

    @ApiModelProperty(value = "시작일자")
    private String startDate;

    @ApiModelProperty(value = "종료일자")
    private String endDate;

    @ApiModelProperty(value = "D-Day")
    private Long dday;

    @ApiModelProperty(value = "상세 HTML")
    private String detailHtml;

    @ApiModelProperty(value = "종료여부")
    private String endYn;

    @ApiModelProperty(value = "일반기획전 그룹")
    private List<ExhibitGroupByUserVo> group;

    public NormalByUserResponseDto(NormalByUserVo vo) {
        this.title = vo.getTitle();
        this.description = vo.getDescription();
        this.alwaysYn = vo.getAlwaysYn();
        this.startDate = vo.getStartDate();
        this.endDate = vo.getEndDate();
        this.detailHtml = vo.getDetailHtml();
        this.endYn = vo.getEndYn();
    }
}
