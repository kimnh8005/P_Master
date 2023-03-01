package kr.co.pulmuone.v1.promotion.point.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.ClaimEnums;
import kr.co.pulmuone.v1.comm.enums.PointEnums;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@ApiModel(description = "보유한도 초과 부분적립 내역 dto")
public class PointPartialDepositOverLimitDto {

    @ApiModelProperty(value = "회원 고유번호", required = true)
    private Long urUserId;

    @ApiModelProperty(value = "최대적립금액", required = true)
    private Long maximumAccrualAmount;

    @ApiModelProperty(value = "적립 요청금액", required = true)
    private Long requestAmount;

    @ApiModelProperty(value = "재적립 가능 금액", required = true)
    private Long redepositPointVal;

    @ApiModelProperty(value = "적립금 프로세스 유형", required = true)
    private PointEnums.PointProcessType pointProcessType;

    @ApiModelProperty(value = "적립금 정산 유형 enum", required = true)
    private PointEnums.PointSettlementType pointSettlementType;

    @ApiModelProperty(value = "포인트 만료일", required = true)
    private String expirationDate;

    @ApiModelProperty(value = "적립금 정산 법인 코드")
    private String issueDeptCd;

    @ApiModelProperty(value = "적립금 설정 고유값")
    private Long pmPointId;

    @ApiModelProperty(value = "참조 1 ex 등주문번호, 프로모션 번호")
    private String refNo1;

    @ApiModelProperty(value = "참조 2")
    private String refNo2;

    @ApiModelProperty(value = "미적립(환불) 연관 적립금 이력 고유값")
    private Long refDproPointUsedDetlId;

    @ApiModelProperty(value = "환불 재적립 시 기존 적립금 이용가능일")
    private Date availableDate;

    @ApiModelProperty(value = "적립금 미지급 내역 고유값")
    private Long pmPointNotIssueId;

    @ApiModelProperty(value = "수정자 ID")
    private Long modifyId;

    @ApiModelProperty(value = "사유")
    private String cmnt;

    @ApiModelProperty(value = "환불 귀책 사유")
    private ClaimEnums.ReasonAttributableType reasonAttributableType;


    @Builder
    public PointPartialDepositOverLimitDto(Long urUserId, Long maximumAccrualAmount, Long requestAmount, PointEnums.PointProcessType pointProcessType,
            PointEnums.PointSettlementType pointSettlementType, String expirationDate, String issueDeptCd, Long pmPointId, String refNo1, String refNo2,
                                           Long refDproPointUsedDetlId, Date availableDate, Long pmPointNotIssueId, Long modifyId, String cmnt, ClaimEnums.ReasonAttributableType reasonAttributableType){

        this.urUserId = urUserId;
        this.maximumAccrualAmount = maximumAccrualAmount == null ? 0 : maximumAccrualAmount;
        this.requestAmount = requestAmount;
        this.redepositPointVal = requestAmount-this.maximumAccrualAmount;
        this.pointProcessType = pointProcessType;
        this.pointSettlementType = pointSettlementType;
        this.expirationDate = expirationDate;
        this.issueDeptCd = issueDeptCd;
        this.pmPointId = pmPointId == null ? 0 : pmPointId;
        this.refNo1 = refNo1;
        this.refNo2 = refNo2;
        this.refDproPointUsedDetlId = refDproPointUsedDetlId == null ? 0 : refDproPointUsedDetlId;
        this.availableDate = availableDate;
        this.pmPointNotIssueId = pmPointNotIssueId;
        this.modifyId = modifyId;
        this.cmnt = cmnt;
        this.reasonAttributableType = reasonAttributableType;
    }
}
