package kr.co.pulmuone.v1.user.company.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "본사 회사정보 Vo")
public class CommmonHeadQuartersCompanyVo {
    @ApiModelProperty(value = "회사정보 ID")
    private long companyId;

    @ApiModelProperty(value = "회사구분")
    private String companyType;

    @ApiModelProperty(value = "회사구분명")
    private String companyTypeName;

    @ApiModelProperty(value = "회사명")
    private String companyName;

    @ApiModelProperty(value = "대표자명")
    private String companyCeoName;

    @ApiModelProperty(value = "사업자번호")
    private String businessNumber;

    @ApiModelProperty(value = "연락처")
    private String companyTelephone;

    @ApiModelProperty(value = "메일주소")
    private String companyMail;

    @ApiModelProperty(value = "우편번호")
    private String zipCode;

    @ApiModelProperty(value = "주소")
    private String address;

    @ApiModelProperty(value = "상세주소")
    private String detailAddress;

    @ApiModelProperty(value = "메모")
    private String memo;

    @ApiModelProperty(value = "사용여부")
    private String useYn;

    @ApiModelProperty(value = "본사정보 ID")
    private long headquartersId;

    @ApiModelProperty(value = "법인번호")
    private String corporationNumber;

    @ApiModelProperty(value = "통신판매업 번호")
    private String mailOrderNumber;

    @ApiModelProperty(value = "호스팅 제공자")
    private String hostingProvider;

    @ApiModelProperty(value = "건강기능식품 신고")
    private String healthFunctFoodReport;

    @ApiModelProperty(value = "고객센터 전화번호")
    private String serviceCenterPhoneNumber;

    @ApiModelProperty(value = "개인정보보호책임자")
    private String securityDirector;

    @ApiModelProperty(value = "고객센터 운영 시작시간")
    private String serviceCenterOperatorOpenTime;

    @ApiModelProperty(value = "고객센터 운영 종료시간")
    private String serviceCenterOperatorCloseTime;

    @ApiModelProperty(value = "고객센터 점심 시작시간")
    private String serviceCenterLunchTimeStart;

    @ApiModelProperty(value = "고객센터 점심 종료시간")
    private String serviceCenterLunchTimeEnd;

    @ApiModelProperty(value = "고객센터 점심시간 사용YN")
    private String lunchTimeYn;

    @ApiModelProperty(value = "에스크로 인증정보 원본파일명")
    private String escrowOriginFileName;

    @ApiModelProperty(value = "에스크로 인증정보 파일명")
    private String escrowFileName;

    @ApiModelProperty(value = "에스크로 인증정보 파일경로")
    private String escrowFilePath;

    @ApiModelProperty(value = "에스크로 설명")
    private String escrowDescription;

    @ApiModelProperty(value = "에스크로 가입 URL")
    private String escrowSubscriptionUrl;

}
