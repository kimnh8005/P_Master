package kr.co.pulmuone.v1.comm.mapper.goods.stock;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.goods.stock.dto.GoodsStockDisposalRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.GoodsStockDisposalResultVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsStockDisposalMapper {

    Page<GoodsStockDisposalResultVo> getGoodsStockDisposalList(GoodsStockDisposalRequestDto goodsStockDisposalRequestDto);

}
