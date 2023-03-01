package kr.co.pulmuone.v1.batch.collectionmall.ezadmin;

import kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto.EZAdminOrderInfoRequestDto;
import kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto.EZAdminOrderInfoResponseDto;
import kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto.EZAdminQnaInfoRequestDto;
import kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto.EZAdminRunOrderInfoRequestDto;
import kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto.vo.EZAdminOrderInfoOrderVo;
import kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto.vo.EZAdminTransNoTargetVo;

import java.util.List;

public interface CollectionMallEZAdminBatchBiz {

    void runOrderInfo(EZAdminRunOrderInfoRequestDto dto) throws Exception;

    EZAdminOrderInfoResponseDto callGetOrderInfo(EZAdminOrderInfoRequestDto reqDto, String batchTp, int paramThreadMaxCount) throws Exception;

    void orderExecute(List<EZAdminOrderInfoOrderVo> orderInfoList) throws Exception;

    void claimExecute(List<EZAdminOrderInfoOrderVo> claimInfoList) throws Exception;

    void runTransNo() throws Exception;

    void addOrderInfo(int page,long ifEasyadminInfoId, EZAdminOrderInfoRequestDto reqDto,String batchTp) throws Exception;

    EZAdminRunOrderInfoRequestDto calcBatchDateTime(String action, String batchTp);

    EZAdminRunOrderInfoRequestDto calcQnaBatchDateTime(String ezadminBatchTypeCd);

    int runOutmallQnaInfo(EZAdminQnaInfoRequestDto reqDto) throws Exception;

    void checkBosCollectionMallInterfaceFail() throws Exception;
}
