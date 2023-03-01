package kr.co.pulmuone.v1.comm.mapper.goods.goods;

import kr.co.pulmuone.v1.goods.goods.dto.*;
import kr.co.pulmuone.v1.goods.goods.dto.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface GoodsCodeMapper {

	// 풀무원샵 상품코드 리스트
	List<GoodsCodeVo> getGoodsCodeList(@Param("ilGoodsId") String ilGoodsId);

	// 풀무원샵 상품코드 등록
	int addGoodsCode(@Param("ilGoodsId") String ilGoodsId, @Param("goodsCodeList") List<GoodsCodeVo> goodsCodeList, @Param("createId") String createId);

	// 풀무원샵 상품코드 전체 삭제
	int delAllGoodsCode(@Param("ilGoodsId") String ilGoodsId);

	// 풀무원샵 상품코드 정보가 있는지 체크
	List<GoodsCodeVo> goodsCodeExistChk(@Param("goodsCodeList")  List<GoodsCodeVo> goodsCodeList) throws Exception;

}
