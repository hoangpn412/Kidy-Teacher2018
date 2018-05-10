package vn.com.kidy.teacher.interactor;

import io.reactivex.Observable;
import vn.com.kidy.teacher.data.model.comment.Comment;
import vn.com.kidy.teacher.data.model.comment.CommentContent;
import vn.com.kidy.teacher.data.model.comment.Comments;
import vn.com.kidy.teacher.network.client.Service;

/**
 * Created by admin on 1/25/18.
 */

public class PostCommentInteractor {
    private Service service;

    public PostCommentInteractor(Service service) {
        this.service = service;
    }

    public Observable<Void> postComments(String classId, CommentContent commentContent) {
        return service.postComments(classId, commentContent);
    }
}
