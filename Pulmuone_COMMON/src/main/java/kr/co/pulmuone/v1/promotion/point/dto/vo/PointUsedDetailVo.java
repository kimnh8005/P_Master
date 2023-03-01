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
@ApiModel(description = "적립금 사용 상세 내역 VO")
public class PointUsedDetailVo {

    private Long pmPointUsedDetlId;

    @ApiModelProperty(value = "회원 고유번호")
    private Long urUserId;

    @ApiModelProperty(value = "적립/차감액")
    private Long amount;

    @ApiModelProperty(value = "연관_포인트이력_고유값")
    private Long refPointUsedDetlId;

    @ApiModelProperty(value = "재적립(환불) 연관 포인트 이력 고유값")
    private Long refDproPointUsedDetlId;

    @ApiModelProperty(value = "적립금 내역 고유값")
    private Long pmPointUsedId;

    @ApiModelProperty(value = "적립금 사용가능일")
    private Date availableDt;

    @ApiModelProperty(value = "적립금 만료일")
    private String expirationDt;

    @ApiModelProperty(value = "적립금 지급 유형")
    private PointEnums.PointPayment paymentTp;

    @ApiModelProperty(value = "참조 1 ex 등주문번호, 프로모션 번호")
    private String refNo1;

    @ApiModelProperty(value = "참조 2")
    private String refNo2;

    @ApiModelProperty(value = "조직 코드")
    private String deptCd;

    @ApiModelProperty(value = "적립금 처리 유형")
    private PointEnums.PointProcessType pointProcessTp;

    @ApiModelProperty(value = "적립금 정산 유형")
    private PointEnums.PointSettlementType pointSettlementTp;

    @ApiModelProperty(value = "종료 여부")
    private String closeYn;

    @ApiModelProperty(value = "생성일시")
    private String createDt;

    @ApiModelProperty(value = "적립금 설정 고유값")
    private Long pmPointId;

    @ApiModelProperty(value = "미지급 적립금 고유값")
    private Long pmPointNotIssueId;

    @ApiModelProperty(value = "요청적립금액")
    private Long issueVal;

    @ApiModelProperty(value = "부분적립금액")
    private Long partPointVal;

    @ApiModelProperty(value = "재적립 가능 금액")
    private Long redepositPointVal;

    @ApiModelProperty(value = "적립일")
    private String depositDt;

    @ApiModelProperty(value = "수정자")
    private Long modifyId;

    @ApiModelProperty(value = "수정일")
    private String modifyDt;

    @ApiModelProperty(value = "사유")
    private String cmnt;

}
