package vn.com.kidy.teacher.presenter;

import android.util.Log;

import java.util.ArrayList;

import vn.com.kidy.teacher.data.Constants;
import vn.com.kidy.teacher.data.model.calendar.TimeTable;
import vn.com.kidy.teacher.data.model.note.Notes;
import vn.com.kidy.teacher.interactor.CalendarInteractor;

/**
 * Created by admin on 1/25/18.
 */

public class CalendarPresenter extends Presenter<CalendarPresenter.View> {
    private CalendarInteractor calendarInteractor;

    public CalendarPresenter(CalendarInteractor calendarInteractor) {
        this.calendarInteractor = calendarInteractor;
    }



    public void onGetClassNotes(String classId, int year, int month, int day) {
        calendarInteractor.getClassNotes(classId, year, month, day).subscribe(notes -> {
            if (notes == null) {
                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
            } else {
                int count = 0;
                boolean ok;
                Notes newNotes = new Notes();
                Log.e("a", notes.getMessages().size() + " size classnotes");
                for (int i = 0; i < notes.getMessages().size(); i++) {
                    if (i == 0) {
                        notes.getMessages().get(i).setMesssagePos(0);
                        continue;
                    }
                    ok = false;
                    for (int j = 0; j < i; j ++) {
                        if (notes.getMessages().get(i).getChildrenId().equals(notes.getMessages().get(j).getChildrenId())) {
                            notes.getMessages().get(i).setMesssagePos(notes.getMessages().get(j).getMesssagePos());
                            Log.e("a", "j: " + j + " " + i);
                            ok = true;
                            break;
                        }
                    }
                    if (!ok) {
                        count ++;
                        notes.getMessages().get(i).setMesssagePos(count);
                    }
                }

                newNotes.setMessages(new ArrayList<>());
                Log.e("a", "count: " + count);
                for (int i = 0; i <= count; i ++) {
                    for (int j = 0; j < notes.getMessages().size(); j ++) {
                        if (notes.getMessages().get(j).getMesssagePos() == i) {
                            newNotes.getMessages().add(notes.getMessages().get(j));
                        }
                    }
                }
                getView().getClassNotesSuccess(newNotes);
            }
        }, throwable -> {
            throwable.printStackTrace();
            getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
        });
    }

    public void onGetTimeTables(String classId, int dayOfWeek) {
        calendarInteractor.getTimeTables(classId, dayOfWeek).subscribe(timeTables -> {
            if (timeTables == null) {
                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
            } else {
                getView().getTimeTableSuccess(timeTables);
            }
        }, throwable -> {
            throwable.printStackTrace();
            getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
        });
    }

    public void onGetMeanMenu(String classId, int dayOfWeek) {
        calendarInteractor.getMeanMenu(classId, dayOfWeek).subscribe(meanMenu -> {
            if (meanMenu == null) {
                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
            } else {
                getView().getMeanMenuSuccess(meanMenu);
            }
        }, throwable -> {
            throwable.printStackTrace();
            getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
        });
    }


    public interface View extends Presenter.View {
//        void getDataSuccess(Calendar calendar);

        void getDataError(int statusCode);

        void getClassNotesSuccess(Notes notes);

        void getTimeTableSuccess(ArrayList<TimeTable> timeTables);

        void getMeanMenuSuccess(ArrayList<TimeTable> meanMenu);
    }
}
