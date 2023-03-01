package kr.co.pulmuone.v1.goods.goods.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsDirectLinkCategoryMappingListVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "네이버 직연동 표준 카테고리 Response")
public class GoodsDirectLinkCategoryMappingResponseDto extends BaseRequestPageDto{

	// # 그리드
    @ApiModelProperty(value = "직연동몰")
    private String gearCateType;

    @ApiModelProperty(value = "표준 카테고리 코드")
    private String ilCtgryStdId;

    @ApiModelProperty(value = "풀무원 표준 카테고리")
    private String ilCtgryStdFullName;

    @ApiModelProperty(value = "카테고리 코드")
    private String categoryId;

    @ApiModelProperty(value = "직연동몰 카테고리")
    private String categoryName;

    @ApiModelProperty(value = "리스트전체건수")
    private long total;

    @ApiModelProperty(value = "리스트")
    private List<GoodsDirectLinkCategoryMappingListVo> rows;

    @ApiModelProperty(value = "결과코드")
    private String resultCode;

    @ApiModelProperty(value = "결과메시지")
    private String resultMessage;


}
