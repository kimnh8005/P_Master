package kr.co.pulmuone.bos.approval.item;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.approval.auth.dto.ApprovalItemRegistRequestDto;
import kr.co.pulmuone.v1.approval.auth.dto.ApprovalItemRegistResponseDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.goods.item.dto.ItemApprovalResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemPriceApprovalRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemPriceDelRequestDto;
import kr.co.pulmuone.v1.goods.item.service.GoodsItemBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0		20210222		박승현              최초작성
 * <p>
 * =======================================================================
 * </PRE>
 */
@Slf4j
@RestController
public class ApprovalItemPriceController {

    @Autowired
    private GoodsItemBiz goodsItemBiz;

    @Autowired
    private HttpServletRequest request;

    @PostMapping(value = "/admin/approval/item/getApprovalItemPriceList")
    @ApiOperation(value = "품목가격 승인 목록 조회", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ItemApprovalResponseDto.class)
    })
    public ApiResult<?> getApprovalItemPriceList(ApprovalItemRegistRequestDto requestDto) throws Exception {
        return goodsItemBiz.getApprovalItemPriceList((ApprovalItemRegistRequestDto) BindUtil.convertRequestToObject(request, ApprovalItemRegistRequestDto.class));
    }

    @RequestMapping(value = "/admin/approval/item/putCancelRequestApprovalItemPrice")
    @ApiOperation(value = "품목가격 승인 요청철회", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ApprovalItemRegistResponseDto.class)
    })
    public ApiResult<?> putCancelRequestApprovalItemPrice(@RequestBody ApprovalItemRegistRequestDto dto) throws Exception {
        return goodsItemBiz.putCancelRequestApprovalItemPrice(dto);
    }

    @RequestMapping(value = "/admin/approval/item/putApprovalProcessItemPrice")
    @ApiOperation(value = "품목가격 승인 / 반려 처리", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ApprovalItemRegistResponseDto.class)
    })
    public ApiResult<?> putApprovalProcessItemPrice(@RequestBody ApprovalItemRegistRequestDto dto) throws Exception {
        return goodsItemBiz.putApprovalProcessItemPrice(dto);
    }

    @RequestMapping(value = "/admin/approval/item/putDisposalApprovalItemPrice")
    @ApiOperation(value = "품목가격 폐기 처리", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data")
    })
    public ApiResult<?> putDisposalApprovalItemPrice(@RequestBody ApprovalItemRegistRequestDto dto) throws Exception {
        return goodsItemBiz.putDisposalApprovalItemPrice(dto);
    }

    @RequestMapping(value = "/admin/approval/item/getApprovalDeniedInfo")
    @ApiOperation(value = "품목가격 반려 사유 조회", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = String.class)
    })
    public ApiResult<?> getApprovalDeniedInfo(@RequestBody ApprovalItemRegistRequestDto dto) throws Exception {
        if (dto.getIlItemPriceApprIdList() == null || dto.getIlItemPriceApprIdList().size() == 0)
            return ApiResult.fail();
        Long ilItemPriceApprId = Long.valueOf(dto.getIlItemPriceApprIdList().get(0));
        return ApiResult.success(goodsItemBiz.getApprovalDeniedInfo(ilItemPriceApprId));
    }

    @RequestMapping(value = "/admin/approval/item/addApprovalItemPrice")
    @ApiOperation(value = "품목가격 승인요청", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data")
    })
    public ApiResult<?> addApprovalItemPrice(@RequestBody ItemPriceApprovalRequestDto dto) throws Exception {
        return goodsItemBiz.addApprovalItemPrice(dto);
    }

    @RequestMapping(value = "/admin/approval/item/delItemPriceOrig")
    @ApiOperation(value = "품목가격 삭제처리", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data")
    })
    public ApiResult<?> delItemPriceOrig(ItemPriceDelRequestDto dto) throws Exception {
        return goodsItemBiz.delItemPriceOrig(dto);
    }

}

