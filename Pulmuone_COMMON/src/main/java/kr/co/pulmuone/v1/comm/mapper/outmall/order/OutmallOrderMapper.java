package kr.co.pulmuone.v1.comm.mapper.outmall.order;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.outmall.order.dto.ClaimOrderListRequestDto;
import kr.co.pulmuone.v1.outmall.order.dto.ClaimOrderProgressRequestDto;
import kr.co.pulmuone.v1.outmall.order.dto.CollectionMallInterfaceFailRequestDto;
import kr.co.pulmuone.v1.outmall.order.dto.CollectionMallInterfaceListRequestDto;
import kr.co.pulmuone.v1.outmall.order.dto.CollectionMallInterfaceProgressRequestDto;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallExcelFailRequestDto;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallExcelListRequestDto;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderDto;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderSellersDto;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallShippingInfoDownloadHistRequestDto;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallShippingInfoDownloadRequestDto;
import kr.co.pulmuone.v1.outmall.order.dto.vo.ClaimOrderListVo;
import kr.co.pulmuone.v1.outmall.order.dto.vo.CollectionMallInterfaceFailVo;
import kr.co.pulmuone.v1.outmall.order.dto.vo.CollectionMallInterfaceListVo;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallExcelFailVo;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallExcelInfoVo;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallExcelSuccessVo;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallShippingExceldownHistVo;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallShippingInfoVo;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallTrackingNumberHistVo;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallTrackingNumberHistRequestDto;

@Mapper
public interface OutmallOrderMapper {

    Page<CollectionMallInterfaceListVo> getCollectionMallInterfaceList(CollectionMallInterfaceListRequestDto dto);

    Page<CollectionMallInterfaceListVo> getCollectionMallInterfaceListForTrans(CollectionMallInterfaceListRequestDto dto);

    void putCollectionMallInterfaceProgress(@Param("processCode") String processCode, @Param("adminId") Long adminId, @Param("ifEasyadminInfoId") Long ifEasyadminInfoId);

    void putCollectionMallInterfaceProgressList(CollectionMallInterfaceProgressRequestDto dto);

    List<CollectionMallInterfaceFailVo> getCollectionMallFailExcelDownload(CollectionMallInterfaceFailRequestDto dto);

    List<CollectionMallInterfaceFailVo> getCollectionMallFailExcelDownloadForTrans(CollectionMallInterfaceFailRequestDto dto);

    List<GetCodeListResultVo> getSellersList(@Param("sellersGroupCode") String sellersGroupCode);

    Page<ClaimOrderListVo> getClaimOrderList(ClaimOrderListRequestDto dto);

    void putClaimOrderProgress(ClaimOrderProgressRequestDto dto);

    void putClaimOrderProgressList(ClaimOrderProgressRequestDto dto);

    void addOutMallExcelInfo(OutMallExcelInfoVo vo);

    void putOutMallExcelInfo(OutMallExcelInfoVo vo);

    void addOutMallExcelSuccess(@Param("ifOutmallExcelInfoId") Long ifOutmallExcelInfoId, @Param("voList") List<OutMallExcelSuccessVo> voList);

    void deleteOutMallExcelSuccess(@Param("ifOutmallExcelSuccId") Long ifOutmallExcelSuccId);

    void addOutMallExcelFail(@Param("ifOutmallExcelInfoId") Long ifOutmallExcelInfoId, @Param("voList") List<OutMallExcelFailVo> voList);

    void addOutMallExcelFailSelectInsert(@Param("ifOutmallExcelInfoId") Long ifOutmallExcelInfoId, @Param("ifOutmallExcelSuccId") Long ifOutmallExcelSuccId, @Param("failMessage") String failMessage);

    List<String> getOutMallExcelDetailId(@Param("outmallType") String outmallType, @Param("detailIdList") List<String> detailIdList);

    OutMallExcelInfoVo getOutMallExcelInfo(OutMallExcelFailRequestDto outMallExcelFailRequestDto);

    Page<OutMallExcelInfoVo> getOutMallExcelInfoList(OutMallExcelListRequestDto dto);

    List<OutMallExcelFailVo> getOutMallFailExcelDownload(OutMallExcelFailRequestDto dto);

    List<String> getCollectionMallIdList(@Param("outmallType") String outmallType, @Param("collectionMallIdList")  List<String> collectionMallIdList);

    List<String> getCollectionMallDetailIdList(@Param("outmallType") String outmallType, @Param("collectionMallDetailIdList")  List<String> collectionMallDetailIdList);

    List<String> getOutmallIdList(@Param("outmallType") String outmallType, @Param("outmallIdList")  List<String> outmallIdList);

    int addEasyAdminExcelRatioPrice(long ifOutmallExcelInfoId);

    int putEasyAdminExcelRatioPrice(long ifOutmallExcelInfoId);

    int addSabangnetExcelRatioPrice(long ifOutmallExcelInfoId);

    int putSabangnetExcelRatioPrice(long ifOutmallExcelInfoId);

    int putOutMallExcelSuccRatioPrice(long ifOutmallExcelInfoId);

    int putOutMallExcelSuccGiftPrice(long ifOutmallExcelInfoId);

    long getLastIfOutmallExcelInfo();

    List<OutMallOrderDto> getOutmallOrderCreateTargetList(long ifOutmallExcelInfoId);

    List<OutMallOrderSellersDto> getOmSellersInfoList(@Param(value = "excelList") List<String> excelList) throws Exception;

    List<OutMallShippingInfoVo> getOutmallShippingInfoDownload(OutMallShippingInfoDownloadRequestDto reqDto) throws Exception;

    int addOutmallShippingExcelDownHist(OutMallShippingInfoDownloadRequestDto reqDto) throws Exception;

    List<OutMallShippingExceldownHistVo> getOutMallShippingExceldownHist(OutMallShippingInfoDownloadHistRequestDto reqDto) throws Exception;

    int getOutMallShippingExceldownHistCount(OutMallShippingInfoDownloadHistRequestDto reqDto) throws Exception;

    int putGoodsOutmallStatus(@Param(value = "updateTargetList") List<Long> updateTargetList) throws Exception;

    List<OutMallTrackingNumberHistVo> getOutMallTrackingNumberHist(OutMallTrackingNumberHistRequestDto reqDto) throws Exception;

    int getOutMallTrackingNumberHistCount(OutMallTrackingNumberHistRequestDto reqDto) throws Exception;

    int putOutmallExcelSuccOrderCreateYn(@Param("collectionMallId")String collectionMallId, @Param("ifOutmallExcelInfoId")Long ifOutmallExcelInfoId) throws Exception;

    List<HashMap> getNotOrderCreateInIfOutmallExcelSucc(long ifOutmallExcelInfoId) throws Exception;

    int getOrderCreateCount(long ifOutmallExcelInfoId) throws Exception;
}
