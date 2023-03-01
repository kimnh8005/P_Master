package kr.co.pulmuone.v1.batch.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * ERP API request dto
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "ErpIfRequestDto")
@Builder
public class ErpIfRequestDto {

    @ApiModelProperty(value = "총 페이지")
    private Integer totalPage;

    @ApiModelProperty(value = "현재 페이지 번호")
    private Integer currentPage;

    @ApiModelProperty(value = "header DTO list")
    private List<?> header;
}
