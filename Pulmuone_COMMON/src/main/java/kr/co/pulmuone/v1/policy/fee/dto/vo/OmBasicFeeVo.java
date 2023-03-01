package kr.co.pulmuone.v1.policy.fee.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.*;

//@Builder
//@AllArgsConstructor
@Getter
@Setter
@ToString
@ApiModel(description = "OmBasicFeeVo")
public class OmBasicFeeVo extends BaseRequestDto {

    @ApiModelProperty(value = "기본수수료 pk")
    private long omBasicFeeId;

    @ApiModelProperty(value = "판매처 그룹코드")
    private String sellersGroupCd;

    @ApiModelProperty(value = "판매처 PK")
    private long omSellersId;

    @ApiModelProperty(value = "공급처 PK")
    private long urSupplierId;

    @ApiModelProperty(value = "정산방식")
    private String calcType;

    @ApiModelProperty(value = "수수료")
    private int fee;

    @ApiModelProperty(value = "시작일자")
    private String startDt;

    @ApiModelProperty(value= "등록자 ID")
    private long createId;

    @ApiModelProperty(value = "등록일")
    private String createDt;

    @ApiModelProperty(value = "수정자 ID")
    private long modifyId;

    @ApiModelProperty(value = "수정일")
    private String modifyDt;

    @ApiModelProperty(value = "공급처 코드")
    private String supplierCd;

    @ApiModelProperty(value = "공급처명")
    private String supplierNm;

}
