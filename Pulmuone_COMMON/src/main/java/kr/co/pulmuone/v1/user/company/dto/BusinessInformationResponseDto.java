package kr.co.pulmuone.v1.user.company.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.user.company.dto.vo.BusinessInformationVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "사업자정보관리 Response")
public class BusinessInformationResponseDto {

    @ApiModelProperty(value = "사업자정보관리 상세정보")
    private BusinessInformationVo bizInfo;
}
