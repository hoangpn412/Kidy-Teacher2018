package vn.com.kidy.teacher.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.com.kidy.teacher.R;
import vn.com.kidy.teacher.data.Constants;
import vn.com.kidy.teacher.data.model.comment.Comment;
import vn.com.kidy.teacher.data.model.comment.CommentContent;
import vn.com.kidy.teacher.data.model.login.ClassInfo;
import vn.com.kidy.teacher.data.model.login.Kid;
import vn.com.kidy.teacher.interactor.PostCommentInteractor;
import vn.com.kidy.teacher.network.client.Client;
import vn.com.kidy.teacher.presenter.ClassListPresenter;
import vn.com.kidy.teacher.presenter.PostCommentPresenter;
import vn.com.kidy.teacher.utils.Tools;
import vn.com.kidy.teacher.view.activity.MainActivity;
import vn.com.kidy.teacher.view.adapter.KidAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PostCommentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PostCommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostCommentFragment extends Fragment implements View.OnClickListener, PostCommentPresenter.View{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int classPos;
    private ClassInfo cls;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.rl_main)
    RelativeLayout rl_main;
    @BindView(R.id.btn_back)
    ImageView btn_back;
    @BindView(R.id.txt_note_name)
    TextView txt_note_name;
    @BindView(R.id.txt_note_time)
    TextView txt_note_time;
    @BindView(R.id.txt_view_history)
    TextView txt_view_history;
    @BindView(R.id.edt_comment)
    EditText edt_comment;
    @BindView(R.id.btn_submit_comment)
    Button btn_submit_comment;
    @BindView(R.id.rv_class)
    RecyclerView rv_class;

    private PostCommentPresenter postCommentPresenter;

    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1;
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int week = calendar.get(Calendar.WEEK_OF_YEAR);

    private ArrayList<Kid> classList;

    private KidAdapter adapter;

    public PostCommentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment PostCommentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostCommentFragment newInstance(int param1) {
        PostCommentFragment fragment = new PostCommentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            classPos = getArguments().getInt(ARG_PARAM1);
            cls = ((MainActivity) getActivity()).getClasses().get(classPos);
        }
        postCommentPresenter = new PostCommentPresenter(new PostCommentInteractor(new Client(Constants.API_SLL_URL)));
        postCommentPresenter.setView(this);

        classList = ((MainActivity) getActivity()).getCommentList();
        adapter = new KidAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_comment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            rl_main.setPadding(0, Tools.getStatusBarHeight(getContext()), 0, 0);
        }

        txt_note_name.setText("Nhận xét");
        txt_note_time.setText(Tools.longtoDate(calendar.getTimeInMillis()));

        rv_class.requestFocus();
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_class.setLayoutManager(linearLayoutManager);
        rv_class.setAdapter(adapter);
        adapter.setItems(classList);
        adapter.setItemClickListener(new KidAdapter.ItemClickListener() {
            @Override
            public void onItemClick(List<Kid> classes, Kid cls, int position) {
            }

            @Override
            public void onItemCheckedClick(List<Kid> classes, Kid cls, int position, boolean isChecked) {

            }

        });

        btn_submit_comment.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        txt_view_history.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit_comment:
                submitComment();
                break;
            case R.id.btn_back:
                ((MainActivity) getActivity()).onBackPressed();
                break;
        }
    }

    private void submitComment() {
        String content = edt_comment.getText().toString();
        if (content != null && content.length() > 0) {
            String teacherId = ((MainActivity) getActivity()).getUser().getContact().getUserId();

            ArrayList<String> childrenIds = new ArrayList<>();
            for (int i = 0; i < classList.size(); i ++) {
                childrenIds.add(classList.get(i).getBabyId());
            }
            //

            CommentContent commentContent = new CommentContent(teacherId, week, year, childrenIds, content);
            postCommentPresenter.onPostComment(cls.getId(), commentContent);
        } else {
            Toast.makeText(getContext(), "Bạn chưa nhập nội dung", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void postCommentSuccess() {
        Log.e("a", "comment Success");
        Toast.makeText(getContext(), "Đã nhận xét thành công", Toast.LENGTH_SHORT).show();
        for (int i = 0; i < classList.size(); i ++) {
            classList.get(i).setHasComment(true);
        }
        ((MainActivity) getActivity()).updatehasComment();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getDataError(int statusCode) {

    }

    @Override
    public Context context() {
        return null;
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
