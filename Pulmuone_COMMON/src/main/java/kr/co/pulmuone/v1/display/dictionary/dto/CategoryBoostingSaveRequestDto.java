package kr.co.pulmuone.v1.display.dictionary.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.display.dictionary.dto.vo.CategoryBoostingVo;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

@Getter
@Setter
public class CategoryBoostingSaveRequestDto extends BaseRequestDto {

    String insertData;
    String updateData;
    String deleteData;

    List<CategoryBoostingVo> insertList;
    List<CategoryBoostingVo> updateList;
    List<CategoryBoostingVo> deleteList;

    public boolean hasInsertData() {
        return CollectionUtils.isNotEmpty(insertList);
    }
    public boolean hasUpdateData() {
        return CollectionUtils.isNotEmpty(updateList);
    }
    public boolean hasDeleteData() {
        return CollectionUtils.isNotEmpty(deleteList);
    }

    public void convertDataList() throws Exception {
        this.insertList = BindUtil.convertJsonArrayToDtoList(this.insertData, CategoryBoostingVo.class);
        this.updateList = BindUtil.convertJsonArrayToDtoList(this.updateData, CategoryBoostingVo.class);
        this.deleteList = BindUtil.convertJsonArrayToDtoList(this.deleteData, CategoryBoostingVo.class);
    }
}
