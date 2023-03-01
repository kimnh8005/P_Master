package kr.co.pulmuone.v1.goods.item.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPoListRequestVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPoListResultVo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GoodsItemPoListServiceTest extends CommonServiceTestBaseForJunit5 {

	@Autowired
    private GoodsItemPoListService goodsItemPoListService;

	@Test
    void 발주저장이력_생성() {

		String ilPoId = "1227";
		String createId = "1";

    	// given
    	ItemPoListRequestVo vo = new ItemPoListRequestVo();
		vo.setIlPoId(ilPoId);
		vo.setCreateId(createId);

    	log.info("vo: {}", vo);

    	// when
        int count = goodsItemPoListService.addItemPoSavedLog(vo);

        // then
        assertTrue(count > 0);
    }

	@Test
    void 발주_리스트_목록조회()  {

    	// given
		ItemPoListRequestDto dto = new ItemPoListRequestDto();

		dto.setSearchUrWarehouseId("85");//용인물류
		dto.setSearchUrSupplierId("1");//풀무원식품
		dto.setSearchBaseDt("20210308");//기준일자

        // when
		Page<ItemPoListResultVo> getPoList = goodsItemPoListService.getPoList(dto);

		getPoList.stream().forEach(
                i -> log.info(" getPoList : {}",  i)
        );

    	//then
		assertTrue(CollectionUtils.isNotEmpty(getPoList));
    }

	@Test
    void 발주_내역_목록조회()  {

    	// given
		ItemPoListRequestDto dto = new ItemPoListRequestDto();

		dto.setSearchUrWarehouseId("85");//용인물류
		dto.setSearchUrSupplierId("1");//풀무원식품
		dto.setSearchBaseDt("2021-03-12");//기준일자
		dto.setSearchPoIfYn("Y");//기준일자

        // when
		Page<ItemPoListResultVo> getPoResultList = goodsItemPoListService.getPoList(dto);

		getPoResultList.forEach(
                i -> log.info(" getPoList : {}",  i)
        );

    	//then
		assertTrue(CollectionUtils.isNotEmpty(getPoResultList));
    }

	@Test
    void 발주_리스트_엑셀다운로드()  {

    	// given
		ItemPoListRequestDto dto = new ItemPoListRequestDto();

		dto.setSearchUrWarehouseId("85");//용인물류
		dto.setSearchUrSupplierId("2");//올가식품
		dto.setSearchBaseDt("20210308");//기준일자

        // when
		List<ItemPoListResultVo> downLoadList = goodsItemPoListService.getPoList(dto);

		downLoadList.stream().forEach(
                i -> log.info(" getPoList : {}",  i)
        );

    	//then
		assertTrue(CollectionUtils.isNotEmpty(downLoadList));
    }

	@Test
    void ERP_카테고리_조회()  {

    	// given
		ItemPoListRequestDto dto = new ItemPoListRequestDto();

		dto.setSearchUrWarehouseId("85");//용인물류
		dto.setSearchUrSupplierId("1");//풀식

        // when
		List<ItemPoListResultVo> getErpCtgryList = goodsItemPoListService.getErpCtgryList(dto);

		getErpCtgryList.stream().forEach(
                i -> log.info(" getErpCtgryList : {}",  i)
        );

    	//then
		assertTrue(CollectionUtils.isNotEmpty(getErpCtgryList));
    }

}
