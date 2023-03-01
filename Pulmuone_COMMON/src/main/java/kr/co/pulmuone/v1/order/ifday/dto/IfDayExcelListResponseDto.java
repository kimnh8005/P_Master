package kr.co.pulmuone.v1.order.ifday.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.order.ifday.dto.vo.IfDayExcelInfoVo;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallExcelInfoVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@ApiModel(description = "I/F 일자 변경 엑셀업로드 내역 Response Dto")
public class IfDayExcelListResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "count")
    private int total;

    @ApiModelProperty(value = "리스트")
    private List<IfDayExcelInfoVo> rows;

}