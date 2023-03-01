package kr.co.pulmuone.batch.erp.domain.model.system.itgc.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.batch.erp.common.enums.SystemEnums;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class ItgcRequestDto {

    @ApiModelProperty(value = "메뉴 PK")
    private Long stMenuId;

    @ApiModelProperty(value = "구분")
    private String itgcType;

    @ApiModelProperty(value = "ITSM 계정")
    private String itsmId;

    @ApiModelProperty(value = "항목")
    private String itgcDetail;

    @ApiModelProperty(value = "추가/삭제")
    private String itgcAddType;

    @ApiModelProperty(value = "대상자")
    private String targetInfo;

    @ApiModelProperty(value = "대상자 PK")
    private Long targetUserId;

    @ApiModelProperty(value = "등록일")
    private Long createId;

}
