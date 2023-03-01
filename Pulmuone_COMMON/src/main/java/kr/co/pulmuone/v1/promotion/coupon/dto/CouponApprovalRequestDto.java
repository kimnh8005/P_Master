package kr.co.pulmuone.v1.promotion.coupon.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "CouponApprovalRequestDto")
public class CouponApprovalRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "전시쿠폰명")
	private String searchDisplayCouponName;

	@ApiModelProperty(value = "관리자쿠폰명")
	private String searchBosCouponName;

	@ApiModelProperty(value = "발급방법")
	private String searchIssueType;

	@ApiModelProperty(value = "쿠폰종류")
	private String searchCouponType;

	@ApiModelProperty(value = "승인상태")
	private String searchApprovalStatus;

	@ApiModelProperty(value = "승인상태Array", required = false)
    private ArrayList<String> approvalStatusArray;

	@ApiModelProperty(value = "승인요청일자 검색 시작일")
	private String approvalReqStartDate;

	@ApiModelProperty(value = "승인요청일자 검색 종료일")
	private String approvalReqEndDate;

	@ApiModelProperty(value = "승인처리일자 검색 시작일")
	private String approvalChgStartDate;

	@ApiModelProperty(value = "승인처리일자 검색 종료일")
	private String approvalChgEndDate;

	@ApiModelProperty(value = "쿠폰 마스터 상태")
	private String couponMasterStat;

	@ApiModelProperty(value = "승인상태")
	private String apprStat;

	@ApiModelProperty(value = "승인처리자 단계")
	private String apprStep;

	@ApiModelProperty(value = "승인요청자 아이디/회원 타입")
	private String searchApprReqUserType;

	@ApiModelProperty(value = "승인요청자 아이디/회원 조회값")
	private String searchApprReqUser;

	@ApiModelProperty(value = "승인처리자 아이디/회원 타입")
	private String searchApprChgUserType;

	@ApiModelProperty(value = "승인처리자 아이디/회원 조회값")
	private String searchApprChgUser;

	@ApiModelProperty(value = "쿠폰 ID")
	private String pmCouponId;

	@ApiModelProperty(value = "쿠폰승인.쿠폰 ID 목록")
	private List<String> pmCouponIdList;

	@ApiModelProperty(value = "승인처리 메세지")
	private String statusComment;

	@ApiModelProperty(value = "발급방법 목록")
	private List<String> issueTypeList;

}
