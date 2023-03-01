package kr.co.pulmuone.v1.comm.mapper.goods.stock;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.goods.stock.dto.StockExcelUploadListRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockExcelUploadResultVo;

@Mapper
public interface GoodsStockExcelUploadMapper {

	int addExcelUpload(StockExcelUploadResultVo vo);

	List<StockExcelUploadResultVo> getStockOrgList(StockExcelUploadListRequestDto dto);

	List<StockExcelUploadResultVo> getStockSubOrgList(StockExcelUploadListRequestDto dto);

	List<StockExcelUploadResultVo> getStockSubNotOrgList(StockExcelUploadListRequestDto dto);

	int getSearchTypeCnt(StockExcelUploadListRequestDto dto);

	int getSubSearchTypeCnt(StockExcelUploadListRequestDto dto);

	int addErpStock(StockExcelUploadResultVo vo);

	int addSuccessExcelDetlLog(StockExcelUploadResultVo vo);

	int addFailExcelDetlLog(StockExcelUploadListRequestDto dto);

	int addExcelUploadLog();

	int putExcelUploadLog(@Param("fileNm") String fileNm, @Param("userId") String userId, @Param("successCnt") int successCnt, @Param("failCnt") int failCnt);

	String getEnvironment();

	List<StockExcelUploadResultVo> getStockInfoList(StockExcelUploadListRequestDto dto);

	List<StockExcelUploadResultVo> getStockExprList();

	int delStockExprYongin(@Param("baseDt") String baseDt);

	String getConfigValue(String psKey);

	int getSuccessCnt();

	int getFailCnt();

}
