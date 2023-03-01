package kr.co.pulmuone.v1.comm.mapper.goods.etc;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.goods.etc.dto.SpecificsFieldRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.SpecificsListRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.SpecificsListResponseDto;
import kr.co.pulmuone.v1.goods.etc.dto.vo.SpecificsFieldVo;
import kr.co.pulmuone.v1.goods.etc.dto.vo.SpecificsMasterFieldVo;
import kr.co.pulmuone.v1.goods.etc.dto.vo.SpecificsMasterVo;
import kr.co.pulmuone.v1.goods.item.dto.ItemSpecValueRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.MasterItemVo;

@Mapper
public interface GoodsSpecificsMapper {

    /**
     * @Desc 상품군 목록 조회
     * @param specificsListRequestDto
     * @return List<SpecificsMasterVo>
     */
    List<SpecificsMasterVo> getSpecificsMasterList(SpecificsListRequestDto specificsListRequestDto);

    /**
     * @Desc 상품군별 정보고시 항목 조회
     * @param specificsListRequestDto
     * @return List<SpecificsMasterFieldVo>
     */
    List<SpecificsMasterFieldVo> getSpecificsMasterFieldList(SpecificsListRequestDto specificsListRequestDto);

    /**
     * @Desc 상품정보제공고시항목 목록 조회
     * @param specificsFieldRequestDto
     * @return List<SpecificsFieldVo>
     */
    List<SpecificsFieldVo> getSpecificsFieldList(SpecificsFieldRequestDto specificsFieldRequestDto);

    /**
     * @Desc 품목별 상품정보제공고시 값 사용유무
     * @param specificsFieldRequestDto
     * @return boolean
     */
    boolean getItemSpecificsValueUseYn(SpecificsFieldRequestDto specificsFieldRequestDto);

    /**
     * @Desc 상품정보제공고시 분류 항목 관계 삭제
     * @param specificsMasterFieldVo
     * @return int
     */
    int delSpecificsMasterField(SpecificsMasterFieldVo specificsMasterFieldVo);

    /**
     * @Desc 상품정보제공고시 분류 항목 관계 등록
     * @param specificsMasterFieldVo
     * @return int
     */
    int addSpecificsMasterField(SpecificsMasterFieldVo specificsMasterFieldVo);

    /**
     * @Desc 상품정보제공고시항목 삭제
     * @param specificsFieldRequestDto
     * @return int
     */
    int delSpecificsField(SpecificsFieldVo specificsFieldVo);

    /**
     * @Desc 상품정보제공고시항목 등록
     * @param specificsFieldVo
     * @return int
     */
    int addSpecificsField(SpecificsFieldVo specificsFieldVo);

    /**
     * @Desc 상품정보제공고시항목 수정
     * @param specificsFieldVo
     * @return int
     */
    int putSpecificsField(SpecificsFieldVo specificsFieldVo);

    /**
     * @Desc 품목에 상품정보제공고시분류 적용 유무
     * @param specificsListRequestDto
     * @return boolean
     */
    boolean getItemSpecificsMasterUseYn(SpecificsListRequestDto specificsListRequestDto);

    /**
     * @Desc 상품정보제공고시분류 삭제
     * @param specificsMasterVo
     * @return int
     */
    int delSpecificsMaster(SpecificsMasterVo specificsMasterVo);

    /**
     * @Desc 상품정보제공고시분류 등록
     * @param specificsMasterVo
     * @return int
     */
    int addSpecificsMaster(SpecificsMasterVo specificsMasterVo);

    /**
     * @Desc 상품정보제공고시분류 수정
     * @param specificsMasterVo
     * @return int
     */
    int putSpecificsMaster(SpecificsMasterVo specificsMasterVo);

    /**
     * @Desc 상품군명 중복 유무
     * @param specificsListRequestDto
     * @return boolean
     */
    boolean getSpecificsMasterNameDuplicateYn(SpecificsListRequestDto specificsListRequestDto);

    /**
     * @Desc 정보고시항목명 중복 유무
     * @param specificsFieldRequestDto
     * @return boolean
     */
    boolean getSpecificsFieldNameDuplicateYn(SpecificsFieldRequestDto specificsFieldRequestDto);

    /**
     * @Desc 상품군 목록 조회
     * @param specificsListRequestDto
     * @return List<SpecificsMasterVo>
     */
    List<MasterItemVo> getItemSpecificsList(@Param("ilSpecMasterId")String ilSpecMasterId);

    /**
     * @Desc 상품정보제공고시분류 수정
     * @param specificsMasterVo
     * @return int
     */
    int putItemSpecificsValue(ItemSpecValueRequestDto specificsMasterVo);




}
