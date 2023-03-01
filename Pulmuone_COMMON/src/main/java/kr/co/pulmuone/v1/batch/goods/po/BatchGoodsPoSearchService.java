package kr.co.pulmuone.v1.batch.goods.po;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.batch.goods.po.dto.vo.GoodsPoSearchResultVo;
import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfPoSearchResponseDto;
import kr.co.pulmuone.v1.comm.enums.ItemEnums;
import kr.co.pulmuone.v1.comm.enums.PoEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mappers.batch.master.goods.po.BatchGoodsPoSearchMapper;
import lombok.RequiredArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 * ERP 올가R2발주스케쥴 조회 배치 Service
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021.02.16     이성준            최초작성
 * =======================================================================
 * 로직 설명 :
 * 1. ERP 연동 데이터중 ordStpYn의 값에 의해 발주가능 여부 판단
 * 2. ERP 연동 데이터를 IL_PO_TP_IF_TEMP 임시 테이블에 저장
 * 3. IL_PO_TP 테이블과 LEFT JOIN을 하여 연동에 의해 업데이트 된 값이 있으면 연동데이터를 그렇지 않으면 기존데이터를 셋팅하여 INSERT/MERGE를 함.
 * 3-1. ERP연동 RAW DATA의 경우 업데이트 시에는 해당 요일의 정보만 보내주기 때문에 기존 데이터와 LEFT JOIN을 할 필요가 있음.
 * </PRE>
 */
@Service
@RequiredArgsConstructor
public class BatchGoodsPoSearchService {

	@Autowired
    private BatchGoodsPoSearchMapper batchPoSearchMapper;

	protected void runGoodsPoSearchJob(List<ErpIfPoSearchResponseDto> getErpPoSearchList, long batchId) throws BaseException {
		// 공급처 조회
		Integer urSupplierId = null;

		// 발주유형VO
		GoodsPoSearchResultVo poTypeInfo = new GoodsPoSearchResultVo();

		Boolean mergeFlag = false;
		int size = getErpPoSearchList.size();

		for(int i=0; i < size; i++) {

			int orgCnt = 0;
			int targetCnt = 0;
			int calCnt = 0;
			int resultCnt = 0;
			int poTargetCnt = 0;
			int poCalCnt = 0;
			int poResultCnt = 0;

			//품목코드
			String ilItemCd = getErpPoSearchList.get(i).getIlItemCd();

			//공급처 조회
			urSupplierId = getUrSupplierId(ilItemCd);

			// 공급처가 존재하면 저장
			if (urSupplierId != null && urSupplierId != 0) {

				//발주일 N로 초기세팅
			    poTypeInfo.setPoSunYn("");
			    poTypeInfo.setPoMonYn("");
			    poTypeInfo.setPoTueYn("");
				poTypeInfo.setPoWedYn("");
				poTypeInfo.setPoThuYn("");
				poTypeInfo.setPoFriYn("");
				poTypeInfo.setPoSatYn("");

				//입고예정일 및 이동요청일 0으로 세팅
				poTypeInfo.setMoveReqSun(0);
				poTypeInfo.setMoveReqMon(0);
				poTypeInfo.setMoveReqTue(0);
				poTypeInfo.setMoveReqWed(0);
				poTypeInfo.setMoveReqThu(0);
				poTypeInfo.setMoveReqFri(0);
				poTypeInfo.setMoveReqSat(0);
				poTypeInfo.setScheduledSun(0);
				poTypeInfo.setScheduledMon(0);
				poTypeInfo.setScheduledTue(0);
				poTypeInfo.setScheduledWed(0);
				poTypeInfo.setScheduledThu(0);
				poTypeInfo.setScheduledFri(0);
				poTypeInfo.setScheduledSat(0);
				poTypeInfo.setPoReqSun(0);
				poTypeInfo.setPoReqMon(0);
				poTypeInfo.setPoReqTue(0);
				poTypeInfo.setPoReqWed(0);
				poTypeInfo.setPoReqThu(0);
				poTypeInfo.setPoReqFri(0);
				poTypeInfo.setPoReqSat(0);

				// 발주일 날짜계산
				if (getErpPoSearchList.get(i).getReqDay().equals(PoEnums.PoDay.PO_DAY_MONDAY.getCodeName())) {
					if ("Y".equals(getErpPoSearchList.get(i).getOrderStopYn())) {
						poTypeInfo.setPoMonYn("N");
					} else {
						orgCnt = 1;
						poTypeInfo.setPoMonYn("Y");
					}
				} else if (getErpPoSearchList.get(i).getReqDay().equals(PoEnums.PoDay.PO_DAY_TUESDAY.getCodeName())) {
					if ("Y".equals(getErpPoSearchList.get(i).getOrderStopYn())) {
						poTypeInfo.setPoTueYn("N");
					} else {
						orgCnt = 2;
						poTypeInfo.setPoTueYn("Y");
					}
				} else if (getErpPoSearchList.get(i).getReqDay().equals(PoEnums.PoDay.PO_DAY_WEDNESDAY.getCodeName())) {
					if ("Y".equals(getErpPoSearchList.get(i).getOrderStopYn())) {
						poTypeInfo.setPoWedYn("N");
					} else {
						orgCnt = 3;
						poTypeInfo.setPoWedYn("Y");
					}
				} else if (getErpPoSearchList.get(i).getReqDay().equals(PoEnums.PoDay.PO_DAY_THURSDAY.getCodeName())) {
					if ("Y".equals(getErpPoSearchList.get(i).getOrderStopYn())) {
						poTypeInfo.setPoThuYn("N");
					} else {
						orgCnt = 4;
						poTypeInfo.setPoThuYn("Y");
					}
				} else if (getErpPoSearchList.get(i).getReqDay().equals(PoEnums.PoDay.PO_DAY_FRIDAY.getCodeName())) {
					if ("Y".equals(getErpPoSearchList.get(i).getOrderStopYn())) {
						poTypeInfo.setPoFriYn("N");
					} else {
						orgCnt = 5;
						poTypeInfo.setPoFriYn("Y");
					}
				} else if (getErpPoSearchList.get(i).getReqDay().equals(PoEnums.PoDay.PO_DAY_SATURDAY.getCodeName())) {
					if ("Y".equals(getErpPoSearchList.get(i).getOrderStopYn())) {
						poTypeInfo.setPoSatYn("N");
					} else {
						orgCnt = 6;
						poTypeInfo.setPoSatYn("Y");
					}
				} else if (getErpPoSearchList.get(i).getReqDay().equals(PoEnums.PoDay.PO_DAY_SUNDAY.getCodeName())) {
					if ("Y".equals(getErpPoSearchList.get(i).getOrderStopYn())) {
						poTypeInfo.setPoSunYn("N");
					} else {
						orgCnt = 7;
						poTypeInfo.setPoSunYn("Y");
					}
				}

				// 입고예정일 및 이동요청일 계산
				if (getErpPoSearchList.get(i).getInputDay().equals(PoEnums.PoDay.PO_DAY_MONDAY.getCodeName())) {
					targetCnt = 1;
				} else if (getErpPoSearchList.get(i).getInputDay().equals(PoEnums.PoDay.PO_DAY_TUESDAY.getCodeName())) {
					targetCnt = 2;
				} else if (getErpPoSearchList.get(i).getInputDay().equals(PoEnums.PoDay.PO_DAY_WEDNESDAY.getCodeName())) {
					targetCnt = 3;
				} else if (getErpPoSearchList.get(i).getInputDay().equals(PoEnums.PoDay.PO_DAY_THURSDAY.getCodeName())) {
					targetCnt = 4;
				} else if (getErpPoSearchList.get(i).getInputDay().equals(PoEnums.PoDay.PO_DAY_FRIDAY.getCodeName())) {
					targetCnt = 5;
				} else if (getErpPoSearchList.get(i).getInputDay().equals(PoEnums.PoDay.PO_DAY_SATURDAY.getCodeName())) {
					targetCnt = 6;
				} else if (getErpPoSearchList.get(i).getInputDay().equals(PoEnums.PoDay.PO_DAY_SUNDAY.getCodeName())){
					targetCnt = 7;
				}

				// PO요청일 계산
				if (getErpPoSearchList.get(i).getOutDat().equals(ItemEnums.PoDay.PO_DAY_MONDAY.getCodeName())) {
					poTargetCnt = 1;
				} else if (getErpPoSearchList.get(i).getOutDat().equals(ItemEnums.PoDay.PO_DAY_TUESDAY.getCodeName())) {
					poTargetCnt = 2;
				} else if (getErpPoSearchList.get(i).getOutDat().equals(ItemEnums.PoDay.PO_DAY_WEDNESDAY.getCodeName())) {
					poTargetCnt = 3;
				} else if (getErpPoSearchList.get(i).getOutDat().equals(ItemEnums.PoDay.PO_DAY_THURSDAY.getCodeName())) {
					poTargetCnt = 4;
				} else if (getErpPoSearchList.get(i).getOutDat().equals(ItemEnums.PoDay.PO_DAY_FRIDAY.getCodeName())) {
					poTargetCnt = 5;
				} else if (getErpPoSearchList.get(i).getOutDat().equals(ItemEnums.PoDay.PO_DAY_SATURDAY.getCodeName())) {
					poTargetCnt = 6;
				} else if (getErpPoSearchList.get(i).getOutDat().equals(ItemEnums.PoDay.PO_DAY_SUNDAY.getCodeName())){
					poTargetCnt = 7;
				}

				// 날짜차이 계산 로직수행
				if (orgCnt > 0) {
					calCnt = orgCnt - targetCnt;
					if (calCnt == 0) {
						resultCnt = 7;
					} else if (calCnt < 0) {
						resultCnt = calCnt * -1;
					} else {
						resultCnt = 7 - calCnt;
					}

					// PO요청일 계산
					poCalCnt = orgCnt - poTargetCnt;
					if (poCalCnt == 0) {
						poResultCnt = 7;
					} else if (poCalCnt < 0) {
						poResultCnt = poCalCnt * -1;
					} else {
						poResultCnt = 7 - poCalCnt;
					}
				}

				// 입고예정일 및 이동요청일 계산값 SET
				if (getErpPoSearchList.get(i).getReqDay().equals(PoEnums.PoDay.PO_DAY_MONDAY.getCodeName())) {
					poTypeInfo.setScheduledMon(resultCnt);
					poTypeInfo.setMoveReqMon(resultCnt);
					poTypeInfo.setPoReqMon(poResultCnt);
				} else if (getErpPoSearchList.get(i).getReqDay().equals(PoEnums.PoDay.PO_DAY_TUESDAY.getCodeName())) {
					poTypeInfo.setScheduledTue(resultCnt);
					poTypeInfo.setMoveReqTue(resultCnt);
					poTypeInfo.setPoReqTue(poResultCnt);
				} else if (getErpPoSearchList.get(i).getReqDay().equals(PoEnums.PoDay.PO_DAY_WEDNESDAY.getCodeName())) {
					poTypeInfo.setScheduledWed(resultCnt);
					poTypeInfo.setMoveReqWed(resultCnt);
					poTypeInfo.setPoReqWed(poResultCnt);
				} else if (getErpPoSearchList.get(i).getReqDay().equals(PoEnums.PoDay.PO_DAY_THURSDAY.getCodeName())) {
					poTypeInfo.setScheduledThu(resultCnt);
					poTypeInfo.setMoveReqThu(resultCnt);
					poTypeInfo.setPoReqThu(poResultCnt);
				} else if (getErpPoSearchList.get(i).getReqDay().equals(PoEnums.PoDay.PO_DAY_FRIDAY.getCodeName())) {
					poTypeInfo.setScheduledFri(resultCnt);
					poTypeInfo.setMoveReqFri(resultCnt);
					poTypeInfo.setPoReqFri(poResultCnt);
				} else if (getErpPoSearchList.get(i).getReqDay().equals(PoEnums.PoDay.PO_DAY_SATURDAY.getCodeName())) {
					poTypeInfo.setScheduledSat(resultCnt);
					poTypeInfo.setMoveReqSat(resultCnt);
					poTypeInfo.setPoReqSat(poResultCnt);
				} else if (getErpPoSearchList.get(i).getReqDay().equals(PoEnums.PoDay.PO_DAY_SUNDAY.getCodeName())){
					poTypeInfo.setScheduledSun(resultCnt);
					poTypeInfo.setMoveReqSun(resultCnt);
					poTypeInfo.setPoReqSun(poResultCnt);
				}

				String poTpNm = "품목별상이("+getErpPoSearchList.get(i).getIlItemCd()+")";
				poTypeInfo.setPoTpNm(poTpNm);
				poTypeInfo.setIlItemCd(getErpPoSearchList.get(i).getIlItemCd());

				addPoSearch(poTypeInfo);

				mergeFlag = true;
			}
		}

		//merge실행 및 temp삭제
		if(mergeFlag == true) {
		   addPoSearchMerge(poTypeInfo);
		   delPoSearch();
		   putItemPoTpByBatch();
		}

    }

	/**
     * @Desc 공급처 조회
     * @param string
     * @return Integer
     */
	protected Integer getUrSupplierId(String ilItemCd) {
		return batchPoSearchMapper.getUrSupplierId(ilItemCd);
	}

	/**
     * @Desc 발주유형 저장
     * @param GoodsPoSearchResultVo
     * @return int
     */
	protected int addPoSearch(GoodsPoSearchResultVo poTypeInfo) {
		return batchPoSearchMapper.addPoSearch(poTypeInfo);
	}

	/**
     * @Desc 발주유형 Merge
     * @param GoodsPoSearchResultVo
     * @return int
     */
	protected int addPoSearchMerge(GoodsPoSearchResultVo poTypeInfo) {
		return batchPoSearchMapper.addPoSearchMerge(poTypeInfo);
	}

	/**
     * @Desc 발주유형 temp삭제
     * @return int
     */
	protected int delPoSearch() {
		return batchPoSearchMapper.delPoSearch();
	}

	/**
     * @Desc 품목에 배치에 의한 품목별 발주유형 일괄 업데이트
     * @return int
     */
	protected int putItemPoTpByBatch() {
		return batchPoSearchMapper.putItemPoTpByBatch();
	}

}
