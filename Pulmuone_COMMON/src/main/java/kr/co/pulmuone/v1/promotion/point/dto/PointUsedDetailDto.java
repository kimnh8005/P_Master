package kr.co.pulmuone.v1.promotion.point.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.PointEnums;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.promotion.point.dto.vo.PmOrganizationVo;
import kr.co.pulmuone.v1.promotion.point.dto.vo.PointVo;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@ToString
@ApiModel(description = "적립금 사용 상세 내역 Dto")
public class PointUsedDetailDto {

    @ApiModelProperty(value = "적립금 이력 상세 고유값")
    private Long pmPointUsedDetlId;

    @ApiModelProperty(value = "회원 고유번호", required = true)
    private Long urUserId;

    @ApiModelProperty(value = "적립/차감액", required = true)
    private Long amount;

    @ApiModelProperty(value = "연관_포인트이력_고유값")
    private Long refPointUsedDetlId;

    @ApiModelProperty(value = "재적립(환불) 연관 포인트 이력 고유값")
    private Long refDproPointUsedDetlId;

    @ApiModelProperty(value = "적립금 내역 고유값")
    private Long pmPointUsedId;

    @ApiModelProperty(value = "포인트 만료일")
    private String expirationDt;

    @ApiModelProperty(value = "적립금 지급 유형")
    private PointEnums.PointPayment paymentTp;

    @ApiModelProperty(value = "참조 1 ex 등주문번호, 프로모션 번호")
    private String refNo1;

    @ApiModelProperty(value = "참조 2")
    private String refNo2;

    @ApiModelProperty(value = "조직 코드", required = true)
    private String deptCd;

    @ApiModelProperty(value = "적립금 처리 유형", required = true)
    private PointEnums.PointProcessType pointProcessTp;

    @ApiModelProperty(value = "적립금 정산 유형", required = true)
    private PointEnums.PointSettlementType pointSettlementTp;

    @ApiModelProperty(value = "종료 여부")
    private String closeYn;

    @ApiModelProperty(value = "분담율")
    private int percentage;

    @ApiModelProperty(value = "적립금 설정 고유값")
    private Long pmPointId;


    @Builder
    public PointUsedDetailDto(PointUsedDto pointUsedDto, PointVo pointVo, Long refPointUsedDetlId, Long refDproPointUsedDetlId, String closeYn){

        this.urUserId = pointUsedDto.getUrUserId();
        this.amount = pointUsedDto.getAmount();
        this.refPointUsedDetlId = refPointUsedDetlId;
        this.refDproPointUsedDetlId = refDproPointUsedDetlId;
        this.pmPointUsedId = pointUsedDto.getPmPointUsedId();
        this.pmPointId = pointUsedDto.getPmPointId();
        this.deptCd = pointVo.getIssueDeptCd();

        String expirationDate = "";
        if(PointEnums.ValidityType.PERIOD.equals(pointVo.getValidityTp())){
            expirationDate = pointVo.getValidityEndDt();
        }else{
            expirationDate = LocalDate.now().plusDays(pointVo.getValidityDay()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 23:59:59";
        }

        this.expirationDt = expirationDate;
        this.paymentTp = pointUsedDto.getPaymentTp();
        this.refNo1 = pointUsedDto.getRefNo1();
        this.refNo2 = pointUsedDto.getRefNo2();
        this.pointProcessTp = pointUsedDto.getPointProcessTp();
        this.pointSettlementTp = pointUsedDto.getPointSettlementTp();
        this.closeYn = StringUtil.isEmpty(closeYn)? "N" : closeYn;
        //this.percentage = pmOrganization.getPercentage();

    }
}
