package kr.co.pulmuone.v1.order.claim.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimInfoExcelUploadInfoVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@ApiModel(description = "클레임 엑셀업로드 내역 Response Dto")
public class ClaimInfoExcelUploadListResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "count")
    private int total;

    @ApiModelProperty(value = "리스트")
    private List<ClaimInfoExcelUploadInfoVo> rows;

}