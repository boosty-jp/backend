package jp.boosty.backend.domain.domainmodel.tag;

public class Tag {
    private TagId id;
    private TagName name;

    private Tag(TagId id, TagName name) {
        this.id = id;
        this.name = name;
    }

    public static Tag of(String id) {
        return new Tag(TagId.of(id), TagName.of("nothing")); //TODO:仮作成なのでなんとかしたい
    }

    public static Tag of(String id, String name) {
        return new Tag(TagId.of(id), TagName.of(name));
    }

    public TagId getId() {
        return id;
    }

    public TagName getName() {
        return name;
    }
}
