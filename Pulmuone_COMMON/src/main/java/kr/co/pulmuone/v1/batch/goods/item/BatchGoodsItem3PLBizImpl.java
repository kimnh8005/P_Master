package kr.co.pulmuone.v1.batch.goods.item;

import java.util.ArrayList;
import java.util.List;


import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.batch.goods.item.dto.ErpGoods3PLUpdateHeaderCondRequestDto;
import kr.co.pulmuone.v1.batch.goods.item.dto.ErpGoods3PLUpdateHeaderRequestDto;
import kr.co.pulmuone.v1.batch.goods.item.dto.ErpGoods3PLHeaderRequestDto;
import kr.co.pulmuone.v1.batch.goods.item.vo.ErpBaekamGoodsItemResultVo;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.api.service.ErpApiExchangeService;
import kr.co.pulmuone.v1.comm.constants.BatchConstants;
import kr.co.pulmuone.v1.comm.exception.BaseException;

/**
 * <PRE>
 * Forbiz Korea
 *
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0      20210205          정형진         최초작성
 * =======================================================================
 * </PRE>
 */

@Service
public class BatchGoodsItem3PLBizImpl implements BatchGoodsItem3PLBiz{

	private static final Logger log = LoggerFactory.getLogger(BatchGoodsItem3PLBizImpl.class);


    // 3PL INSERT INTERFACE
    private static final String ITEM_GOODS_3PL_INSERT_INTERFACE_ID = "IF_GOODS_3PL_INP";

    // 3PL UPDATE INTERFACE
    private static final String ITEM_GOODS_3PL_UPDATE_INTERFACE_ID = "IF_GOODS_3PL_UPD";

    @Autowired
    private BatchGoodsItem3PLService batchGoodsItem3PLService;

    @Autowired
    ErpApiExchangeService erpApiExchangeService;

	@Override
	public void addPutItem3PLJob() throws BaseException {
		// TODO Auto-generated method stub

		// 당일 기준 신규 데이터 입력 insert
		List<ErpBaekamGoodsItemResultVo> resultList = batchGoodsItem3PLService.getInsertTargetItemList();

	    BaseApiResponseVo baseApiResponseVo;

		if(resultList.size() > 0) {
			ErpGoods3PLHeaderRequestDto requestDto = ErpGoods3PLHeaderRequestDto.builder()
					.totalPage(1)
					.currentPage(1)
					.header(resultList)
					.build();

			baseApiResponseVo = erpApiExchangeService.post(requestDto, ITEM_GOODS_3PL_INSERT_INTERFACE_ID);

	        log.info("addGoodsPurchaseOrderJob baseApiResponseVo:" + ToStringBuilder.reflectionToString(baseApiResponseVo));

	        //ERP IF실패시 재시도 총 3회
	        if (!baseApiResponseVo.isSuccess()) {
	        	retryApiCall(requestDto);
	        }
		}

		resultList.clear();

		// 당일 이전 데이터로 당일 수정한 품목 업데이트
		resultList = batchGoodsItem3PLService.getUpdateTargetItemList();

		if(resultList.size() > 0) {
			List<ErpGoods3PLUpdateHeaderCondRequestDto> headList = new ArrayList();

			// 업데이트 대상 resultVo 에 매핑
			for(ErpBaekamGoodsItemResultVo resultVo : resultList) {

				ErpGoods3PLUpdateHeaderCondRequestDto condRequestDto = ErpGoods3PLUpdateHeaderCondRequestDto.builder()
						.itmNo(resultVo.getItmNo())
						.itmNam(resultVo.getItmNam())
						.kanCd(resultVo.getKanCd())
						.boxSiz(resultVo.getBoxSiz())
						.eaSiz(resultVo.getEaSiz())
						.boxEa(resultVo.getBoxEa())
						.boxKanCd(resultVo.getBoxKanCd())
						.strKey(resultVo.getStrKey())
						.boxCrr(resultVo.getBoxCrr())
						.extBoxCd(resultVo.getExtBoxCd())
						.regDat(resultVo.getRegDat())
						.durDay(resultVo.getDurDay())
						.limOut(resultVo.getLimOut())
						.lifDay(resultVo.getLifDay())
						.tmpVal(resultVo.getTmpVal())
						.updFlg(resultVo.getUpdFlg())
						.updDat(resultVo.getUpdDat())
						.itfDat(resultVo.getItfDat())
						.itfFlg(resultVo.getItfFlg())
						.build();

				headList.add(condRequestDto);

				ErpGoods3PLUpdateHeaderRequestDto headRequestDto = ErpGoods3PLUpdateHeaderRequestDto.builder()
						.totalPage(1)
						.currentPage(1)
						.build();

				headRequestDto.setHeader(headList);

				baseApiResponseVo = erpApiExchangeService.put(headRequestDto, ITEM_GOODS_3PL_UPDATE_INTERFACE_ID);

				//ERP IF실패시 재시도 총 3회
				if (!baseApiResponseVo.isSuccess()) {
					retryPutApiCall(headRequestDto);
				}
				log.info("BatchGoodsItem3PLBizImpl baseApiResponseVo:" + ToStringBuilder.reflectionToString(baseApiResponseVo));
			}
		}
	}

	// 마스터품목 정보 추가시
	public void retryApiCall(Object requestDto) {
		BaseApiResponseVo retryBaseApiResponseVo;

		for (int failCnt = 0; failCnt < BatchConstants.BATCH_FAIL_RETRY_COUNT; failCnt++ ) {
            retryBaseApiResponseVo = erpApiExchangeService.post(requestDto, ITEM_GOODS_3PL_INSERT_INTERFACE_ID);

            if (retryBaseApiResponseVo.isSuccess()) {
                log.info("retryBaseApiResponseVo:" + ToStringBuilder.reflectionToString(retryBaseApiResponseVo));
                break;
            } else {
                //3번 재시도 모두 실패시 알람
            }
        }
	}

	// 마스터품목 정보 변경시
	public void retryPutApiCall(Object requestDto) {
		BaseApiResponseVo retryBaseApiResponseVo;

		for (int failCnt = 0; failCnt < BatchConstants.BATCH_FAIL_RETRY_COUNT; failCnt++ ) {
			retryBaseApiResponseVo = erpApiExchangeService.put(requestDto, ITEM_GOODS_3PL_UPDATE_INTERFACE_ID);

			if (retryBaseApiResponseVo.isSuccess()) {
				log.info("retryBaseApiResponseVo:" + ToStringBuilder.reflectionToString(retryBaseApiResponseVo));
				break;
			} else {
				//3번 재시도 모두 실패시 알람
			}
		}
	}


}
