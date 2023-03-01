package kr.co.pulmuone.v1.goods.stock.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.StockEnums;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.stock.dto.StockDeadlineHistParamDto;
import kr.co.pulmuone.v1.goods.stock.dto.StockDeadlineHistResponseDto;
import kr.co.pulmuone.v1.goods.stock.dto.StockDeadlineRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.StockDeadlineResponseDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockDeadlineHistResultVo;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockDeadlineResultVo;


/**
 * <PRE>
 * Forbiz Korea
 * 재고 기한관리 BizImpl
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200813    	강윤경           최초작성
 * =======================================================================
 * </PRE>
 */

@Service
public class GoodsStockDeadlineBizImpl implements GoodsStockDeadlineBiz {

    @Autowired
    GoodsStockDeadlineService stockDeadlineService;

	/**
	 * 재고기한관리 목록 조회
	 * @param	StockDeadlineRequestDto
	 * @return	StockDeadlineResponseDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getStockDeadlineList(StockDeadlineRequestDto stockDeadlineRequestDto) throws Exception {
		StockDeadlineResponseDto stockDeadlineResponseDto = new StockDeadlineResponseDto();

		Page<StockDeadlineResultVo> stockDeadlineList = stockDeadlineService.getStockDeadlineList(stockDeadlineRequestDto);

		stockDeadlineResponseDto.setTotal(stockDeadlineList.getTotal());
		stockDeadlineResponseDto.setRows(stockDeadlineList.getResult());

        return ApiResult.success(stockDeadlineResponseDto);

	}



	/**
	 * 재고기한관리 등록
	 * @param	StockDeadlineRequestDto
	 * @return	StockDeadlineResponseDto
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ApiResult<?> addStockDeadline(StockDeadlineRequestDto stockDeadlineRequestDto) throws Exception {

/*
		//조회 --> 중복데이터 count 비교하기
		int dataCount = stockDeadlineService.getStockDeadlineForCheck(stockDeadlineRequestDto);
		if(dataCount > 0) {
			return ApiResult.result(StockEnums.StockDeadlineAddDataType.DUPLICATE_DATA);
		}

		//유통기간이 기간
		int distributionPeriodStart = StringUtil.nvlInt(stockDeadlineRequestDto.getDistributionPeriodStart());
		int distributionPeriodEnd = StringUtil.nvlInt(stockDeadlineRequestDto.getDistributionPeriodEnd());
		long imminent = stockDeadlineRequestDto.getImminent();	//임박기준
		long delivery = stockDeadlineRequestDto.getDelivery();	//출고기준

		//유통기간이 기간일 경우
		if(distributionPeriodStart > 0 && distributionPeriodEnd > 0) {
			for (int i = distributionPeriodStart; i <= distributionPeriodEnd; i++) {
				stockDeadlineRequestDto.setDistributionPeriod(StringUtil.nvl(i));	//유통기간

				//기한관리 등록
				addStockDeadlineComn(stockDeadlineRequestDto, imminent, delivery);
			}
		} else {
			//기한관리 등록
			addStockDeadlineComn(stockDeadlineRequestDto, imminent, delivery);
		}
*/
		long imminent = stockDeadlineRequestDto.getImminent();	//임박기준
		long delivery = stockDeadlineRequestDto.getDelivery();	//출고기준
		//기한관리 등록
		addStockDeadlineComn(stockDeadlineRequestDto, imminent, delivery);

		return ApiResult.success();
	 }



	/**
	 * 재고기한관리 등록 처리 공통
	 * @param	StockDeadlineRequestDto
	 * @return	StockDeadlineResponseDto
	 * @throws Exception
	 */
	private void addStockDeadlineComn(StockDeadlineRequestDto stockDeadlineRequestDto, long imminent, long delivery) throws Exception {
		//% 계산
		if(GoodsEnums.StockDeadlineType.PERCENT.getCode().equals(stockDeadlineRequestDto.getStdType())) {
			long addImminent = StringUtil.nvlInt(stockDeadlineRequestDto.getDistributionPeriod()) * imminent/100;
			long addDelivery = StringUtil.nvlInt(stockDeadlineRequestDto.getDistributionPeriod()) * delivery/100;
			stockDeadlineRequestDto.setImminent(addImminent);
			stockDeadlineRequestDto.setDelivery(addDelivery);
		}

		// 등록된 카운트 수 조회
		HashMap countMap = stockDeadlineService.getStockDeadlineCheckCountByPeriod(stockDeadlineRequestDto);
		int totalCount = Integer.parseInt(countMap.get("totalCount").toString());
		int totalYesCount = Integer.parseInt(countMap.get("totalYesCount").toString());

		//기본설정이 N인 경우, 기존에 기본설정이 Y가 없으면 Y로 변경하여 등록한다.
		if (stockDeadlineRequestDto.getPopBasicYn().equals("N") && totalYesCount == 0) {
			stockDeadlineRequestDto.setPopBasicYn("Y");
		}

		//기본설정이 Y인경우, 기존에 기본설정이 Y가 있으면 이전 데이터를 N로 설정한다.
		if(stockDeadlineRequestDto.getPopBasicYn().equals("Y") && totalYesCount > 0) {
		   stockDeadlineService.putStockDeadlineBasicYn(stockDeadlineRequestDto);
		}

		//데이터 set
		stockDeadlineRequestDto.setUrWarehouseId(StringUtil.nvl(stockDeadlineRequestDto.getUrWarehouseId(), "0"));
		stockDeadlineRequestDto.setCreateId((Integer.valueOf((SessionUtil.getBosUserVO()).getUserId())));

		//기한관리 등록
		stockDeadlineService.addStockDeadline(stockDeadlineRequestDto);

		//이력저장
		StockDeadlineHistParamDto stockDeadlineHistParamDto = StockDeadlineHistParamDto.builder()
				 .histTp(StockEnums.HistTpCode.INSERT.getCode())
				 .ilStockDeadlineId(stockDeadlineRequestDto.getIlStockDeadlineId())
				 .urSupplierId(stockDeadlineRequestDto.getUrSupplierId())
				 .urWarehouseId(stockDeadlineRequestDto.getUrWarehouseId())
				 .distributionPeriod(stockDeadlineRequestDto.getDistributionPeriod())
				 .imminent(stockDeadlineRequestDto.getImminent())
				 .delivery(stockDeadlineRequestDto.getDelivery())
				 .targetStockRatio(stockDeadlineRequestDto.getTargetStockRatio())
				 .basicYn(stockDeadlineRequestDto.getPopBasicYn())
				 .origCreateId(stockDeadlineRequestDto.getOrigCreateId())
				 .origCreateDate(stockDeadlineRequestDto.getOrigCreateDate())
				 .origModifyId(stockDeadlineRequestDto.getOrigModifyId())
				 .origModifyDate(stockDeadlineRequestDto.getOrigModifyDate())
				 .createId((Integer.valueOf((SessionUtil.getBosUserVO()).getUserId())))
	    		 .build();
		stockDeadlineService.addStockDeadlineHist(stockDeadlineHistParamDto);

	 }


	/**
	 * 재고기한관리 상세 조회
	 * @param	StockDeadlineRequestDto
	 * @return	StockDeadlineResponseDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getStockDeadline(StockDeadlineRequestDto stockDeadlineRequestDto) throws Exception {
		StockDeadlineResponseDto stockDeadlineResponseDto = new StockDeadlineResponseDto();

		StockDeadlineResultVo stockDeadlineResultVo = stockDeadlineService.getStockDeadline(stockDeadlineRequestDto);	// rows
		stockDeadlineResponseDto.setStockDeadlineResultVo(stockDeadlineResultVo);

		return ApiResult.success(stockDeadlineResponseDto);
	}



	/**
	 * 재고기한관리 수정
	 * @param	StockDeadlineRequestDto
	 * @return	StockDeadlineResponseDto
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ApiResult<?> putStockDeadline(StockDeadlineRequestDto stockDeadlineRequestDto) throws Exception {

/*
		//입력 count 체크
		int chkUpdateCount = 1;
		int distributionPeriodStart = StringUtil.nvlInt(stockDeadlineRequestDto.getDistributionPeriodStart());
		int distributionPeriodEnd = StringUtil.nvlInt(stockDeadlineRequestDto.getDistributionPeriodEnd());
		//유통기간이 기간일 경우
		if(distributionPeriodStart > 0 && distributionPeriodEnd > 0) {
			chkUpdateCount = distributionPeriodEnd - distributionPeriodStart + 1;
		}

		//조회 --> 중복데이터 count 비교하기
		int dataCount = stockDeadlineService.getStockDeadlineForCheck(stockDeadlineRequestDto);

		if(chkUpdateCount != dataCount || chkUpdateCount == 0) {
			return ApiResult.result(StockEnums.StockDeadlineAddDataType.UNABLE_UPDATE_DATA);
		}

		//basicYn의 Y값의 건수조회
		int basicYnCount = stockDeadlineService.getStockDeadlineBasicYnCount(stockDeadlineRequestDto);

		//basicYn값 체크
		String basicYnCheck = stockDeadlineService.getStockDeadlineBasicYnCheck(stockDeadlineRequestDto);

		if(basicYnCount == 1 && basicYnCheck.equals("Y")){
		   return ApiResult.result(StockEnums.StockDeadlineAddDataType.UNABLE_BASICYN_UPDATE_DATA);
		}
*/


		long imminent = stockDeadlineRequestDto.getImminent();	//임박기준
		long delivery = stockDeadlineRequestDto.getDelivery();	//출고기준

/*
		//유통기간이 기간일 경우
		if(distributionPeriodStart > 0 && distributionPeriodEnd > 0) {
			for (int i = distributionPeriodStart; i <= distributionPeriodEnd; i++) {
				stockDeadlineRequestDto.setDistributionPeriod(StringUtil.nvl(i));	//유통기간
				//기한관리 수정
				putStockDeadlineComn(stockDeadlineRequestDto, imminent, delivery);
			}
		} else {
			//기한관리 수정
			putStockDeadlineComn(stockDeadlineRequestDto, imminent, delivery);
		}
*/
		//기한관리 수정
		return putStockDeadlineComn(stockDeadlineRequestDto, imminent, delivery);
	 }


	/**
	 * 재고기한관리 수정 처리
	 * @param	StockDeadlineRequestDto
	 * @return	StockDeadlineResultVo
	 * @return
	 * @throws Exception
	 */
	private ApiResult<?> putStockDeadlineComn(StockDeadlineRequestDto stockDeadlineRequestDto, long imminent, long delivery) throws Exception {
		//% 계산
		if(GoodsEnums.StockDeadlineType.PERCENT.getCode().equals(stockDeadlineRequestDto.getStdType())) {
			long addImminent = StringUtil.nvlInt(stockDeadlineRequestDto.getDistributionPeriod()) * imminent/100;
			long addDelivery = StringUtil.nvlInt(stockDeadlineRequestDto.getDistributionPeriod()) * delivery/100;
			stockDeadlineRequestDto.setImminent(addImminent);
			stockDeadlineRequestDto.setDelivery(addDelivery);
		}

		// 등록된 카운트 수 조회
		HashMap countMap = stockDeadlineService.getStockDeadlineCheckCountByPeriod(stockDeadlineRequestDto);
		int totalCount = Integer.parseInt(countMap.get("totalCount").toString());
		int totalYesCount = Integer.parseInt(countMap.get("totalYesCount").toString());

		//기본설정이 N인 경우, 기존에 기본설정이 Y가 없으면 에러 리턴.
		if (stockDeadlineRequestDto.getPopBasicYn().equals("N") && totalYesCount == 0) {
			   return ApiResult.result(StockEnums.StockDeadlineAddDataType.UNABLE_BASICYN_UPDATE_DATA);
		}

		//기본설정이 Y인경우, 기존에 기본설정이 Y가 있으면 이전 데이터를 N로 설정한다.
		if(stockDeadlineRequestDto.getPopBasicYn().equals("Y") && totalYesCount > 0) {
		   stockDeadlineService.putStockDeadlineBasicYn(stockDeadlineRequestDto);
		}

		//데이터 set
		stockDeadlineRequestDto.setUrWarehouseId(StringUtil.nvl(stockDeadlineRequestDto.getUrWarehouseId(),"0"));
		stockDeadlineRequestDto.setModifyId((Integer.valueOf((SessionUtil.getBosUserVO()).getUserId())));
		//기한관리 등록
		stockDeadlineService.putStockDeadline(stockDeadlineRequestDto);

		//이력저장
		StockDeadlineHistParamDto stockDeadlineHistParamDto = StockDeadlineHistParamDto.builder()
				 .histTp(StockEnums.HistTpCode.UPDATE.getCode())
				 .ilStockDeadlineId(stockDeadlineRequestDto.getIlStockDeadlineId())
				 .urSupplierId(stockDeadlineRequestDto.getUrSupplierId())
				 .urWarehouseId(stockDeadlineRequestDto.getUrWarehouseId())
				 .distributionPeriod(stockDeadlineRequestDto.getDistributionPeriod())
				 .imminent(stockDeadlineRequestDto.getImminent())
				 .delivery(stockDeadlineRequestDto.getDelivery())
				 .targetStockRatio(stockDeadlineRequestDto.getTargetStockRatio())
				 .basicYn(stockDeadlineRequestDto.getPopBasicYn())
				 .origCreateId(stockDeadlineRequestDto.getOrigCreateId())
				 .origCreateDate(stockDeadlineRequestDto.getOrigCreateDate())
				 .origModifyId(stockDeadlineRequestDto.getOrigModifyId())
				 .origModifyDate(stockDeadlineRequestDto.getOrigModifyDate())
				 .createId((Integer.valueOf((SessionUtil.getBosUserVO()).getUserId())))
	    		 .build();
		stockDeadlineService.addStockDeadlineHist(stockDeadlineHistParamDto);

/*
		//기본설정이 Y인경우
		if(stockDeadlineRequestDto.getPopBasicYn().equals("Y")) {
		   stockDeadlineService.putStockDeadlineBasicYn(stockDeadlineRequestDto);
		}
*/
		return ApiResult.success();
	}


	/**
	 * 재고기한관리 이력 목록 조회
	 * @param	StockDeadlineRequestDto
	 * @return	StockDeadlineHistResponseDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getStockDeadlineHistList(StockDeadlineRequestDto stockDeadlineRequestDto) throws Exception {
		StockDeadlineHistResponseDto stockDeadlineHistResponseDto = new StockDeadlineHistResponseDto();
		Page<StockDeadlineHistResultVo> stockDeadlineHistList = stockDeadlineService.getStockDeadlineHistList(stockDeadlineRequestDto);

		stockDeadlineHistResponseDto.setTotal(stockDeadlineHistList.getTotal());
		stockDeadlineHistResponseDto.setRows(stockDeadlineHistList.getResult());

	    return ApiResult.success(stockDeadlineHistResponseDto);

	}


}
