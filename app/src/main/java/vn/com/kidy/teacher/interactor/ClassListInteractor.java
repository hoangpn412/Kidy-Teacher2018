package vn.com.kidy.teacher.interactor;

import java.util.ArrayList;

import io.reactivex.Observable;
import vn.com.kidy.teacher.data.model.login.Kid;
import vn.com.kidy.teacher.network.client.Service;

/**
 * Created by admin on 1/25/18.
 */

public class ClassListInteractor {
    private Service service;

    public ClassListInteractor(Service service) {
        this.service = service;
    }

    public Observable<ArrayList<Kid>> getChildreninClass(String classId, int week, int year) {
        return service.getChildreninClass(classId, week, year);
    }
}
