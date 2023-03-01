package kr.co.pulmuone.v1.user.company.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "본사 VO")
public class HeadQuartersVo {

    @ApiModelProperty(value = "본사정보 ID")
    private Long headquartersId;

    @ApiModelProperty(value = "회사정보 ID")
    private Long companyId;

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

    @ApiModelProperty(value = "등록자ID")
    private String createId;

}
