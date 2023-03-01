package kr.co.pulmuone.v1.policy.shippingarea.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ShippingAreaExcelUploadResponseDto {

    @ApiModelProperty(value = "도서산간/배송불가 업로드 정보 PK")
    private Long psShippingAreaExcelInfoId;

    @ApiModelProperty(value = "전체 건수")
    private int totalCount;

    @ApiModelProperty(value = "성공 건수")
    private int successCount;

    @ApiModelProperty(value = "실패 건수")
    private int failCount;

    @ApiModelProperty(value = "삭제 건수")
    private int deleteCount;

    @ApiModelProperty(value = "실패 메세지")
    private String failMessage;

}
