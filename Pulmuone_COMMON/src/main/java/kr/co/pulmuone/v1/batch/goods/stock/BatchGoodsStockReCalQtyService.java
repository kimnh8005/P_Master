package kr.co.pulmuone.v1.batch.goods.stock;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.batch.goods.stock.dto.vo.ItemErpStockReCalQtyResultVo;
import kr.co.pulmuone.v1.comm.api.dto.basic.ErpStockReCalQtyResponseDto;
import kr.co.pulmuone.v1.comm.mappers.batch.master.goods.stock.BatchItemErpStockReCalQtyMapper;
import lombok.RequiredArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 * 품목 연동재고 조회 후 품목 유통기한별 재고 재계산 배치 Service
 * </PRE>
 *
 * <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020.12.15    이성준              최초작성
* =======================================================================
 * </PRE>
 */
@Service
@RequiredArgsConstructor
public class BatchGoodsStockReCalQtyService {

	@Autowired
    private BatchItemErpStockReCalQtyMapper batchItemErpStockReCalQtyMapper;

	protected void runGoodsStockReCalQtyJob() {

        List<ErpStockReCalQtyResponseDto> erpStockOrgList = getErpStockOrgList(); //품목 연동재고 조회

		for(ErpStockReCalQtyResponseDto dto : erpStockOrgList) {

            	int stockExprSum = getStockExprSum(dto);//품목 유통기한별 재고합계

	            if(dto.getStockQty() > stockExprSum) { //ERP 연동 전일마감 재고 총합 > 유통기한별 전일마감 재고 총합

	               int rest = dto.getStockQty() - stockExprSum;//차액

	               // 품목 유통기간 획득
	               int itemDistributionPeriod = dto.getDistributionPeriod() - 1;
	               if (itemDistributionPeriod < 0) {
	            	   itemDistributionPeriod = 0;
	               }

	               // 추가할 유통기한 계산
	               LocalDate currentDate = LocalDate.now();
	               LocalDate expirationDate = currentDate.plusDays(itemDistributionPeriod);

	               // 추가할 유통기한이 존재하는지 확인
            	   ItemErpStockReCalQtyResultVo inStockInfo = ItemErpStockReCalQtyResultVo.builder() //
                           .ilItemWarehouseId(dto.getIlItemWarehouseId()) //
                           .baseDt(dto.getBaseDt()) //
                           .expirationDt(expirationDate.format(DateTimeFormatter.ISO_LOCAL_DATE)) //
                           .build();
	               ItemErpStockReCalQtyResultVo vo = getStockExprSearchByExpirationDate(inStockInfo);

	               // 존재하면 update 그렇지 않으면 insert
	               if (vo != null) {
		               vo.setStockQty(vo.getStockQty()+rest);//마지막 유통기한row에 차액을 더함
		               putStockExprTotalSum(vo);//품목 유통기한별 재고 수정
	               } else {
	            	   ItemErpStockReCalQtyResultVo insertVo = ItemErpStockReCalQtyResultVo.builder() //
	                           .ilItemWarehouseId(dto.getIlItemWarehouseId()) //
	                           .baseDt(dto.getBaseDt()) //
	                           .expirationDt(expirationDate.format(DateTimeFormatter.ISO_LOCAL_DATE)) //
	                           .stockQty(rest) //
	                           .build();
	            	   addItemStockExpr(insertVo);//품목 유통기한별 재고 저장
	               }

	            }else if(dto.getStockQty() < stockExprSum) {

	               int rest = stockExprSum - dto.getStockQty();//차액
	               List<ItemErpStockReCalQtyResultVo> stockExprSearchList = getStockExprSearchList(dto);//품목 유통기한별 재고ID 및 수량 목록 조회

	               for(ItemErpStockReCalQtyResultVo vo : stockExprSearchList) {

                       int calQty = vo.getStockQty() - rest;

                       if(calQty < 0 && rest != 0) {
                    	   vo.setStockQty(0);
                    	   putStockExprTotalSum(vo);//품목 유통기한별 재고 수정
                    	   rest = (calQty*-1);
                       }else if(calQty > 0 && rest != 0) {
                    	   vo.setStockQty(calQty);
                    	   putStockExprTotalSum(vo);//품목 유통기한별 재고 수정
                    	   rest = 0;
                       }else if(calQty == 0) {
                    	   vo.setStockQty(0);
                    	   putStockExprTotalSum(vo);//품목 유통기한별 재고 수정
                    	   rest = 0;
                       }
	                 }
	              }
		  }//for end

/* ERP에서 들어온 데이터는 변경하지 않음.
		//전일마감재고 재계산(유통기한이 지난 품목별 재고합계로 차감)
		for(ErpStockReCalQtyResponseDto dto : erpStockOrgList) {

				Integer stockExprExcessSum = getStockExprExcessSum(dto);//유통기한이 지난 품목 유통기한별 재고합계

			   if(stockExprExcessSum != null && stockExprExcessSum > 0) {
				  int rest = dto.getStockQty() - stockExprExcessSum.intValue(); //품목 연동재고에서 유통기한별 재고합계를 차감
				  dto.setStockQty(rest);
				  putItemErpStock(dto); // 차감된 수량으로 수정
				  putItemErpStockHistory(dto); // 차감된 수량으로 이력수정
			   }

		}
*/
	  }

	/**
	 * 품목 연동재고 조회
	 * @return List<ErpStockReCalQtyResponseDto>
	 */
	protected List<ErpStockReCalQtyResponseDto> getErpStockOrgList(){
		return batchItemErpStockReCalQtyMapper.getErpStockOrgList();
	}

	/**
	 * 품목 유통기한별 재고 건수조회
	 * @param ErpStockReCalQtyResponseDto
	 * @return int
	 */
	protected int getStockExprCount(ErpStockReCalQtyResponseDto dto) {
		return batchItemErpStockReCalQtyMapper.getStockExprCount(dto);
	}

	/**
	 * 품목 유통기한별 재고합계
	 * @param ErpStockReCalQtyResponseDto
	 * @return int
	 */
	protected int getStockExprSum(ErpStockReCalQtyResponseDto dto) {
		return batchItemErpStockReCalQtyMapper.getStockExprSum(dto);
	}

	/**
	 * 유통기한이 지난 품목 유통기한별 재고합계
	 * @param ErpStockReCalQtyResponseDto
	 * @return Integer
	 */
	protected Integer getStockExprExcessSum(ErpStockReCalQtyResponseDto dto) {
		return batchItemErpStockReCalQtyMapper.getStockExprExcessSum(dto);
	}

	/**
	 * 품목 유통기한별 재고ID 및 수량 조회(마지막 유통기한을 조회)
	 * @param ErpStockReCalQtyResponseDto
	 * @return ItemErpStockReCalQtyResultVo
	 */
	protected ItemErpStockReCalQtyResultVo getStockExprSearch(ErpStockReCalQtyResponseDto dto) {
		return batchItemErpStockReCalQtyMapper.getStockExprSearch(dto);
	}

	/**
	 * 품목 유통기한별 재고ID 및 수량 목록 조회
	 * @param ErpStockReCalQtyResponseDto
	 * @return List<ItemErpStockReCalQtyResultVo>
	 */
	protected List<ItemErpStockReCalQtyResultVo> getStockExprSearchList(ErpStockReCalQtyResponseDto dto){
		return batchItemErpStockReCalQtyMapper.getStockExprSearchList(dto);
	}

	/**
	 * 품목 유통기한별 재고 수정
	 * @param ErpStockReCalQtyResponseDto
	 * @return int
	 */
	protected int putStockExprTotalSum(ItemErpStockReCalQtyResultVo vo) {
		return batchItemErpStockReCalQtyMapper.putStockExprTotalSum(vo);
	}

	/**
	 * 유통기한이 지난 품목별 재고합계로 수정
	 * @param ErpStockReCalQtyResponseDto
	 * @return int
	 */
	protected int putItemErpStock(ErpStockReCalQtyResponseDto dto) {
		return batchItemErpStockReCalQtyMapper.putItemErpStock(dto);
	}

	/**
	 * 유통기한이 지난 품목별 재고합계로 이력수정
	 * @param ErpStockReCalQtyResponseDto
	 * @return int
	 */
	protected int putItemErpStockHistory(ErpStockReCalQtyResponseDto dto) {
		return batchItemErpStockReCalQtyMapper.putItemErpStockHistory(dto);
	}

	/**
	 * 품목 유통기한별 재고 생성
	 * @param ErpStockReCalQtyResponseDto
	 * @return int
	 */
	protected int addItemStockExpr(ItemErpStockReCalQtyResultVo vo) {
		return batchItemErpStockReCalQtyMapper.addItemStockExpr(vo);
	}

	/**
	 * 품목 유통기한별 재고 ID 조회 - 유통기한 날짜로 조회
	 * @param ErpStockReCalQtyResponseDto
	 * @return int
	 */
	protected ItemErpStockReCalQtyResultVo getStockExprSearchByExpirationDate(ItemErpStockReCalQtyResultVo dto) {
		return batchItemErpStockReCalQtyMapper.getStockExprSearchByExpirationDate(dto);
	}

}
