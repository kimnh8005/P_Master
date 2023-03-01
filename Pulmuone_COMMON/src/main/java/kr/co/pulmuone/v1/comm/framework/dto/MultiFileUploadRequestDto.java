package kr.co.pulmuone.v1.comm.framework.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@ApiModel(description = "multiFileUploadRequestDto")
public class MultiFileUploadRequestDto {

    /*
     * 업로드 파일 객체 : 멀티 업로드 지원을 위해 List 로 선언
     */
    @ApiModelProperty(value = "")
    @Builder.Default
    private List<UploadFileDto> uploadFileDtoList = new ArrayList<>();

    /*
     * 화면에서 업로드 전송시 전달된 업무 도메인 prefix
     */
    private String domainPrefix;

    /*
     * 화면에서 업로드 전송시 전달된 저장 타입
     */
    private String storageType;

    /*
     * 현재 업로드 파일들의 실제 저장 경로
     */
    private String fullStoragePath;

    /*
     * 업로드 옵션 Info 객체 : 업로드와 관련된 추가 정보 저장
     */
    private UploadOptionInfoDto uploadOptionInfo;

    // ---------- Custom Method ---------- //

    public void addUploadFileDto(UploadFileDto uploadFileDto) {

        if (uploadFileDtoList == null) { // NullPointerException 방지
            uploadFileDtoList = new ArrayList<>();
        }

        uploadFileDtoList.add(uploadFileDto);
    }

}
