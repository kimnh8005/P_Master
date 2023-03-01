package kr.co.pulmuone.v1.comm.mappers.slavePov.sample;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface PovSampleMapper {

	String get();
}
