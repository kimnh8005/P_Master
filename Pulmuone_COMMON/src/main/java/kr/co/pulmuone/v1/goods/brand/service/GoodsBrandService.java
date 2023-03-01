package kr.co.pulmuone.v1.goods.brand.service;

import kr.co.pulmuone.v1.comm.mapper.goods.brand.GoodsBrandMapper;
import kr.co.pulmuone.v1.goods.brand.dto.vo.DpBrandListResultVo;
import kr.co.pulmuone.v1.goods.brand.dto.vo.UrBrandListResultVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    20200824   	 천혜현            최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsBrandService {

    private final GoodsBrandMapper goodsBrandMapper;

    /**
     * 표준 브랜드 리스트
     *
     * @return List<UrBrandListResultVo>
     * @throws Exception Exception
     */
    protected List<UrBrandListResultVo> getUrBrandList() throws Exception {
        return goodsBrandMapper.getUrBrandList();
    }

    /**
     * 전시 브랜드 리스트
     *
     * @return List<DpBrandListResultVo>
     * @throws Exception Exception
     */
    protected List<DpBrandListResultVo> getDpBrandList() throws Exception {
        return goodsBrandMapper.getDpBrandList();
    }

    protected String getDpBrandTitleById(Long dpBrandId) throws Exception {
        return goodsBrandMapper.getDpBrandTitleById(dpBrandId);
    }
}
