package kr.co.pulmuone.v1.comm.base.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "FileVO")
@SuppressWarnings("serial")
public class FileVo implements Serializable {

	@ApiModelProperty(value = "")
    private String fieldName = "";  //업로드 필드명 HTML <input type="file" name="fieldName"> tag 에 작성한 name 속성값

	@ApiModelProperty(value = "")
	private String	originalFileName  ;  //원본 파일명(fileName) (확장자 없다)

	@ApiModelProperty(value = "")
    private String physicalFileName ;  //물리적 파일명 (확장자 없다)

	@ApiModelProperty(value = "")
    private String contentType ;    //업로드 파일 content-type

	@ApiModelProperty(value = "")
	private long fileSize = 0L;     //업로드 파일 size

	@ApiModelProperty(value = "")
	private	 String	fileExt ;        //업로드 파일 확장자

	@ApiModelProperty(value = "")
    private String serverSubPath = ""; //하위 디렉토리 지정

	@ApiModelProperty(value = "")
	private	String	saveResult;     //업로드 처리 결과

    /*------------------------------------------------------
    -- 기타 변수 - 시작
    ------------------------------------------------------*/
	@ApiModelProperty(value = "첨부파일 고유값")
	private	String	psAttcId; //파일을 저장하면서 PS_ATTC 테이블에서 추가한 첨부파일 고유값
    /*------------------------------------------------------
    -- 기타 변수 - 끝
    ------------------------------------------------------*/
}
