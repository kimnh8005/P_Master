package kr.co.pulmuone.v1.comm.mappers.batch.master.customer;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomerQnaBatchMapper {

    List<Long> getCustomerQna();

    void putCustomerQnaDelay(@Param("csQnaId") Long csQnaId);

}
