package kr.co.pulmuone.v1.outmall.ezadmin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.ibatis.annotations.Param;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EZAdminCreateOrderFailDto {

    @ApiModelProperty(value = "이지어드민주문실패상세정보PK")
    private Long ifEasyadminOrderFailId;

    @ApiModelProperty(value = "실패 메세지")
    private String failMessage;

    @ApiModelProperty(value = "이지어드민주문성공상세정보PK")
    private Long ifEasyadminOrderSuccDetlId;

    @ApiModelProperty(value = "실패구분(U:업로드 B:배치)")
    private String failType;
    
}
