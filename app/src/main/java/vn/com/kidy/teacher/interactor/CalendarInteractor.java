package vn.com.kidy.teacher.interactor;

import java.util.ArrayList;

import io.reactivex.Observable;
import vn.com.kidy.teacher.data.model.calendar.Calendar;
import vn.com.kidy.teacher.data.model.calendar.TimeTable;
import vn.com.kidy.teacher.data.model.note.Notes;
import vn.com.kidy.teacher.network.client.Service;

/**
 * Created by admin on 1/25/18.
 */

public class CalendarInteractor {
    private Service service;

    public CalendarInteractor(Service service) {
        this.service = service;
    }

    public Observable<Calendar> getCalendar(String kidId, long date) {
        return service.getCalendar(kidId, date);
    }

    public Observable<Notes> getHomeNotes(String classId, String childrenId, int year, int month, int day) {
        return service.getHomeNotes(classId, childrenId, year, month, day);
    }

    public Observable<Notes> getClassNotes(String classId, int year, int month, int day) {
        return service.getClassNotes(classId, year, month, day);
    }

    public Observable<ArrayList<TimeTable>> getTimeTables(String classId, int dayOfWeek) {
        return service.getTimeTables(classId, dayOfWeek);
    }

    public Observable<ArrayList<TimeTable>> getMeanMenu(String classId, int dayOfWeek) {
        return service.getMeanMenu(classId, dayOfWeek);
    }
}
