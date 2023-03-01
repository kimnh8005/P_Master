package kr.co.pulmuone.v1.comm.framework.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class UploadOptionInfoDto {

    @Builder.Default
    private long maxFileSize = 0L; // 해당 업로드시 용량 제한 : 0 인 경우 제한하지 않음

    @Builder.Default
    private Boolean fileExtentionCheckc = true; // false : 파일 확장자 체크하지 않음

    @Builder.Default
    private Boolean fileSizeCheck = true; // false : 파일 사이즈 체크하지 않음

}
