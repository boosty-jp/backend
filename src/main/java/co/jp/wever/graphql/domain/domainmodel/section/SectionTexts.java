package co.jp.wever.graphql.domain.domainmodel.section;

import java.util.List;
import java.util.stream.Collectors;

public class SectionTexts {
    private final static int MIN_TEXTS_BLOCK_COUNT = 1;
    private final static int MAX_TEXTS_BLOCK_COUNT = 2;
    private List<SectionText> value;

    private SectionTexts(List<SectionText> value) {
        this.value = value;
    }

    public static SectionTexts of(List<String> texts) {

        if (texts.size() < MIN_TEXTS_BLOCK_COUNT) {
            throw new IllegalArgumentException();
        }

        if (texts.size() > MAX_TEXTS_BLOCK_COUNT) {
            throw new IllegalArgumentException();
        }

        List<SectionText> sectionTexts = texts.stream().map(t -> SectionText.of(t)).collect(Collectors.toList());
        return new SectionTexts(sectionTexts);
    }

    public List<String> getValue() {
        return this.value.stream().map(t -> t.getValue()).collect(Collectors.toList());
    }
}
