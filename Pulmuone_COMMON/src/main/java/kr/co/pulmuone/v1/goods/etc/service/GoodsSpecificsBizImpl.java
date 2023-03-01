package kr.co.pulmuone.v1.goods.etc.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.goods.etc.dto.SpecificsFieldRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.SpecificsFieldResponseDto;
import kr.co.pulmuone.v1.goods.etc.dto.SpecificsListRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.SpecificsListResponseDto;
import kr.co.pulmuone.v1.goods.etc.dto.vo.SpecificsFieldVo;
import kr.co.pulmuone.v1.goods.etc.dto.vo.SpecificsMasterFieldVo;
import kr.co.pulmuone.v1.goods.etc.dto.vo.SpecificsMasterVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsDetailImageBiz;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemSpecificsValueVo;
import kr.co.pulmuone.v1.goods.item.service.GoodsItemSpecificsValueBiz;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


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
public class GoodsSpecificsBizImpl  implements GoodsSpecificsBiz {

    @Autowired
    GoodsSpecificsService goodsSpecificsService;

    @Autowired
    GoodsItemSpecificsValueBiz goodsItemSpecificsValueBiz;

    @Autowired
    GoodsDetailImageBiz goodsDetailImageBiz;

    /**
     * @Desc 상품군 목록 조회
     * @param specificsListRequestDto
     * @return ApiResult
     */
    @Override
    public ApiResult<?> getSpecificsMasterList(SpecificsListRequestDto specificsListRequestDto){
        SpecificsListResponseDto specificsListResponseDto = new SpecificsListResponseDto();

        List<SpecificsMasterVo> specificsMasterList = goodsSpecificsService.getSpecificsMasterList(specificsListRequestDto);
        specificsListResponseDto.setSpecificsMasterList(specificsMasterList);

        return ApiResult.success(specificsListResponseDto);
    }

    /**
     * @Desc 상품군별 정보고시 항목 조회
     * @param specificsListRequestDto
     * @return ApiResult
     */
    @Override
    public ApiResult<?> getSpecificsMasterFieldList(SpecificsListRequestDto specificsListRequestDto){
        SpecificsListResponseDto specificsListResponseDto = new SpecificsListResponseDto();

        List<SpecificsMasterFieldVo> specificsMasterFieldList = goodsSpecificsService.getSpecificsMasterFieldList(specificsListRequestDto);
        specificsListResponseDto.setSpecificsMasterFieldList(specificsMasterFieldList);

        return ApiResult.success(specificsListResponseDto);
    }

    /**
     * @Desc 상품정보제공고시항목 목록 조회
     * @param specificsFieldRequestDto
     * @return ApiResult
     */
    @Override
    public ApiResult<?> getSpecificsFieldList(SpecificsFieldRequestDto specificsFieldRequestDto){
        SpecificsFieldResponseDto specificsFieldResponseDto = new SpecificsFieldResponseDto();

        List<SpecificsFieldVo> specificsFieldList = goodsSpecificsService.getSpecificsFieldList(specificsFieldRequestDto);

        if( StringUtils.isNotEmpty(specificsFieldRequestDto.getSpecificsFieldIds()) ) {
            List<String> specificsFieldIdList = Arrays.asList(specificsFieldRequestDto.getSpecificsFieldIds().split(","));

            specificsFieldList = specificsFieldList.stream()
                                                   .sorted(Comparator.comparing(SpecificsFieldVo::getSpecificsFieldCode,
                                                                                Comparator.nullsFirst(Comparator.naturalOrder()))
                                                                     .thenComparing(t -> specificsFieldIdList.indexOf(String.valueOf(t.getSpecificsFieldId())))
                                                                     .thenComparing(SpecificsFieldVo::getCreateDate).reversed())
                                                   .collect(Collectors.toList());
        }

        specificsFieldResponseDto.setSpecificsFieldList(specificsFieldList);

        return ApiResult.success(specificsFieldResponseDto);
    }

    /**
     * @Desc 품목별 상품정보제공고시 값 사용유무
     * @param specificsFieldRequestDto
     * @return ApiResult
     */
    @Override
    public ApiResult<?> getItemSpecificsValueUseYn(SpecificsFieldRequestDto specificsFieldRequestDto){
        SpecificsFieldResponseDto specificsFieldResponseDto = new SpecificsFieldResponseDto();

        boolean itemSpecificsValueUseYn = goodsSpecificsService.getItemSpecificsValueUseYn(specificsFieldRequestDto);
        specificsFieldResponseDto.setItemSpecificsValueUseYn(itemSpecificsValueUseYn);

        return ApiResult.success(specificsFieldResponseDto);
    }

    /**
     * @Desc 상품정보제공고시항목 삭제
     * @param specificsFieldRequestDto
     * @return ApiResult
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> delSpecificsField(SpecificsFieldRequestDto specificsFieldRequestDto) throws Exception{

        // 품목별 상품정보제공고시 값 삭제
        ItemSpecificsValueVo itemSpecificsValueVo = new ItemSpecificsValueVo();
        itemSpecificsValueVo.setSpecificsFieldId(specificsFieldRequestDto.getSpecificsFieldId());
        goodsItemSpecificsValueBiz.delItemSpecificsValue(itemSpecificsValueVo);

        // 상품정보제공고시 분류 항목 관계 삭제
        SpecificsMasterFieldVo specificsMasterFieldVo = new SpecificsMasterFieldVo();
        specificsMasterFieldVo.setSpecificsFieldId(specificsFieldRequestDto.getSpecificsFieldId());
        goodsSpecificsService.delSpecificsMasterField(specificsMasterFieldVo);

        // 상품정보제공고시항목 삭제
        SpecificsFieldVo specificsFieldVo = new SpecificsFieldVo();
        specificsFieldVo.setSpecificsFieldId(specificsFieldRequestDto.getSpecificsFieldId());
        goodsSpecificsService.delSpecificsField(specificsFieldVo);

        return ApiResult.success();
    }

    /**
     * @Desc 상품정보제공고시항목 저장
     * @param specificsFieldRequestDto
     * @return ApiResult
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> putSpecificsField(SpecificsFieldRequestDto specificsFieldRequestDto) throws Exception{
        SpecificsFieldVo specificsFieldVo = new SpecificsFieldVo();
        specificsFieldVo.setSpecificsFieldName(specificsFieldRequestDto.getSpecificsFieldName());
        specificsFieldVo.setBasicValue(specificsFieldRequestDto.getBasicValue());
        specificsFieldVo.setSpecificsDesc(specificsFieldRequestDto.getSpecificsDesc());
        specificsFieldVo.setCreateId(specificsFieldRequestDto.getUserVo().getUserId());

        if( specificsFieldRequestDto.getSpecificsFieldId() == null ) {

            goodsSpecificsService.addSpecificsField(specificsFieldVo);
        }else {

            specificsFieldVo.setSpecificsFieldId(specificsFieldRequestDto.getSpecificsFieldId());
            goodsSpecificsService.putSpecificsField(specificsFieldVo);
        }

        // 상품상세 이미지 관리 테이블 Insert : 업데이트 된 상품고시정보 등록
        goodsDetailImageBiz.putUpdateGoodsIdInfoForDetailImage(specificsFieldRequestDto);

        return ApiResult.success();
    }

    /**
     * @Desc 품목에 상품정보제공고시분류 적용 유무
     * @param specificsListRequestDto
     * @return ApiResult
     */
    @Override
    public ApiResult<?> getItemSpecificsMasterUseYn(SpecificsListRequestDto specificsListRequestDto){
        SpecificsListResponseDto specificsListResponseDto = new SpecificsListResponseDto();

        boolean itemSpecificsMasterUseYn = goodsSpecificsService.getItemSpecificsMasterUseYn(specificsListRequestDto);
        specificsListResponseDto.setItemSpecificsMasterUseYn(itemSpecificsMasterUseYn);

        return ApiResult.success(specificsListResponseDto);
    }

    /**
     * @Desc 상품정보제공고시분류 삭제
     * @param specificsListRequestDto
     * @return ApiResult
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> delSpecificsMaster(SpecificsListRequestDto specificsListRequestDto) throws Exception{

        // 상품정보제공고시 분류 항목 관계 삭제
        SpecificsMasterFieldVo specificsMasterFieldVo = new SpecificsMasterFieldVo();
        specificsMasterFieldVo.setSpecificsMasterId(specificsListRequestDto.getSpecificsMasterId());
        goodsSpecificsService.delSpecificsMasterField(specificsMasterFieldVo);

        // 상품정보제공고시분류 삭제
        SpecificsMasterVo specificsMasterVo = new SpecificsMasterVo();
        specificsMasterVo.setSpecificsMasterId(specificsListRequestDto.getSpecificsMasterId());
        goodsSpecificsService.delSpecificsMaster(specificsMasterVo);

        return ApiResult.success();
    }

    /**
     * @Desc 상품정보제공고시 저장
     * @param specificsListRequestDto
     * @return ApiResult
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> putSpecificsMaster(SpecificsListRequestDto specificsListRequestDto) throws Exception{

        // 삭제 할 품목별 상품정보제공고시 값 리스트 체크하여 삭제
        if( CollectionUtils.isNotEmpty(specificsListRequestDto.getDelItemSpecificsValueList()) ) {
            for( SpecificsMasterFieldVo delSpecificsMasterFieldVo : specificsListRequestDto.getDelItemSpecificsValueList() ){
                // 품목별 상품정보제공고시 값 삭제
                ItemSpecificsValueVo itemSpecificsValueVo = new ItemSpecificsValueVo();
                itemSpecificsValueVo.setSpecificsMasterId(delSpecificsMasterFieldVo.getSpecificsMasterId());
                itemSpecificsValueVo.setSpecificsFieldId(delSpecificsMasterFieldVo.getSpecificsFieldId());
//                goodsItemSpecificsValueBiz.delItemSpecificsValue(itemSpecificsValueVo);
            }
        }

        // 상품정보제공고시분류 초기값 셋팅
        SpecificsMasterVo specificsMasterVo = new SpecificsMasterVo();
        specificsMasterVo.setSpecificsMasterName(specificsListRequestDto.getSpecificsMasterName());
        specificsMasterVo.setSort(specificsListRequestDto.getSort());
        specificsMasterVo.setUseYn(specificsListRequestDto.getUseYn());
        specificsMasterVo.setCreateId(specificsListRequestDto.getUserVo().getUserId());

        if( specificsListRequestDto.getSpecificsMasterId() == null ) {

            // 상품정보제공고시분류 등록
            goodsSpecificsService.addSpecificsMaster(specificsMasterVo);
        }else {

            // 상품정보제공고시 분류 항목 관계 삭제
            SpecificsMasterFieldVo specificsMasterFieldVo = new SpecificsMasterFieldVo();
            specificsMasterFieldVo.setSpecificsMasterId(specificsListRequestDto.getSpecificsMasterId());
            goodsSpecificsService.delSpecificsMasterField(specificsMasterFieldVo);

            // 상품정보제공고시분류 수정
            specificsMasterVo.setSpecificsMasterId(specificsListRequestDto.getSpecificsMasterId());
            goodsSpecificsService.putSpecificsMaster(specificsMasterVo);
        }

        // 상품정보제공고시 분류 항목 관계 등록
        int specificsMasterFieldSort = 1;
        for( SpecificsMasterFieldVo specificsMasterFieldVo : specificsListRequestDto.getSpecificsMasterFieldList() ) {
            specificsMasterFieldVo.setSpecificsMasterId(specificsMasterVo.getSpecificsMasterId());
            specificsMasterFieldVo.setSort(specificsMasterFieldSort);
            specificsMasterFieldVo.setCreateId(specificsListRequestDto.getUserVo().getUserId());
            goodsSpecificsService.addSpecificsMasterField(specificsMasterFieldVo);

            specificsMasterFieldSort++;
        }
        SpecificsFieldRequestDto specificsFieldRequestDto = new SpecificsFieldRequestDto();
        specificsFieldRequestDto.setSpecificsMasterId(specificsListRequestDto.getSpecificsMasterId());

        // 상품상세 이미지 관리 테이블 Insert : 업데이트 된 상품고시정보 등록
        goodsDetailImageBiz.putUpdateGoodsIdInfoForDetailImage(specificsFieldRequestDto);


        // 폼목 - IL_ITEM_SPEC_VALUE 업데이트
//        String specificsMasterId = String.valueOf(specificsListRequestDto.getSpecificsMasterId());
//
//        List<MasterItemVo> itemSpecificsList = goodsSpecificsService.getItemSpecificsList(specificsMasterId);
//
//        UserVo userVo = SessionUtil.getBosUserVO();
//		long userId = Long.valueOf(userVo.getUserId());
//
//        for(MasterItemVo masterItemVo : itemSpecificsList) {
//        	ItemSpecValueRequestDto itemSpecValueRequestDto = new ItemSpecValueRequestDto();
//        	itemSpecValueRequestDto.setIlItemCode(masterItemVo.getIlItemCode());
//
//            for( SpecificsMasterFieldVo specificsMasterFieldVo : specificsListRequestDto.getSpecificsMasterFieldList() ) {
//            	String specFieldId = String.valueOf(specificsMasterFieldVo.getSpecificsFieldId());
//            	itemSpecValueRequestDto.setIlSpecFieldId(Integer.valueOf(specFieldId));
//            	itemSpecValueRequestDto.setSpecFieldValue(specificsMasterFieldVo.getBasicValue());
//            	itemSpecValueRequestDto.setCreateId(userId);
//            	itemSpecValueRequestDto.setModifyid(userId);
//
//            	goodsSpecificsService.putItemSpecificsValue(itemSpecValueRequestDto);
//            }
//        }


        return ApiResult.success();
    }

    /**
     * @Desc 상품군명 중복 유무
     * @param specificsListRequestDto
     * @return ApiResult
     */
    @Override
    public ApiResult<?> getSpecificsMasterNameDuplicateYn(SpecificsListRequestDto specificsListRequestDto){
        SpecificsListResponseDto specificsListResponseDto = new SpecificsListResponseDto();

        boolean specificsMasterNameDuplicateYn = goodsSpecificsService.getSpecificsMasterNameDuplicateYn(specificsListRequestDto);
        specificsListResponseDto.setSpecificsMasterNameDuplicateYn(specificsMasterNameDuplicateYn);

        return ApiResult.success(specificsListResponseDto);
    }

    /**
     * @Desc 정보고시항목명 중복 유무
     * @param specificsFieldRequestDto
     * @return ApiResult
     */
    @Override
    public ApiResult<?> getSpecificsFieldNameDuplicateYn(SpecificsFieldRequestDto specificsFieldRequestDto){
        SpecificsFieldResponseDto specificsFieldResponseDto = new SpecificsFieldResponseDto();

        boolean specificsFieldNameDuplicateYn = goodsSpecificsService.getSpecificsFieldNameDuplicateYn(specificsFieldRequestDto);
        specificsFieldResponseDto.setSpecificsFieldNameDuplicateYn(specificsFieldNameDuplicateYn);

        return ApiResult.success(specificsFieldResponseDto);
    }
}
