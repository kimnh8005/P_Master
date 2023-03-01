package kr.co.pulmuone.v1.goods.item.dto;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemSpecVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@ApiModel(description = "품목별 상품정보 제공고시 조회 Vo => Dto 변환 클래스")
public class ItemSpecDto {

    /*
     * 품목별 상품정보 제공고시 조회 Vo => Dto 변환 클래스
     */

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

    // "유통년월일 및 제조연월일 (포장 또는 생산연도)", "풀무원고객기쁨센터 전화번호" 와 같은 특수한 경우에 세팅되는 메시지
    private String detailMessage;

    // 해당 상품정보제공고시 항목의 수정 가능 여부
    private boolean canModified;

    public static ItemSpecDto toDto(ItemSpecVo getItemSpecVo, String detailMessage, boolean canModified) {

        return ItemSpecDto.builder() //
                .ilspecMasterId(getItemSpecVo.getIlspecMasterId()) // 상품정보제공고시 분류 PK
                .ilSpecFieldId(getItemSpecVo.getIlSpecFieldId()) // 상품정보제공고시 항목 PK
                .specMasterCode(getItemSpecVo.getSpecMasterCode()) // 상품정보제공고시 분류 코드
                .specMasterName(getItemSpecVo.getSpecMasterName()) // 상품정보제공고시 이름
                .specFieldCode(getItemSpecVo.getSpecFieldCode()) // 상품정보제공고시 항목 코드
                .specFieldName(getItemSpecVo.getSpecFieldName()) // 상품정보제공고시 항목명
                .basicValue(getItemSpecVo.getBasicValue()) // 상품정보제공고시 항목 기본값
                .specDescription(getItemSpecVo.getSpecDescription()) // 상품정보제공고시 항목 상세 설명
                .specMasterSort(getItemSpecVo.getSpecMasterSort()) // 상품정보제공고시 분류 정렬 순서
                .specMasterFieldSort(getItemSpecVo.getSpecMasterFieldSort()) // 상품정보제공고시 항목 정렬 순서
                .detailMessage(detailMessage) //
                .canModified(canModified) //
                .build();

    }

}
