package co.jp.wever.graphql.domain.domainmodel.section;

public class UpdateSection {
    private SectionId id;
    private SectionTitle title;
    private SectionText text;
    private SectionNumber number;


    private UpdateSection(
        SectionId id, SectionTitle title, SectionText text, SectionNumber number) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.number = number;
    }

    public static UpdateSection of(String id, String title, String text, int number) {
        SectionId sectionId = SectionId.of(id);
        SectionTitle sectionTitle = SectionTitle.of(title);
        SectionText sectionText = SectionText.of(text);
        SectionNumber sectionNumber = SectionNumber.of(number);

        return new UpdateSection(sectionId, sectionTitle, sectionText, sectionNumber);
    }

    public String getId() {
        return id.getValue();
    }

    public String getTitle() {
        return title.getValue();
    }

    public String getText() {
        return text.getValue();
    }

    public int getNumber() {
        return number.getValue();
    }

}
