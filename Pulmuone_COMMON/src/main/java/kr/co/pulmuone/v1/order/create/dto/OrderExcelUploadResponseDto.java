package kr.co.pulmuone.v1.order.create.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OrderExcelUploadResponseDto {

    @ApiModelProperty(value = "성공여부")
    private boolean uploadFlag;

    @ApiModelProperty(value = "전체 건수")
    private int totalCount;

    @ApiModelProperty(value = "성공 건수")
    private int successCount;

    @ApiModelProperty(value = "실패 건수")
    private int failCount;

    @ApiModelProperty(value = "실패 메세지")
    private String failMessage;


    @ApiModelProperty(value = "성공 리스트")
    private List<OrderExcelResponseDto> successList;

    @ApiModelProperty(value = "실패 리스트")
    private List<OrderExcelResponseDto> failList;

}
