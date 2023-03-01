package kr.co.pulmuone.v1.policy.shippingarea.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "도서산간/배송불가 엑셀업로드 FAIL 검색조건 Request Dto")
public class ShippingAreaExcelUploadFailRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "도서산간/배송불가 업로드 정보 PK")
    private Long psShippingAreaExcelInfoId;

    @ApiModelProperty(value = "우편번호")
    private String zipCd;

    @ApiModelProperty(value = "권역선택")
    private String undeliverableTp;
}
