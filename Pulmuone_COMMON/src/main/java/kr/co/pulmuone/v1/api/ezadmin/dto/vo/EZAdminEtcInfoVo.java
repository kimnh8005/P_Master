package kr.co.pulmuone.v1.api.ezadmin.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@ApiModel(description = "이지어드민 기타정보 조회 Vo")
public class EZAdminEtcInfoVo {

    @ApiModelProperty(value = "코드")
    private String code;

    @ApiModelProperty(value = "이름")
    private String name;
}
