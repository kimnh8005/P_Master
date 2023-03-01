package kr.co.pulmuone.bos.goods.specifics;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.goods.etc.dto.SpecificsFieldRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.SpecificsListRequestDto;
import kr.co.pulmuone.v1.goods.etc.service.GoodsSpecificsBiz;
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
@RestController
@RequiredArgsConstructor
public class GoodsSpecificsController {
    private final GoodsSpecificsBiz goodsSpecificsBiz;

    @ApiOperation(value = "상품군 목록 조회")
    @PostMapping(value = "/admin/goods/specifics/getSpecificsMasterList")
    public ApiResult<?> getSpecificsMasterList(SpecificsListRequestDto specificsListRequestDto) {

        return goodsSpecificsBiz.getSpecificsMasterList(specificsListRequestDto);
    }

    @ApiOperation(value = "상품군별 정보고시 항목 조회")
    @PostMapping(value = "/admin/goods/specifics/getSpecificsMasterFieldList")
    public ApiResult<?> getSpecificsMasterFieldList(SpecificsListRequestDto specificsListRequestDto) {

        return goodsSpecificsBiz.getSpecificsMasterFieldList(specificsListRequestDto);
    }

    @ApiOperation(value = "상품정보제공고시항목 목록 조회")
    @PostMapping(value = "/admin/goods/specifics/getSpecificsFieldList")
    public ApiResult<?> getSpecificsFieldList(SpecificsFieldRequestDto specificsFieldRequestDto){

        return goodsSpecificsBiz.getSpecificsFieldList(specificsFieldRequestDto);
    }

    @ApiOperation(value = "품목별 상품정보제공고시 값 사용유무")
    @PostMapping(value = "/admin/goods/specifics/getItemSpecificsValueUseYn")
    public ApiResult<?> getItemSpecificsValueUseYn(SpecificsFieldRequestDto specificsFieldRequestDto){

        return goodsSpecificsBiz.getItemSpecificsValueUseYn(specificsFieldRequestDto);
    }

    @ApiOperation(value = "상품정보제공고시항목 삭제")
    @PostMapping(value = "/admin/goods/specifics/delSpecificsField")
    public ApiResult<?> delSpecificsField(SpecificsFieldRequestDto specificsFieldRequestDto) throws Exception{

        return goodsSpecificsBiz.delSpecificsField(specificsFieldRequestDto);
    }

    @ApiOperation(value = "상품정보제공고시항목 저장")
    @PostMapping(value = "/admin/goods/specifics/putSpecificsField")
    public ApiResult<?> putSpecificsField(@RequestBody SpecificsFieldRequestDto specificsFieldRequestDto) throws Exception{

        return goodsSpecificsBiz.putSpecificsField(specificsFieldRequestDto);
    }

    @ApiOperation(value = "품목에 상품정보제공고시분류 적용 유무")
    @PostMapping(value = "/admin/goods/specifics/getItemSpecificsMasterUseYn")
    public ApiResult<?> getItemSpecificsMasterUseYn(SpecificsListRequestDto specificsListRequestDto) {

        return goodsSpecificsBiz.getItemSpecificsMasterUseYn(specificsListRequestDto);
    }

    @ApiOperation(value = "상품정보제공고시분류 삭제")
    @PostMapping(value = "/admin/goods/specifics/delSpecificsMaster")
    public ApiResult<?> delSpecificsMaster(SpecificsListRequestDto specificsListRequestDto) throws Exception{

        return goodsSpecificsBiz.delSpecificsMaster(specificsListRequestDto);
    }

    @ApiOperation(value = "상품정보제공고시 저장")
    @PostMapping(value = "/admin/goods/specifics/putSpecificsMaster")
    public ApiResult<?> putSpecificsMaster(@RequestBody SpecificsListRequestDto specificsListRequestDto) throws Exception{

        return goodsSpecificsBiz.putSpecificsMaster(specificsListRequestDto);
    }

    @ApiOperation(value = "상품군명 중복 유무")
    @PostMapping(value = "/admin/goods/specifics/getSpecificsMasterNameDuplicateYn")
    public ApiResult<?> getSpecificsMasterNameDuplicateYn(SpecificsListRequestDto specificsListRequestDto){

        return goodsSpecificsBiz.getSpecificsMasterNameDuplicateYn(specificsListRequestDto);
    }

    @ApiOperation(value = "정보고시항목명 중복 유무")
    @PostMapping(value = "/admin/goods/specifics/getSpecificsFieldNameDuplicateYn")
    public ApiResult<?> getSpecificsFieldNameDuplicateYn(SpecificsFieldRequestDto specificsFieldRequestDto){

        return goodsSpecificsBiz.getSpecificsFieldNameDuplicateYn(specificsFieldRequestDto);
    }
}