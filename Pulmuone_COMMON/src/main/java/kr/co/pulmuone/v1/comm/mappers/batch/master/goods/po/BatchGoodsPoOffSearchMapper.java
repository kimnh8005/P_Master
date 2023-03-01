package kr.co.pulmuone.v1.comm.mappers.batch.master.goods.po;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BatchGoodsPoOffSearchMapper {

	String getIlItemCd(@Param("ilItemCd") String ilItemCd);

	int putIlItem(@Param("ilItemCd") String ilItemCd, @Param("poProRea") String poProRea);

}
