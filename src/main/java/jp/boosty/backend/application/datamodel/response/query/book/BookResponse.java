package jp.boosty.backend.application.datamodel.response.query.book;

import jp.boosty.backend.application.datamodel.response.query.tag.TagResponse;

import java.util.List;

import jp.boosty.backend.application.datamodel.response.query.user.UserResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookResponse {
    private String id;
    private String title;
    private String imageUrl;
    private String description;
    private int price;
    private boolean canSale;
    private List<String> features;
    private BookTargetResponse targets;
    private String status;
    private boolean meaningful;
    private String lastViewedPageId;

    private boolean purchased;
    private long purchasedCount;

    private boolean liked;
    private long likedCount;

    private List<TagResponse> tags;
    private List<SectionResponse> sections;
    private UserResponse author;

    private String createDate;
    private String updateDate;
}
