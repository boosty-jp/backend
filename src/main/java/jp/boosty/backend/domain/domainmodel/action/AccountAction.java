package jp.boosty.backend.domain.domainmodel.action;

public class AccountAction {

    private boolean liked;
    private boolean learned;

    public AccountAction(boolean liked, boolean learned) {
        this.liked = liked;
        this.learned = learned;
    }

    public boolean isLiked() {
        return liked;
    }

    public boolean isLearned() {
        return learned;
    }

}
