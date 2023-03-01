package kr.co.pulmuone.v1.comm.excel.util;

import kr.co.pulmuone.v1.comm.util.BeanUtils;
import kr.co.pulmuone.v1.goods.search.GoodsSearchBiz;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchOutMallVo;
import kr.co.pulmuone.v1.order.create.dto.OrderExcelRequestDto;
import kr.co.pulmuone.v1.order.create.dto.OrderExcelResponseDto;
import kr.co.pulmuone.v1.order.create.dto.OrderExeclDto;
import kr.co.pulmuone.v1.order.create.service.OrderCreateBiz;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderExcelGoodsIdListData {

    @Autowired
    private GoodsSearchBiz goodsSearchBiz;

    //@Autowired
    private OrderCreateBiz orderCreateBiz;


    /**
     * BOS Create
     * @param excelList
     * @return
     */
    public List<OrderExcelResponseDto> getBosCreateIlGoodsIdList(List<OrderExeclDto> excelList) throws Exception {

        OrderExcelRequestDto orderExcelRequestDto = new OrderExcelRequestDto();
        orderExcelRequestDto.setOrderExcelList(excelList);

        OrderCreateBiz orderCreateBiz = BeanUtils.getBeanByClass(OrderCreateBiz.class);
        return orderCreateBiz.getExcelUploadList(orderExcelRequestDto);
    }

    /**
     * EasyAdmin
     * @param excelList
     * @return
     * @throws Exception
     */
    public Map<Long, GoodsSearchOutMallVo> getEasyAdminIlGoodsIdList(List<OutMallOrderDto> excelList) throws Exception {
        List<String> ilGoodsIdList = excelList.stream()
                .map(OutMallOrderDto::getIlGoodsId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        return getGoodsMaps(ilGoodsIdList);
    }

    /**
     * Sabangnet
     * @param excelList
     * @return
     * @throws Exception
     */
    public Map<Long, GoodsSearchOutMallVo> getSabangnetIlGoodsIdList(List<OutMallOrderDto> excelList) throws Exception {
        List<String> ilGoodsIdList = excelList.stream()
                .map(OutMallOrderDto::getIlGoodsId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        return getGoodsMaps(ilGoodsIdList);
    }

    /**
     * 공통 상품코드 조회
     * @param ilGoodsIdList
     * @return
     * @throws Exception
     */
    public Map<Long, GoodsSearchOutMallVo> getGoodsMaps(List<String> ilGoodsIdList) throws Exception {
        List<GoodsSearchOutMallVo> goodsResponse = goodsSearchBiz.getGoodsFromOutMall(ilGoodsIdList);

        return goodsResponse.stream()
                .collect(Collectors.toMap(GoodsSearchOutMallVo::getGoodsId, vo -> vo));

    }

}
