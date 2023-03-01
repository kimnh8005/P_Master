package kr.co.pulmuone.v1.order.schedule.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ErpIfCustOrdInpRequestDto")
@Builder
public class ErpIfCustOrdInpRequestDto {

	/*
     * ERP API 구매발주 입력 dto
     */
    @ApiModelProperty(value = "총 페이지")
    private Integer totalPage;

    @ApiModelProperty(value = "현재 페이지 번호")
    private Integer currentPage;

    @ApiModelProperty(value = "일일배송 녹즙 header DTO list")
    private List<ErpIfCustOrdInpHeaderRequestDto> header;

}
