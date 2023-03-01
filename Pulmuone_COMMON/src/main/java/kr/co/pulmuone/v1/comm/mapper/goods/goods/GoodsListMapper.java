package kr.co.pulmuone.v1.comm.mapper.goods.goods;

import java.util.List;

import kr.co.pulmuone.v1.goods.price.dto.vo.GoodsPriceVo;
import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.approval.auth.dto.ApprovalGoodsRequestDto;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalStatusVo;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsListRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsApprovalResultVo;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsVo;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GoodsListMapper {

    /**
     * @Desc 상품 목록 조회
     * @param goodsRequestDto
     * @return Page<GoodsVo>
     */
    Page<GoodsVo> getGoodsList(GoodsListRequestDto goodsRequestDto);

    /**
     * @Desc 상품목록 판매상태 변경
     * @param goodsVo
     * @return int
     */
    int putGoodsSaleStatusChange(GoodsVo goodsVo);

    /**
     * @Desc 상품 판매상태 조회
     * @param goodsVo
     * @return GoodsRegistRequestDto
     */
    GoodsRegistRequestDto getGoodsSaleStatusInfo(GoodsVo goodsVo);

    List<GoodsVo> getGoodsListExcel(GoodsListRequestDto goodsRequestDto);

    /*
	 * 승인 관리 추가
	 */
    Page<GoodsApprovalResultVo> getApprovalGoodsRegistList(ApprovalGoodsRequestDto approvalGoodsRequestDto);

    Page<GoodsApprovalResultVo> getApprovalGoodsDiscountList(ApprovalGoodsRequestDto approvalGoodsRequestDto);

    GoodsApprovalResultVo getApprovalGoodsDetailClient(@Param("ilGoodsApprId") String ilGoodsApprId);

    int putCancelRequestApprovalGoodsRegist(ApprovalStatusVo approvalVo);

	int putApprovalProcessGoodsRegist(ApprovalStatusVo approvalVo);

	int addGoodsRegistStatusHistory(ApprovalStatusVo history);

	int putCancelRequestApprovalGoodsDiscount(ApprovalStatusVo approvalVo);

	int putDisposalApprovalGoodsDiscount(ApprovalStatusVo approvalVo);

	int putApprovalProcessGoodsDiscount(ApprovalStatusVo approvalVo);

	int addGoodsDiscountStatusHistory(ApprovalStatusVo history);

	GoodsPriceVo getCurrentGoodsPriceByGoodsDiscountApprId(@Param("goodsDiscountApprId") String goodsDiscountApprId);



}
