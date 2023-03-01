package kr.co.pulmuone.v1.batch.goods.goods;

import kr.co.pulmuone.v1.batch.goods.goods.dto.GoodsDetailImageBatchRequestDto;
import kr.co.pulmuone.v1.comm.mappers.batch.master.goods.goods.GoodsDetailImageBatchMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoodsDetailImageBatchService {

    @Autowired
    private final GoodsDetailImageBatchMapper goodsDetailImageBatchMapper;


    /**
     * 상품 상세 이미지 생성 대상 조회
     * @param
     * @return IL_GOODS_DETL_IMG_ID List
     */
    protected List<Long> getNoBatchGoodsDetailImage() {
        return goodsDetailImageBatchMapper.getNoBatchGoodsDetailImage();
    }

    /**
     * 상품 상세 이미지 배치동작 후 등록정보 수정
     * @param goodsDetailImageBatchRequestDto
     */
    protected void updateGoodsDetailImageGenInfo(GoodsDetailImageBatchRequestDto goodsDetailImageBatchRequestDto) {
        goodsDetailImageBatchMapper.updateGoodsDetailImageGenInfo(goodsDetailImageBatchRequestDto);
    }

}
