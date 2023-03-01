package kr.co.pulmuone.v1.comm.mapper.promotion.shoplive;

import kr.co.pulmuone.v1.promotion.shoplive.dto.vo.ShopliveGoodsVo;
import kr.co.pulmuone.v1.promotion.shoplive.dto.vo.ShopliveInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShopliveMapper {

    ShopliveInfoVo getShopliveInfo(@Param("evShopliveId") Long evShopliveId) throws Exception;

    /**
     * 동기화 할 상품 목록 조회 (현재 일자가 이벤트 시작일 7일 전, 이벤트 종류 후 7일)
     * @return
     * @throws Exception
     */
    List<ShopliveGoodsVo> getShopliveGoodsList() throws Exception;
}
