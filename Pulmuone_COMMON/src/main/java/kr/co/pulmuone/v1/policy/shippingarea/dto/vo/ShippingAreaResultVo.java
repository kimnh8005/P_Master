package kr.co.pulmuone.v1.policy.shippingarea.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "도서산간/배송불가 권역관리 조회 결과 Vo")
public class ShippingAreaResultVo {

    @ApiModelProperty(value = "도서산간/배송불가 업로드 정보 PK")
    private String psShippingAreaExcelInfoId;

    @ApiModelProperty(value = "배송불가권역")
    private String undeliverableTp;

    @ApiModelProperty(value = "배송불가권역 명")
    private String undeliverableTpNm;

    @ApiModelProperty(value = "성공건수")
    private int successCnt;

    @ApiModelProperty(value = "실패건수")
    private int failCnt;

    @ApiModelProperty(value = "등록 키워드")
    private String keyword;

    @ApiModelProperty(value = "파일명")
    private String fileNm;

    @ApiModelProperty(value = "등록자")
    private Long createId;

    @ApiModelProperty(value = "등록일자")
    private String createDt;

}
