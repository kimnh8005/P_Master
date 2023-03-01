package kr.co.pulmuone.bos.display.search.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.display.dictionary.dto.CustomDictionarySearchDto;
import kr.co.pulmuone.v1.display.dictionary.dto.SynonymSearchRequestDto;
import kr.co.pulmuone.v1.display.dictionary.dto.vo.CustomDictionaryVo;
import kr.co.pulmuone.v1.display.dictionary.dto.vo.SynonymDictionaryVo;
import kr.co.pulmuone.v1.display.dictionary.service.CustomDictionaryBiz;
import kr.co.pulmuone.v1.display.dictionary.service.SynonymDictionaryBiz;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class SearchDictionaryService {

    @Autowired
    private CustomDictionaryBiz customDictionaryBiz;

    @Autowired
    private SynonymDictionaryBiz synonymDictionaryBiz;


    @Value("${elasticsearch.dicDataPath}")
    private String dicPath;

    @Value("${elasticsearch.dicDataBackupPath}")
    private String backupPath;


    public ApiResult<?> uploadCustomDictionary() {

        String fileName = "userdict_ko.txt";

        File file = makeDictionaryFile(fileName);

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));

            CustomDictionarySearchDto searchDto = CustomDictionarySearchDto.builder().useYn("Y").build();
            List<CustomDictionaryVo> dictionaryList = customDictionaryBiz.getCustomDictionary(searchDto);

            for (CustomDictionaryVo data : dictionaryList) {
                bufferedWriter.write(data.getCustomizeWord());
                bufferedWriter.newLine();
            }
            bufferedWriter.close();

            ZonedDateTime utcNow = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
            String dateTimeStr = utcNow.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            FileUtils.copyFile(FileUtils.getFile(dicPath + fileName), FileUtils.getFile(backupPath + fileName + "." + dateTimeStr));
        }
        catch (Exception e) {
            e.printStackTrace();
            return ApiResult.fail();
        }
        return ApiResult.success();
    }


    public ApiResult<?> uploadSynonymDictionary() {

        String fileName = "synonyms.txt";

        File file = makeDictionaryFile(fileName);

        SynonymSearchRequestDto searchRequestDto = new SynonymSearchRequestDto();
        searchRequestDto.setUseYn("Y");
        List<SynonymDictionaryVo> list = synonymDictionaryBiz.getSynonymDictionary(searchRequestDto);

        try {

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));

            for (SynonymDictionaryVo data : list) {
                bufferedWriter.write(data.getRepresentSynonym() + "," + data.getSynonym());
                bufferedWriter.newLine();
            }
            bufferedWriter.close();

            ZonedDateTime utcNow = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
            String dateTimeStr = utcNow.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            FileUtils.copyFile(FileUtils.getFile(dicPath + fileName), FileUtils.getFile(backupPath + fileName + "." + dateTimeStr));
        }
        catch (IOException e) {
            e.printStackTrace();
            return ApiResult.fail();
        }

        return ApiResult.success();
    }



    private File makeDictionaryFile(String fileName) {
        File file = new File(dicPath + fileName);
        log.info("test");
        File dir = new File(dicPath);
        File backupDir = new File(backupPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (!backupDir.exists()) {
            backupDir.mkdirs();
        }
        return file;
    }
}
