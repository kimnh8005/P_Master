package kr.co.pulmuone.v1.company.notice.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "대시보드 공지사항 팝업 상세내용 ResultVo" )
public class GetNoticePopupResultVo {

    @ApiModelProperty(value = "")
    String csCompanyBbsId;

    @ApiModelProperty(value = "")
    String content;

    @ApiModelProperty(value = "")
    String popupDisplayTodayYn;

}
