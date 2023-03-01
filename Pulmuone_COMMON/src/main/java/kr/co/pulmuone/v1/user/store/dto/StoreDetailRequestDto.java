package kr.co.pulmuone.v1.user.store.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.comm.framework.dto.UploadFileDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "매장상세 Request")
public class StoreDetailRequestDto extends BaseRequestDto{

	@ApiModelProperty(value = "매장 스토어 ID", required = true)
	private String urStoreId;

	@ApiModelProperty(value = "매장 PC 이미지 파일 업로드 정보", required = false)
	List<UploadFileDto> storePcImageUploadResultList;

	@ApiModelProperty(value = "매장 PC 이미지 URL", required = false)
	private String storePcImageUrl;

	@ApiModelProperty(value = "매장 Mobile 이미지 파일 업로드 정보", required = false)
	List<UploadFileDto> storeMobileImageUploadResultList;

	@ApiModelProperty(value = "매장 Mobile 이미지 URL", required = false)
	private String storeMobileImageUrl;

	@ApiModelProperty(value = "이미지를 저장할 최상위 저장 디렉토리 경로", required = false)
	private String imageRootStoragePath;

	@ApiModelProperty(value = "노출여부", required = false)
	private String useYn;

	@ApiModelProperty(value = "이미지 타입", required = false)
	private String storeImageType;

	@ApiModelProperty(value = "매장상태", required = false)
	private String shopStockStatus;

	@ApiModelProperty(value = "배송타입코드", required = false)
	private String storeDeliveryTp;

}
