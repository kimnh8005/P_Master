package kr.co.pulmuone.v1.comm.mappers.slaveCjFront.sample;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface CjFrontSampleMapper {

	String get();
}
