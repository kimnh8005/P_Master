package kr.co.pulmuone.bos.goods.notice;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.goods.notice.dto.GoodsNoticeDto;
import kr.co.pulmuone.v1.goods.notice.dto.vo.GoodsNoticeVo;
import kr.co.pulmuone.v1.goods.notice.service.GoodsNoticeBiz;
import lombok.RequiredArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0		20201203		박승현              최초작성
 *
 * =======================================================================
 * </PRE>
 *
 */
@RestController
@RequiredArgsConstructor
public class GoodsNoticeController {


	@Autowired
	private GoodsNoticeBiz goodsNoticeBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	/**
	 * 상품공통공지 조회
	 * @param ilNoticeId
	 * @return ApiResult
	 */
	@GetMapping(value = "/admin/goods/notice/getGoodsNoticeInfo")
	@ApiOperation(value = "상품공통공지 조회", httpMethod = "GET")
    @ApiImplicitParams({ @ApiImplicitParam(name = "ilNoticeId", value = "상품공통공지 PK", required = true, dataType = "String")})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GoodsNoticeVo.class)
	})
	public ApiResult<?> getGoodsNoticeInfo(@RequestParam(value = "ilNoticeId", required = true) String ilNoticeId){
		return goodsNoticeBiz.getGoodsNoticeInfo(ilNoticeId);
	}
	/**
	 * 상품공통공지 목록 조회
	 * @param GoodsNoticeDto
	 * @return ApiResult
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/goods/notice/getGoodsNoticeList")
	@ApiOperation(value = "상품공통공지 목록 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = GoodsNoticeDto.class)
	})
	public ApiResult<?> getGoodsNoticeList(GoodsNoticeDto dto) throws Exception {
		return goodsNoticeBiz.getGoodsNoticeList((GoodsNoticeDto)BindUtil.convertRequestToObject(request, GoodsNoticeDto.class));
	}

	/**
	 * 상품공통공지 신규 등록
	 * @param GoodsNoticeDto
	 * @return ApiResult
	 */
	@RequestMapping(value = "/admin/goods/notice/addGoodsNotice")
	@ApiOperation(value = "상품공통공지 신규 등록", httpMethod = "POST")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> addGoodsNotice(GoodsNoticeDto dto) {
		return goodsNoticeBiz.addGoodsNotice(dto);
	}

	/**
	 * 상품공통공지 수정
	 * @param GoodsNoticeDto
	 * @return ApiResult
	 */
	@RequestMapping(value = "/admin/goods/notice/putGoodsNotice")
	@ApiOperation(value = "상품공통공지 수정", httpMethod = "POST")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> putGoodsNotice(GoodsNoticeDto dto) {
		return goodsNoticeBiz.putGoodsNotice(dto);
	}
}

