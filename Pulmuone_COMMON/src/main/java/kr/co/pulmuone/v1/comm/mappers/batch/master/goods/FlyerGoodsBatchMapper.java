package kr.co.pulmuone.v1.comm.mappers.batch.master.goods;

import kr.co.pulmuone.v1.goods.goods.dto.vo.FlyerGoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FlyerGoodsBatchMapper {

    // 일반, 임직원 할인율순 20개 상품 조회
    List<FlyerGoodsVo> getFlyerGoodsList(@Param("execTime")String execTime, @Param("priceType")String priceType);

    // 상품데이터 삭제
    int deleteFlyerGoods();

    // 상품리스트 추가
    void addFlyerGoodsList(@Param("insertList") List<FlyerGoodsVo> insertList);

}
