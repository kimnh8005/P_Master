package kr.co.pulmuone.bos.item.po;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoTypeListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoTypeListResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoTypeRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoTypeResponseDto;
import kr.co.pulmuone.v1.goods.item.service.GoodsItemPoTypeBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * 발주 유형관리
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020.07.28	  박영후            최초작성
 *  1.0    2020.10.27	  이성준            리팩토링
 * =======================================================================
 * </PRE>
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemPoTypeController {

	private final GoodsItemPoTypeBiz goodsItemPoTypeBiz;


	@ApiOperation(value = "발주 유형관리 목록 조회")
	@PostMapping(value = "/admin/item/potype/getItemPoTypeList")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ItemPoTypeListResponseDto.class)
    })
	public ApiResult<?> getItemPoTypeList(HttpServletRequest request, ItemPoTypeListRequestDto itemPoTypeListRequestDto) throws Exception {
		return goodsItemPoTypeBiz.getItemPoTypeList(BindUtil.bindDto(request, ItemPoTypeListRequestDto.class));
	}


	@ApiOperation(value = "발주 유형관리 상세 조회")
	@PostMapping(value = "/admin/item/potype/getItemPoType")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ItemPoTypeResponseDto.class)
    })
	public ApiResult<?> getItemPoType(@RequestParam(value = "ilPoTpId", required = true) String ilPoTpId) {
		return goodsItemPoTypeBiz.getItemPoType(ilPoTpId);
	}

	@ApiOperation(value = "발주일 상세 조회")
	@PostMapping(value = "/admin/item/potype/getItemPoDay")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ItemPoTypeResponseDto.class)
    })
	public ApiResult<?> getItemPoDay(@RequestParam(value = "ilPoTpId", required = true) String ilPoTpId
			                        ,@RequestParam(value = "eventStartDtNumber", required = true) String eventStartDtNumber
			                        ) {
		return goodsItemPoTypeBiz.getItemPoDay(ilPoTpId, eventStartDtNumber);
	}

	@ApiOperation(value = "발주 유형관리 추가")
	@PostMapping(value = "/admin/item/potype/addItemPoType")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ItemPoTypeRequestDto.class),
            @ApiResponse(code = 901, message = "" + "DUPLICATE_DATA - 중복된 데이터가 존재합니다.")
    })
	public ApiResult<?> addItemPoType(ItemPoTypeRequestDto itemPoTypeRequestDto) throws Exception {
		     return goodsItemPoTypeBiz.addItemPoType(itemPoTypeRequestDto);
    }


	@ApiOperation(value = "발주 유형관리 수정")
	@PostMapping(value = "/admin/item/potype/putItemPoType")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ItemPoTypeRequestDto.class),
            @ApiResponse(code = 901, message = "" + "DUPLICATE_DATA - 중복된 데이터가 존재합니다.")
    })
	public ApiResult<?> putItemPoType(ItemPoTypeRequestDto itemPoTypeRequestDto) throws Exception {
		     return goodsItemPoTypeBiz.putItemPoType(itemPoTypeRequestDto);
	}

	/**
	 * 발주 유형관리 삭제
	 * @param	ilPoTpId 발주유형 PK
	 * @return	PoTypeResponseDto
	 * @throws Exception
	 */
//	@PostMapping(value = "/admin/il/poType/delPoType")
//	public PoTypeResponseDto delPoType(@RequestParam(value = "ilPoTpId", required = true) String ilPoTpId) throws Exception {
//
//		return poTypeService.delPoType(ilPoTpId);
//	}

}
