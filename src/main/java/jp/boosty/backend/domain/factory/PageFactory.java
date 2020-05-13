package jp.boosty.backend.domain.factory;

import jp.boosty.backend.application.datamodel.request.page.PageInput;
import jp.boosty.backend.domain.domainmodel.content.ContentTitle;
import jp.boosty.backend.domain.domainmodel.page.PageText;
import jp.boosty.backend.domain.domainmodel.page.Page;

public class PageFactory {
    public static Page make(PageInput pageInput) {
        ContentTitle title = ContentTitle.of(pageInput.getTitle());

        PageText text = PageText.of(pageInput.getText());

        return Page.of(title, text);
    }
}
