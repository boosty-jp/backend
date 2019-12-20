package co.jp.wever.graphql.infrastructure.constant;

//TODO: 細分化する
public enum GraphQLErrorMessage {
    EMPTY_TITLE("タイトルを入力してください"),
    TITLE_OVER("タイトルは60文字以内で入力してください"),
    EMPTY_TEXT("内容を入力してください"),
    TEXT_OVER("内容は10000文字以内で入力してください"),
    DESCRIPTION_OVER("説明文は500文字以内で入力してください"),
    PLAN_ELEMENT_EMPTY("プランの要素は1つ以上入力してください"),
    PLAN_ELEMENT_OVER("プランの要素は30までです"),
    PLAN_ELEMENT_NUMBER_DUPLICATED("プランの番号が重複しています"),
    PLAN_ELEMENT_ID_DUPLICATED("重複するプランの要素は指定できません"),
    PLAN_ELEMENT_SELF_ID("自分自身のIDはプランの要素に指定できません"),
    PLAN_ELEMENT_SHORTAGE("プランの要素は1つ以上指定してください"),
    PLAN_ELEMENT_ID_EMPTY("プランの要素を指定してください"),
    USER_ID_EMPTY("ユーザIDを指定してください"),
    PLAN_ID_EMPTY("プランIDを指定してください"),
    TAG_ID_EMPTY("タグIDを指定してください"),
    TAG_NAME_EMPTY("タグ名を指定してください"),
    TAG_ID_DUPLICATED("重複したタグは設定できません"),
    USER_DESCRIPTION_OVER("説明文は200文字以内にしてください"),
    SECTION_NUMBER_INVALID("正しいセクション番号を指定してください"),
    SECTION_SIZE_OVER("指定できるセクションの数は30までです"),
    SECTION_EMPTY("セクションを入力してください"),
    SKILL_NAME_EMPTY("スキル名を入力してください"),
    SKILL_ID_EMPTY("スキルIDを入力してください"),
    SKILL_ID_DUPLICATED("重複したスキルは設定できません"),
    ARTICLE_SKILL_COUNT_OVER("記事に含められるスキルは3つまでです"),
    CANNOT_PUBLISH_ARTICLE("記事を公開できません。入力内容に誤りがないか、ご確認ください。"),
    INVALID_SKILL_LEVEL("無効なスキルレベルです"),
    USER_NAME_OVER("ユーザー名は30文字以内にしてください"),
    USER_NAME_EMPTY("ユーザー名を入力してください"),
    USER_NOT_FOUND("対象のユーザーが見つかりません"),
    INVALID_SEARCH_CONDITION("無効な検索条件です。"),
    SEARCH_FORBIDDEN("無効な検索条件です。"),
    INVALID_SNS_ID("正しいIDを指定してください"),
    FORBIDDEN_REQUEST("権限のないリクエストです"),
    INVALID_PLAN_ELEMENT_ID("下書きまたは削除済みの要素が含まれています"),
    INVALID_ARTICLE_BLOCK("記事に無効な内容が含まれています"),
    TOO_LONG_ARTICLE_BLOCK("記事のデータが大きすぎます"),
    TAG_DUPLICATED("タグが重複しています"),
    INVALID_IMAGE_URL("不正な画像URLです"),
    INVALID_USER_URL("URLが長すぎます"),
    INVALID_TAG_COUNT("設定できるタグは5つまでです"),
    INVALID_STATUS("無効なコンテンツステータスです"),
    INTERNAL_SERVER_ERROR("INTERNAL SERVER ERROR"),
    NEED_LOGIN("ログインして、ご利用ください"),
    INVALID_TOKEN("無効なトークン形式です"),
    NULL_DATA("データを入れてください"),
    EXPIRED_TOKEN("期限切れのトークンです"),
    NEED_RE_AUTH("期限切れのセッションです。ログインしてご利用ください"),
    FOLLOW_OWN("自分自身はフォローできません");

    private String value;

    private GraphQLErrorMessage(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;

    }
}
