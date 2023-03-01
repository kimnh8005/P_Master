package kr.co.pulmuone.v1.outmall.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.outmall.order.dto.vo.ClaimOrderListVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@ApiModel(description = "수집몰 연동내역 리스트 검색조건 Response Dto")
public class ClaimOrderListResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "count")
    private int total;

    @ApiModelProperty(value = "리스트")
    private List<ClaimOrderListVo> rows;

}

