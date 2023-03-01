package kr.co.pulmuone.v1.comm.mappers.batch.master.legacysync;

import kr.co.pulmuone.v1.batch.legacysync.purchase.dto.CreateLegacyPurchaseOrderDto;
import kr.co.pulmuone.v1.batch.legacysync.purchase.dto.vo.PurchaseOrderVo;
import kr.co.pulmuone.v1.batch.legacysync.stock.dto.*;
import kr.co.pulmuone.v1.batch.legacysync.stock.dto.vo.LegacyOrderStockIfItemWarehouseVo;
import kr.co.pulmuone.v1.batch.legacysync.stock.dto.vo.LegacyOrderStockIfVo;
import kr.co.pulmuone.v1.batch.legacysync.stock.dto.vo.OrderDetailStockGroupByVo;
import kr.co.pulmuone.v1.batch.legacysync.stock.dto.vo.OrderStockIfGroupByVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface LegacySyncBatchMapper {

    List<OrderDetailStockGroupByVo> getOrderDetailStockGroupByList();

    OrderStockIfGroupByVo getOrderStockIfGroupByInfo(SearchOrderStockIfDto searchOrderStockIfDto);

    int insertOrderStockIf(CreateOrderStockIfDto createOrderStockIfDto);

    List<LegacyOrderStockIfVo> getLegacyOrderStockIfList();

    long insertLegacyOrderStockIlItemErpStock(CreateItemErpStockDto createItemErpStockDto);

    int insertLegacyOrderStockIlItemErpStockHistory(@Param("ilItemErpStockId") long ilItemErpStockId);

    int updateLegacyOrderStockFlag(Map<String, Object> parameterMap);

    LegacyOrderStockIfItemWarehouseVo getLegacyOrderStockIfItemWarehouse(SearchItemWarehouseIdDto searchItemWarehouseIdDto);

    void spItemStockCalculatedPrepareByLegacyOrderStock();

    List<PurchaseOrderVo> getPurchaseOrderList(@Param("erpPoTypeList") List<String> erpPoTypeList);

    int insertLegacyPurchaseOrder(CreateLegacyPurchaseOrderDto createLegacyPurchaseOrderDto);

    OrderStockIfGroupByVo getCancelStockIfGroupByInfo(SearchOrderStockIfDto searchOrderStockIfDto);
}
