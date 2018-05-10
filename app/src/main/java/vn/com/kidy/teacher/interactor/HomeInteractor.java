package vn.com.kidy.teacher.interactor;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import vn.com.kidy.teacher.data.model.dayoff.ChildrenOff;
import vn.com.kidy.teacher.data.model.dayoff.DayOffList;
import vn.com.kidy.teacher.data.model.media.Medias;
import vn.com.kidy.teacher.data.model.note.NoteContent;
import vn.com.kidy.teacher.data.model.note.Notes;
import vn.com.kidy.teacher.data.model.news.News;
import vn.com.kidy.teacher.network.client.Service;

/**
 * Created by Family on 6/2/2017.
 */

public class HomeInteractor {
    private Service service;

    public HomeInteractor(Service service) {
        this.service = service;
    }

//    public Observable<Home> getHome(String kidId) {
//        return service.getHome(kidId);
//    }

    public Observable<News> getHomeNews(String schoolId) {
        return service.getHomeNews(schoolId);
    }

    public Observable<Notes> getClassNotes(String classId, int year, int month, int day) {
        return service.getClassNotes(classId, year, month, day);
    }

    public Observable<Medias> getHomeMedias(String classId, int pageSize, int pageIndex) {
        return service.getMedias(classId, pageSize, pageIndex);
    }

    public Observable<DayOffList> getDayOffList(String classId) {
        return service.getDayOffList(classId);
    }

    public Observable<Void> submitNote(String classId, String childrenId, NoteContent noteContent) {
        return service.submitNote(classId, childrenId, noteContent);
    }
}
