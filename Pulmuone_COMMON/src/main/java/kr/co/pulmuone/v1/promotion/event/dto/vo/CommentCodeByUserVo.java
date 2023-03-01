package kr.co.pulmuone.v1.promotion.event.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentCodeByUserVo {

    @ApiModelProperty(value = "이벤트 댓글 구분 코드 PK")
    private Long evEventCommentCodeId;

    @ApiModelProperty(value = "댓글 코드 값")
    private String commentValue;
    
}
