package vn.com.kidy.teacher.presenter;

import android.util.Log;

import io.reactivex.functions.Consumer;
import retrofit2.HttpException;
import vn.com.kidy.teacher.data.Constants;
import vn.com.kidy.teacher.data.model.comment.Comment;
import vn.com.kidy.teacher.data.model.comment.CommentContent;
import vn.com.kidy.teacher.data.model.comment.Comments;
import vn.com.kidy.teacher.interactor.CommentsInteractor;
import vn.com.kidy.teacher.interactor.PostCommentInteractor;
import vn.com.kidy.teacher.utils.Tools;

/**
 * Created by admin on 1/30/18.
 */

public class PostCommentPresenter extends Presenter<PostCommentPresenter.View> {

    private PostCommentInteractor postCommentInteractor;

    public PostCommentPresenter(PostCommentInteractor postCommentInteractor) {
        this.postCommentInteractor = postCommentInteractor;
    }

    public void onPostComment(String classId, CommentContent commentContent) {
        postCommentInteractor.postComments(classId, commentContent).subscribe(aVoid -> {
            Log.e("a", "Success post comment");
            getView().postCommentSuccess();
        }, throwable -> {
            Log.e("a", "b " + (throwable instanceof HttpException));
            throwable.printStackTrace();
            if (throwable instanceof NullPointerException) {
                getView().postCommentSuccess();
            } else {
                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
            }
        });
    }

    public interface View extends Presenter.View {
        void postCommentSuccess();

        void getDataError(int statusCode);
    }
}
