package jp.boosty.graphql.domain.factory;

import jp.boosty.graphql.application.datamodel.request.page.PageInput;
import jp.boosty.graphql.domain.domainmodel.content.ContentTitle;
import jp.boosty.graphql.domain.domainmodel.page.PageText;
import jp.boosty.graphql.domain.domainmodel.page.Page;

public class PageFactory {
    public static Page make(PageInput pageInput) {
        ContentTitle title = ContentTitle.of(pageInput.getTitle());

        PageText text = PageText.of(pageInput.getText());

        return Page.of(title, text);
    }
}
