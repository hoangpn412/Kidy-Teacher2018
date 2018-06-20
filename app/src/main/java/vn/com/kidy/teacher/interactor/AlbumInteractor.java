package vn.com.kidy.teacher.interactor;

import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import vn.com.kidy.teacher.data.model.media.Photo;
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

    public Observable<ArrayList<Photo>> uploadImage(String token, MultipartBody.Part part) {
        return service.uploadImage(token, part);
    }

    public Observable<ArrayList<Photo>> uploadFile(String token, MultipartBody.Part file, RequestBody name) {
        return service.uploadFile(token, file, name);
    }
}
