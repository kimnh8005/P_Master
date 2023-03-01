package kr.co.pulmuone.v1.batch.user.join.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JoinBatchVo {

    @ApiModelProperty(value = "회원 PK")
    private Long urUserId;

    @ApiModelProperty(value = "추천받은 회원 PK")
    private Long recommUserId;

    @ApiModelProperty(value = "추천받은 회원 그룹 PK")
    private Long recommUrGroupId;

}
