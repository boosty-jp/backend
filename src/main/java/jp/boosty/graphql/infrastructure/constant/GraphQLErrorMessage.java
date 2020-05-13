package jp.boosty.graphql.infrastructure.constant;

public enum GraphQLErrorMessage {
    // 支払い
    PAYMENT_NOT_SUCCEEDED("支払いが完了していません"),

    // コンテンツ共通
    TITLE_OVER("タイトルは60文字以内で入力してください"),
    CONTENT_TEXT_OVER("内容は1000文字以内で入力してください"),
    DESCRIPTION_OVER("説明文は500文字以内で入力してください"),
    INVALID_STATUS("無効なコンテンツステータスです"),
    NOT_AUTHORIZED("編集権限がありません"),

    // ユーザー
    USER_ID_EMPTY("ユーザIDを指定してください"),
    USER_DESCRIPTION_OVER("説明文は200文字以内にしてください"),
    USER_NAME_OVER("ユーザー名は30文字以内にしてください"),
    USER_NAME_EMPTY("ユーザー名を入力してください"),
    USER_NOT_FOUND("対象のユーザーが見つかりません"),
    NEED_RE_AUTH("無効なセッションです。ログインしてご利用ください"),
    NEED_LOGIN("ログインして、ご利用ください"),
    INVALID_TOKEN("無効なトークン形式です"),
    ALREADY_STRIPE_REGISTERED("すでに口座登録が行われています"),

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

    // 解答
    QUESTION_ID_EMPTY("問題のIDを設定してください"),
    ANSWERS_DUPLICATED("重複した解答があります"),
    ANSWERS_NOT_COMPLETED("すべての解答が埋められていません"),

    // タグ
    TAG_ID_EMPTY("タグIDを指定してください"),
    TAG_NAME_EMPTY("タグ名を指定してください"),
    INVALID_TAG_NAME("このタグ名は作成できません"),
    TAG_DUPLICATED("タグが重複しています"),
    INVALID_TAG_COUNT("設定できるタグは5つまでです"),

    // 本
    NEED_REGISTER_TO_SALE("有料化するには登録が必要です"),
    INVALID_BOOK_DATA("無効な本データです"),
    SECTION_SIZE_OVER("作成できるセクションの数は20までです"),
    SECTION_NULL("セクションを入力してください"),
    EMPTY_SECTION_TITLE("セクション名を入力してください"),
    OVER_SECTION_TITLE("セクション名は40文字までです"),
    OVER_PRICE("設定できる販売金額は50000円までです"),
    LESS_PRICE("設定できる販売金額は50円以上です"),
    OVER_FEATURE_WORD("特徴に入力できる最大文字数は50文字までです"),
    EMPTY_FEATURE_WORD("特徴を入力してください"),
    OVER_FEATURE_COUNT("設定できる特徴数は最大6つまでです"),
    OVER_TARGET_DESCRIPTION_WORD("対象読者の説明は50文字までです"),
    EMPTY_TARGET_DESCRIPTION_WORD("対象読者の説明を入力してください"),
    OVER_TARGET_DESCRIPTION_COUNT("設定できる説明は最大6つまでです"),
    INVALID_LEVEL("無効なレベルです"),
    PAGE_SIZE_OVER("作成できるページ数は1セクションあたり20までです"),
    INVALID_SECTION_DATA("無効なセクションデータです"),
    INVALID_PAGE_DATA("無効なページデータです"),
    EMPTY_PAGE_SECTION("ページがないセクションがあります"),
    DUPLICATED_SECTION("重複したセクションが設定されています"),
    EMPTY_SECTION("セクションは1つ以上作成してください"),
    EMPTY_IMAGE("画像を設定してください"),
    EMPTY_BOOK_TITLE("本のタイトルを設定してください"),
    EMPTY_TAG("タグは１つ以上設定してください"),
    EMPTY_PAGE_TITLE("ページタイトルを設定してください"),
    NOT_ON_SALE("販売されていない本です"),
    ALREADY_PURCHASED("すでに購入された本です"),
    NEED_PURCHASE("購入手続きを行ってください"),

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
    INVALID_SNS_ID("正しいIDを指定してください"),
    INVALID_ARTICLE_BLOCK("記事に無効な内容が含まれています"),
    TOO_LONG_PAGE_CONTENT("記事のデータが大きすぎます"),
    INVALID_IMAGE_URL("無効な画像URLです"),
    INVALID_USER_URL("URLが長すぎます"),
    FORBIDDEN_REQUEST("権限のないリクエストです"),
    INTERNAL_SERVER_ERROR("エラーが発生しました、お手数ですが再度お試しください。"),
    NULL_DATA("データを入れてください");

    private String value;

    private GraphQLErrorMessage(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;

    }
}
