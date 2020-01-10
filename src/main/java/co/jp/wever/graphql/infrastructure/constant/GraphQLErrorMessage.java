package co.jp.wever.graphql.infrastructure.constant;

public enum GraphQLErrorMessage {

    // コンテンツ共通
    TITLE_OVER("タイトルは60文字以内で入力してください"),
    CONTENT_TEXT_OVER("内容は1000文字以内で入力してください"),
    DESCRIPTION_OVER("説明文は300文字以内で入力してください"),
    INVALID_STATUS("無効なコンテンツステータスです"),

    // ユーザー
    USER_ID_EMPTY("ユーザIDを指定してください"),
    USER_DESCRIPTION_OVER("説明文は200文字以内にしてください"),
    USER_NAME_OVER("ユーザー名は30文字以内にしてください"),
    USER_NAME_EMPTY("ユーザー名を入力してください"),
    USER_NOT_FOUND("対象のユーザーが見つかりません"),
    NEED_RE_AUTH("無効なセッションです。ログインしてご利用ください"),
    NEED_LOGIN("ログインして、ご利用ください"),
    INVALID_TOKEN("無効なトークン形式です"),

    // 問題
    REFERENCE_ID_EMPTY("参考情報のIDを指定してください"),
    REFERENCE_OVER("設定できる参考情報は5つまでです"),
    DUPLICATED_REFERENCE("参考情報が重複しています"),
    EMPTY_EXPLANATION("解説が設定されていません"),
    SELECT_ANSWER_SHORTAGE("選択式の問題は2つ以上の選択肢を設定してください"),
    SELECT_ANSWER_OVER("選択式の問題で設定できる選択肢は5つまでです"),
    EMPTY_ANSWER_TYPE("解答の種類を設定してください"),
    INVALID_ANSWER_TYPE("解答の種類が無効です"),
    INVALID_SELECT_ANSWERS("無効な選択肢情報です"),
    INVALID_QUESTION_ANSWER_TYPE("正しい問題の解答形式を選択してください"),
    EMPTY_QUESTION("問題は1つ以上設定してください"),
    QUESTION_OVER("設定できる問題は20つまでです"),
    TEXT_ANSWER_OVER("解答として入力できる文字数は100文字までです"),
    TEXT_ANSWER_EMPTY("解答を入力してください"),
    EXPLANATION_OVER("設定できる解説は5つまでです"),
    CANNOT_PUBLISH_TEST("問題に無効な内容が含まれているため、公開できません"),

    // タグ
    TAG_ID_EMPTY("タグIDを指定してください"),
    TAG_NAME_EMPTY("タグ名を指定してください"),
    INVALID_TAG_NAME("このタグ名は作成できません"),
    TAG_DUPLICATED("タグが重複しています"),
    INVALID_TAG_COUNT("設定できるタグは5つまでです"),

    // コース
    NOT_PUBLISHED_ARTICLE("公開されていない記事が含まれています"),
    INVALID_COURSE_DATA("無効なコースデータです"),
    DUPLICATE_CONTENTS_DATA("重複したコンテンツが含まれています"),
    SECTION_SIZE_OVER("投稿できるセクションの数は10までです"),
    SECTION_NULL("セクションを入力してください"),
    SECTION_CONTENT_NUMBER_OVER("セクションに設定できる記事の数は10までです"),
    INVALID_SECTION_CONTENT("セクションに設定されている記事データが存在しません"),

    // スキル
    SKILL_NAME_EMPTY("スキル名を入力してください"),
    SKILL_ID_EMPTY("スキルIDを入力してください"),
    SKILL_ID_DUPLICATED("重複したスキルは設定できません"),
    INVALID_SKILL_NAME("このスキル名は作成できません"),
    INVALID_SKILL_LEVEL("無効なスキルレベルです"),

    // 記事
    ARTICLE_SKILL_COUNT_OVER("記事に含められるスキルは3つまでです"),
    CANNOT_PUBLISH_ARTICLE("記事を公開できません。入力内容に誤りがないか、ご確認ください。"),
    INVALID_SEARCH_CONDITION("無効な検索条件です。"),
    SEARCH_FORBIDDEN("無効な検索条件です。"),
    INVALID_SNS_ID("正しいIDを指定してください"),
    INVALID_ARTICLE_BLOCK("記事に無効な内容が含まれています"),
    TOO_LONG_ARTICLE_BLOCK("記事のデータが大きすぎます"),
    INVALID_IMAGE_URL("無効な画像URLです"),
    INVALID_USER_URL("URLが長すぎます"),
    FORBIDDEN_REQUEST("権限のないリクエストです"),
    INTERNAL_SERVER_ERROR("INTERNAL SERVER ERROR"),
    NULL_DATA("データを入れてください");

    private String value;

    private GraphQLErrorMessage(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;

    }
}
