package kr.co.pulmuone.v1.company.notice.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.company.notice.dto.vo.GetNoticeAttachResultVo;
import kr.co.pulmuone.v1.company.notice.dto.vo.GetNoticePopupListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "대시보드 공지사항 팝업 List ResponseDto")
public class GetNoticePopupListResponseDto extends BaseResponseDto{

    @ApiModelProperty(value = "")
    private List<GetNoticePopupListResultVo> getNoticePopupListResultVo;

    @ApiModelProperty(value = "")
    private GetNoticeAttachResultVo rowsFile;

}
