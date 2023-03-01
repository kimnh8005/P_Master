package kr.co.pulmuone.v1.user.urcompany.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.user.urcompany.dto.vo.GetSupplierListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@ApiModel(description = "GetSupplierListResponseDto")
public class GetSupplierListResponseDto  extends BaseResponseDto{

	@ApiModelProperty(value = "리스트")
	private	List<GetSupplierListResultVo> rows;

}
