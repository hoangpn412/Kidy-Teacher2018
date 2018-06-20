package vn.com.kidy.teacher.presenter;

import android.util.Log;

import vn.com.kidy.teacher.data.Constants;
import vn.com.kidy.teacher.data.model.media.AlbumContent;
import vn.com.kidy.teacher.data.model.media.AlbumId;
import vn.com.kidy.teacher.data.model.media.Medias;
import vn.com.kidy.teacher.interactor.MediasInteractor;

/**
 * Created by admin on 1/30/18.
 */

public class MediasPresenter extends Presenter<MediasPresenter.View> {

    private MediasInteractor mediasInteractor;

    public MediasPresenter(MediasInteractor mediasInteractor) {
        this.mediasInteractor = mediasInteractor;
    }

    public void onGetMedias(String classId, int pageSize, int pageIndex) {
        mediasInteractor.getMedias(classId, pageSize, pageIndex).subscribe(medias -> {
            if (medias == null) {
                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
            } else {
//                for (int i = 0; i < medias.getAlbums().size(); i++) {
//                    medias.getAlbums().get(i).setDateStr(Tools.longtoDate(medias.getAlbums().get(i).getDate()));
//                }
                getView().getDataSuccess(medias);
            }
        }, Throwable::printStackTrace);
    }

    public void onCreateAlbum(String schoolId, String classId, AlbumContent albumContent) {
        mediasInteractor.createAlbum(schoolId, classId, albumContent).subscribe(albumId -> {
            Log.e("a", "create OK: " + albumId.getAlbumId());
            if (albumId != null) {
                getView().createAlbumSuccess(albumId);
            } else {
                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
            }
        }, Throwable::printStackTrace);
    }

    public interface View extends Presenter.View {
        void getDataSuccess(Medias medias);
        void createAlbumSuccess(AlbumId albumId);
        void getDataError(int statusCode);
    }
}
