package kr.co.pulmuone.v1.comm.framework.dto;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@Builder
@ApiModel(description = "ServerUrlDto")
public class ServerUrlDto extends BaseResponseDto {

	// BOS URL
    private String bosUrl;

    // MALL URL
    private String mallUrl;

    // IMAGE URL
    private String imageUrl;

}
