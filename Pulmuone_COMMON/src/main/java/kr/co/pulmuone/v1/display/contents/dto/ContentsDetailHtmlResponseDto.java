package kr.co.pulmuone.v1.display.contents.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.display.contents.dto.vo.ContentsDetailVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "컨텐츠 정보 Html ResponseDto")
public class ContentsDetailHtmlResponseDto {

    @ApiModelProperty(value = "전시 컨텐츠 PK")
    private Long dpContsId;

    @ApiModelProperty(value = "전시 시작일시")
    private String dpStartDate;

    @ApiModelProperty(value = "전시 종료일시")
    private String dpEndDate;

    @ApiModelProperty(value = "전시 범위")
    private String dpRangeTypeName;

    @ApiModelProperty(value = "타이틀명")
    private String titleName;

    @ApiModelProperty(value = "노출순서")
    private int sort;

    @ApiModelProperty(value = "PC HTML")
    private String htmlPc;

    @ApiModelProperty(value = "모바일 HTML")
    private String htmlMobile;

    public ContentsDetailHtmlResponseDto(ContentsDetailVo vo) {
        this.dpContsId = vo.getDpContsId();
        this.dpStartDate = vo.getDpStartDate();
        this.dpEndDate = vo.getDpEndDate();
        this.dpRangeTypeName = vo.getDpRangeTypeName();
        this.titleName = vo.getTitleName();
        this.sort = vo.getSort();
        this.htmlPc = vo.getHtmlPc();
        this.htmlMobile = vo.getHtmlMobile();
    }
}
