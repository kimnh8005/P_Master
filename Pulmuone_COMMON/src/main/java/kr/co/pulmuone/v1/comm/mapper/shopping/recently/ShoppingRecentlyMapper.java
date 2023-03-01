package kr.co.pulmuone.v1.comm.mapper.shopping.recently;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.shopping.recently.dto.CommonGetRecentlyViewListByUserRequestDto;
import kr.co.pulmuone.v1.shopping.recently.dto.vo.RecentlyViewVo;

@Mapper
public interface ShoppingRecentlyMapper
{
	HashMap<String, Object> getGoodsRecentlyView(@Param("urUserId") String urUserId, @Param("ilGoodsId") Long ilGoodsId) throws Exception;

	void addGoodsRecentlyView(@Param("urUserId") String urUserId, @Param("ilGoodsId") Long ilGoodsId) throws Exception;

	void putGoodsRecentlyViewLastViewDate(@Param("urUserId") String urUserId, @Param("ilGoodsId") Long ilGoodsId) throws Exception;

	List<Long> getGoodsRecentlyViewLimit(@Param("urUserId") String urUserId) throws Exception;

	RecentlyViewVo getGoodsRecentlyViewByUrPcidCd(@Param("urPcidCd") String urPcidCd, @Param("ilGoodsId") Long ilGoodsId) throws Exception;

	void putRecentlyViewLastViewDateByPcidCd(@Param("urPcidCd") String urPcidCd, @Param("ilGoodsId") Long ilGoodsId) throws Exception;

	void addRecentlyViewFromNonMember(@Param("urPcidCd") String urPcidCd, @Param("ilGoodsId") Long ilGoodsId) throws Exception;

	List<Long> getRecentlyViewLimitByPcidCd(@Param("urPcidCd") String urPcidCd) throws Exception;

	void putRecentlyViewUserId(@Param("urPcidCd") String urPcidCd, @Param("urUserId") Long urUserId) throws Exception;

	void putRecentlyViewNonMember(@Param("urPcidCd") String urPcidCd, @Param("urUserId") Long urUserId) throws Exception;

	List<Long> getRecentlyViewNonMemberOverlap(@Param("urPcidCd") String urPcidCd, @Param("urUserId") Long urUserId) throws Exception;

	Page<Long> getRecentlyViewListByUser(CommonGetRecentlyViewListByUserRequestDto dto) throws Exception;

    void delRecentlyViewByGoodsId(@Param("ilGoodsId")Long ilGoodsId, @Param("urUserId")Long urUserId) throws Exception;

    void delRecentlyViewByUserId(@Param("urUserId")Long urUserId) throws Exception;

    void delRecentlyViewLimitBySpRecentlyViewId(@Param("spRecentlyViewIds") List<Long> spRecentlyViewIds) throws Exception;
}
