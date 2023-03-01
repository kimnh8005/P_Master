package kr.co.pulmuone.v1.customer.notice.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingLoginId;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoticeBosListVo {

	@ApiModelProperty(value = "분류")
    private String noticeType;

	@ApiModelProperty(value = "분류명")
    private String noticeTypeName;

	@ApiModelProperty(value = "제목")
    private String noticeTitle;

    @ApiModelProperty(value = "등록자명")
    @UserMaskingUserName
    private String userName;

    @ApiModelProperty(value = "등록자 ID")
    @UserMaskingLoginId
    private String userId;

    @ApiModelProperty(value = "노출여부")
    private String displayYn;

    @ApiModelProperty(value = "등록일자")
    private String createDate;

    @ApiModelProperty(value = "조회 count")
    private String viewCount;

    @ApiModelProperty(value = "노출순서")
    private String viewSort;

    @ApiModelProperty(value = "공지사항 내용")
    private String content;

    @ApiModelProperty(value = "공지사항 ID")
    private String noticeId;

    @ApiModelProperty(value = "상단고정여부")
    private String topDisplayYn;

    @ApiModelProperty(value = "수정자 ID")
    @UserMaskingLoginId
    private String modifyId;

}
