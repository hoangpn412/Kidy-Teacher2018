package vn.com.kidy.teacher.view.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.com.kidy.teacher.R;
import vn.com.kidy.teacher.data.Constants;
import vn.com.kidy.teacher.data.model.dayoff.ChildrenOff;
import vn.com.kidy.teacher.data.model.dayoff.DayOffList;
import vn.com.kidy.teacher.data.model.login.ClassInfo;
import vn.com.kidy.teacher.data.model.news.News;
import vn.com.kidy.teacher.data.model.news.NewsList;
import vn.com.kidy.teacher.data.model.note.Message;
import vn.com.kidy.teacher.data.model.note.NoteContent;
import vn.com.kidy.teacher.data.model.note.Notes;
import vn.com.kidy.teacher.interactor.HomeInteractor;
import vn.com.kidy.teacher.network.client.Client;
import vn.com.kidy.teacher.network.retrofit.RetrofitService;
import vn.com.kidy.teacher.presenter.HomePresenter;
import vn.com.kidy.teacher.view.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements HomePresenter.View, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private int classPos;
    private String classId;
    private ClassInfo cls;
    //    private Home home;
    private Notes notes;
    private News news;
    //    private Medias medias;
    private DayOffList dayoffList;

    private OnFragmentInteractionListener mListener;

    private HomePresenter homePresenter;

    @BindView(R.id.progress_loading)
    ProgressBar progress_loading;
    @BindView(R.id.sc_data)
    ScrollView sc_data;
    @BindView(R.id.item_home_news)
    RelativeLayout item_home_news;
    //    @BindView(R.id.item_home_media)
//    RelativeLayout item_home_media;
    @BindView(R.id.txt_card_news_name)
    TextView txt_card_news_name;

    //    @BindView(R.id.rl_add_note)
//    RelativeLayout rl_add_note;
//    @BindView(R.id.rl_add_request)
//    RelativeLayout rl_add_request;
//    @BindView(R.id.media_image)
//    SimpleDraweeView media_image;
//    @BindView(R.id.txt_media_name)
//    TextView txt_media_name;
//    @BindView(R.id.txt_media_date)
//    TextView txt_media_date;
    @BindView(R.id.card_home_news)
    LinearLayout card_home_news;
    @BindView(R.id.card_home_news_data)
    LinearLayout card_home_news_data;
    //
    @BindView(R.id.card_home_note)
    LinearLayout card_home_note;
    @BindView(R.id.card_home_note_data)
    LinearLayout card_home_note_data;
    @BindView(R.id.txt_card_note_name)
    TextView txt_card_note_name;
    @BindView(R.id.txt_card_note_date)
    TextView txt_card_note_date;

    @BindView(R.id.ln_dayoff_content)
    LinearLayout ln_dayoff_content;

    private boolean dataFetched = false;

    private Message addNote;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param classPos Parameter 1.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(int classPos) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, classPos);
        fragment.setArguments(args);
        Log.e("a", "newInstance: " + classPos);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("a", "onCreate");
        if (getArguments() != null) {
            classPos = getArguments().getInt(ARG_PARAM1);
            cls = ((MainActivity) getActivity()).getClasses().get(classPos);
            classId = cls.getId();
        }
        homePresenter = new HomePresenter(new HomeInteractor(new Client(Constants.API_NEWS_URL)));
        homePresenter.setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        Log.e("a", "onViewCreated Home Fragment: " + classPos + " " + ((MainActivity) getActivity()).getClassPos());

//        rl_add_note.setOnClickListener(this);
//        rl_add_request.setOnClickListener(this);

        if (!dataFetched || news == null || notes == null || dayoffList == null) {
            if (classPos != ((MainActivity) getActivity()).getClassPos()) {
                classPos = ((MainActivity) getActivity()).getClassPos();
                cls = ((MainActivity) getActivity()).getClasses().get(classPos);
                classId = cls.getId();
            }
            getHomeData();
        } else {
            if (classPos != ((MainActivity) getActivity()).getClassPos()) {
                classPos = ((MainActivity) getActivity()).getClassPos();
                cls = ((MainActivity) getActivity()).getClasses().get(classPos);
                classId = cls.getId();
                Log.e("a", "classPos: " + classPos + " " + cls.getId());
                refreshData();
            } else {
                getHomeNewsSuccess(news);
                getClassNotesSuccess(notes);
                getDayOffListSuccess(dayoffList);
//                getHomeMediaSuccess(medias);
            }
        }
    }


    private void setNewBaseUrl(String baseUrl) {
        Retrofit retrofit = retrofitBuilder(baseUrl);
        retrofit.create(RetrofitService.class);
    }


    private Retrofit retrofitBuilder(String baseUrl) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();

        return new Retrofit.Builder().baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    private void getHomeData() {
//        setNewBaseUrl(Constants.API_NEWS_URL);
        homePresenter.onGetHomeNews(cls.getSchoolUuid());
        RetrofitUrlManager.getInstance().putDomain("douban", Constants.API_SLL_URL);
        homePresenter.onGetDayOffList(cls.getId());
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        RetrofitUrlManager.getInstance().putDomain("douban", Constants.API_SLL_URL);
        homePresenter.onGetClassNotes(cls.getId(), year, month, day);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void refreshData() {
        dataFetched = false;
        getHomeData();
        progress_loading.setVisibility(View.VISIBLE);
        sc_data.setVisibility(View.INVISIBLE);
    }

//    @Override
//    public void getDataSuccess(Home home) {
//        Log.e("a", "getDataSuccess");
//        dataFetched = true;
//        progress_loading.setVisibility(View.INVISIBLE);
//        this.home = home;
//        ((MainActivity) getActivity()).setNotes(home.getNotes());
//
//        if (home.getStatus() == Constants.STATUS_CODE.SERVER_ERROR) {
//            Toast.makeText(getContext(), home.getMessage(), Toast.LENGTH_LONG).show();
//            return;
//        }
//        // Data Success
//        sc_data.setVisibility(View.VISIBLE);
//
//        // Card Home
//        if (home.getNews().getData().size() == 0) {
//            item_home_news.setVisibility(View.GONE);
//        } else {
//            card_home_news_data.removeAllViews();
//            txt_card_news_name.setText(home.getNews().getTitle());
//
//            int newsSize = home.getNews().getData().size();
//            for (int i = 0; i < newsSize; i++) {
//                final NewsList mNew = home.getNews().getData().get(i);
//                View view = LayoutInflater.from(getContext()).inflate(R.layout.item_new, null, false);
//                TextView txt_title_new = view.findViewById(R.id.txt_title_new);
//                TextView txt_time_new = view.findViewById(R.id.txt_time_new);
//                txt_title_new.setText(mNew.getNewsTitle());
//                txt_time_new.setText(mNew.getCreatedDate());
//                if (i == newsSize - 1) {
//                    View view_line_new = view.findViewById(R.id.view_line_new);
//                    view_line_new.setVisibility(View.GONE);
//                }
//                view.setOnClickListener(view1 -> ((MainActivity) getActivity()).addArticleFragment(mNew.getNewsContent(), mNew.getNewsId(), mNew.getCreatedDate(), mNew.getNewsPresentImage()));
//                card_home_news_data.addView(view);
//            }
//        }
//
//        // Card Note
////        txt_card_note_name.setText(home.getNotes().getTitle());
////        txt_card_note_date.setText(home.getNotes().getDateStr());
//        card_home_note_data.removeAllViews();
//        Message note;
//        for (int i = 0; i < home.getNotes().getMessages().size(); i++) {
//            note = home.getNotes().getMessages().get(i);
//            addNote(note, false);
//        }
//        // Card Media
//        Log.e("a", home.getMedia().getTitle() + "...");
//        if (home.getMedia().getTitle().equals("")) {
//            item_home_media.setVisibility(View.GONE);
//        } else {
//            txt_media_name.setText(home.getMedia().getTitle());
//            txt_media_date.setText(home.getMedia().getDateStr());
//            media_image.setImageURI(Uri.parse(home.getMedia().getImage()));
//            item_home_media.setOnClickListener(view -> {
//                Log.e("a", "Media: " + home.getMedia().getAlbumId());
//                ((MainActivity) getActivity()).addAlbumFragment(home.getMedia().getAlbumId(), home.getMedia().getTitle());
//            });
//        }
//    }



    private void doReplyMessage(Message note, String content) {
        NoteContent noteContent = new NoteContent(content, true);

        RetrofitUrlManager.getInstance().putDomain("douban", Constants.API_SLL_URL);
        homePresenter.submitNote(cls.getId(), note.getChildrenId(), noteContent);

        addNote = new Message();
        addNote.setChildName(note.getChildName());
        addNote.setChildrenId(note.getChildrenId());
        addNote.setContent(content);
        addNote.setMesssagePos(note.getMesssagePos());
        addNote.setIsFromParent(false);
//            addNote.setName("Phụ huynh");
        addNote.setContent(content);
    }

    private void showDialogReply(Message note) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
        alertDialogBuilderUserInput.setView(mView);

        final EditText userInputDialogEditText = mView.findViewById(R.id.userInputDialog);
        final TextView dialogTitle = mView.findViewById(R.id.dialogTitle);

        dialogTitle.setText(Html.fromHtml("Trả lời phụ huynh bé <b>" + note.getChildName() + "</b>"));

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Gửi", (dialogBox, id) -> {
                    // ToDo get user input here
                    String content = userInputDialogEditText.getText().toString();
                    if (content == null || content.length() == 0) {
                        Toast.makeText(getContext(), "Bạn chưa nhập nội dung", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    doReplyMessage(note, content);
                })

                .setNegativeButton("Huỷ",
                        (dialogBox, id) -> dialogBox.cancel());

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();

    }

    private void addParentTitle(Message note) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_note_title, null, false);
        TextView txt_note_title = view.findViewById(R.id.txt_note_title);
        Button btn_reply = view.findViewById(R.id.btn_reply);
        btn_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Reply: " + note.getChildName(), Toast.LENGTH_LONG).show();
                showDialogReply(note);
            }
        });

        String html = "Phụ huynh bé <b>";
        html += note.getChildName() + "</b>";
        txt_note_title.setText(Html.fromHtml(html));
        card_home_note_data.addView(view);
    }

    public void addNote(Message note, boolean insert) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_note, null, false);
        View view_circle = view.findViewById(R.id.view_note_circle);
        TextView txt_note_content = view.findViewById(R.id.txt_note_content);
        String html;
        if (!note.getIsFromParent()) {
            view_circle.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.circle_blue));
            html = "<font color=\"#2CA4A2\">Giáo viên: </font>";
        } else {
            html = "<font color=\"#F9A83C\">Phụ huynh: </font>";
            view_circle.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.cirle_orange));
        }
        html += note.getContent();
        txt_note_content.setText(Html.fromHtml(html));
        if (insert) {
            card_home_note_data.addView(view, 0);
//            home.getNotes().getData().add(0, note);
        } else {
            card_home_note_data.addView(view);
        }
    }

    @Override
    public void getDataError(int statusCode) {

    }

    @Override
    public void getHomeNewsSuccess(News news) {
        this.news = news;
        progress_loading.setVisibility(View.INVISIBLE);
        Log.e("a", news.getNewsList().size() + " news");
        sc_data.setVisibility(View.VISIBLE);
        if (news.getNewsList().size() == 0) {
            card_home_news.setVisibility(View.GONE);
        } else {
            card_home_news_data.removeAllViews();
            int newsSize = news.getNewsList().size();
            if (newsSize > 3) newsSize = 3;
            for (int i = 0; i < newsSize; i++) {
                final NewsList mNew = news.getNewsList().get(i);
                View view = LayoutInflater.from(getContext()).inflate(R.layout.item_new, null, false);
                TextView txt_title_new = view.findViewById(R.id.txt_title_new);
                TextView txt_time_new = view.findViewById(R.id.txt_time_new);
                txt_title_new.setText(mNew.getNewsTitle());
                txt_time_new.setText(Html.fromHtml(mNew.getCreatedDate()));
                if (i == newsSize - 1) {
                    View view_line_new = view.findViewById(R.id.view_line_new);
                    view_line_new.setVisibility(View.GONE);
                }
                view.setOnClickListener(view1 -> ((MainActivity) getActivity()).addArticleFragment(mNew.getNewsTitle(), mNew.getNewsContent(), mNew.getCreatedDate(), mNew.getNewsPresentImage()));
                card_home_news_data.addView(view);
            }
        }
    }

    @Override
    public void getClassNotesSuccess(Notes notes) {
        Log.e("a", "getHomeNotesSuccess");
        card_home_note_data.removeAllViews();
        this.notes = notes;
        ((MainActivity) getActivity()).setNotes(notes);
        Message note;
        int count = -1;
        for (int i = 0; i < notes.getMessages().size(); i++) {
            Log.e("a", count + " " + notes.getMessages().get(i).getMesssagePos());
            note = notes.getMessages().get(i);
            if (notes.getMessages().get(i).getMesssagePos() == count) {
                addNote(note, false);
            } else {
                count++;
                addParentTitle(note);
                addNote(note, false);
            }
        }
    }

    @Override
    public void getDayOffListSuccess(DayOffList dayofflist) {
        Log.e("a", "getDayoffSuccess");
        progress_loading.setVisibility(View.INVISIBLE);
        sc_data.setVisibility(View.VISIBLE);

        this.dayoffList = dayofflist;
        ln_dayoff_content.removeAllViews();
        ChildrenOff childrenOff;
        for (int i = 0; i < dayofflist.getData().size(); i++) {
            childrenOff = dayofflist.getData().get(i);
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_dayoff_content, null, false);
            TextView txt_time = view.findViewById(R.id.txt_table_time);
            TextView txt_content = view.findViewById(R.id.txt_table_content);
            txt_time.setText(childrenOff.getName());
            txt_content.setText(Html.fromHtml(childrenOff.getReason()));
            ln_dayoff_content.addView(view);
        }
    }

    @Override
    public void addNoteSuccess() {
        for (int i = 0; i < notes.getMessages().size(); i ++) {
            if (notes.getMessages().get(i).getMesssagePos() == addNote.getMesssagePos()) {
                notes.getMessages().add(i, addNote);
                break;
            }
        }
        getClassNotesSuccess(this.notes);
    }

    @Override
    public Context context() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_add_note:
                addNote();
                break;
            case R.id.rl_add_request:
                addRequest();
                break;
        }
    }

    public void addNote() {
        ((MainActivity) getActivity()).addNoteFragment();
    }

    public void addRequest() {
        ((MainActivity) getActivity()).addRequestFragment();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
