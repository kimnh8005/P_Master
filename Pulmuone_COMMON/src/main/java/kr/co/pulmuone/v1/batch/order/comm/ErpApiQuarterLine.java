package kr.co.pulmuone.v1.batch.order.comm;

import kr.co.pulmuone.v1.batch.order.dto.vo.*;
import kr.co.pulmuone.v1.comm.api.constant.SupplierTypes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 API 배치 QuarterLine
 * </PRE>
 */

@Service
@RequiredArgsConstructor
public class ErpApiQuarterLine {

    private final ErpApiLine erpApiLine;

    /**
     * 용인물류 일반배송 주문 line 생성
     * @param linelist
     * @return List<?>
     * @throws
     */
    public List<?> getNormalOrderLineList(List<?> linelist) {

        List<Object> resultList = new ArrayList<>();

        for(int i=0; i<linelist.size(); i++){
            DeliveryOrderListVo lineItem = (DeliveryOrderListVo) linelist.get(i);
            String urSupplierId = lineItem.getUrSupplierId(); // 공급처 ID

            // 공급처가 풀무원식품일 경우
            if (SupplierTypes.FOOD.getCode().equals(urSupplierId)) {
                resultList.add(erpApiLine.getNormalDeliveryOrderFoodLine(lineItem));
            }
            // 공급처가 올가홀푸드일 경우
            else if (SupplierTypes.ORGA.getCode().equals(urSupplierId)) {
                resultList.add(erpApiLine.getNormalDeliveryOrderOrgaLine(lineItem));
            }

        }

        return resultList;
    }

    /**
     * 용인물류 새벽배송 주문 line 생성
     * @param linelist
     * @return List<?>
     * @throws
     */
    public List<?> getDawnOrderLineList(List<?> linelist) {

        List<Object> resultList = new ArrayList<>();

        for(int i=0; i<linelist.size(); i++){
            DeliveryOrderListVo lineItem = (DeliveryOrderListVo) linelist.get(i);
            String urSupplierId = lineItem.getUrSupplierId(); // 공급처 ID

            // 공급처가 풀무원식품일 경우
            if (SupplierTypes.FOOD.getCode().equals(urSupplierId)) {
                resultList.add(erpApiLine.getDawnDeliveryOrderFoodLine(lineItem));
            }
            // 공급처가 올가홀푸드일 경우
            else if (SupplierTypes.ORGA.getCode().equals(urSupplierId)) {
                resultList.add(erpApiLine.getDawnDeliveryOrderOrgaLine(lineItem));
            }

        }

        return resultList;
    }

    /**
     * 백암물류 주문 line 생성
     * @param linelist
     * @return List<?>
     * @throws
     */
    public List<?> getCjOrderLineList(List<?> linelist) {

        List<Object> resultList = new ArrayList<>();

        for(int i=0; i<linelist.size(); i++){
            CjOrderListVo lineItem = (CjOrderListVo) linelist.get(i);
            resultList.add(erpApiLine.getCjOrderLine(lineItem));
        }

        return resultList;
    }

    /**
     * 올가 매장배송 주문 line 생성
     * @param linelist
     * @return List<?>
     * @throws
     */
    public List<?> getOrgaStoreDeliveryOrderLineList(List<?> linelist) {

        List<Object> resultList = new ArrayList<>();

        for(int i=0; i<linelist.size(); i++){
            OrgaStoreDeliveryOrderListVo lineItem = (OrgaStoreDeliveryOrderListVo) linelist.get(i);
            resultList.add(erpApiLine.getOrgaStoreDeliveryOrderLine(lineItem));
        }

        return resultList;
    }

    /**
     * 하이톡 택배배송 주문 line 생성
     * @param linelist
     * @return List<?>
     * @throws
     */
    public List<?> getHitokNormalDeliveryOrderLineList(List<?> linelist) {

        List<Object> resultList = new ArrayList<>();

        for(int i=0; i<linelist.size(); i++){
            HitokNormalDeliveryOrderListVo lineItem = (HitokNormalDeliveryOrderListVo) linelist.get(i);
            resultList.add(erpApiLine.getHitokNormalDeliveryOrderLine(lineItem));
        }

        return resultList;
    }

    /**
     * 하이톡 일일배송 주문 line 생성
     * @param linelist
     * @return List<?>
     * @throws
     */
    public List<?> getHitokDailyDeliveryOrderLineList(List<?> linelist) {

        List<Object> resultList = new ArrayList<>();

        for(int i=0; i<linelist.size(); i++){
            HitokDailyDeliveryOrderListVo lineItem = (HitokDailyDeliveryOrderListVo) linelist.get(i);
            resultList.add(erpApiLine.getHitokDailyDeliveryOrderLine(lineItem));
        }

        return resultList;
    }

    /**
     * 풀무원건강생활(LDS) 주문 line 생성
     * @param linelist
     * @return List<?>
     * @throws
     */
    public List<?> getLohasDirectSaleOrderLineList(List<?> linelist) {

        List<Object> resultList = new ArrayList<>();

        for(int i=0; i<linelist.size(); i++){
            LohasDirectSaleOrderListVo lineItem = (LohasDirectSaleOrderListVo) linelist.get(i);
            resultList.add(erpApiLine.getLohasDirectSaleOrderLine(lineItem));
        }

        return resultList;
    }

    /**
     * 베이비밀 일일배송 주문 line 생성
     * @param linelist
     * @return List<?>
     * @throws
     */
    public List<?> getBabymealDailyOrderLineList(List<?> linelist) {

        List<Object> resultList = new ArrayList<>();

        for(int i=0; i<linelist.size(); i++){
            BabymealOrderListVo lineItem = (BabymealOrderListVo) linelist.get(i);
            resultList.add(erpApiLine.getBabymealDailyOrderLine(lineItem));
        }

        return resultList;
    }

    /**
     * 베이비밀 택배배송 주문 line 생성
     * @param linelist
     * @return List<?>
     * @throws
     */
    public List<?> getBabymealNormalOrderLineList(List<?> linelist) {

        List<Object> resultList = new ArrayList<>();

        for(int i=0; i<linelist.size(); i++){
            BabymealNormalOrderListVo lineItem = (BabymealNormalOrderListVo) linelist.get(i);
            resultList.add(erpApiLine.getBabymealNormalOrderLine(lineItem));
        }

        return resultList;
    }

    /**
     * 매출 주문 (풀무원식품) line 생성
     * @param linelist
     * @return List<?>
     * @throws
     */
    public List<?> getSalesOrderFoodLineList(List<?> linelist) {

        List<Object> resultList = new ArrayList<>();

        for(int i=0; i<linelist.size(); i++){
            SalesOrderListVo lineItem = (SalesOrderListVo) linelist.get(i);
            resultList.add(erpApiLine.getSalesOrderFoodLine(lineItem));
        }

        return resultList;
    }

    /**
     * 매출 주문 (올가홀푸드) line 생성
     * @param linelist
     * @return List<?>
     * @throws
     */
    public List<?> getSalesOrderOrgaLineList(List<?> linelist) {

        List<Object> resultList = new ArrayList<>();

        for(int i=0; i<linelist.size(); i++){
            SalesOrderListVo lineItem = (SalesOrderListVo) linelist.get(i);
            resultList.add(erpApiLine.getSalesOrderOrgaLine(lineItem));
        }

        return resultList;
    }

    /**
     * 반품매출 주문 (풀무원식품) line 생성
     * @param linelist
     * @return List<?>
     * @throws
     */
    public List<?> getReturnSalesOrderFoodLineList(List<?> linelist) {

        List<Object> resultList = new ArrayList<>();

        for(int i=0; i<linelist.size(); i++){
            ReturnSalesOrderListVo lineItem = (ReturnSalesOrderListVo) linelist.get(i);
            resultList.add(erpApiLine.getReturnSalesOrderFoodLine(lineItem));
        }

        return resultList;
    }

    /**
     * 반품매출 주문 (올가홀푸드) line 생성
     * @param linelist
     * @return List<?>
     * @throws
     */
    public List<?> getReturnSalesOrderOrgaLineList(List<?> linelist) {

        List<Object> resultList = new ArrayList<>();

        for(int i=0; i<linelist.size(); i++){
            ReturnSalesOrderListVo lineItem = (ReturnSalesOrderListVo) linelist.get(i);
            resultList.add(erpApiLine.getReturnSalesOrderOrgaLine(lineItem));
        }

        return resultList;
    }

    /**
     * 하이톡 택배배송 반품 주문 line 생성
     * @param linelist
     * @return List<?>
     * @throws
     */
    public List<?> getHitokNormalDeliveryReturnOrderLineList(List<?> linelist) {

        List<Object> resultList = new ArrayList<>();

        for(int i=0; i<linelist.size(); i++){
            HitokDeliveryReturnOrderListVo lineItem = (HitokDeliveryReturnOrderListVo) linelist.get(i);
            resultList.add(erpApiLine.getHitokNormalDeliveryReturnOrderLine(lineItem));
        }

        return resultList;
    }

    /**
     * 풀무원건강생활(LDS) 반품 주문 line 생성
     * @param linelist
     * @return List<?>
     * @throws
     */
    public List<?> getLohasDirectSaleReturnOrderLineList(List<?> linelist) {

        List<Object> resultList = new ArrayList<>();

        for(int i=0; i<linelist.size(); i++){
            LohasDirectSaleReturnOrderListVo lineItem = (LohasDirectSaleReturnOrderListVo) linelist.get(i);
            resultList.add(erpApiLine.getLohasDirectSaleReturnOrderLine(lineItem));
        }

        return resultList;
    }
}
