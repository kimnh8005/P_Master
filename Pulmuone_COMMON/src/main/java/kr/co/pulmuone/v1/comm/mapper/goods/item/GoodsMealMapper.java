package kr.co.pulmuone.v1.comm.mapper.goods.item;

import kr.co.pulmuone.v1.goods.item.dto.GoodsMealListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.GoodsMealVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsMealMapper {

    /** 일일상품 식단 등록 내역 카운트 조회 */
    long getGoodsMealListCount(GoodsMealListRequestDto goodsMealListRequestDto);

    /** 일일상품 식단 등록 내역 조회 */
    List<GoodsMealVo> getGoodsMealList(GoodsMealListRequestDto goodsMealListRequestDto);

}
