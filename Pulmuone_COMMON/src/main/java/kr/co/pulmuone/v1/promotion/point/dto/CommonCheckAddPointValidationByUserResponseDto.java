package kr.co.pulmuone.v1.promotion.point.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.PointEnums;
import kr.co.pulmuone.v1.promotion.point.dto.vo.CommonGetAddPointValidationInfoVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "유저 적립금 등록 검증 ResponseDto")
public class CommonCheckAddPointValidationByUserResponseDto {

    @ApiModelProperty(value = "유저 적립금 등록 검증 resultVo")
    private CommonGetAddPointValidationInfoVo data;

    @ApiModelProperty(value = "유저 적립금 등록 검증 결과 Enum")
    private PointEnums.AddPointValidation validationEnum;
}
