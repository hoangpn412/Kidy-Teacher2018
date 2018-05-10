package vn.com.kidy.teacher.interactor;

import io.reactivex.Observable;
import vn.com.kidy.teacher.data.model.comment.Comments;
import vn.com.kidy.teacher.network.client.Service;

/**
 * Created by admin on 1/25/18.
 */

public class CommentsInteractor {
    private Service service;

    public CommentsInteractor(Service service) {
        this.service = service;
    }

    public Observable<Comments> getComments(String classId, String childrenId, int pageSize, int pageIndex) {
        return service.getComments(classId, childrenId, pageSize, pageIndex);
    }
}
