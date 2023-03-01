package kr.co.pulmuone.v1.goods.item.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemCertificationCodeVo { // 상품 인증정보 코드 조회 Vo

    private String ilCertificationId; // 상품 인증정보 ID

    private String certificationName; // 상품 인증정보명

    private String defaultCertificationDescription; // 상품 인증정보 상세정보 : 기본 메시지로 사용

    private String imagePath; // 상품 인증정보 이미지 경로

    private String imageName; // 상품 인증정보 이미지 물리적 파일명

}
