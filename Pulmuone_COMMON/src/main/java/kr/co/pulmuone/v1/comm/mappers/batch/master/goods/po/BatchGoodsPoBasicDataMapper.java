package kr.co.pulmuone.v1.comm.mappers.batch.master.goods.po;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BatchGoodsPoBasicDataMapper {

    int addGoodsPoBasicData(String baseDt);
}
