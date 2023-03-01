package kr.co.pulmuone.v1.policy.shippingarea.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "ShippingAreaExcelUploadDto")
public class ShippingAreaExcelUploadDto {

    @ApiModelProperty(value = "도서산간/배송불가 엑셀 정보 PK")
    private Long psShippingAreaExcelInfoId;

    @ApiModelProperty(value = "도서산간/배송불가 권역")
    private String undeliverableTp;

    @ApiModelProperty(value = "도서산간/배송불가 권역명")
    private String undeliverableNm;

    @ApiModelProperty(value = "우편번호")
    private String zipCd;

    @ApiModelProperty(value = "사용여부(등록/삭제)")
    private String useYn;

    @ApiModelProperty(value = "대체배송유형")
    private String alternateDeliveryTp;

    @ApiModelProperty(value = "등록키워드")
    private String keyword;

    @ApiModelProperty(value = "등록자")
    private Long createId;

    @ApiModelProperty(value = "업로드 파일명")
    private String fileNm;

    @ApiModelProperty(value = "성공여부")
    private boolean success;

    @ApiModelProperty(value = "실패사유")
    private String failMsg;

}
