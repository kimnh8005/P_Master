package kr.co.pulmuone.v1.comm.mapper.goods.stock;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.goods.stock.dto.vo.ItemStockVo;

@Mapper
public interface ItemStockMapper {

    public List<ItemStockVo> getItemStock(ItemStockVo itemStockVo);

    public int addItemStock(ItemStockVo itemStockVo);

    public int modifyItemStock(ItemStockVo itemStockVo);

}
