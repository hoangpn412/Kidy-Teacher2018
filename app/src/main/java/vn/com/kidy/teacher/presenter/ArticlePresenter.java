package vn.com.kidy.teacher.presenter;

import vn.com.kidy.teacher.data.Constants;
import vn.com.kidy.teacher.data.model.comment.Content;
import vn.com.kidy.teacher.interactor.ArticleInteractor;

/**
 * Created by admin on 1/30/18.
 */

public class ArticlePresenter extends Presenter<ArticlePresenter.View> {

    private ArticleInteractor articleInteractor;

    public ArticlePresenter(ArticleInteractor articleInteractor) {
        this.articleInteractor = articleInteractor;
    }

    public void onGetArticleContent(String articleId) {
        articleInteractor.getArticleContent(articleId).subscribe(content -> {
            if (content == null) {
                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
            } else {
                getView().getDataSuccess(content);
            }
        }, Throwable::printStackTrace);
    }

    public interface View extends Presenter.View {
        void getDataSuccess(Content content);

        void getDataError(int statusCode);
    }
}
