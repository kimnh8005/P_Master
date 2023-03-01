package kr.co.pulmuone.v1.comm.mapper.shopping.restock;

import kr.co.pulmuone.v1.shopping.restock.dto.vo.ShoppingRestockVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShoppingRestockMapper {

    List<Long> getClearStockIdList();

    List<ShoppingRestockVo> getSendGoodsIdList();

    void delRestockIdList(@Param("spRestockIdList") List<Long> spRestockIdList);

    void putRestockStatus(@Param("spRestockIdList") List<Long> spRestockIdList);

    int putRetockInfo(@Param("ilGoodsId") Long ilGoodsId, @Param("urUserId") String urUserId) throws Exception;

    int getDupCnt(@Param("ilGoodsId") Long ilGoodsId, @Param("urUserId") String urUserId) throws Exception;
}
