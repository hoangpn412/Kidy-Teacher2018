package vn.com.kidy.teacher.presenter;


import android.util.Log;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;
import retrofit2.HttpException;
import vn.com.kidy.teacher.data.Constants;
import vn.com.kidy.teacher.data.model.dayoff.ChildrenOff;
import vn.com.kidy.teacher.data.model.dayoff.DayOffList;
import vn.com.kidy.teacher.data.model.media.Medias;
import vn.com.kidy.teacher.data.model.note.NoteContent;
import vn.com.kidy.teacher.data.model.note.Notes;
import vn.com.kidy.teacher.data.model.news.News;
import vn.com.kidy.teacher.interactor.HomeInteractor;
import vn.com.kidy.teacher.utils.Tools;

/**
 * Created by admin on 1/22/18.
 */

public class HomePresenter extends Presenter<HomePresenter.View> {

    private HomeInteractor homeInteractor;

    public HomePresenter(HomeInteractor homeInteractor) {
        this.homeInteractor = homeInteractor;
    }

    public void onGetHome(String kidId) {
//        homeInteractor.getHome(kidId).subscribe(home -> {
//            Log.e("a", home.getMessage() + kidId);
//            if (home == null) {
//                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
//            } else {
//                for (int i = 0; i < home.getNews().getData().size(); i++) {
//                    home.getNews().getData().get(i).setDateStr(Tools.longtoDate(home.getNews().getData().get(i).getDate()));
//                }
//                home.getNotes().setDateStr(Tools.longtoDate(home.getNotes().getDate()));
//                home.getMedia().setDateStr(Tools.longtoDate(home.getMedia().getDate()));
//                getView().getDataSuccess(home);
//            }
//        }, Throwable::printStackTrace);
    }

    public void onGetHomeNews(String schoolId) {
        homeInteractor.getHomeNews(schoolId).subscribe(news -> {
            if (news == null) {
                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
            } else {
                for (int i = 0; i < news.getNewsList().size(); i++) {
                    news.getNewsList().get(i).setCreatedDate(Tools.dateToStringDate(news.getNewsList().get(i).getCreatedDate()));
                }
                getView().getHomeNewsSuccess(news);
            }
        }, throwable -> {
            throwable.printStackTrace();
            getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
        });
    }

    public void onGetDayOffList(String classId) {
        homeInteractor.getDayOffList(classId).subscribe(dayofflist -> {
            if (dayofflist == null) {
                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
            } else {
                getView().getDayOffListSuccess(dayofflist);
            }
        }, throwable -> {
            throwable.printStackTrace();
            getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
        });
    }

//    public void onGetHomeNotes(String classId, String childrenId, int year, int month, int day) {
//        homeInteractor.getHomeNotes(classId, childrenId, year, month, day).subscribe(notes -> {
//            if (notes == null) {
//                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
//            } else {
//                getView().getHomeNotesSuccess(notes);
//            }
//        }, throwable -> {
//            throwable.printStackTrace();
//            getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
//        });
//    }

    public void onGetClassNotes(String classId, int year, int month, int day) {
        homeInteractor.getClassNotes(classId, year, month, day).subscribe(notes -> {
            if (notes == null) {
                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
            } else {
                int count = 0;
                boolean ok;
                Notes newNotes = new Notes();
                Log.e("a", notes.getMessages().size() + " size");
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

    public void submitNote(String classId, String childrenId, NoteContent noteContent) {
        homeInteractor.submitNote(classId, childrenId, noteContent).subscribe(new Consumer<Void>() {
            @Override
            public void accept(Void aVoid) throws Exception {
                Log.e("a", "Success add note");
                getView().addNoteSuccess();
            }

        }, throwable -> {
            Log.e("a", "b" + (throwable instanceof HttpException));
            throwable.printStackTrace();
            if (throwable instanceof NullPointerException) {
                getView().addNoteSuccess();
            } else {
                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
            }
        });
    }

//    public void onGetHomeMedias(String classId, int pageSize, int pageIndex) {
//        homeInteractor.getHomeMedias(classId, pageSize, pageIndex).subscribe(medias -> {
//            if (medias == null) {
//                getView().getDataError(Constants.STATUS_CODE.SERVER_ERROR);
//            } else {
////                for (int i = 0; i < medias.getAlbums().size(); i++) {
////                    medias.getAlbums().get(i).setDateStr(Tools.longtoDate(medias.getAlbums().get(i).getDate()));
////                }
//                getView().getHomeMediaSuccess(medias);
//            }
//        }, Throwable::printStackTrace);
//    }

    public interface View extends Presenter.View {

        void getDataError(int statusCode);

        void getHomeNewsSuccess(News news);

        void getClassNotesSuccess(Notes notes);

//        void getHomeMediaSuccess(Medias medias);

        void getDayOffListSuccess(DayOffList dayofflist);

        void addNoteSuccess();
    }
}
