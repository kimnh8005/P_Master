package kr.co.pulmuone.v1.approval.auth.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "승인권한.승인권한정보 VO")
public class ApprovalAuthInfoVo{

	@ApiModelProperty(value = "업무별_고유값")
	private String taskPk;

	@ApiModelProperty(value = "업무종류")
	private String taskCode;

	@ApiModelProperty(value = "승인권한정보.마스터상태")
	private String masterStat;

	@ApiModelProperty(value = "승인권한정보.승인상태")
	private String apprStat;

	@ApiModelProperty(value = "승인권한정보.승인요청일")
	private String apprReqDt;

	@ApiModelProperty(value = "승인권한정보.승인요청자")
	private String apprReqUserId;

	@ApiModelProperty(value = "승인권한정보.승인 1차 담당자")
	private String apprSubUserId;

	@ApiModelProperty(value = "승인권한정보.승인 1차 처리자")
	private String apprSubChgUserId;

	@ApiModelProperty(value = "승인권한정보.승인 1차 처리일")
	private String apprSubChgDt;

	@ApiModelProperty(value = "승인권한정보.승인 1차 담당자 회원상태")
	private String apprSubUserStatus;

	@ApiModelProperty(value = "승인권한정보.승인 1차 담당자 권한 위임 담당자")
	private String grantSubAuthUserId;

	@ApiModelProperty(value = "승인권한정보.승인 1차 담당자 권한 위임 시작일")
	private String grantSubAuthStartDt;

	@ApiModelProperty(value = "승인권한정보.승인 1차 담당자 권한 위임 종료일")
	private String grantSubAuthEndDt;

	@ApiModelProperty(value = "승인권한정보.승인 1차 담당자 권한 위임 중지 여부")
	private String grantSubAuthStopYn;

	@ApiModelProperty(value = "승인권한정보.승인 1차 담당자 권한 위임자 회원상태")
	private String grantSubUserStatus;

	@ApiModelProperty(value = "승인권한정보.승인 2차 담당자")
	private String apprUserId;

	@ApiModelProperty(value = "승인권한정보.승인 2차 처리자")
	private String apprChgUserId;

	@ApiModelProperty(value = "승인권한정보.승인 2차 처리자")
	private String apprChgDt;

	@ApiModelProperty(value = "승인권한정보.승인 2차 처리자 회원상태")
	private String apprUserStatus;

	@ApiModelProperty(value = "승인권한정보.승인 2차 담당자 권한 위임 담당자")
	private String grantAuthUserId;

	@ApiModelProperty(value = "승인권한정보.승인 2차 담당자 권한 위임 시작일")
	private String grantAuthStartDt;

	@ApiModelProperty(value = "승인권한정보.승인 2차 담당자 권한 위임 종료일")
	private String grantAuthEndDt;

	@ApiModelProperty(value = "승인권한정보.승인 2차 담당자 권한 위임 중지 여부")
	private String grantAuthStopYn;

	@ApiModelProperty(value = "승인권한정보.승인 2차 담당자 권한 위임자 회원상태")
	private String grantUserStatus;

	@ApiModelProperty(value = "할인 종류")
	private String discountTp;

	@ApiModelProperty(value = "품목코드")
	private String ilItemCd;

	@ApiModelProperty(value = "상품ID")
	private String ilGoodsId;

}
