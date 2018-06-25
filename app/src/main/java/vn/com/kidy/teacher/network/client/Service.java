package vn.com.kidy.teacher.network.client;


import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import vn.com.kidy.teacher.data.model.calendar.Calendar;
import vn.com.kidy.teacher.data.model.calendar.CalendarNotes;
import vn.com.kidy.teacher.data.model.calendar.Events;
import vn.com.kidy.teacher.data.model.calendar.TimeTable;
import vn.com.kidy.teacher.data.model.comment.Comment;
import vn.com.kidy.teacher.data.model.comment.CommentContent;
import vn.com.kidy.teacher.data.model.comment.Comments;
import vn.com.kidy.teacher.data.model.comment.Content;
import vn.com.kidy.teacher.data.model.dayoff.ChildrenOff;
import vn.com.kidy.teacher.data.model.dayoff.DayOffList;
import vn.com.kidy.teacher.data.model.media.AlbumContent;
import vn.com.kidy.teacher.data.model.media.AlbumId;
import vn.com.kidy.teacher.data.model.media.CreateImageRespone;
import vn.com.kidy.teacher.data.model.media.Photo;
import vn.com.kidy.teacher.data.model.media.PhotoContent;
import vn.com.kidy.teacher.data.model.media.Photos;
import vn.com.kidy.teacher.data.model.note.Notes;
import vn.com.kidy.teacher.data.model.login.Account;
import vn.com.kidy.teacher.data.model.login.ClassInfo;
import vn.com.kidy.teacher.data.model.login.Kid;
import vn.com.kidy.teacher.data.model.login.User;
import vn.com.kidy.teacher.data.model.login.Login;
import vn.com.kidy.teacher.data.model.media.Medias;
import vn.com.kidy.teacher.data.model.news.News;
import vn.com.kidy.teacher.data.model.note.DayOffContent;
import vn.com.kidy.teacher.data.model.note.NoteContent;
import vn.com.kidy.teacher.data.model.notification.Notifications;

/**
 * Created by Family on 4/22/2017.
 */

public interface Service {
    Observable<Login> getLogin(Account account);
    Observable<User> getLoginContact(String token);
    Observable<ArrayList<Kid>> getLoginKids(String parent);
    Observable<ClassInfo> getClassInfo(String classId);
    Observable<News> getHomeNews(String schoolid);
    Observable<Notes> getHomeNotes(String classId, String childrenId, int year, int month, int day);
    Observable<Notes> getClassNotes(String classId, int year, int month, int day);

    Observable<Void> submitNote(String classId, String childrenId, NoteContent noteContent);

//    Observable<Home> getHome(String kidId);
    Observable<Calendar> getCalendar(String kidId, long date);
    Observable<ArrayList<TimeTable>> getTimeTables(String classId, int dayOfWeek);
    Observable<ArrayList<TimeTable>> getMeanMenu(String classId, int dayOfWeek);
//    Observable<News> getNews(String kidId);
    Observable<Comments> getComments(String classId, String childrenId, int pageSize, int pageIndex);
    Observable<Medias> getMedias(String classId, int pageSize, int pageIndex);
//    Observable<SubmitNote> submitNote(String kidId, long date, String note);
    Observable<Content> getContentComment(String commentId);
    Observable<Content> getArticleContent(String articleId);
    Observable<Notifications> getNotifications(String kidId);
    Observable<CalendarNotes> getCalendarNotes(String classId, Events events);
    Observable<Photos> getAlbum(String classId, String albumId, int pageSize, int pageIndex);

    Observable<Void> submitDayOff(DayOffContent dayOffContent);

    Observable<ArrayList<ClassInfo>> getClassList(String userId);
    Observable<DayOffList> getDayOffList(String classId);
    Observable<ArrayList<Kid>> getChildreninClass(String classId, int week, int year);
    Observable<Void> postComments(String classId, CommentContent commentContent);
    Observable<AlbumId> createAlbum(String schoolId, String classId, AlbumContent albumContent);
    Observable<ArrayList<Photo>> uploadImage(String token, MultipartBody.Part part);
    Observable<ArrayList<String>> uploadFile(String token, MultipartBody.Part file, RequestBody name);
    Observable<CreateImageRespone> createImage(String schoolId, String classId, String albumId, PhotoContent photoContent);
}
