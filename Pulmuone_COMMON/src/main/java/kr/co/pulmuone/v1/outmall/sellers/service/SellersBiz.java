package kr.co.pulmuone.v1.outmall.sellers.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.outmall.sellers.dto.SellersListRequestDto;
import kr.co.pulmuone.v1.outmall.sellers.dto.SellersRequestDto;
import kr.co.pulmuone.v1.policy.fee.dto.OmBasicFeeListDto;

import java.util.List;

/**
 * 외부몰관리 > 판매처관리 : 판매처관리 관련 Interface
 */
public interface SellersBiz {

	ApiResult<?> getSellersList(SellersListRequestDto sellersListRequestDto) throws Exception;

    ExcelDownloadDto getSellersExcelList(SellersListRequestDto sellersListRequestDto);

    ApiResult<?> getSellersGroupList(SellersListRequestDto sellersListRequestDto) throws Exception;

    ApiResult<?> getSellers(SellersRequestDto sellersRequestDto) throws Exception;

    ApiResult<?> addSellers(SellersRequestDto sellersRequestDto) throws Exception;

    ApiResult<?> putSellers(SellersRequestDto sellersRequestDto) throws Exception;

    List<OmBasicFeeListDto> getApplyOmBasicFeeList(long omSellersId) throws Exception;

    Long putErpInterfaceStatusChg(SellersRequestDto sellersRequestDto) throws Exception;
}
