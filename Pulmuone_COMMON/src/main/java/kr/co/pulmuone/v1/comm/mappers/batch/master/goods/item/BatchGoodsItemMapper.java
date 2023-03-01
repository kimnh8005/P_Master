package kr.co.pulmuone.v1.comm.mappers.batch.master.goods.item;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.goods.item.dto.vo.MasterItemVo;

@Mapper
public interface BatchGoodsItemMapper {

    /**
     * @Desc ERP 연동 품목의 품목정보 수정
     * @param MasterItemVo : 마스터 품목 Vo
     * @return int
     */
    public int modifyErpItem(MasterItemVo masterItemVo);

    public MasterItemVo getItemByItemCd(@Param("ilItemCode")String ilItemCode);

    public String getPoTpIdByErpPoTp(@Param("erpPoTp")String erpPoTp);

    public int updatePoInfo(@Param("ilItemCode")String ilItemCode, @Param("ilPoTpId")String ilPoTpId, @Param("preOrderYn")String preOrderYn, @Param("preOrderTp")String preOrderTp);

}
