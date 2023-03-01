package kr.co.pulmuone.v1.policy.shippingarea.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.policy.shippingarea.dto.ShippingAreaExcelUploadDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ShippingAreaExcelUploadFailVo {
    @ApiModelProperty(value = "도서산간/배송불가 업로드 정보 PK")
    private Long psShippingAreaExcelInfoId;

    @ApiModelProperty(value = "배송불가권역 구분")
    private String undeliverableTp;

    @ApiModelProperty(value = "배송불가권역 명")
    private String undeliverableNm;

    @ApiModelProperty(value = "등록 키워드")
    private String keyword;

    @ApiModelProperty(value = "대체배송여부(Y)")
    private String alternateDeliveryTp;

    @ApiModelProperty(value = "우편번호")
    private String zipCd;

    @ApiModelProperty(value = "실패사유")
    private String failMsg;

    public ShippingAreaExcelUploadFailVo(){}

    public ShippingAreaExcelUploadFailVo(ShippingAreaExcelUploadDto dto) {
        this.undeliverableTp = dto.getUndeliverableTp();
        this.zipCd = dto.getZipCd();
        this.keyword = dto.getKeyword();
        this.alternateDeliveryTp = dto.getAlternateDeliveryTp();
        this.failMsg = dto.getFailMsg();
    }
}
