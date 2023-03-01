package kr.co.pulmuone.v1.comm.mapper.goods.item;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.goods.item.dto.MasterItemListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.OrgaDiscountRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.MasterItemListVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.OrgaDiscountListVo;

@Mapper
public interface GoodsOrgaDisMapper {

	/**
     * @Desc 마스터 품목 리스트 검색 목록 조회
     * @param masterItemListRequestDto : 마스터 품목 검색 request dto
     * @return Page<MasterItemListVo> : 마스터 품목 검색 목록
     */
    Page<OrgaDiscountListVo> getOrgaDisList(OrgaDiscountRequestDto paramDto);

    /**
     * @Desc 마스터 품목 리스트 엑셀 다운로드 목록 조회
     * @param masterItemListRequestDto : 마스터 품목 검색 request dto
     * @return List<MasterItemListVo> : 마스터 품목 검색 목록
     */
    List<OrgaDiscountListVo> getOrgaDisListExcel(OrgaDiscountRequestDto paramDto);


}
