package kr.co.pulmuone.v1.goods.goods.service;

import kr.co.pulmuone.v1.comm.mapper.goods.goods.GoodsDetailImageMapper;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.etc.dto.SpecificsFieldRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.vo.GoodsDetailImageVo;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDetailImageDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDetailImageListRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDetailImageListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoodsDetailImageService {

    @Autowired
    private final GoodsDetailImageMapper goodsDetailImageMapper;

    /**
     * 상품 상세 이미지 다운로드 리스트 조회
     * @param goodsDetailImageListRequestDto
     * @return Page<GoodsDetailImageDto>
     */
    protected GoodsDetailImageListResponseDto getGoodsDetailImageList(GoodsDetailImageListRequestDto goodsDetailImageListRequestDto) {
        GoodsDetailImageListResponseDto goodsDetailImageListResponseDto = new GoodsDetailImageListResponseDto();

        ArrayList<String> ilItemCdArray = new ArrayList<String>();
        String codeStrFlag = "Y";
        if (!StringUtil.isEmpty(goodsDetailImageListRequestDto.getFindKeyword())) {

            // 화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
            String ilItemCodeListStr = goodsDetailImageListRequestDto.getFindKeyword().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");

            String regExp = "^[0-9]+$";
            String[] ilItemCodeListArray = ilItemCodeListStr.split(",+");
            for(int i = 0; i < ilItemCodeListArray.length; i++) {
                String ilItemCodeSearchVal = ilItemCodeListArray[i];
                if(ilItemCodeSearchVal.isEmpty()) {
                    continue;
                }
                ilItemCdArray.add(ilItemCodeSearchVal);
            }
        }

        goodsDetailImageListRequestDto.setFindKeywordList(ilItemCdArray); // 검색어
        goodsDetailImageListRequestDto.setFindKeywordStrFlag(codeStrFlag);

        List<GoodsDetailImageDto> goodsDetailImageList = goodsDetailImageMapper.getGoodsDetailImageList(goodsDetailImageListRequestDto);

        Long totalCnt = goodsDetailImageMapper.getGoodsDetailImageListCount(goodsDetailImageListRequestDto);
        goodsDetailImageListResponseDto.setRows(goodsDetailImageList);
        goodsDetailImageListResponseDto.setTotal(totalCnt);

        return goodsDetailImageListResponseDto;
    }

    /**
     * @Desc 상품고시정보 업데이트 상품코드 조회
     * @param specificsFieldRequestDto
     * @return List<GoodsDetailImageVo>
     */
    protected List<GoodsDetailImageVo> getSpecGoodsIdList(SpecificsFieldRequestDto specificsFieldRequestDto){
        return goodsDetailImageMapper.getSpecGoodsIdList(specificsFieldRequestDto);
    }

    /**
     * @Desc 상품상세정보 업데이트 상품코드 조회
     * @param specificsFieldRequestDto
     * @return List<GoodsDetailImageVo>
     */
    protected List<GoodsDetailImageVo> getDetailGoodsIdList(SpecificsFieldRequestDto specificsFieldRequestDto){
        return goodsDetailImageMapper.getDetailGoodsIdList(specificsFieldRequestDto);
    }

    /**
     * @Desc 품목 업데이트 후 상품코드 조회
     * @param ilItemCode
     * @return List<GoodsDetailImageVo>
     */
    protected List<GoodsDetailImageVo> getUpdateItemInfo(String ilItemCode){
        return goodsDetailImageMapper.getUpdateItemInfo(ilItemCode);
    }


    /**
     * @Desc 상품 업데이트 후 상품코드 조회
     * @param ilGoodsId
     * @return List<GoodsDetailImageVo>
     */
    protected List<GoodsDetailImageVo> getUpdateGoodsInfoForDetailImage(String ilGoodsId){
        return goodsDetailImageMapper.getUpdateGoodsInfoForDetailImage(ilGoodsId);
    }

    /**
     * @Desc 상품군명 중복 유무
     * @param goodsDetailImageVo
     * @return boolean
     */
    protected boolean getImageBatchYn(GoodsDetailImageVo goodsDetailImageVo) {
        return goodsDetailImageMapper.getImageBatchYn(goodsDetailImageVo);
    }

    /**
     * @Desc 상품 상세 이미지 관리 등록
     * @param goodsDetailImageVo
     * @return int
     * @throws Exception
     */
    protected int putUpdateGoodsIdInfoForDetailImage(GoodsDetailImageVo goodsDetailImageVo) {
        return goodsDetailImageMapper.putUpdateGoodsIdInfoForDetailImage(goodsDetailImageVo);
    }
}
