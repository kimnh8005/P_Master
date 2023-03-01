package kr.co.pulmuone.v1.goods.stock.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.goods.stock.dto.GoodsStockDisposalRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.GoodsStockDisposalResponseDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.GoodsStockDisposalResultVo;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.CharArrayMap;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoodsStockDisposalBizImpl implements GoodsStockDisposalBiz {

    private final GoodsStockDisposalService goodsStockDisposalService;

    @Override
    public ApiResult<?> getGoodsStockDisposalList(GoodsStockDisposalRequestDto goodsNutritionRequestDto) {
        PageHelper.startPage(goodsNutritionRequestDto.getPage(), goodsNutritionRequestDto.getPageSize());
        Page<GoodsStockDisposalResultVo> resultVoPage = goodsStockDisposalService.getGoodsStockDisposalList(goodsNutritionRequestDto);

        GoodsStockDisposalResponseDto responseDto = GoodsStockDisposalResponseDto
                                                        .builder()
                                                        .total(resultVoPage.getTotal())
                                                        .baseTimestamp(DateUtil.getCurrentDate("yyyyMMddHHmm"))
                                                        .rows(resultVoPage)
                                                        .build();

        return ApiResult.success(responseDto);
    }

    @Override
    public ExcelDownloadDto getGoodsStockDisposalExcelList(GoodsStockDisposalRequestDto goodsNutritionRequestDto) {
        return goodsStockDisposalService.getGoodsStockDisposalExcelList(goodsNutritionRequestDto);
    }
}
