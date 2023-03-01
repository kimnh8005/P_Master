package kr.co.pulmuone.v1.outmall.order.service;


import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.base.dto.GetCodeListResponseDto;
import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.ApiEnums;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadValidateEnums;
import kr.co.pulmuone.v1.comm.enums.OutmallEnums;
import kr.co.pulmuone.v1.comm.mapper.outmall.ezadmin.EZAdminOrderRegistrationMapper;
import kr.co.pulmuone.v1.comm.mapper.outmall.order.OutmallOrderMapper;
import kr.co.pulmuone.v1.outmall.order.dto.*;
import kr.co.pulmuone.v1.outmall.order.dto.vo.*;
import kr.co.pulmuone.v1.outmall.sellers.service.SellersBiz;
import kr.co.pulmuone.v1.policy.fee.dto.OmBasicFeeListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutmallOrderService {

    private final OutmallOrderMapper outmallOrderMapper;

    private final EZAdminOrderRegistrationMapper ezadminOrderRegistrationMapper;

    private final SellersBiz sellersBiz;

    public CollectionMallInterfaceListResponseDto getCollectionMallInterfaceList(CollectionMallInterfaceListRequestDto dto) {

    	dto.setEasyadminBatchTp(ApiEnums.EZAdminGetOrderInfoOrderCs.ORDER.getBatchTp());

        if (StringUtils.isNotEmpty(dto.getProcessCodeFilter())) {
        	if(dto.getProcessCodeFilter().indexOf("ALL") < 0) {
	            dto.setProcessCodeList(Stream.of(dto.getProcessCodeFilter().split(Constants.ARRAY_SEPARATORS))
	                    .map(String::trim)
	                    .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
	                    .collect(Collectors.toList()));
        	}
        }

        PageMethod.startPage(dto.getPage(), dto.getPageSize());

        Page<CollectionMallInterfaceListVo> result = null;
        // 주문연동 / 송장연동구분
        if(ApiEnums.EZAdminApiAction.SET_TRANS_NO.getCode().equals(dto.getActionNm())) {
            // 송장연동
            result = outmallOrderMapper.getCollectionMallInterfaceListForTrans(dto);
        } else {
            // 주문연동
            result = outmallOrderMapper.getCollectionMallInterfaceList(dto);
        }

        return CollectionMallInterfaceListResponseDto.builder()
                .total((int) result.getTotal())
                .rows(result.getResult())
                .build();
    }

    public void putCollectionMallInterfaceProgress(String processCode, Long adminId, Long ifEasyadminInfoId) {
        outmallOrderMapper.putCollectionMallInterfaceProgress(processCode, adminId, ifEasyadminInfoId);
    }

    public void putCollectionMallInterfaceProgressList(CollectionMallInterfaceProgressRequestDto dto) {
        outmallOrderMapper.putCollectionMallInterfaceProgressList(dto);
    }

    public List<CollectionMallInterfaceFailVo> getCollectionMallFailExcelDownload(CollectionMallInterfaceFailRequestDto dto) {

        if(OutmallEnums.OutmallFailType.TRANS.getCode().equals(dto.getFailType())) {
            dto.setBatchStartDateTime(dto.getBatchStartDateTime().replaceAll("-", ""));
            return outmallOrderMapper.getCollectionMallFailExcelDownloadForTrans(dto);
        } else {
            return outmallOrderMapper.getCollectionMallFailExcelDownload(dto);
        }
    }

    public GetCodeListResponseDto getSellersList(String sellersGroupCode) {
        List<GetCodeListResultVo> rows = outmallOrderMapper.getSellersList(sellersGroupCode);

        GetCodeListResponseDto result = new GetCodeListResponseDto();
        result.setRows(rows);

        return result;
    }

    public ClaimOrderListResponseDto getClaimOrderList(ClaimOrderListRequestDto dto) {

        if (StringUtils.isNotEmpty(dto.getOutMallType())) {
            if (dto.getOutMallType().equals("")) {
                dto.setShopIdList(Stream.of(dto.getOutMallFilterAll().split(Constants.ARRAY_SEPARATORS))
                        .map(String::trim)
                        .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                        .collect(Collectors.toList()));
            }else if(dto.getOutMallType().equals(ExcelUploadValidateEnums.SellersGroup.DIRECT_BUY.getCode())){
                dto.setShopIdList(Stream.of(dto.getOutMallFilterDirectBuy().split(Constants.ARRAY_SEPARATORS))
                        .map(String::trim)
                        .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                        .collect(Collectors.toList()));
            }else if(dto.getOutMallType().equals(ExcelUploadValidateEnums.SellersGroup.DIRECT_MNG.getCode())){
                dto.setShopIdList(Stream.of(dto.getOutMallFilterDirectMng().split(Constants.ARRAY_SEPARATORS))
                        .map(String::trim)
                        .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                        .collect(Collectors.toList()));
            }else if(dto.getOutMallType().equals(ExcelUploadValidateEnums.SellersGroup.VENDOR.getCode())){
                dto.setShopIdList(Stream.of(dto.getOutMallFilterVendor().split(Constants.ARRAY_SEPARATORS))
                        .map(String::trim)
                        .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                        .collect(Collectors.toList()));
            }
        }

        if (StringUtils.isNotEmpty(dto.getOrderCsFilter())) {
            dto.setOrderCsList(Stream.of(dto.getOrderCsFilter().split(Constants.ARRAY_SEPARATORS))
                    .map(String::trim)
                    .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                    .collect(Collectors.toList()));
        }

        if (StringUtils.isNotEmpty(dto.getOrderStatusFilter())) {
            dto.setOrderStatusList(Stream.of(dto.getOrderStatusFilter().split(Constants.ARRAY_SEPARATORS))
                    .map(String::trim)
                    .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x) && !"NULL".equalsIgnoreCase(x))
                    .collect(Collectors.toList()));

            if(dto.getOrderStatusFilter().indexOf("ALL") >= 0 || dto.getOrderStatusFilter().indexOf("NULL") >= 0) {
                dto.setOrderStatusNotFilter("NULL");
            }
        }

        if (StringUtils.isNotEmpty(dto.getProcessCodeFilter())) {
            dto.setProcessCodeList(Stream.of(dto.getProcessCodeFilter().split(Constants.ARRAY_SEPARATORS))
                    .map(String::trim)
                    .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                    .collect(Collectors.toList()));
        }

        PageMethod.startPage(dto.getPage(), dto.getPageSize());
        Page<ClaimOrderListVo> result = outmallOrderMapper.getClaimOrderList(dto);

        return ClaimOrderListResponseDto.builder()
                .total((int) result.getTotal())
                .rows(result.getResult())
                .build();
    }

    public void putClaimOrderProgress(ClaimOrderProgressRequestDto dto) {
        outmallOrderMapper.putClaimOrderProgress(dto);
    }

    public void putClaimOrderProgressList(ClaimOrderProgressRequestDto dto) {
        outmallOrderMapper.putClaimOrderProgressList(dto);
    }

    public void addOutMallExcelInfo(OutMallExcelInfoVo vo) {
        outmallOrderMapper.addOutMallExcelInfo(vo);
    }

    public void putOutMallExcelInfo(OutMallExcelInfoVo vo) {
        outmallOrderMapper.putOutMallExcelInfo(vo);
    }

    public void addOutMallExcelSuccess(Long ifOutmallExcelInfoId, List<OutMallExcelSuccessVo> voList) {
        outmallOrderMapper.addOutMallExcelSuccess(ifOutmallExcelInfoId, voList);
    }

    public void deleteOutMallExcelSuccess(Long ifOutmallExcelSuccId) {
        outmallOrderMapper.deleteOutMallExcelSuccess(ifOutmallExcelSuccId);
    }



    public void addOutMallExcelFail(Long ifOutmallExcelInfoId, List<OutMallExcelFailVo> voList) {
        outmallOrderMapper.addOutMallExcelFail(ifOutmallExcelInfoId, voList);
    }

    public void addOutMallExcelFailSelectInsert(Long ifOutmallExcelInfoId, Long ifOutmallExcelSuccId, String failMessage) {
        outmallOrderMapper.addOutMallExcelFailSelectInsert(ifOutmallExcelInfoId, ifOutmallExcelSuccId, failMessage);
    }

    public List<String> getOutMallExcelDetailId(String outmallType, List<String> detailIdList) {
        return outmallOrderMapper.getOutMallExcelDetailId(outmallType, detailIdList);
    }

    public OutMallExcelListResponseDto getOutMallExcelInfoList(OutMallExcelListRequestDto dto) {
        PageMethod.startPage(dto.getPage(), dto.getPageSize());
        Page<OutMallExcelInfoVo> result = outmallOrderMapper.getOutMallExcelInfoList(dto);

        return OutMallExcelListResponseDto.builder()
                .total((int) result.getTotal())
                .rows(result.getResult())
                .build();
    }

    protected OutMallExcelInfoVo getOutMallExcelInfo(OutMallExcelFailRequestDto outMallExcelFailRequestDto) {
    	return outmallOrderMapper.getOutMallExcelInfo(outMallExcelFailRequestDto);
    }

    public List<OutMallExcelFailVo> getOutMallFailExcelDownload(OutMallExcelFailRequestDto dto) {
        return outmallOrderMapper.getOutMallFailExcelDownload(dto);
    }

    public List<String> getCollectionMallIdList(String outmallType, List<String> collectionMallDetailIdList) {
        return outmallOrderMapper.getCollectionMallIdList(outmallType, collectionMallDetailIdList);
    }

    public List<String> getCollectionMallDetailIdList(String outmallType, List<String> collectionMallDetailIdList) {
        return outmallOrderMapper.getCollectionMallDetailIdList(outmallType, collectionMallDetailIdList);
    }

    public List<String> getOutmallIdList(String outmallType, List<String> collectionMallDetailIdList) {
        return outmallOrderMapper.getOutmallIdList(outmallType, collectionMallDetailIdList);
    }

    public int addEasyAdminExcelRatioPrice(long ifOutmallExcelInfoId) {
        return outmallOrderMapper.addEasyAdminExcelRatioPrice(ifOutmallExcelInfoId);
    }

    public int putEasyAdminExcelRatioPrice(long ifOutmallExcelInfoId) {
        return outmallOrderMapper.putEasyAdminExcelRatioPrice(ifOutmallExcelInfoId);
    }

    public int addSabangnetExcelRatioPrice(long ifOutmallExcelInfoId) {
        return outmallOrderMapper.addSabangnetExcelRatioPrice(ifOutmallExcelInfoId);
    }

    public int putSabangnetExcelRatioPrice(long ifOutmallExcelInfoId) {
        return outmallOrderMapper.putSabangnetExcelRatioPrice(ifOutmallExcelInfoId);
    }

    public int putOutMallExcelSuccRatioPrice(long ifOutmallExcelInfoId) {
        return outmallOrderMapper.putOutMallExcelSuccRatioPrice(ifOutmallExcelInfoId);
    }

    public int putOutMallExcelSuccGiftPrice(long ifOutmallExcelInfoId) {
        return outmallOrderMapper.putOutMallExcelSuccGiftPrice(ifOutmallExcelInfoId);
    }



    public long getLastIfOutmallExcelInfo() {
        return outmallOrderMapper.getLastIfOutmallExcelInfo();
    }


    public List<OutMallOrderDto> getOutmallOrderCreateTargetList(long ifOutmallExcelInfoId) {
        return outmallOrderMapper.getOutmallOrderCreateTargetList(ifOutmallExcelInfoId);
    }

    protected List<Long> getOdOrderDetlIds(List<String> odOrderIds) throws Exception {
        return ezadminOrderRegistrationMapper.getOdOrderDetlIds(odOrderIds);
    }

    protected List<OutMallOrderSellersDto> getOmSellersInfoList(List<String> excelList) throws Exception {
        List<OutMallOrderSellersDto> omSellersInfoList = outmallOrderMapper.getOmSellersInfoList(excelList);

        if(CollectionUtils.isNotEmpty(omSellersInfoList)){
            for(OutMallOrderSellersDto sellerDto : omSellersInfoList){
                List<OmBasicFeeListDto> supplierList = sellersBiz.getApplyOmBasicFeeList(sellerDto.getOmSellersId());
                sellerDto.setSupplierList(supplierList);
            }
        }

    	return omSellersInfoList;
    }

    protected List<OutMallShippingInfoVo> getOutmallShippingInfoDownload(OutMallShippingInfoDownloadRequestDto reqDto) throws Exception{
    	return outmallOrderMapper.getOutmallShippingInfoDownload(reqDto);
    }

    protected int addOutmallShippingExcelDownHist(OutMallShippingInfoDownloadRequestDto reqDto) throws Exception{
    	return outmallOrderMapper.addOutmallShippingExcelDownHist(reqDto);
    }

    protected OutMallShippingInfoDownloadHistResponseDto getOutMallShippingExceldownHist(OutMallShippingInfoDownloadHistRequestDto reqDto) throws Exception{
    	return OutMallShippingInfoDownloadHistResponseDto.builder()
    				.rows(outmallOrderMapper.getOutMallShippingExceldownHist(reqDto))
    				.total(outmallOrderMapper.getOutMallShippingExceldownHistCount(reqDto))
    				.build();
    }

    protected int putGoodsOutmallStatus(List<Long> updateTargetList) throws Exception{
        return outmallOrderMapper.putGoodsOutmallStatus(updateTargetList);
    }

    protected OutMallTrackingNumberHistResponseDto getOutMallTrackingNumberHist(OutMallTrackingNumberHistRequestDto reqDto) throws Exception{
        return OutMallTrackingNumberHistResponseDto.builder()
                .rows(outmallOrderMapper.getOutMallTrackingNumberHist(reqDto))
                .total(outmallOrderMapper.getOutMallTrackingNumberHistCount(reqDto))
                .build();
    }

    protected int putOutmallExcelSuccOrderCreateYn(String collectionMallId, Long ifOutmallExcelInfoId) throws Exception{
        return outmallOrderMapper.putOutmallExcelSuccOrderCreateYn(collectionMallId, ifOutmallExcelInfoId);
    }

    protected List<HashMap> getNotOrderCreateInIfOutmallExcelSucc(long ifOutmallExcelInfoId) throws Exception{
        return outmallOrderMapper.getNotOrderCreateInIfOutmallExcelSucc(ifOutmallExcelInfoId);
    }

    protected int getOrderCreateCount(long ifOutmallExcelInfoId) throws Exception{
        return outmallOrderMapper.getOrderCreateCount(ifOutmallExcelInfoId);
    }
}
