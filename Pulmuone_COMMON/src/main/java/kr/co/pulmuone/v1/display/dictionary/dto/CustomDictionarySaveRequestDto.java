package kr.co.pulmuone.v1.display.dictionary.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

@Getter
@Setter
@ToString
public class CustomDictionarySaveRequestDto extends BaseRequestDto {

    private String insertData;
    private String updateData;
    private String deleteData;

    private List<CustomDictionaryDto> insertList;
    private List<CustomDictionaryDto> updateList;
    private List<CustomDictionaryDto> deleteList;

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
        this.insertList = BindUtil.convertJsonArrayToDtoList(this.insertData, CustomDictionaryDto.class);
        this.updateList = BindUtil.convertJsonArrayToDtoList(this.updateData, CustomDictionaryDto.class);
        this.deleteList = BindUtil.convertJsonArrayToDtoList(this.deleteData, CustomDictionaryDto.class);
    }

}
