package vn.com.kidy.teacher.interactor;

import io.reactivex.Observable;
import vn.com.kidy.teacher.data.model.media.Medias;
import vn.com.kidy.teacher.network.client.Service;

/**
 * Created by admin on 1/25/18.
 */

public class MediasInteractor {
    private Service service;

    public MediasInteractor(Service service) {
        this.service = service;
    }

    public Observable<Medias> getMedias(String classId, int pageSize, int pageIndex) {
        return service.getMedias(classId, pageSize, pageIndex);
    }
}
