package kr.co.pulmuone.v1.customer.feedback.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.MallBaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FeedbackTargetListByUserRequestDto extends MallBaseRequestPageDto {

    @ApiModelProperty(value = "회원 ID")
    private Long urUserId;

    @ApiModelProperty(value = "그룹 ID")
    private Long urGroupId;

}
