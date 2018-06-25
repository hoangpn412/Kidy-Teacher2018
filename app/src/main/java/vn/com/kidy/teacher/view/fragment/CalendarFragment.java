package vn.com.kidy.teacher.view.fragment;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.com.kidy.teacher.R;
import vn.com.kidy.teacher.data.Constants;
import vn.com.kidy.teacher.data.model.calendar.TimeTable;
import vn.com.kidy.teacher.data.model.login.ClassInfo;
import vn.com.kidy.teacher.data.model.note.Message;
import vn.com.kidy.teacher.data.model.note.Notes;
import vn.com.kidy.teacher.interactor.CalendarInteractor;
import vn.com.kidy.teacher.network.client.Client;
import vn.com.kidy.teacher.presenter.CalendarPresenter;
import vn.com.kidy.teacher.utils.Tools;
import vn.com.kidy.teacher.view.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalendarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment implements CalendarPresenter.View, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private int classPos;
    private String classId;
    private ClassInfo cls;

    private ArrayList<TimeTable> meanMenu, timeTables;

    private OnFragmentInteractionListener mListener;

    private CalendarPresenter calendarPresenter;

    private boolean dataFetched = false;
    private long curDate;
    private int year, month, day, dayofWeek, todayYear, todayMonth, todayDay;
//    private Calendar calendar;

    @BindView(R.id.sc_data)
    ScrollView sc_data;
    @BindView(R.id.rl_date)
    RelativeLayout rl_date;

    @BindView(R.id.card_home_note)
    LinearLayout card_home_note;
    @BindView(R.id.card_home_note_data)
    LinearLayout card_home_note_data;
    @BindView(R.id.txt_card_note_name)
    TextView txt_card_note_name;
    @BindView(R.id.txt_card_note_date)
    TextView txt_card_note_date;

    @BindView(R.id.ln_time_table_content)
    LinearLayout ln_time_table_content;
    @BindView(R.id.ln_foot_menu_content)
    LinearLayout ln_foot_menu_content;
    @BindView(R.id.progress_loading)
    ProgressBar progress_loading;
    //    @BindView(R.id.rl_add_note)
//    RelativeLayout rl_add_note;
//    @BindView(R.id.rl_add_request)
//    RelativeLayout rl_add_request;
    @BindView(R.id.txt_date)
    TextView txt_date;

    private Notes notes;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param classPos Parameter 1.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(int classPos) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, classPos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            classPos = getArguments().getInt(ARG_PARAM1);
            cls = ((MainActivity) getActivity()).getClasses().get(classPos);
            classId = cls.getId();
        }
        calendarPresenter = new CalendarPresenter(new CalendarInteractor(new Client(Constants.API_SLL_URL)));
        calendarPresenter.setView(this);
        curDate = System.currentTimeMillis();

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        year = calendar.get(java.util.Calendar.YEAR);
        month = calendar.get(java.util.Calendar.MONTH) + 1;
        day = calendar.get(java.util.Calendar.DAY_OF_MONTH);
        dayofWeek = calendar.get(java.util.Calendar.DAY_OF_WEEK);
        todayDay = day;
        todayMonth = month;
        todayYear = year;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Log.e("a", "onViewCreated CalendarFragment");

        txt_card_note_date.setVisibility(View.INVISIBLE);
        rl_date.setOnClickListener(this);

        txt_date.setText(Html.fromHtml(Tools.longtoDateWithDayofWeekString(curDate)));

        if (!dataFetched || notes == null || meanMenu == null || timeTables == null) {
            if (classPos != ((MainActivity) getActivity()).getClassPos()) {
                classPos = ((MainActivity) getActivity()).getClassPos();
                cls = ((MainActivity) getActivity()).getClasses().get(classPos);
                classId = cls.getId();
            }
            getData();
        } else {
            if (classPos != ((MainActivity) getActivity()).getClassPos()) {
                classPos = ((MainActivity) getActivity()).getClassPos();
                cls = ((MainActivity) getActivity()).getClasses().get(classPos);
                classId = cls.getId();
                refreshData();
            } else {
                getClassNotesSuccess(notes);
                getMeanMenuSuccess(meanMenu);
                getTimeTableSuccess(timeTables);
            }
        }
    }

    private void getData() {
//        calendarPresenter.onGetHomeNotes(cls.getId(), cls.getBabyId(), year, month, day);
        calendarPresenter.onGetClassNotes(cls.getId(), year, month, day);
        calendarPresenter.onGetTimeTables(cls.getId(), dayofWeek);
        calendarPresenter.onGetMeanMenu(cls.getId(), dayofWeek);
    }

    private void refreshData() {
        Log.e("a", "refreshData Calendar");
        dataFetched = false;

        getData();

        progress_loading.setVisibility(View.VISIBLE);
        sc_data.setVisibility(View.INVISIBLE);
    }

    public void refreshDateData(CalendarDay calendarDay) {
        Log.e("a", "refreshDateData");
        curDate = calendarDay.getDate().getTime();

        year = calendarDay.getYear();
        month = calendarDay.getMonth() + 1;
        day = calendarDay.getDay();
        dayofWeek = calendarDay.getCalendar().get(java.util.Calendar.DAY_OF_WEEK);

        txt_date.setText(Html.fromHtml(Tools.longtoDateWithDayofWeekString(curDate)));
        refreshData();
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
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void addParentTitle(Message note) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_note_title, null, false);
        TextView txt_note_title = view.findViewById(R.id.txt_note_title);
        Button btn_reply = view.findViewById(R.id.btn_reply);
//        if (year != todayYear || month != todayMonth || day != todayDay) {
        btn_reply.setVisibility(View.INVISIBLE);
//        }
        btn_reply.setOnClickListener(view1 -> Toast.makeText(getContext(), "Reply: " + note.getChildName(), Toast.LENGTH_LONG).show());

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
        Log.e("a", "getDataerror: " + statusCode);
    }

    @Override
    public void getClassNotesSuccess(Notes notes) {
        Log.e("a", "getCalendarNotesSuccess: " + notes.getMessages().size());

        progress_loading.setVisibility(View.INVISIBLE);
        sc_data.setVisibility(View.VISIBLE);

        card_home_note_data.removeAllViews();
        this.notes = notes;
//        ((MainActivity) getActivity()).setNotes(notes);
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
    public void getTimeTableSuccess(ArrayList<TimeTable> timeTables) {
        // Card Time Table
        this.timeTables = timeTables;
        ln_time_table_content.removeAllViews();
        TimeTable timeData;
        for (int i = 0; i < timeTables.size(); i++) {
            timeData = timeTables.get(i);
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_time_table_content, null, false);
            TextView txt_time = view.findViewById(R.id.txt_table_time);
            TextView txt_content = view.findViewById(R.id.txt_table_content);
            txt_time.setText(timeData.getTime());
            txt_content.setText(Html.fromHtml(timeData.getContent()));
            ln_time_table_content.addView(view);
        }
    }

    @Override
    public void getMeanMenuSuccess(ArrayList<TimeTable> meanMenu) {
        this.meanMenu = meanMenu;
        ln_foot_menu_content.removeAllViews();
        TimeTable timeData;
        for (int i = 0; i < meanMenu.size(); i++) {
            timeData = meanMenu.get(i);
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_time_table_content, null, false);
            TextView txt_time = view.findViewById(R.id.txt_table_time);
            TextView txt_content = view.findViewById(R.id.txt_table_content);
            txt_time.setText(timeData.getTime());
            txt_content.setText(Html.fromHtml(timeData.getContent()));
            ln_foot_menu_content.addView(view);
        }
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
            case R.id.rl_date:
                ((MainActivity) getActivity()).addFullCalendarFragment(classPos, curDate);
                break;
        }
    }

    public void addRequest() {
        ((MainActivity) getActivity()).addRequestFragment();
    }

    private void addNote() {
        ((MainActivity) getActivity()).addNoteFragment();
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
}
