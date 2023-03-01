package kr.co.pulmuone.v1.customer.qna.dto.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "1:1문의 이미지 등록 VO")
public class OnetooneQnaByUserAttcVo {

	/*문의 PK*/
    private Long csQnaId;

    /*이미지 원본 파일명 ::*/
    private String imageOriginalName;

    /*이미지 파일명*/
    private String imageName;

    /*이미지 저장경로 ::*/
    private String imagePath;

    /*썸네일 원본 파일명*/
    private String thumbnailOriginalName;

    /*썸네일 파일명*/
    private String thumbnailName;

    /*썸네일 파일경로*/
    private String thumbnailPath;

}
