package kr.co.pulmuone.v1.comm.mappers.batch.master.display;

import kr.co.pulmuone.v1.batch.display.contents.dto.vo.ContentsBatchVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DisplayContentsBatchMapper {

    List<ContentsBatchVo> getBatchTarget(String inventoryCode);

    Long getInventoryId(String inventoryCode);

    void addContents(ContentsBatchVo vo);

    void putContentsLevel1(Long dpContsId);
    void putContentsLevel3(Long dpContsId);

    void delContentsByInventoryId(@Param("dpInventoryId") Long dpInventoryId, @Param("contentsLevel") String contentsLevel);

}
