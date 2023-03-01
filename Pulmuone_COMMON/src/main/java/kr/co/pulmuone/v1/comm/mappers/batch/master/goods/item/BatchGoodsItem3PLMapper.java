package kr.co.pulmuone.v1.comm.mappers.batch.master.goods.item;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.batch.goods.item.vo.ErpBaekamGoodsItemResultVo;


@Mapper
public interface BatchGoodsItem3PLMapper {

	public List<ErpBaekamGoodsItemResultVo> getInsertTargetItemList();

	public List<ErpBaekamGoodsItemResultVo> getUpdateTargetItemList();


}
