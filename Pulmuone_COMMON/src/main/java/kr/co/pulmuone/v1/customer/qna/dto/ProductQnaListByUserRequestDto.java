package kr.co.pulmuone.v1.customer.qna.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.MallBaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetProductQnaListByUserRequestDto")
public class ProductQnaListByUserRequestDto extends MallBaseRequestPageDto {
    @ApiModelProperty(value = "발급회원코드", hidden = true)
    private Long urUserId;

    @ApiModelProperty(value = "조회시작일")
    private String startDate;

    @ApiModelProperty(value = "조회종료일")
    private String endDate;
}
