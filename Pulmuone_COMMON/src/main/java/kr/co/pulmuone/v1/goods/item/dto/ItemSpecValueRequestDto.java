package kr.co.pulmuone.v1.goods.item.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "품목별 상품정보 제공고시 상세항목 dto")
public class ItemSpecValueRequestDto {
    /*
     * 품목별 상품정보 제공고시 상세항목 dto
     */

    @ApiModelProperty(value = "품목코드", required = true)
    private String ilItemCode;

    @ApiModelProperty(value = "상품정보제공고시항목 PK", required = true)
    private int ilSpecFieldId;

    @ApiModelProperty(value = "상품정보제공고시 상세 항목 정보", required = true)
    private String specFieldValue;

    // directYn 값이 true 인 경우에만 BooleanTypeHandler 에 의해 IL_ITEM_SPEC_VALUE 테이블의 DIRECT_YN 컬럼에 'Y' 값 저장
    // directYn 값이 false 인 경우는 화면에서 null 값 전송 : DIRECT_YN 컬럼에도 null 로 저장
    @ApiModelProperty(value = "상품정보제공고시 직접입력 여부", required = true)
    private Boolean directYn;

    private Long createId; // 등록자 ID

    private Long modifyid; // 수정자 ID

}
