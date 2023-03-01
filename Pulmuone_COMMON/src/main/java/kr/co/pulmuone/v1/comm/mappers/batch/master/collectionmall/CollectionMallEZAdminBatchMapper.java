package kr.co.pulmuone.v1.comm.mappers.batch.master.collectionmall;

import kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto.EZAdminInfoDto;
import kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto.EZAdminResponseDefaultDto;
import kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto.EZAdminSellersInfoDto;
import kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CollectionMallEZAdminBatchMapper {

	int addEasyAdminInfo(EZAdminResponseDefaultDto ezadminResponseDefaultDto);

	int addEasyAdminInfoReqData(EZAdminResponseDefaultDto ezadminResponseDefaultDto);

	int addEasyAdminOrderSuccess(EZAdminOrderInfoOrderVo ezadminOrderVo);

	int addEasyAdminOrderSuccessDetail(EZAdminOrderInfoOrderProductVo ezadminProductVo);

	int addEasyAdminOrderFail(EZAdminOrderInfoOrderVo ezadminOrderVo);

	int addEasyAdminOrderFailDetail(EZAdminOrderInfoOrderProductVo ezadminProductVo);

	int addEasyAdminOrderClaim(EZAdminOrderInfoOrderVo ezadminOrderVo);

	int addEasyAdminOrderClaimDetail(EZAdminOrderInfoOrderProductVo ezadminProductVo);

	List<EZAdminSellersInfoDto> getOmSellesInfoList(@Param(value = "orderInfoList") List<EZAdminOrderInfoOrderVo> orderInfoList);

	int putEasyAdminInfo(EZAdminInfoDto ezadminInfoDto);

	List<Long> getDistinctGoodsIdList(List<String> goodsIdList);

    int addEasyAdminOrderRatioPrice(@Param(value = "ifEasyadminInfoId") long ifEasyadminInfoId, @Param(value="ifEasyadminInfoReqDataId")long ifEasyadminInfoReqDataId);

	int putEasyAdminOrderRatioPrice(@Param(value = "ifEasyadminInfoId") long ifEasyadminInfoId, @Param(value="succIdList")List<Long> succIdList);

	int putEasyAdminOrderSuccRatioPrice(@Param(value = "ifEasyadminInfoId") long ifEasyadminInfoId, @Param(value="succIdList")List<Long> succIdList);

	int getOrderCnt(@Param(value = "collectionMallId") String collectionMallId);

	int getOrderDetailCnt(@Param(value = "collectionMallDetailId") String collectionMallDetailId);

	List<EZAdminTransNoTargetVo> getTransNoTargetList(@Param("startDIDate")String startDIDate, @Param("endDIDate")String endDIDate);

    int addEasyAdminQnaInfo(EZAdminResponseDefaultDto ezadminResponseDefaultDto);

	int addCsOutmallQnaEasyAdminQnaDetail(EZAdminQnaInfoVo qnaInfo);

	int addCsOutmallQnaEasyAdminQnaAnswerDetail(EZAdminQnaInfoVo qnaInfo);

	int putSuccessInsertOrderCount(long ifEasyadminInfoId);

	int putIfEasyadminInfoReqDateCollectCd(long ifEasyadminInfoId);

	String getEasyAdminInfo(@Param("action")String action, @Param("batchTp")String batchTp);

	String getEasyAdminQnaInfo(String ezadminBatchTypeCd);

	int putOdTrackingNumberEzadminApiYn(EZAdminTransNoTargetVo eZAdminTransNoTargetVo);

	int duplicateCsOutmallQnaSeq(int seq);

	int addIfEasyadminApiInfo(EZAdminApiInfoVo ezAdminApiInfoVo);

	int isBosCollectionMallInterfaceFail(@Param(value = "orderCs") String orderCs);

}
