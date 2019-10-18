package co.jp.wever.graphql.domain.domainmodel.section;

import java.util.List;

import co.jp.wever.graphql.domain.domainmodel.user.User;

public class Section {


    private SectionTitle title;
    private SectionTexts texts;
    private SectionNumber number;
    private User author;

    public Section(
        SectionTitle title, SectionTexts texts, SectionNumber number, User author) {
        this.title = title;
        this.texts = texts;
        this.number = number;
        this.author = author;
    }

    public String getTitle() {
        return title.getValue();
    }

    public List<String> getTexts() {
        return texts.getValue();
    }

    public SectionNumber getSectionNumber() {
        return number;
    }

    public int getNumber() {
        return number.getValue();
    }

    public boolean canUpdate(User user, SectionNumber sectionNumber) {
        if (!author.isSame(user)) {
            return false;
        }

        return number.isSame(sectionNumber);
    }

    public boolean canDelete(User user) {
        return author.isSame(user);
    }
}
