package kr.co.pulmuone.v1.calculate.collation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Builder
@Setter
@Getter
@ToString
@ApiModel(description = "외부몰 주문 대사 상세내역 조회 응답 Response Dto")
public class CalOutmallDetlListResponseDto extends BaseResponseDto {
	@ApiModelProperty(value = "외부몰 주문 대사 리스트")
    private List<CalOutmallDetlListDto> rows;

    @ApiModelProperty(value = "외부몰 주문 대사 카운트")
    private	long total;

    @ApiModelProperty(value = "매출금액 합계")
    private	long totalAmt;
}
