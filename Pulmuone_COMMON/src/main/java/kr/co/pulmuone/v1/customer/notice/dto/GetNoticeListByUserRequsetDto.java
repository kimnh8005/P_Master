package kr.co.pulmuone.v1.customer.notice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.MallBaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "공지사항 리스트 RequestDto")
public class GetNoticeListByUserRequsetDto  extends MallBaseRequestPageDto{

	@ApiModelProperty(value= "공지사항 분류")
	private String noticeType;

	@ApiModelProperty(value= "디바이스 구분")
	private String deviceType;


}
