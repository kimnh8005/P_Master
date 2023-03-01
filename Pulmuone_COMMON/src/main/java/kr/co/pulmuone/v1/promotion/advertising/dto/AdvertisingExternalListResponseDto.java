package kr.co.pulmuone.v1.promotion.advertising.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.promotion.advertising.dto.vo.AdvertisingExternalVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class AdvertisingExternalListResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "목록 조회 리스트")
    private List<AdvertisingExternalVo> rows;

    @ApiModelProperty(value = "목록 조회 총 Count")
    private long total;

}
