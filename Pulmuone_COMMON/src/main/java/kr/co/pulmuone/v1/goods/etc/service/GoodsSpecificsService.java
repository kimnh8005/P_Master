package kr.co.pulmuone.v1.goods.etc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.mapper.goods.etc.GoodsSpecificsMapper;
import kr.co.pulmuone.v1.goods.etc.dto.SpecificsFieldRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.SpecificsListRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.SpecificsListResponseDto;
import kr.co.pulmuone.v1.goods.etc.dto.vo.SpecificsFieldVo;
import kr.co.pulmuone.v1.goods.etc.dto.vo.SpecificsMasterFieldVo;
import kr.co.pulmuone.v1.goods.etc.dto.vo.SpecificsMasterVo;
import kr.co.pulmuone.v1.goods.item.dto.ItemSpecValueRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.MasterItemListVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.MasterItemVo;
import lombok.RequiredArgsConstructor;


/**
* <PRE>
* Forbiz Korea
* 상품정보고시관리
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 07. 17.               박영후          최초작성
*  1.0    2020. 10. 12.               손진구          NEW 변경
* =======================================================================
* </PRE>
*/
@Service
@RequiredArgsConstructor
public class GoodsSpecificsService {

    @Autowired
    private final GoodsSpecificsMapper goodsSpecificsMapper;

    /**
     * @Desc 상품군 목록 조회
     * @param specificsListRequestDto
     * @return List<SpecificsMasterVo>
     */
    protected List<SpecificsMasterVo> getSpecificsMasterList(SpecificsListRequestDto specificsListRequestDto){
        return goodsSpecificsMapper.getSpecificsMasterList(specificsListRequestDto);
    }

    /**
     * @Desc 상품군별 정보고시 항목 조회
     * @param specificsListRequestDto
     * @return List<SpecificsMasterFieldVo>
     */
    protected List<SpecificsMasterFieldVo> getSpecificsMasterFieldList(SpecificsListRequestDto specificsListRequestDto){
        return goodsSpecificsMapper.getSpecificsMasterFieldList(specificsListRequestDto);
    }

    /**
     * @Desc 상품정보제공고시항목 목록 조회
     * @param specificsFieldRequestDto
     * @return List<SpecificsFieldVo>
     */
    protected List<SpecificsFieldVo> getSpecificsFieldList(SpecificsFieldRequestDto specificsFieldRequestDto){
        return goodsSpecificsMapper.getSpecificsFieldList(specificsFieldRequestDto);
    }

    /**
     * @Desc 품목별 상품정보제공고시 값 사용유무
     * @param specificsFieldRequestDto
     * @return boolean
     */
    protected boolean getItemSpecificsValueUseYn(SpecificsFieldRequestDto specificsFieldRequestDto) {
        return goodsSpecificsMapper.getItemSpecificsValueUseYn(specificsFieldRequestDto);
    }

    /**
     * @Desc 상품정보제공고시 분류 항목 관계 삭제
     * @param specificsMasterFieldVo
     * @return int
     */
    protected int delSpecificsMasterField(SpecificsMasterFieldVo specificsMasterFieldVo) {
        return goodsSpecificsMapper.delSpecificsMasterField(specificsMasterFieldVo);
    }

    /**
     * @Desc 상품정보제공고시 분류 항목 관계 등록
     * @param specificsMasterFieldVo
     * @return int
     */
    protected int addSpecificsMasterField(SpecificsMasterFieldVo specificsMasterFieldVo) {
        return goodsSpecificsMapper.addSpecificsMasterField(specificsMasterFieldVo);
    }

    /**
     * @Desc 상품정보제공고시항목 삭제
     * @param specificsFieldVo
     * @return int
     */
    protected int delSpecificsField(SpecificsFieldVo specificsFieldVo) {
        return goodsSpecificsMapper.delSpecificsField(specificsFieldVo);
    }

    /**
     * @Desc 상품정보제공고시항목 등록
     * @param specificsFieldVo
     * @return int
     */
    protected int addSpecificsField(SpecificsFieldVo specificsFieldVo) {
        return goodsSpecificsMapper.addSpecificsField(specificsFieldVo);
    }

    /**
     * @Desc 상품정보제공고시항목 수정
     * @param specificsFieldVo
     * @return int
     */
    protected int putSpecificsField(SpecificsFieldVo specificsFieldVo) {
        return goodsSpecificsMapper.putSpecificsField(specificsFieldVo);
    }

    /**
     * @Desc 품목에 상품정보제공고시분류 적용 유무
     * @param specificsListRequestDto
     * @return boolean
     */
    protected boolean getItemSpecificsMasterUseYn(SpecificsListRequestDto specificsListRequestDto) {
        return goodsSpecificsMapper.getItemSpecificsMasterUseYn(specificsListRequestDto);
    }

    /**
     * @Desc 상품정보제공고시분류 삭제
     * @param specificsMasterVo
     * @return int
     */
    protected int delSpecificsMaster(SpecificsMasterVo specificsMasterVo) {
        return goodsSpecificsMapper.delSpecificsMaster(specificsMasterVo);
    }

    /**
     * @Desc 상품정보제공고시분류 등록
     * @param specificsMasterVo
     * @return int
     */
    protected int addSpecificsMaster(SpecificsMasterVo specificsMasterVo) {
        return goodsSpecificsMapper.addSpecificsMaster(specificsMasterVo);
    }

    /**
     * @Desc 상품정보제공고시분류 수정
     * @param specificsMasterVo
     * @return int
     */
    protected int putSpecificsMaster(SpecificsMasterVo specificsMasterVo) {
        return goodsSpecificsMapper.putSpecificsMaster(specificsMasterVo);
    }

    /**
     * @Desc 상품군명 중복 유무
     * @param specificsListRequestDto
     * @return boolean
     */
    protected boolean getSpecificsMasterNameDuplicateYn(SpecificsListRequestDto specificsListRequestDto) {
        return goodsSpecificsMapper.getSpecificsMasterNameDuplicateYn(specificsListRequestDto);
    }

    /**
     * @Desc 정보고시항목명 중복 유무
     * @param specificsFieldRequestDto
     * @return boolean
     */
    protected boolean getSpecificsFieldNameDuplicateYn(SpecificsFieldRequestDto specificsFieldRequestDto) {
        return goodsSpecificsMapper.getSpecificsFieldNameDuplicateYn(specificsFieldRequestDto);
    }

    /**
     * @Desc 품목 상품정보고 조회
     * @param specificsFieldRequestDto
     * @return boolean
     */
    protected List<MasterItemVo> getItemSpecificsList(String ilSpecMasterId){
        return goodsSpecificsMapper.getItemSpecificsList(ilSpecMasterId);
    }

    /**
     * @Desc 품목 상품정보고시 업데이트
     * @param specificsMasterVo
     * @return int
     */
    protected int putItemSpecificsValue(ItemSpecValueRequestDto specificsMasterVo) {
        return goodsSpecificsMapper.putItemSpecificsValue(specificsMasterVo);
    }




}
