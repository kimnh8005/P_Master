package kr.co.pulmuone.v1.goods.item.dto.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class ItemImageRegisterVo {

    /*
     * 품목 이미지 등록 VO
     */

    private String ilItemCode; // 품목 코드
    private String ilItemApprId;
    @Builder.Default
    private boolean basicYn = false; // 품목 기본 이미지 여부 ( DB 저장시 => Y : 기본 이미지 )

    private String imagePath; // 이미지 원본 파일 경로 ( 물리적 파일명 포함 )
    private String imageOriginalName; // 이미지 원본 파일명
    private String size640imagePath; // 640*640 이미지 파일 경로 ( 물리적 파일명 포함 )
    private String size320imagePath; // 320*320 이미지 파일 경로 ( 물리적 파일명 포함 )
    private String size216imagePath; // 216*216 이미지 파일 경로 ( 물리적 파일명 포함 )
    private String size180imagePath; // 180*180 이미지 파일 경로 ( 물리적 파일명 포함 )
    private String size75imagePath; // 75*75 파일 이미지 경로 ( 물리적 파일명 포함 )
    private int sort; // 정렬 순서

    private Long createId; // 등록자

    private Long modifyId; // 수정자

    /*
     * 해당 이미지의 원본 파일명이 인자로 받은 대표 이미지명과 같은 경우 => 품목 기본 이미지 여부 true, 정렬순서 0 으로 세팅
     *
     * - 최초 이미지 등록시 원본 파일명 : 사용자가 올린 이미지의 파일명
     *
     * - 마스터 품목 복사시 원본 파일명 : 기존 물리적 파일명 ( 경로 제외 ) 을 원본 파일명으로 사용하고 물리적 파일명 새로 생성
     */
    public void setBasicYn(String representativeImageName) {

        if (this.imageOriginalName.equals(representativeImageName)) {
            this.basicYn = true;
            this.sort = 0;
        }

    }

    /*
     * 기존에 이미 등록된 품목 이미지를 대표 이미지로 지정시 사용하는 setter
     */
    public void setBasicYn(boolean basicYn) {
        this.basicYn = basicYn;
    }

}
