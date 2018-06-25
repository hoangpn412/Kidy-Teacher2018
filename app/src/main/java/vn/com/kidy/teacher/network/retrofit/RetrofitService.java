package vn.com.kidy.teacher.network.retrofit;

import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.com.kidy.teacher.data.Constants;
import vn.com.kidy.teacher.data.model.calendar.Calendar;
import vn.com.kidy.teacher.data.model.calendar.CalendarNotes;
import vn.com.kidy.teacher.data.model.calendar.Events;
import vn.com.kidy.teacher.data.model.calendar.TimeTable;
import vn.com.kidy.teacher.data.model.comment.Comment;
import vn.com.kidy.teacher.data.model.comment.CommentContent;
import vn.com.kidy.teacher.data.model.comment.Comments;
import vn.com.kidy.teacher.data.model.comment.Content;
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
import vn.com.kidy.teacher.data.model.login.Login;
import vn.com.kidy.teacher.data.model.login.User;
import vn.com.kidy.teacher.data.model.media.Medias;
import vn.com.kidy.teacher.data.model.news.News;
import vn.com.kidy.teacher.data.model.note.DayOffContent;
import vn.com.kidy.teacher.data.model.note.NoteContent;
import vn.com.kidy.teacher.data.model.notification.Notifications;

/**
 * Created by Family on 4/22/2017.
 */

public interface RetrofitService {

    @POST(Constants.EndPoint.LOGIN)
    Observable<Login> getLogin(@Body Account account);

    @GET(Constants.EndPoint.LOGIN_CONTACT)
    Observable<User> getLoginContact(@Path(Constants.Params.TOKEN) String token);

    @GET(Constants.EndPoint.LOGIN_KIDS)
    Observable<ArrayList<Kid>> getLoginKids(@Path(Constants.Params.PARENT) String parent);

    @GET(Constants.EndPoint.CLASS_INFO)
    Observable<ClassInfo> getClassInfo(@Path(Constants.Params.CLASSID) String classId);

    @GET(Constants.EndPoint.HOME_NEWS)
    Observable<News> getHomeNews(@Path(Constants.Params.SCHOOLID) String schoolId);

    @Headers({"Domain-Name: douban"}) // Add the Domain-Name header
    @GET(Constants.EndPoint.CHILDREN_NOTES)
    Observable<Notes> getHomeNotes(@Path(Constants.Params.CLASSID) String classId, @Path(Constants.Params.CHILDRENID) String childrenId, @Path(Constants.Params.YEAR) int year, @Path(Constants.Params.MONTH) int month, @Path(Constants.Params.DAY) int day);

    @Headers({"Domain-Name: douban"}) // Add the Domain-Name header
    @POST(Constants.EndPoint.CREATE_MESSAGE)
    Observable<Void> submitNote(@Path(Constants.Params.CLASSID) String classId, @Path(Constants.Params.CHILDRENID) String childrenId, @Body NoteContent noteContent);

//    @GET(Constants.EndPoint.HOME)
//    Observable<Home> getHome(@Query(Constants.Params.KID_ID) String kidId);

    @GET(Constants.EndPoint.CALENDAR)
    Observable<Calendar> getCalendar(@Query(Constants.Params.KID_ID) String kidId, @Query(Constants.Params.DATE) long date);

    @GET(Constants.EndPoint.TIMETABLE)
    Observable<ArrayList<TimeTable>> getTimeTables(@Path(Constants.Params.CLASSID) String classId, @Path(Constants.Params.DAYOFWEEK) int dayOfWeek);

    @GET(Constants.EndPoint.MEANMENU)
    Observable<ArrayList<TimeTable>> getMeanMenu(@Path(Constants.Params.CLASSID) String classId, @Path(Constants.Params.DAYOFWEEK) int dayOfWeek);

    @GET(Constants.EndPoint.NEWS)
    Observable<News> getNews(@Query(Constants.Params.KID_ID) String kidId);

    @GET(Constants.EndPoint.COMMENTS)
    Observable<Comments> getComments(@Path(Constants.Params.CLASSID) String classId, @Path(Constants.Params.CHILDRENID) String childrenId, @Query(Constants.Params.PAGESIZE) int pageSize, @Query(Constants.Params.PAGEINDEX) int pageIndex);

    @Headers({"Domain-Name: douban"}) // Add the Domain-Name header
    @GET(Constants.EndPoint.MEDIAS)
    Observable<Medias> getMedias(@Path(Constants.Params.CLASSID) String classId, @Query(Constants.Params.PAGESIZE) int pageSize, @Query(Constants.Params.PAGEINDEX) int pageIndex);

//    @GET(Constants.EndPoint.SUBMIT_NOTE)
//    Observable<SubmitNote> submitNote(@Query(Constants.Params.KID_ID) String kidId, @Query(Constants.Params.DATE) long date, @Query(Constants.Params.NOTE) String note);

    @GET(Constants.EndPoint.COMMENT)
    Observable<Content> getContentComment(@Query(Constants.Params.COMMENTID) String commentId);

    @GET(Constants.EndPoint.ARTICLE)
    Observable<Content> getArticle(@Query(Constants.Params.ARTICLEID) String articleId);

    @GET(Constants.EndPoint.NOTIFICATION)
    Observable<Notifications> getNotifications(@Query(Constants.Params.KID_ID) String kidId);

    @POST(Constants.EndPoint.FULLCALENDAR)
    Observable<CalendarNotes> getCalendarNotes(@Path(Constants.Params.CLASSID) String classId, @Body Events events);

    @POST(Constants.EndPoint.DAYOFF)
    Observable<Void> submitDayOff(@Body DayOffContent dayOffContent);

    @GET(Constants.EndPoint.ALBUM)
    Observable<Photos> getAlbum(@Path(Constants.Params.CLASSID) String classId, @Path(Constants.Params.ALBUMID) String albumId, @Query(Constants.Params.PAGESIZE) int pageSize, @Query(Constants.Params.PAGEINDEX) int pageIndex);

    // Teacher
    @GET(Constants.EndPoint.CLASSLIST)
    Observable<ArrayList<ClassInfo>> getClassList(@Path(Constants.Params.USERID) String userId);

    @Headers({"Domain-Name: douban"})
    @GET(Constants.EndPoint.DAYOFFLIST)
    Observable<DayOffList> getDayOffList(@Path(Constants.Params.CLASSID) String classId);

    @GET(Constants.EndPoint.CHILDINCLASS)
    Observable<ArrayList<Kid>> getChildreninClass(@Path(Constants.Params.CLASSID) String classId, @Path(Constants.Params.WEEK) int week, @Path(Constants.Params.YEAR) int year);

    @Headers({"Domain-Name: douban"}) // Add the Domain-Name header
    @GET(Constants.EndPoint.CLASS_NOTES)
    Observable<Notes> getClassNotes(@Path(Constants.Params.CLASSID) String classId, @Path(Constants.Params.YEAR) int year, @Path(Constants.Params.MONTH) int month, @Path(Constants.Params.DAY) int day);

    @POST(Constants.EndPoint.POSTCOMMENTS)
    Observable<Void> postComments(@Path(Constants.Params.CLASSID) String classId, @Body CommentContent commentContent);

    @POST(Constants.EndPoint.CREATE_ALBUM)
    Observable<AlbumId> createAlbum(@Path(Constants.Params.SCHOOLID) String schoolId, @Path(Constants.Params.CLASSID) String classId, @Body AlbumContent albumContent);

    @Headers({"Domain-Name: douban"})
    @Multipart
    @POST(Constants.EndPoint.UPLOAD_IMAGE)
    Observable<ArrayList<Photo>> uploadImage(@Path(Constants.Params.TOKEN) String token, @Part MultipartBody.Part image);

    @Headers({"Domain-Name: douban"})
    @Multipart
    @POST(Constants.EndPoint.UPLOAD_IMAGE)
    Observable<ArrayList<String>> uploadFile(@Path(Constants.Params.TOKEN) String token, @Part MultipartBody.Part file,
                                    @Part("file") RequestBody name);

    @POST(Constants.EndPoint.CREATE_IMAGE)
    Observable<CreateImageRespone> createImage(@Path(Constants.Params.SCHOOLID) String schoolId, @Path(Constants.Params.CLASSID) String classId, @Path(Constants.Params.ALBUMID) String albumId, @Body PhotoContent photoContent);
}
