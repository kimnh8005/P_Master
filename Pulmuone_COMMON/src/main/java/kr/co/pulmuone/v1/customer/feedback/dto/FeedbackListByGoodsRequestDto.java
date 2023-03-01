package kr.co.pulmuone.v1.customer.feedback.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.MallBaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class FeedbackListByGoodsRequestDto extends MallBaseRequestPageDto {
    @ApiModelProperty(value = "상품 PK", required = true)
    private Long ilGoodsId;

    @ApiModelProperty(value = "page", required = true)
    private int page;

    @ApiModelProperty(value = "limit", required = true)
    private int limit;

    @ApiModelProperty(value = "포토후기만보기")
    private String photoYn;

    @ApiModelProperty(value = "정렬순서")
    private String sortCode;

    @ApiModelProperty(value = "urUserId", hidden = true)
    private Long urUserId;

    @ApiModelProperty(value = "상품 PK LIST", hidden = true)
    private List<Long> ilGoodsIdList;

    @ApiModelProperty(value = "회원여부", hidden = true)
    boolean isMember;

    @ApiModelProperty(value = "임직원여부", hidden = true)
    boolean isEmployee;

}
