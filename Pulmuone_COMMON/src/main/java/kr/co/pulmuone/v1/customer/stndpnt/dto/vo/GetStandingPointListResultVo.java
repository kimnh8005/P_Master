package kr.co.pulmuone.v1.customer.stndpnt.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingEmail;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingMobile;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품입점 상담관리 ResultVo")
public class GetStandingPointListResultVo {

	/* 상품입점 상담 관리 PK */
	private Long csStandPntId;

	/* 회사명 */
	private String compNm;

	/* 대표자명 */
	private String compCeoNm;

	/* 사업자등록번호 */
	private String bizRegNum;

	/* 문의내용 */
	private String question;

	/* 담당자명 */
	@UserMaskingUserName
	private String managerUserName;

	/* 담당자 */
	private String managerUrUserId;

	/* 주소 */
	private String address;

	/* 휴대폰번호 */
	@UserMaskingMobile
	private String mobile;

	/* 연락처 */
	private String tel;

	/* 이메일 */
	@UserMaskingEmail
	private String email;

	/* 문의상태 */
	private String questionStat;

	/* 문의상태 */
	private String questionStatName;

	/* 승인담당자 */
	private String apprUserId;

	/* 승인담당자명*/
	private String apprUserName;

	/* 등록자명 */
	private String createName;

	/* 등록자 */
	private String createId;

	/* 등록일 */
	private String createDt;

    @ApiModelProperty(value = "순번")
    private String rowNumber;

}
