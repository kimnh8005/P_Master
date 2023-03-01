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
@ApiModel(description = "회사정보 VO")
public class CompanyVo {

    @ApiModelProperty(value = "회사정보 ID")
    private Long companyId;

    @ApiModelProperty(value = "회사구분")
    private String companyType;

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

    @ApiModelProperty(value = "등록자ID")
    private String createId;

}
