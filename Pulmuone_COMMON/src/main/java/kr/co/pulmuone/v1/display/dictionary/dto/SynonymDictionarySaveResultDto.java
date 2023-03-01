package kr.co.pulmuone.v1.display.dictionary.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SynonymDictionarySaveResultDto {

    private List<String> duplicateWordList = new ArrayList<>();

    private List<String> customWordList = new ArrayList<>();
}
