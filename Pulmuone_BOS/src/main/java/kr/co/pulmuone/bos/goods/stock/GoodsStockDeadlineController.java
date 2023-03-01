package kr.co.pulmuone.bos.goods.stock;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.goods.stock.dto.StockDeadlineRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.StockDeadlineResponseDto;
import kr.co.pulmuone.v1.goods.stock.service.GoodsStockDeadlineBiz;

/**
 * <PRE>
 * Forbiz Korea
 * 재고 기한관리
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200707		   	박영후            최초작성
 *  	   20200810		   	강윤경            수정
 * =======================================================================
 * </PRE>
 */
@RestController
@RequiredArgsConstructor
public class GoodsStockDeadlineController {

	private final GoodsStockDeadlineBiz stockDeadlineBiz;

	@Autowired(required=true)
	private HttpServletRequest request;


	@ApiOperation(value = "재고기한관리 목록 조회")
	@PostMapping(value = "/admin/il/stock/getStockDeadlineList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = StockDeadlineResponseDto.class)
	})
	public ApiResult<?> getStockDeadlineList(StockDeadlineRequestDto stockDeadlineRequestDto) throws Exception {
		return stockDeadlineBiz.getStockDeadlineList(BindUtil.bindDto(request, StockDeadlineRequestDto.class));
	}


	@ApiOperation(value = "재고기한관리 추가")
	@PostMapping(value = "/admin/il/stock/addStockDeadline")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = StockDeadlineResponseDto.class),
            @ApiResponse(code = 901, message = "" +
                    "DUPLICATE_DATA - 이미 등록 된 유통기간 정보 입니다 "
            )
	})
	public ApiResult<?> addStockDeadline(StockDeadlineRequestDto stockDeadlineRequestDto) throws Exception {
		return stockDeadlineBiz.addStockDeadline(stockDeadlineRequestDto);
	}

	@ApiOperation(value = "재고기한관리 상세 조회")
	@PostMapping(value = "/admin/il/stock/getStockDeadline")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = StockDeadlineResponseDto.class)
	})
	public ApiResult<?> getStockDeadline(StockDeadlineRequestDto stockDeadlineRequestDto) throws Exception {
		return stockDeadlineBiz.getStockDeadline(stockDeadlineRequestDto);
	}


	@ApiOperation(value = "재고기한관리 상세 수정")
	@PostMapping(value = "/admin/il/stock/putStockDeadline")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = StockDeadlineResponseDto.class),
            @ApiResponse(code = 901, message = "" +
                    "UNABLE_UPDATE_DATA - 업데이트 할 수 없는 유통기간 정보 입니다. "
            )
	})
	public ApiResult<?> putStockDeadline(StockDeadlineRequestDto stockDeadlineRequestDto) throws Exception {
		return stockDeadlineBiz.putStockDeadline(stockDeadlineRequestDto);
	}


	@ApiOperation(value = "재고기한관리 이력 목록 조회")
	@PostMapping(value = "/admin/il/stock/getStockDeadlineHistList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = StockDeadlineResponseDto.class)
	})
	public ApiResult<?> getStockDeadlineHistList(StockDeadlineRequestDto stockDeadlineRequestDto) throws Exception {
		return stockDeadlineBiz.getStockDeadlineHistList(BindUtil.bindDto(request, StockDeadlineRequestDto.class));
	}
}



