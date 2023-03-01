package kr.co.pulmuone.v1.comm.mappers.batch.master.goods.stock;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.batch.goods.stock.dto.vo.ErpLinkItemVo;
import kr.co.pulmuone.v1.batch.goods.stock.dto.vo.ItemErpStockResultVo;
import kr.co.pulmuone.v1.batch.goods.stock.dto.vo.ItemErpStockVo;

@Mapper
public interface BatchItemErpStockMapper {

    public List<ErpLinkItemVo> getErpLinkItemList(@Param("shiFroOrgId") String shiFroOrgId);

    public List<ItemErpStockVo> getItemErpStock(ItemErpStockVo itemErpStockVo);

    public String getUrWarehouseIdByShiFroOrgId(@Param("shiFroOrgId") String shiFroOrgId);

    public long addItemErpStock(ItemErpStockVo itemErpStockVo);

    public int addItemErpStockHistory(@Param("ilItemErpStockId") long ilItemErpStockId);

    public List<ItemErpStockVo> getIlItemStockExpr();

    int addExcelUploadCal();

    int spItemStockCaculatedPrepare();

    int putExcelUpload(ItemErpStockResultVo vo);

    int putIlItemWarehouse(ItemErpStockResultVo vo);

    int addErpStock(ItemErpStockResultVo vo);

    int getIlItemErpStockCount(ItemErpStockVo vo);

    int putItemErpStock(ItemErpStockVo vo);

    int putItemErpStockHistory(ItemErpStockVo vo);

    public List<ItemErpStockResultVo> getStockExprCalList(ItemErpStockVo vo);

}
