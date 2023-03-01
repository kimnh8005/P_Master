package kr.co.pulmuone.v1.user.brand.dto.vo;

import java.util.List;

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
@ApiModel(description = "GetBrandResultVo")
public class GetBrandResultVo {

    /*------------------------------------------------------
    ---- 브랜드 정보
    ------------------------------------------------------*/
    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "브랜크 코드")
    private String urBrandId;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "브랜크 이름")
    private String brandName;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "공급업체 코드")
    private String urSupplierId;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "사용여부")
    private String useYn;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "등록일자")
    private String createDate;

    @ApiModelProperty(value = "브랜드 ID 리스트")
	private	List<GetBrandResultVo> urIdList;

}