package vn.com.kidy.teacher.interactor;

import io.reactivex.Observable;
import vn.com.kidy.teacher.data.model.media.Photos;
import vn.com.kidy.teacher.network.client.Service;

/**
 * Created by admin on 1/25/18.
 */

public class AlbumInteractor {
    private Service service;

    public AlbumInteractor(Service service) {
        this.service = service;
    }

    public Observable<Photos> getAlbum(String classId, String albumId, int pageSize, int pageIndex) {
        return service.getAlbum(classId, albumId, pageSize, pageIndex);
    }
}
