package kr.co.pulmuone.v1.policy.fee.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "OmLogisticsFeeVo")
public class OmLogisticsFeeVo extends BaseRequestDto {

    @ApiModelProperty(value = "물류수수료 pk")
    private long omLogisticsFeeId;

    @ApiModelProperty(value = "출고처 pk")
    private long urWarehouseId;

    @ApiModelProperty(value = "정산방식")
    private String calcType;

    @ApiModelProperty(value = "수수료")
    private int fee;

    @ApiModelProperty(value = "시작일자")
    private String startDt;

    @ApiModelProperty(value = "공급처 PK")
    private long urSupplierId;

    @ApiModelProperty(value = "공급처명")
    private String supplierNm;

    @ApiModelProperty(value= "등록자 ID")
    private long createId;

    @ApiModelProperty(value = "등록일")
    private String createDt;

    @ApiModelProperty(value = "수정자 ID")
    private long modifyId;

    @ApiModelProperty(value = "수정일")
    private String modifyDt;

}
