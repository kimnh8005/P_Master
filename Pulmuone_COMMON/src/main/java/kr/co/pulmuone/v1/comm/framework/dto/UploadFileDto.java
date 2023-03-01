package kr.co.pulmuone.v1.comm.framework.dto;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.interfaceType.StorageInfoBaseType.SaveResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor // Json deserialization 시 @NoArgsConstructor, @AllArgsConstructor 모두 필요
@AllArgsConstructor
@ApiModel(description = "UploadFileDto")
public class UploadFileDto {

    @ApiModelProperty(value = "")
    private String fieldName; // 업로드 태그명 : HTML <input type="file" name="fieldName"> tag 에 작성한 name 속성값

    @ApiModelProperty(value = "")
    private String originalFileName; // 원본 파일명(fileName) (확장자 없음)

    @ApiModelProperty(value = "")
    private String fileExtension; // 업로드 파일 확장자

    @ApiModelProperty(value = "")
    private String serverSubPath; // 업로드시 하위 디렉토리 지정

    @ApiModelProperty(value = "")
    private String physicalFileName; // 물리적 파일명

    @ApiModelProperty(value = "")
    private String contentType; // 업로드 파일 content-type

    @ApiModelProperty(value = "")
    @Builder.Default
    private long fileSize = 0L; // 업로드 파일 size

    @ApiModelProperty(value = "")
    private SaveResult saveResult; // 업로드 처리 결과

    @JsonIgnore // MultipartFile 객체는 json 변환하지 않음
    @ApiModelProperty(value = "")
    private MultipartFile multipartFile; // 업로드 파일 Multipart 객체

//    @ApiModelProperty(value = "첨부파일 고유값")
//    private String psAttcId; // 파일을 저장하면서 PS_ATTC 테이블에서 추가한 첨부파일 고유값

}
