package vn.com.kidy.teacher.interactor;

import io.reactivex.Observable;
import vn.com.kidy.teacher.data.model.comment.Content;
import vn.com.kidy.teacher.network.client.Service;

/**
 * Created by admin on 1/25/18.
 */

public class ArticleInteractor {
    private Service service;

    public ArticleInteractor(Service service) {
        this.service = service;
    }

    public Observable<Content> getArticleContent(String articleId) {
        return service.getArticleContent(articleId);
    }
}
