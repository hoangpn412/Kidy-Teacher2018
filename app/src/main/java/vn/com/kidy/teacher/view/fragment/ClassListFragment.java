package vn.com.kidy.teacher.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.com.kidy.teacher.R;
import vn.com.kidy.teacher.data.Constants;
import vn.com.kidy.teacher.data.model.login.ClassInfo;
import vn.com.kidy.teacher.data.model.login.Kid;
import vn.com.kidy.teacher.interactor.ClassListInteractor;
import vn.com.kidy.teacher.network.client.Client;
import vn.com.kidy.teacher.presenter.ClassListPresenter;
import vn.com.kidy.teacher.utils.Tools;
import vn.com.kidy.teacher.view.activity.MainActivity;
import vn.com.kidy.teacher.view.adapter.KidAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClassListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClassListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassListFragment extends Fragment implements ClassListPresenter.View, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private int classPos;
    private ClassInfo cls;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.progress_loading)
    ProgressBar progress_loading;
    @BindView(R.id.rv_class)
    RecyclerView rv_class;
    @BindView(R.id.fb_send)
    FloatingActionButton fb_send;

    private ArrayList<Kid> classList;

    private ClassListPresenter classListPresenter;

    private KidAdapter adapter;

    public ClassListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param classPos Parameter 1.
     * @return A new instance of fragment ClassListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClassListFragment newInstance(int classPos) {
        ClassListFragment fragment = new ClassListFragment();
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
        }
        classListPresenter = new ClassListPresenter(new ClassListInteractor(new Client(Constants.API_SLL_URL)));
        classListPresenter.setView(this);
        adapter = new KidAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_class_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (classList != null && classList.size() > 0) {
            progress_loading.setVisibility(View.INVISIBLE);
            getDataSuccess(classList);
        } else {
            progress_loading.setVisibility(View.VISIBLE);
            classListPresenter.onGetChildreninClass(cls.getId());
        }

        fb_send.setOnClickListener(this);

        rv_class.requestFocus();
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_class.setLayoutManager(linearLayoutManager);
        rv_class.setAdapter(adapter);
        adapter.setItemClickListener(new KidAdapter.ItemClickListener() {
            @Override
            public void onItemClick(List<Kid> classes, Kid cls, int position) {
                onKidClick(cls);
            }

            @Override
            public void onItemCheckedClick(List<Kid> classes, Kid cls, int position, boolean isChecked) {
                onKidCheckedClick(cls, position, isChecked);
            }
        });
        adapter.setIsChoose(((MainActivity) getActivity()).getChoosePressed());
    }

    public void updatehasComment(ArrayList<Kid> commentList) {
        for (int i = 0; i < commentList.size(); i ++) {
            for (int j = 0; j < classList.size(); j ++) {
                if (classList.get(j).getBabyId().equals(commentList.get(i).getBabyId())) {
                    classList.get(j).setHasComment(true);
                    break;
                }
            }
        }
        adapter.setItems(classList);
        adapter.notifyDataSetChanged();
    }
    public void onTextChoosePressed(boolean isPressed) {
        adapter.setIsChoose(isPressed);
        if (!isPressed) {
            fb_send.setVisibility(View.INVISIBLE);
        }
    }

    private void onKidClick(Kid kid) {
        if (((MainActivity) getActivity()).getChoosePressed()) return;

        ArrayList<Kid> commentList = new ArrayList<>();
        commentList.add(kid);
        ((MainActivity) getActivity()).setCommentList(commentList);
        ((MainActivity) getActivity()).addPostCommentFragment();
    }

    private void onKidCheckedClick(Kid kid, int position, boolean checked) {
        classList.get(position).setIsChecked(checked);
        int checkedCount = 0;
        for (int i = 0; i < classList.size(); i ++) {
            if (classList.get(i).getIsChecked()) {
                checkedCount ++;
            }
        }

        if (checkedCount == 0) {
            fb_send.setVisibility(View.INVISIBLE);
        } else {
            fb_send.setVisibility(View.VISIBLE);
        }
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
    public void getDataSuccess(ArrayList<Kid> classList) {
        Log.e("a", "getClassListSuccess: " + classList.size());
        this.classList = classList;

        progress_loading.setVisibility(View.INVISIBLE);
        adapter.setItems(classList);
        adapter.notifyDataSetChanged();
        rv_class.smoothScrollToPosition(0);
    }

    @Override
    public void getDataError(int statusCode) {

    }

    @Override
    public Context context() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fb_send:
                ArrayList<Kid> commentList = new ArrayList<>();
                for (int i = 0; i < classList.size(); i ++) {
                    if (classList.get(i).getIsChecked()) {
                        Log.e("a", i + " " + classList.get(i).getIsChecked());
                        commentList.add(classList.get(i));
                    }
                }
                ((MainActivity) getActivity()).setCommentList(commentList);
                ((MainActivity) getActivity()).addPostCommentFragment();
                break;
        }
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
