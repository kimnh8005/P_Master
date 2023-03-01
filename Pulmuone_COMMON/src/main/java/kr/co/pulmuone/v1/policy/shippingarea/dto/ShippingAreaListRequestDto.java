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
@ApiModel(description = "도서산간/배송불가 권역관리 엑셀 적용 내역 검색조건 Request Dto")
public class ShippingAreaListRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "배송불가권역 선택")
    private String undeliverableTp;

    @ApiModelProperty(value = "키워드 검색")
    private String keyword;

    @ApiModelProperty(value = "storage 파일명")
    private String storageFileNm;
}
