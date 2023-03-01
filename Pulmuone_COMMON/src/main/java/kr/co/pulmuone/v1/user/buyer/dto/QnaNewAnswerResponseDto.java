package kr.co.pulmuone.v1.user.buyer.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QnaNewAnswerResponseDto {

    @ApiModelProperty(value = "1:1문의 확인 해야할 답변 유무")
    private String onetooneNewAnswerYn = "N";

    @ApiModelProperty(value = "상품문의 확인 해야할 답변 유무")
    private String productNewAnswerYn = "N";

    @ApiModelProperty(value = "고객보상제 확인 해야할 답변 유무")
    private String rewardNewAnswerYn = "N";

}
