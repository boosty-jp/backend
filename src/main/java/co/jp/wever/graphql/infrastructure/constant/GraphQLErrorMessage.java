package co.jp.wever.graphql.infrastructure.constant;

//TODO: 細分化する
public enum GraphQLErrorMessage {
    EMPTY_TITLE("タイトルを入力してください"),
    TITLE_OVER("タイトルは100文字以内で入力してください"),
    EMPTY_TEXT("内容を入力してください"),
    TEXT_OVER("内容は10000文字以内で入力してください"),
    DESCRIPTION_OVER("説明文は500文字以内で入力してください"),
    PLAN_ELEMENT_EMPTY("プランの要素は1つ以上入力してください"),
    PLAN_ELEMENT_OVER("プランの要素は30までです"),
    PLAN_ELEMENT_ID_EMPTY("プランの要素を指定してください"),
    USER_ID_EMPTY("ユーザIDを指定してください"),
    PLAN_ID_EMPTY("プランIDを指定してください"),
    TAG_ID_EMPTY("タグIDを指定してください"),
    TAG_NAME_EMPTY("タグ名を指定してください"),
    USER_DESCRIPTION_OVER("説明文は200文字以内にしてください"),
    SECTION_NUMBER_INVALID("正しいセクション番号を指定してください"),
    USER_NAME_OVER("ユーザー名は30文字以内にしてください"),
    FORBIDDEN_REQUEST("権限のないリクエストです"),
    INVALID_PLAN_ELEMENT_ID("下書きまたは削除済みの要素が含まれています"),
    TAG_DUPLICATED("タグが重複しています"),
    INVALID_IMAGE_URL("不正な画像URLです"),
    INVALID_USER_URL("URLが長すぎます"),
    INVALID_TAG_COUNT("設定できるタグは5つまでです"),
    INTERNAL_SERVER_ERROR("INTERNAL SERVER ERROR"),
    FOLLOW_OWN("自分自身はフォローできません");

    private String value;

    private GraphQLErrorMessage(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;

    }
}
