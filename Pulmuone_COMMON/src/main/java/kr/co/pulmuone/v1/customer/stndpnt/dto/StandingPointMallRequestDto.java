package kr.co.pulmuone.v1.customer.stndpnt.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.customer.stndpnt.dto.vo.StandingPointQnaAttachVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class StandingPointMallRequestDto {

    @ApiModelProperty(value = "회사명")
    private String companyName;

    @ApiModelProperty(value = "대표자명")
    private String companyCeoName;

    @ApiModelProperty(value = "사업자등록번호")
    private String businessRegistrationNumber;

    @ApiModelProperty(value = "우편번호")
    private String zipCode;

    @ApiModelProperty(value = "주소1")
    private String address1;

    @ApiModelProperty(value = "주소2")
    private String address2;

    @ApiModelProperty(value = "작성자 PK", hidden = true)
    private Long managerUrUserId;

    @ApiModelProperty(value = "담당자명")
    private String managerName;

    @ApiModelProperty(value = "휴대전화번호")
    private String mobile;

    @ApiModelProperty(value = "전화번호")
    private String telephone;

    @ApiModelProperty(value = "이메일")
    private String email;

    @ApiModelProperty(value = "상담내용")
    private String question;

    @ApiModelProperty(value = "문의 승인상태", hidden = true)
    private String questionStatus;

    @ApiModelProperty(value = "작성자 PK", hidden = true)
    private Long createId;

    @ApiModelProperty(value = "상품입정상담 PK", hidden = true)
    private Long csStandPntId;

    @ApiModelProperty(value = "첨부파일")
    private List<StandingPointQnaAttachVo> file;

}
