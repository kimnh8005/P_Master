package kr.co.pulmuone.v1.comm.mappers.batch.master.goods.po;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.batch.goods.po.dto.ErpIfPurchaseOrdLineRequestDto;

@Mapper
public interface BatchGoodsPoInputMapper {

	List<ErpIfPurchaseOrdLineRequestDto> getPoIFList(ErpIfPurchaseOrdLineRequestDto vo);

	int putItemPoIFResult(ErpIfPurchaseOrdLineRequestDto vo);

	String getPoSeq();

	String getMoSeq();

	int addGoodsPoOrderCalculate(String baseDt);

}
