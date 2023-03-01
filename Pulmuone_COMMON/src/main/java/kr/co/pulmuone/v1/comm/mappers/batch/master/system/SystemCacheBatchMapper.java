package kr.co.pulmuone.v1.comm.mappers.batch.master.system;

import kr.co.pulmuone.v1.batch.system.cache.dto.vo.SystemCacheVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SystemCacheBatchMapper {

    Integer getSystemCacheCount();

    List<SystemCacheVo> getSystemCache(@Param("page") int page, @Param("limit") int limit);

    List<SystemCacheVo> getSystemCacheInfo();

    void putSystemCache(@Param("stApiCacheId") Long stApiCacheId, @Param("cacheData") String cacheData);

}
