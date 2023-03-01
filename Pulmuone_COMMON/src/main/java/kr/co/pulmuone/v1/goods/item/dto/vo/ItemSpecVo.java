package kr.co.pulmuone.v1.goods.item.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemSpecVo { // 품목별 상품정보 제공고시 조회 Vo

    private int ilspecMasterId; // 상품정보제공고시 분류 PK

    private int ilSpecFieldId; // 상품정보제공고시 항목 PK

    private String specMasterCode; // 상품정보제공고시 분류 코드

    private String specMasterName; // 상품정보제공고시 이름

    private String specFieldCode; // 상품정보제공고시 항목 코드

    private String specFieldName; // 상품정보제공고시 항목명

    private String basicValue; // 상품정보제공고시 항목 기본값

    private String specDescription; // 상품정보제공고시 항목 상세 설명

    private int specMasterSort; // 상품정보제공고시 분류 정렬 순서

    private int specMasterFieldSort; // 상품정보제공고시 항목 정렬 순서

}
