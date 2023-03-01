package kr.co.pulmuone.v1.goods.item.service;

import kr.co.pulmuone.v1.approval.auth.dto.ApprovalItemRegistRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemPriceApprovalRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemPriceDelRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.MasterItemListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemInfoVo;

public interface GoodsItemBiz {

    public ItemInfoVo getGoodsIdByItemInfo(Long goodsId);

    ApiResult<?> getItemList(MasterItemListRequestDto masterItemListRequestDto);

    ExcelDownloadDto getItemListExcel(MasterItemListRequestDto masterItemListRequestDto);

    ApiResult<?> getApprovalItemRegistList(ApprovalItemRegistRequestDto requestDto);

    ApiResult<?> putCancelRequestApprovalItemRegist(ApprovalItemRegistRequestDto requestDto) throws Exception;

    ApiResult<?> putApprovalProcessItemRegist(ApprovalItemRegistRequestDto requestDto) throws Exception;

    ApiResult<?> getApprovalItemPriceList(ApprovalItemRegistRequestDto requestDto);

    ApiResult<?> putCancelRequestApprovalItemPrice(ApprovalItemRegistRequestDto requestDto) throws Exception;

    ApiResult<?> putApprovalProcessItemPrice(ApprovalItemRegistRequestDto requestDto) throws Exception;

    ApiResult<?> putDisposalApprovalItemPrice(ApprovalItemRegistRequestDto requestDto) throws Exception;

    String getApprovalDeniedInfo(Long ilItemPriceApprId);

    ApiResult<?> getItemGoodsPackageList(MasterItemListRequestDto masterItemListRequestDto);

    ApiResult<?> getApprovalItemClientList(ApprovalItemRegistRequestDto requestDto);

    ApiResult<?> putCancelRequestApprovalItemClient(ApprovalItemRegistRequestDto requestDto) throws Exception;

    ApiResult<?> putDisposalApprovalItemClient(ApprovalItemRegistRequestDto requestDto) throws Exception;

    ApiResult<?> putApprovalProcessItemClient(ApprovalItemRegistRequestDto requestDto) throws Exception;

    ApiResult<?> addApprovalItemPrice(ItemPriceApprovalRequestDto dto) throws Exception;

    ApiResult<?> delItemPriceOrig(ItemPriceDelRequestDto dto) throws Exception;

}
