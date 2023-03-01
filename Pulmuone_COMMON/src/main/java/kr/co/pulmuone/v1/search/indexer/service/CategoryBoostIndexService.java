package kr.co.pulmuone.v1.search.indexer.service;

import kr.co.pulmuone.v1.comm.enums.Indices;
import kr.co.pulmuone.v1.comm.mapper.search.index.SearchIndexMapper;
import kr.co.pulmuone.v1.search.indexer.dto.CategoryBoostDocumentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryBoostIndexService extends IndexServiceTemplate {

    @Autowired
    private SearchIndexMapper searchIndexMapper;

    protected CategoryBoostIndexService() {
        super(Indices.CATEGORY_BOOST);
    }

    @Override
    protected List<CategoryBoostDocumentDto> getIndexTargetList() {
        List<CategoryBoostDocumentDto> categoryBoostList = searchIndexMapper.getCategoryBoostList();
        return categoryBoostList;
    }
}
