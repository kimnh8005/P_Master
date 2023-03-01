package kr.co.pulmuone.v1.outmall.order.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchOutMallVo;
import kr.co.pulmuone.v1.outmall.order.dto.*;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallExcelInfoVo;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallShippingExceldownHistVo;

import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 외부몰관리 > 외부몰주문관리
 */
public interface OutmallOrderBiz {

    ApiResult<?> getCollectionMallInterfaceList(CollectionMallInterfaceListRequestDto dto);

    ApiResult<?> putCollectionMallInterfaceProgress(String processCode, Long ifEasyadminInfoId);

    ApiResult<?> putCollectionMallInterfaceProgressList(CollectionMallInterfaceProgressRequestDto dto);

    ExcelDownloadDto getCollectionMallFailExcelDownload(CollectionMallInterfaceFailRequestDto dto);

    ApiResult<?> getSellersList(String sellersGroupCode);

    ApiResult<?> getClaimOrderList(ClaimOrderListRequestDto dto);

    ExcelDownloadDto getClaimOrderListExportExcel(ClaimOrderListRequestDto dto);

    ApiResult<?> putClaimOrderProgress(ClaimOrderProgressRequestDto dto);

    ApiResult<?> putClaimOrderProgressList(ClaimOrderProgressRequestDto dto);

    ApiResult<?> addOutMallExcelUpload(MultipartFile file) throws Exception;

    List<String> getOutMallExcelDetailId(String outmallType, List<String> detailIdList);

    ApiResult<?> getOutMallExcelInfoList(OutMallExcelListRequestDto dto);

    ExcelDownloadDto getOutMallFailExcelDownload(OutMallExcelFailRequestDto dto) throws InvocationTargetException, IllegalAccessException;


    List<String> getCollectionMallIdList(String outmallType, List<String> searchList);

    List<String> getCollectionMallDetailIdList(String outmallType, List<String> searchList);

    List<String> getOutmallIdList(String outmallType, List<String> searchList);

    ExcelDownloadDto getOutmallShippingInfoDownload(OutMallShippingInfoDownloadRequestDto dto);

    OutMallShippingInfoDownloadHistResponseDto getOutMallShippingExceldownHist(OutMallShippingInfoDownloadHistRequestDto reqDto) throws Exception;

    OutMallTrackingNumberHistResponseDto getOutMallTrackingNumberHist(OutMallTrackingNumberHistRequestDto reqDto) throws Exception;
}