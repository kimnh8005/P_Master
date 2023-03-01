package kr.co.pulmuone.v1.goods.item.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.goods.item.dto.GoodsMealListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.GoodsMealListResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.GoodsMealVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Service
public class GoodsMealBizImpl implements GoodsMealBiz{

    @Autowired
    GoodsMealService goodsMealService;


    /**
     * @Desc
     * @param
     * @return
     */
    @Override
    public ApiResult<?> getGoodsMealList(GoodsMealListRequestDto goodsMealListRequestDto) {

        if (!goodsMealListRequestDto.getFindKeyword().isEmpty()) {
            ArrayList<String> array = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(goodsMealListRequestDto.getFindKeyword(), "\n|,");
            while (st.hasMoreElements()) {
                String object = (String) st.nextElement();
                array.add(object);
            }
            goodsMealListRequestDto.setFindKeywordList(array);
        }

        if (!goodsMealListRequestDto.getSearchMealPatternCd().isEmpty()) {
            ArrayList<String> array = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(goodsMealListRequestDto.getSearchMealPatternCd(), "\n|,");
            while (st.hasMoreElements()) {
                String object = (String) st.nextElement();
                array.add(object);
            }
            goodsMealListRequestDto.setSearchMealPatternCdList(array);
        }

        long totalCnt = goodsMealService.getGoodsMealListCount(goodsMealListRequestDto);

        List<GoodsMealVo> goodsMealList = goodsMealService.getGoodsMealList(goodsMealListRequestDto);

        return ApiResult.success(
                GoodsMealListResponseDto.builder()
                        .rows(goodsMealList)
                        .total(totalCnt)
                        .build()
        );

    }
}
