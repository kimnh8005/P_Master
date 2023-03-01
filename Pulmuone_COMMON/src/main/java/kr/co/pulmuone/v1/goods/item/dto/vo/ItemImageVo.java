package kr.co.pulmuone.v1.goods.item.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemImageVo { // 품목 이미지 조회 VO

    private String ilItemCode; // 품목 코드

    private boolean basicYn; // 품목 기본 이미지 여부 ( DB 저장시 => Y : 기본 이미지 )

    private String imagePath; // 이미지 파일 원본 경로 ( 물리적 파일명 포함 )

    private String imagePhysicalName; // 이미지 물리적 파일명 ( 이미지 저장시 이름 중복 방지 로직 적용 )

    private String imageOriginalName; // 이미지 원본 파일명

    private String size640ImagePath; // 640*640 이미지 파일 경로 ( 물리적 파일명 포함 )

    private String size640ImagePhysicalName; // 640*640 이미지 물리적 파일명

    private String size320ImagePath; // 320*320 이미지 파일 경로 ( 물리적 파일명 포함 )

    private String size320ImagePhysicalName; // 320*320 이미지 물리적 파일명

    private String size216ImagePath; // 216*216 이미지 파일 경로 ( 물리적 파일명 포함 )

    private String size216ImagePhysicalName; // 216*216 이미지 물리적 파일명

    private String size180ImagePath; // 180*180 이미지 파일 경로 ( 물리적 파일명 포함 )

    private String size180ImagePhysicalName; // 180*180 이미지 물리적 파일명

    private String size75ImagePath; // 75*75 이미지 파일 경로 ( 물리적 파일명 포함 )

    private String size75ImagePhysicalName; // 75*75 이미지 물리적 파일명

    private int sort; // 정렬 순서

    private Long createId; // 최초 품목 이미지 등록자 ID

    private Long modifyId; // 수정자 ID

}
