package kr.co.pulmuone.v1.approval.auth.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalStatusVo extends BaseVo{

	@ApiModelProperty(value = "이력_고유값")
	private String statusHistoryId;

	@ApiModelProperty(value = "업무별_고유값")
	private String taskPk;

	@ApiModelProperty(value = "이전 마스터상태 공통코드")
	private String prevMasterStat;

	@ApiModelProperty(value = "이전 승인상태 공통코드(APPR_STAT)")
	private String prevApprStat;

	@ApiModelProperty(value = "이전 승인상태 공통코드(APPR_STAT)명")
	private String prevApprStatName;

	@ApiModelProperty(value = "변경 마스터상태 공통코드")
	private String masterStat;

	@ApiModelProperty(value = "변경 승인상태 공통코드(APPR_STAT)")
	private String apprStat;

	@ApiModelProperty(value = "변경 승인상태 공통코드(APPR_STAT)명")
	private String apprStatName;

	@ApiModelProperty(value = "상태변경_메세지")
	private String statusComment;

	@ApiModelProperty(value = "승인처리자 단계")
	private String apprStep;

	@ApiModelProperty(value = "승인요청자명")
	private String approvalRequestUserName;

	@ApiModelProperty(value = "승인요청자ID")
	private String approvalRequestUserId;

	@ApiModelProperty(value = "승인 1차 담당자")
	private String apprSubUserId;

	@ApiModelProperty(value = "승인 2차 담당자")
	private String apprUserId;

	@ApiModelProperty(value = "등록자명")
	private String createName;

	@ApiModelProperty(value = "등록자 ID")
	private String createLoginId;

	@ApiModelProperty(value = "등록일")
	private String createDate;

	@ApiModelProperty(value = "등록자PK")
	private String createId;

	@ApiModelProperty(value = "할인 종류")
	private String discountTp;

	@ApiModelProperty(value = "품목코드")
	private String ilItemCd;

	@ApiModelProperty(value = "상품ID")
	private String ilGoodsId;

	@ApiModelProperty(value = "(품목가격, 상품할인) 상태처리시점 원가")
	private int standardPriceChg;

	@ApiModelProperty(value = "(품목가격, 상품할인) 상태처리시점 정상가")
	private int recommendedPriceChg;


}
