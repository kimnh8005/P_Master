package kr.co.pulmuone.v1.comm.mapper.display;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.display.contents.dto.LohasBannerRequestDto;
import kr.co.pulmuone.v1.display.contents.dto.vo.ContentsDetailVo;
import kr.co.pulmuone.v1.display.contents.dto.vo.InventoryInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DisplayContentsMapper {

    List<InventoryInfoVo> getInventoryInfoList(@Param("pageCd") String pageCd, @Param("deviceType") String deviceType) throws Exception;

    InventoryInfoVo getInventoryInfo(@Param("inventoryCd") String inventoryCd, @Param("deviceType") String deviceType) throws Exception;

    Long getInventoryId(@Param("inventoryCd") String inventoryCd, @Param("deviceType") String deviceType) throws Exception;

    Page<ContentsDetailVo> getContentsDetail(@Param("dpContsId") Long dpContsId, @Param("deviceType") String deviceType, @Param("contentsLevel") String contentsLevel) throws Exception;

    List<ContentsDetailVo> getContentsDetailByPageCd(@Param("pageCd") String pageCd, @Param("deviceType") String deviceType) throws Exception;

    List<ContentsDetailVo> getContentsDetailByInventoryCd(@Param("inventoryCd") String inventoryCd, @Param("deviceType") String deviceType) throws Exception;

    Page<ContentsDetailVo> getLohasBanner(LohasBannerRequestDto dto) throws Exception;

    boolean isDealGoods(@Param("inventoryCd") String inventoryCd, @Param("ilGoodsId") Long ilGoodsId) throws Exception;
}
