package kr.co.pulmuone.v1.user.company.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "사업자정보관리 Request")
public class BusinessInformationRequestDto extends BaseRequestDto {

    @ApiModelProperty(value = "본사 ID")
    private Long headquartersId;

    @ApiModelProperty(value = "회사 ID")
    private Long companyId;

    @ApiModelProperty(value = "사업자명")
    private String businessName;

    @ApiModelProperty(value = "대표자명")
    private String companyCeoName;

    @ApiModelProperty(value = "사업자번호")
    private String businessNumber;

    @ApiModelProperty(value = "법인번호")
    private String corporationNumber;

    @ApiModelProperty(value = "우편번호")
    private String zipCode;

    @ApiModelProperty(value = "주소")
    private String address;

    @ApiModelProperty(value = "상세주소")
    private String detailAddress;

    @ApiModelProperty(value = "통신판매업 번호")
    private String mailOrderNumber;

    @ApiModelProperty(value = "건강기능식품 신고")
    private String healthFunctFoodReport;

    @ApiModelProperty(value = "호스팅 제공자")
    private String hostingProvider;

    @ApiModelProperty(value = "대표 Email 정보")
    private String representativeEmailInformation;

    @ApiModelProperty(value = "고객센터 전화번호")
    private String serviceCenterPhoneNumber;

    @ApiModelProperty(value = "개인정보보호책임자")
    private String securityDirector;

    @ApiModelProperty(value = "고객센터 운영시간 시작시간")
    private String serviceCenterOperatorOpenTime;

    @ApiModelProperty(value = "고객센터 운영시간 종료시간")
    private String serviceCenterOperatorCloseTime;

    @ApiModelProperty(value = "고객센터 점심시간 사용여부")
    private String lunchTimeYn;

    @ApiModelProperty(value = "고객센터 점심시간 시작시간")
    private String serviceCenterLunchTimeStart;

    @ApiModelProperty(value = "고객센터 점심시간 종료시간")
    private String serviceCenterLunchTimeEnd;

    @ApiModelProperty(value = "에스크로 설명")
    private String escrowDescription;

    @ApiModelProperty(value = "에스크로 가입URL")
    private String escrowSubscriptionUrl;

    @ApiModelProperty(value = "파일정보")
    private String addFile;

    @ApiModelProperty(value = "파일 리스트")
    private List<FileVo> addFileList;
}