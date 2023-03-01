package kr.co.pulmuone.v1.order.claim.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "클레임 엑셀업로드 FAIL 검색조건 Request Dto")
public class ClaimInfoExcelUploadFailRequestDto extends BaseRequestPageDto {


    @ApiModelProperty(value = "클레임 업로드 현황 PK")
    private long ifClaimExcelInfoId;

    @ApiModelProperty(value = "실패타입")
    private String failType;
}

