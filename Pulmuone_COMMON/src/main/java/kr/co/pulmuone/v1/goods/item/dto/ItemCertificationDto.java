package kr.co.pulmuone.v1.goods.item.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "품목별 상품 인증정보 등록 dto")
public class ItemCertificationDto {

    /*
     * 품목별 상품 인증정보 등록 dto
     */

    private String ilItemCode; // 품목 코드
    private String ilCertificationId; // 상품 인증정보 ID
    private String certificationDescription; // 품목별 인증 상세정보

    private Long createId; // 등록자

    private Long modifyId; // 수정자

}
