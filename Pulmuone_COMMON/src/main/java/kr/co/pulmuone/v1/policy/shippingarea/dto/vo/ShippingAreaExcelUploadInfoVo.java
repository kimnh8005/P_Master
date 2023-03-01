package kr.co.pulmuone.v1.policy.shippingarea.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "도서산간/배송불가 권역관리 업로드 정보 Vo")
public class ShippingAreaExcelUploadInfoVo {

    @ApiModelProperty(value = "도서산간/배송불가 업로드 정보 PK")
    private Long psShippingAreaExcelInfoId;

    @ApiModelProperty(value = "우편번호")
    private String zipCd;

    @ApiModelProperty(value = "배송불가권역")
    private String undeliverableTp;

    @ApiModelProperty(value = "배송불가권역 명")
    private String undeliverableNm;

    @ApiModelProperty(value = "대체배송여부")
    private String alternateDeliveryTp;

    @ApiModelProperty(value = "전체건수")
    private int totalCnt = 0;

    @ApiModelProperty(value = "성공건수")
    private int successCnt = 0;

    @ApiModelProperty(value = "실패건수")
    private int failCnt = 0;

    @ApiModelProperty(value = "대체배송건수")
    private int alternateCnt = 0;

    @ApiModelProperty(value = "등록 키워드")
    private String keyword;

    @ApiModelProperty(value = "등록자 ID")
    private Long createId;

    @ApiModelProperty(value = "등록자 이름")
    @UserMaskingUserName
    private String createNm;

    @ApiModelProperty(value = "등록일시")
    private String createDt;

    @ApiModelProperty(value = "파일명")
    private String fileNm;

    @ApiModelProperty(value = "Storage 파일명")
    private String storageFileNm;

    @ApiModelProperty(value = "연동상태")
    private String uploadStatusCd;

    @ApiModelProperty(value = "연동상태명")
    private String uploadStatusCdNm;
}
