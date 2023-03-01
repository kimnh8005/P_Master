package kr.co.pulmuone.v1.comm.mappers.batch.master.goods.po;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.batch.goods.po.dto.vo.GoodsPoSearchResultVo;

@Mapper
public interface BatchGoodsPoSearchMapper {

	Integer getUrSupplierId(@Param("ilItemCd") String ilItemCd);

	int addPoSearch(GoodsPoSearchResultVo poTypeInfo);

	int addPoSearchMerge(GoodsPoSearchResultVo poTypeInfo);

	int delPoSearch();

	int putItemPoTpByBatch();
}
