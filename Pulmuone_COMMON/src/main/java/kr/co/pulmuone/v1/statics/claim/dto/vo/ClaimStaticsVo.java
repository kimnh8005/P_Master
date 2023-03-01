package kr.co.pulmuone.v1.statics.claim.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ClaimStaticsVo {

    @ApiModelProperty(value = "공급업체 명")
    private String urSupplierName;

    @ApiModelProperty(value = "판매처그룹 명")
    private String sellersGroupName;

    @ApiModelProperty(value = "판매처 명")
    private String sellersName;

    @ApiModelProperty(value = "총 주문 상품 수")
    private Integer orderGoodsCount;

    @ApiModelProperty(value = "즉시 취소")
    private Integer directCancelCompleteCount;

    @ApiModelProperty(value = "취소요청")
    private Integer cancelApplyCount;

    @ApiModelProperty(value = "취소철회")
    private Integer cancelWithdrawalCount;

    @ApiModelProperty(value = "취소거부")
    private Integer cancelDenyCount;

    @ApiModelProperty(value = "취소완료")
    private Integer applyCancelCompleteCount;

    @ApiModelProperty(value = "반품요청")
    private Integer returnApplyCount;

    @ApiModelProperty(value = "반품승인")
    private Integer returnIngCount;

    @ApiModelProperty(value = "반품철회")
    private Integer returnWithdrawalCount;

    @ApiModelProperty(value = "반품거부")
    private Integer returnDenyCount;

    @ApiModelProperty(value = "반품완료")
    private Integer returnCompleteCount;

    @ApiModelProperty(value = "재배송")
    private Integer exchangeCompleteCount;

}
