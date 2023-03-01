package kr.co.pulmuone.v1.comm.framework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.comm.interfaceType.StorageInfoBaseType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@ApiModel(description = "UploadResultDto")
public class UploadResultDto extends BaseResponseDto {

    @JsonProperty(StorageInfoBaseType.UPLOAD_RESULT_RETURN_KEY)
    private List<UploadFileDto> uploadFileDtoList;

    // 프로파일별 public 저장소 url 경로
    private String publicStorageUrl;

    // public 저장소를 경유하지 않고 API 서버로 저장소 접근시 url 경로 ( CORS 회피용 )
    private String publicUrlPath;

}
