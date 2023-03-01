package kr.co.pulmuone.v1.goods.stock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.mapper.goods.stock.GoodsStockExcelUploadMapper;
import kr.co.pulmuone.v1.goods.stock.dto.StockExcelUploadListRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockExcelUploadResultVo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoodsStockExcelUploadService {

	@Autowired
	private final GoodsStockExcelUploadMapper goodsStockExcelUploadMapper;


	/**
	 * @Desc  ERP 재고 엑셀 업로드
	 * @param StockExcelUploadListRequestDto
	 * @return int
	 */
	protected int addExcelUpload(StockExcelUploadResultVo vo) {
		return goodsStockExcelUploadMapper.addExcelUpload(vo);
	}

	/**
	 * @Desc  ERP 재고 엑셀 업로드 로그 상세 저장(성공)
	 * @param StockExcelUploadListRequestDto
	 * @return int
	 */
	protected int addSuccessExcelDetlLog(StockExcelUploadResultVo vo) {
		return goodsStockExcelUploadMapper.addSuccessExcelDetlLog(vo);
	}

	/**
	 * @Desc  ERP 재고 엑셀 업로드 로그 상세 저장(실패)
	 * @param StockExcelUploadListRequestDto
	 * @return int
	 */
	protected int addFailExcelDetlLog(StockExcelUploadListRequestDto dto) {
		return goodsStockExcelUploadMapper.addFailExcelDetlLog(dto);
	}

	/**
	 * @Desc  ERP 재고 엑셀 업로드 로그 저장
	 * @param int successCnt, int failCnt
	 * @return int
	 */
	protected int addExcelUploadLog() {
		return goodsStockExcelUploadMapper.addExcelUploadLog();
	}

	/**
	 * @Desc  ERP 재고 엑셀 업로드 로그 수정
	 * @param fileNm, successCnt, failCnt
	 * @return int
	 */
	protected int putExcelUploadLog(String fileNm, String userId, int successCnt, int failCnt) {
		return goodsStockExcelUploadMapper.putExcelUploadLog(fileNm, userId, successCnt, failCnt);
	}

	/**
	 * @Desc  엑셀업로드 설정시간 조회
	 * @return String
	 */
	protected String getEnvironment() {
		return goodsStockExcelUploadMapper.getEnvironment();
	}

	/**
	 * @Desc  품목존재 건수 조회
	 * @param StockExcelUploadListRequestDto
	 * @return int
	 */
	protected List<StockExcelUploadResultVo> getStockInfoList(StockExcelUploadListRequestDto dto) {
		return goodsStockExcelUploadMapper.getStockInfoList(dto);
	}

	/**
	 * @Desc  용인 출고처 품목 유통기한별 재고 삭제
	 * @param baseDt
	 * @return int
	 */
	protected int delStockExprYongin(String baseDt) {
		return goodsStockExcelUploadMapper.delStockExprYongin(baseDt);
	}

	/**
     * @Desc 출고처 정보 조회
     * @return string
     */
    protected String getConfigValue(String psKey) {
    	return goodsStockExcelUploadMapper.getConfigValue(psKey);
    }

    /**
     * @Desc 성공건수 조회
     * @return int
     */
    protected int getSuccessCnt() {
    	return goodsStockExcelUploadMapper.getSuccessCnt();
    }

    /**
     * @Desc 실패건수 조회
     * @return int
     */
    protected int getFailCnt() {
    	return goodsStockExcelUploadMapper.getFailCnt();
    }

	/**
	 * @Desc  용인물류 유통기한별 재고리스트 엑셀 다운로드
	 * @param StockExcelUploadRequestDto
	 * @return int
	 */
	protected List<StockExcelUploadResultVo> getStockExprList() {
		return goodsStockExcelUploadMapper.getStockExprList();
	}

}
