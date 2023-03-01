
package kr.co.pulmuone.v1.company.notice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.company.notice.dto.vo.GetNoticePopupResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "대시보드 공지사항 팝업 상세내용 ResponseDto" )
public class GetNoticePopupResponseDto {
    @ApiModelProperty(value = "")
    private GetNoticePopupResultVo getNoticePopupResultVo;

}
