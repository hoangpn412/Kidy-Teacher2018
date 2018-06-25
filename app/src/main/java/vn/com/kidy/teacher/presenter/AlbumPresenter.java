package vn.com.kidy.teacher.presenter;

import android.util.Log;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import vn.com.kidy.teacher.data.Constants;
import vn.com.kidy.teacher.data.model.media.Photo;
import vn.com.kidy.teacher.data.model.media.PhotoContent;
import vn.com.kidy.teacher.data.model.media.Photos;
import vn.com.kidy.teacher.interactor.AlbumInteractor;

/**
 * Created by admin on 3/2/18.
 */

public class AlbumPresenter extends Presenter<AlbumPresenter.View> {

    private AlbumInteractor albumInteractor;

    public AlbumPresenter(AlbumInteractor albumInteractor) {
        this.albumInteractor = albumInteractor;
    }

    public void onGetAlbum(String classId, String albumId, int pageSize, int pageIndex) {
        albumInteractor.getAlbum(classId, albumId, pageSize, pageIndex).subscribe((Photos photos) -> {
            if (photos == null) {
                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
            } else {
                getView().getDataSuccess(photos);
            }
        }, Throwable::printStackTrace);
    }

    public void onUploadImage(String token, MultipartBody.Part part) {
        albumInteractor.uploadImage(token, part).subscribe((ArrayList<Photo> photos) -> {
            if (photos == null) {
                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
            } else {
//                getView().getDataSuccess(photos);
                Log.e("a", "photos: " + photos.size());
            }
        }, Throwable::printStackTrace);
    }

    public void onUploadFile(String token, MultipartBody.Part file, RequestBody name) {
        albumInteractor.uploadFile(token, file, name).subscribe((ArrayList<String> photos) -> {
            if (photos == null) {
                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
            } else {
                Log.e("a", "photos: " + photos.size());
                getView().uploadSuccess(photos);
            }
        }, throwable -> {
            Log.e("a", "bbbb");
            throwable.printStackTrace();
        });
    }

    public void onCreateImage(String schooId, String classId, String albumId, PhotoContent photoContent) {
        albumInteractor.createImage(schooId, classId, albumId, photoContent).subscribe((createImageRespone) -> {
            if (createImageRespone != null && createImageRespone.getIsSuccess()) {
                getView().createImageSuccess();
            }
        }, throwable -> {
            throwable.printStackTrace();
        });
    }

    public interface View extends Presenter.View {
        void getDataSuccess(Photos photos);

        void getDataError(int statusCode);

        void uploadSuccess(ArrayList<String> photos);

        void createImageSuccess();
    }
}
