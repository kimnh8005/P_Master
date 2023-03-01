package kr.co.pulmuone.v1.goods.stock.service;

import kr.co.pulmuone.v1.comm.mapper.goods.stock.GoodsStockDeadlineMapper;
import kr.co.pulmuone.v1.goods.stock.dto.StockDeadlineHistParamDto;
import kr.co.pulmuone.v1.goods.stock.dto.StockDeadlineRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockDeadlineHistResultVo;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockDeadlineResultVo;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import java.util.HashMap;
import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 재고 기한관리 Service
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
@RequiredArgsConstructor
public class GoodsStockDeadlineService {

	@Autowired
	private final GoodsStockDeadlineMapper stockDeadlineMapper;



	/**
	 * 재고기한관리 목록 조회
	 * @param	StockDeadlineRequestDto
	 * @return	StockDeadlineResponseDto
	 * @throws Exception
	 */
	protected Page<StockDeadlineResultVo> getStockDeadlineList(StockDeadlineRequestDto stockDeadlineRequestDto) throws Exception {
        PageMethod.startPage(stockDeadlineRequestDto.getPage(), stockDeadlineRequestDto.getPageSize());
        return stockDeadlineMapper.getStockDeadlineList(stockDeadlineRequestDto);
	}


	/**
	 * 재고기한관리 데이터 확인
	 * @param	StockDeadlineRequestDto
	 * @return	StockDeadlineResponseDto
	 * @throws Exception
	 */
	protected int getStockDeadlineForCheck(StockDeadlineRequestDto stockDeadlineRequestDto) {
		List<StockDeadlineResultVo> rows =	stockDeadlineMapper.getStockDeadlineForCheck(stockDeadlineRequestDto);
		return rows.size();
	}


	/**
	 * 유통기간에 의한 재고 기한관리 수량 조회
	 * @param	StockDeadlineRequestDto
	 * @return	HashMap
	 * @throws Exception
	 */
	protected HashMap getStockDeadlineCheckCountByPeriod(StockDeadlineRequestDto stockDeadlineRequestDto) {
        return stockDeadlineMapper.getStockDeadlineCheckCountByPeriod(stockDeadlineRequestDto);
	}

	/**
	 * 재고기한관리 기본설정 Y값 건수 조회
	 * @param	StockDeadlineRequestDto
	 * @return	StockDeadlineResponseDto
	 * @throws Exception
	 */
	protected int getStockDeadlineBasicYnCount(StockDeadlineRequestDto stockDeadlineRequestDto) {
		return stockDeadlineMapper.getStockDeadlineBasicYnCount(stockDeadlineRequestDto);
	}

	/**
	 * 재고기한관리 기본설정 Y값 체크
	 * @param	StockDeadlineRequestDto
	 * @return	StockDeadlineResponseDto
	 * @throws Exception
	 */
	protected String getStockDeadlineBasicYnCheck(StockDeadlineRequestDto stockDeadlineRequestDto) {
		return stockDeadlineMapper.getStockDeadlineBasicYnCheck(stockDeadlineRequestDto);
	}


	/**
	 * 재고기한관리 상세 조회
	 * @param	StockDeadlineRequestDto
	 * @return	StockDeadlineResponseDto
	 * @throws Exception
	 */
	protected StockDeadlineResultVo getStockDeadline(StockDeadlineRequestDto stockDeadlineRequestDto) throws Exception {
		return stockDeadlineMapper.getStockDeadline(stockDeadlineRequestDto);
	}



	/**
	 * 재고기한관리 등록 처리
	 * @param	stockDeadlineRequestDto
	 * @return
	 * @throws Exception
	 */
	protected int addStockDeadline(StockDeadlineRequestDto stockDeadlineRequestDto) throws Exception {
		return stockDeadlineMapper.addStockDeadline(stockDeadlineRequestDto);

	}


	/**
	 * 이력저장
	 * @param stockDeadlineHistParamDto
	 * @return
	 * @throws Exception
	 */
	protected int addStockDeadlineHist(StockDeadlineHistParamDto stockDeadlineHistParamDto) throws Exception {
		return stockDeadlineMapper.addStockDeadlineHist(stockDeadlineHistParamDto);
	}


	/**
	 * 재고기한관리 수정 처리
	 * @param	StockDeadlineRequestDto
	 * @return	StockDeadlineResultVo
	 * @return
	 * @throws Exception
	 */
	protected int putStockDeadline(StockDeadlineRequestDto stockDeadlineRequestDto) throws Exception {
		return stockDeadlineMapper.putStockDeadline(stockDeadlineRequestDto);
	}

	/**
	 * 재고기한관리 기본설정 수정 처리
	 * @param	StockDeadlineRequestDto
	 * @return	StockDeadlineResultVo
	 * @return
	 * @throws Exception
	 */
	protected int putStockDeadlineBasicYn(StockDeadlineRequestDto stockDeadlineRequestDto) throws Exception {
		return stockDeadlineMapper.putStockDeadlineBasicYn(stockDeadlineRequestDto);
	}

	/**
	 * 재고기한관리 이력 목록 조회
	 * @param	StockDeadlineRequestDto
	 * @return	StockDeadlineHistResponseDto
	 * @throws Exception
	 */
	protected Page<StockDeadlineHistResultVo> getStockDeadlineHistList(StockDeadlineRequestDto stockDeadlineRequestDto) throws Exception {
		PageMethod.startPage(stockDeadlineRequestDto.getPage(), stockDeadlineRequestDto.getPageSize());
	    return stockDeadlineMapper.getStockDeadlineHistList(stockDeadlineRequestDto);
	}


}
