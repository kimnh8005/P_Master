package kr.co.pulmuone.v1.comm.excel.util;

import kr.co.pulmuone.v1.comm.util.BeanUtils;
import kr.co.pulmuone.v1.goods.search.GoodsSearchBiz;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchOutMallVo;
import kr.co.pulmuone.v1.order.create.dto.OrderExcelRequestDto;
import kr.co.pulmuone.v1.order.create.dto.OrderExcelResponseDto;
import kr.co.pulmuone.v1.order.create.dto.OrderExeclDto;
import kr.co.pulmuone.v1.order.create.service.OrderCreateBiz;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderDto;
import kr.co.pulmuone.v1.outmall.order.service.OutmallOrderBiz;
import kr.co.pulmuone.v1.outmall.order.service.OutmallOrderService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderExcelCollectionMallDetailIdListData {

    //@Autowired
    private OutmallOrderBiz outmallOrderBiz;

    /**
     * EasyAdmin
     * @param excelList
     * @return
     * @throws Exception
     */
    public Map<String, Object> getEasyAdminCollectionMallValidatorIdList(String excelUploadType, List<OutMallOrderDto> excelList) throws Exception {
        List<String> searchCollectionMallIdList = excelList.stream()
                .map(OutMallOrderDto::getCollectionMallId)
                .distinct()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        Map<String, String> collectionMallIdList = getCollectionMallIdMaps(excelUploadType, searchCollectionMallIdList);

        List<String> searchCollectionMallDetailIdList = excelList.stream()
                .map(OutMallOrderDto::getCollectionMallDetailId)
                .distinct()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        Map<String, String> collectionMallDetailIdList = getCollectionMallDetailIdMaps(excelUploadType, searchCollectionMallDetailIdList);
/*

        List<String> searchOutmallIdList = excelList.stream()
                .map(OutMallOrderDto::getOutMallId)
                .distinct()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        Map<String, String> outmallIdList = getOutmallIdMaps(excelUploadType, searchOutmallIdList);
*/


        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("collectionMallIdList",       collectionMallIdList);
        resultMap.put("collectionMallDetailIdList", collectionMallDetailIdList);
        //resultMap.put("outmallIdList",              outmallIdList);

        return resultMap;
    }

    /**
     * Sabangnet
     * @param excelList
     * @return
     * @throws Exception
     */
    public Map<String, Object> getSabangnetCollectionMallValidatorIdList(String excelUploadType, List<OutMallOrderDto> excelList) throws Exception {
        List<String> searchCollectionMallIdList = excelList.stream()
                .map(OutMallOrderDto::getCollectionMallId)
                .distinct()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        Map<String, String> collectionMallIdList = getCollectionMallIdMaps(excelUploadType, searchCollectionMallIdList);

        List<String> searchCollectionMallDetailIdList = excelList.stream()
                .map(OutMallOrderDto::getCollectionMallDetailId)
                .distinct()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        Map<String, String> collectionMallDetailIdList = getCollectionMallDetailIdMaps(excelUploadType, searchCollectionMallDetailIdList);
/*

        List<String> searchOutmallIdList = excelList.stream()
                .map(OutMallOrderDto::getOutMallId)
                .distinct()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        Map<String, String> outmallIdList = getOutmallIdMaps(excelUploadType, searchOutmallIdList);
*/


        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("collectionMallIdList",       collectionMallIdList);
        resultMap.put("collectionMallDetailIdList", collectionMallDetailIdList);
        //resultMap.put("outmallIdList",              outmallIdList);

        return resultMap;
    }

    /**
     * 검증용 수집몰 번호
     * @param collectionMallDetailIdList
     * @return
     * @throws Exception
     */
    public Map<String, String> getCollectionMallIdMaps(String outmallType, List<String> searchList) throws Exception {
        OutmallOrderBiz outmallOrderBiz = BeanUtils.getBeanByClass(OutmallOrderBiz.class);

        List<List<String>> collectionMallIdList = ListUtils.partition(searchList, 1000);
        List<String> list = new ArrayList<>();
        for(List<String> subList : collectionMallIdList){
            list.addAll(outmallOrderBiz.getCollectionMallIdList(outmallType, subList));
        }

        //List<String> list = outmallOrderBiz.getCollectionMallIdList(outmallType, searchList);

        Map<String, String> returnMap = new HashMap<String, String>();

        for(String item: list){
            if(!returnMap.containsKey(item)) {
                returnMap.put(item, item);
            }
        }

        return returnMap;

    }


    /**
     * 검증용 수집몰상세번호
     * @param collectionMallDetailIdList
     * @return
     * @throws Exception
     */
    public Map<String, String> getCollectionMallDetailIdMaps(String outmallType, List<String> searchList) throws Exception {
        OutmallOrderBiz outmallOrderBiz = BeanUtils.getBeanByClass(OutmallOrderBiz.class);

        //List<String> list = outmallOrderBiz.getCollectionMallDetailIdList(outmallType, searchList);

        List<List<String>> collectionMallDetailIdList = ListUtils.partition(searchList,1000);
        List<String> list = new ArrayList<>();
        for(List<String> subList : collectionMallDetailIdList){
            list.addAll(outmallOrderBiz.getCollectionMallDetailIdList(outmallType, subList));
        }

        Map<String, String> returnMap = new HashMap<String, String>();

        for(String item: list){
            if(!returnMap.containsKey(item)) {
                returnMap.put(item, item);
            }
        }

        return returnMap;

    }

    /**
     * 검증용 외부몰 주문번호
     * @param collectionMallDetailIdList
     * @return
     * @throws Exception
     */
    public Map<String, String> getOutmallIdMaps(String outmallType, List<String> searchList) throws Exception {
        OutmallOrderBiz outmallOrderBiz = BeanUtils.getBeanByClass(OutmallOrderBiz.class);

        List<String> list = outmallOrderBiz.getOutmallIdList(outmallType, searchList);

        Map<String, String> returnMap = new HashMap<String, String>();

        for(String item: list){
            if(!returnMap.containsKey(item)) {
                returnMap.put(item, item);
            }
        }

        return returnMap;

    }

}
