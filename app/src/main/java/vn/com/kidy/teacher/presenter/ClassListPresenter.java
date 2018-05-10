package vn.com.kidy.teacher.presenter;

import java.util.ArrayList;
import java.util.Calendar;

import vn.com.kidy.teacher.data.Constants;
import vn.com.kidy.teacher.data.model.login.Kid;
import vn.com.kidy.teacher.interactor.ClassListInteractor;
import vn.com.kidy.teacher.utils.Tools;

/**
 * Created by admin on 1/30/18.
 */

public class ClassListPresenter extends Presenter<ClassListPresenter.View> {

    private ClassListInteractor classListInteractor;

    public ClassListPresenter(ClassListInteractor classListInteractor) {
        this.classListInteractor = classListInteractor;
    }

    public void onGetChildreninClass(String classId) {
        Calendar calendar = Calendar.getInstance();
        int weekofYear = calendar.get(Calendar.WEEK_OF_YEAR);
        int year = calendar.get(Calendar.YEAR);

        classListInteractor.getChildreninClass(classId, weekofYear, year).subscribe(classList -> {
            if (classList == null) {
                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
            } else {
                for (int i = 0; i < classList.size(); i++) {
                    if (classList.get(i).getCommentDate() == null) {
                        classList.get(i).setHasComment(false);
                        continue;
                    }
                    classList.get(i).setCommentDateCal(Tools.stringDatetoCalendar(classList.get(i).getCommentDate()));
                    int woy = classList.get(i).getCommentDateCal().get(Calendar.WEEK_OF_YEAR);
                    if (weekofYear <= woy) {
                        classList.get(i).setHasComment(true);
                    } else {
                        classList.get(i).setHasComment(false);
                    }
                }
                getView().getDataSuccess(classList);
            }
        }, Throwable::printStackTrace);
    }

    public interface View extends Presenter.View {
        void getDataSuccess(ArrayList<Kid> classList);

        void getDataError(int statusCode);
    }
}
