package co.jp.wever.graphql.domain.domainmodel.section;

import java.util.List;

import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.domain.domainmodel.user.UserId;

public class Section {


    private SectionTitle title;
    private SectionTexts texts;
    private SectionNumber number;
    private UserId authorId;

    public Section(
        SectionTitle title, SectionTexts texts, SectionNumber number, UserId authorId) {
        this.title = title;
        this.texts = texts;
        this.number = number;
        this.authorId = authorId;
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

    public boolean canUpdate(UserId userId, SectionNumber sectionNumber) {
        if (!authorId.same(userId)) {
            return false;
        }

        return number.isSame(sectionNumber);
    }

    public boolean canDelete(UserId userId) {
        return authorId.same(userId);
    }
}
