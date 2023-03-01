package kr.co.pulmuone.v1.goods.goods.service;

import kr.co.pulmuone.v1.approval.auth.dto.ApprovalGoodsRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsListRequestDto;

public interface GoodsListBiz {

    ApiResult<?> getGoodsList(GoodsListRequestDto goodsListRequestDto);
    ApiResult<?> putGoodsListSaleStatusChange(GoodsListRequestDto goodsListRequestDto) throws Exception;

    ExcelDownloadDto getGoodsListExcel(GoodsListRequestDto goodsListRequestDto);

    ApiResult<?> getApprovalGoodsRegistList(ApprovalGoodsRequestDto approvalGoodsRequestDto);
    ApiResult<?> putCancelRequestApprovalGoodsRegist(ApprovalGoodsRequestDto approvalGoodsRequestDto) throws Exception;
    ApiResult<?> putApprovalProcessGoodsRegist(ApprovalGoodsRequestDto approvalGoodsRequestDto) throws Exception;

    ApiResult<?> getApprovalGoodsClientList(ApprovalGoodsRequestDto approvalGoodsRequestDto);
    ApiResult<?> putCancelRequestApprovalGoodsClient(ApprovalGoodsRequestDto approvalGoodsRequestDto) throws Exception;
    ApiResult<?> putDisposalApprovalGoodsClient(ApprovalGoodsRequestDto approvalGoodsRequestDto) throws Exception;
    ApiResult<?> putApprovalProcessGoodsClient(ApprovalGoodsRequestDto approvalGoodsRequestDto) throws Exception;
    ApiResult<?> getGoodsClientDetail(String ilGoodsApprId);

    ApiResult<?> getApprovalGoodsDiscountList(ApprovalGoodsRequestDto approvalGoodsRequestDto);
    ApiResult<?> putCancelRequestApprovalGoodsDiscount(ApprovalGoodsRequestDto approvalGoodsRequestDto) throws Exception;
    ApiResult<?> putApprovalProcessGoodsDiscount(ApprovalGoodsRequestDto approvalGoodsRequestDto) throws Exception;
    ApiResult<?> putDisposalApprovalGoodsDiscount(ApprovalGoodsRequestDto approvalGoodsRequestDto) throws Exception;

}
