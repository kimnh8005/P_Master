package kr.co.pulmuone.v1.customer.notice.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoticeBosDetailVo {

	@ApiModelProperty(value = "분류")
    private String noticeType;

	@ApiModelProperty(value = "분류명")
    private String noticeTypeName;

	@ApiModelProperty(value = "제목")
    private String noticeTitle;

    @ApiModelProperty(value = "공지사항 내용")
    private String content;

    @ApiModelProperty(value = "조회 count")
    private String viewCount;

    @ApiModelProperty(value = "노출여부")
    private String displayYn;

    @ApiModelProperty(value = "상단노출시작일")
    private String topDisplayStartDate;

    @ApiModelProperty(value = "상단노출종료일")
    private String topDisplayEndDate;

    @ApiModelProperty(value = "공지사항 ID")
    private String noticeId;

    @ApiModelProperty(value = "공지채널 PC")
    private String channelPcYn;

    @ApiModelProperty(value = "공지채널 Mobile")
    private String channelMoYn;

    @ApiModelProperty(value = "상단고정여부")
    private String topDisplayYn;


}
