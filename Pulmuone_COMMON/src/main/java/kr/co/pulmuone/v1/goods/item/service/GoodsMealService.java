package kr.co.pulmuone.v1.goods.item.service;

import kr.co.pulmuone.v1.comm.mapper.goods.item.GoodsMealMapper;
import kr.co.pulmuone.v1.goods.item.dto.GoodsMealListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.GoodsMealVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoodsMealService {

    @Autowired
    private final GoodsMealMapper goodsMealMapper;

    /**
     * 일일상품 식단 등록 내역 카운트 조회
     * @param goodsMealListRequestDto
     * @return
     */
    protected long getGoodsMealListCount(GoodsMealListRequestDto goodsMealListRequestDto) {
        return goodsMealMapper.getGoodsMealListCount(goodsMealListRequestDto);
    }

    /**
     * 일일상품 식단 등록 내역 조회
     * @param goodsMealListRequestDto
     * @return
     */
    protected List<GoodsMealVo> getGoodsMealList(GoodsMealListRequestDto goodsMealListRequestDto){
        return goodsMealMapper.getGoodsMealList(goodsMealListRequestDto);
    }

}
