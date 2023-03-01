package kr.co.pulmuone.v1.customer.feedback.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.MallBaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FeedbackByUserRequestDto extends MallBaseRequestPageDto {

    @ApiModelProperty(value = "회원 ID", hidden = true)
    private Long urUserId;

    @ApiModelProperty(value = "조회시작일")
    private String startDate;

    @ApiModelProperty(value = "조회종료일")
    private String endDate;

    @ApiModelProperty(value = "베스트 후기 기준", hidden = true)
    private int bestCount;
    
}
