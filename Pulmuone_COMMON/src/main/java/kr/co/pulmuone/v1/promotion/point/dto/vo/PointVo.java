package kr.co.pulmuone.v1.promotion.point.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.PointEnums;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@ApiModel(description = "PointVo 포인트 설정")
public class PointVo {

	@ApiModelProperty(value = "적립금 고유값")
	private Long pmPointId;

	@ApiModelProperty(value = "적립금 타입(이용권(난수번호 발급), 후기, 구매, 자동지급, 관리자 지급/차감)")
	private String pointTp;

	@ApiModelProperty(value = "적립금 명")
	private String pointNm;

	@ApiModelProperty(value = "발급기간_시작일")
	private String issueStartDt;

	@ApiModelProperty(value = "발급기간_종료일")
	private String issueEndDt;

	@ApiModelProperty(value = "발급수량")
	private String issueQty;

	@ApiModelProperty(value = "발급예산")
	private String issueBudget;

	@ApiModelProperty(value = "적립금 정산 법인 코드")
	private String issueDeptCd;

	@ApiModelProperty(value = "유효기간_종료일")
	private String validityEndDt;

	@ApiModelProperty(value = "유효기간_유효일")
	private int validityDay;

	@ApiModelProperty(value = "발급수량제한(1인당 지급제한 건수)")
	private String issueQtyLimit;

	@ApiModelProperty(value = "적립금_세부_타입(자동지급 : 이벤트, 추천인 / 관리자 지급,차감 : 이벤트, 관리자)")
	private String pointDetailTp;

	@ApiModelProperty(value = "난수번호타입(자동생성, 엑셀업로드, 고정값사용)")
	private String serialNumberTp;

	@ApiModelProperty(value = "고정난수번호")
	private String fixSerialNumber;

	@ApiModelProperty(value = "포인 상세 유형")
	private PointEnums.PointProcessType pointProcessTp;

	@ApiModelProperty(value = "적립금 정산 유형")
	private PointEnums.PointSettlementType pointSettlementTp;

	@ApiModelProperty(value = "발급타입(적립,차감)(POINT_PAYMENT_TP)")
	private PointEnums.PointPayment paymentTp;

	@ApiModelProperty(value = "발급사유 타입(적립,차감)")
	private String pointUsedTp;

	@ApiModelProperty(value = "발급사유 상세")
	private String pointUsedMsg;

	@ApiModelProperty(value = "유효기간 설정타입(기간설정, 유효일설정)")
	private PointEnums.ValidityType validityTp;

	@ApiModelProperty(value = "발급기준(배송완료, 구매완료, 구매확정)")
	private String paymentStandardTp;

	@ApiModelProperty(value = "발급기준 상세(즉시, 설정기간)")
	private String paymentStandardDetlTp;

	@ApiModelProperty(value = "발급기준 상세_값")
	private String paymentStandardDetlVal;

	@ApiModelProperty(value = "발급방식(정률, 정액)")
	private String issueTp;

	@ApiModelProperty(value = "발급_값")
	private Long issueVal;

	@ApiModelProperty(value = "상태(승인요청,요청취소,승인, 반려, 중단, 중단회수)")
	private String status;

	@ApiModelProperty(value = "회원별 관리자 포인트 설정 여부")
	private String pointAdminYn;

	@ApiModelProperty(value = "등록일")
	private Date createDt;

	@ApiModelProperty(value = "등록자")
	private String createId;

	@ApiModelProperty(value = "수정자")
	private String modifyId;

	@ApiModelProperty(value = "수정일")
	private Date modifyDt;
}
