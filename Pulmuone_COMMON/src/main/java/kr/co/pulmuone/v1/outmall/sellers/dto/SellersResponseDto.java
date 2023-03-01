package kr.co.pulmuone.v1.outmall.sellers.dto;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.outmall.sellers.dto.vo.SellersListVo;
import kr.co.pulmuone.v1.outmall.sellers.dto.vo.SellersSuppilerVo;
import kr.co.pulmuone.v1.outmall.sellers.dto.vo.SellersVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "판매처 등록/수정 응답 Response Dto")
public class SellersResponseDto extends BaseResponseDto {

	private SellersVo rows;
}
