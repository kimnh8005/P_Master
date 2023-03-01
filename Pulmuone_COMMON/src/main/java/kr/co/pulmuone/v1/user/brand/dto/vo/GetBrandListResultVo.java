package kr.co.pulmuone.v1.user.brand.dto.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "브랜드 목록 조회 결과")
public class GetBrandListResultVo {

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "브랜드 코드")
    private String urBrandId;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "브랜드 이름")
    private String brandName;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "공급업체 이름")
    private String supplierName;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "사용여부")
    private String useYn;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "등록일자")
    private String createDate;

}
