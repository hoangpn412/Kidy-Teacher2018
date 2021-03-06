package vn.com.kidy.teacher.view.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import vn.com.kidy.teacher.R;
import vn.com.kidy.teacher.data.Constants;
import vn.com.kidy.teacher.data.model.login.ClassInfo;
import vn.com.kidy.teacher.data.model.media.AlbumContent;
import vn.com.kidy.teacher.data.model.media.Photo;
import vn.com.kidy.teacher.data.model.media.PhotoContent;
import vn.com.kidy.teacher.data.model.media.Photos;
import vn.com.kidy.teacher.interactor.AlbumInteractor;
import vn.com.kidy.teacher.network.client.Client;
import vn.com.kidy.teacher.presenter.AlbumPresenter;
import vn.com.kidy.teacher.utils.GridSpacingItemDecoration;
import vn.com.kidy.teacher.utils.Tools;
import vn.com.kidy.teacher.view.activity.MainActivity;
import vn.com.kidy.teacher.view.adapter.AlbumAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlbumFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlbumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumFragment extends Fragment implements AlbumPresenter.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String albumId, title;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.rv_album)
    RecyclerView rv_album;
    @BindView(R.id.progress_loading)
    ProgressBar progress_loading;
    @BindView(R.id.rl_toolbar)
    RelativeLayout rl_toolbar;
    @BindView(R.id.txt_note_name)
    TextView txt_note_name;
    @BindView(R.id.txt_note_time)
    TextView txt_note_time;
    @BindView(R.id.btn_back)
    ImageView btn_back;
    @BindView(R.id.fb_add_images)
    FloatingActionButton fb_add_images;

    private AlbumPresenter albumPresenter;
    private AlbumAdapter adapter;
    private Photos photos;

    private int classPos;
    private ClassInfo cls;

    private int imageUpload = 0;
    private int imageUploaded = 0;
    private ArrayList<String> imagesUpload = new ArrayList<>();

    public AlbumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param albumId Parameter 1.
     * @return A new instance of fragment AlbumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlbumFragment newInstance(int kidPos, String albumId, String title) {
        AlbumFragment fragment = new AlbumFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, kidPos);
        args.putString(ARG_PARAM2, albumId);
        args.putString(ARG_PARAM3, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            albumId = getArguments().getString(ARG_PARAM2);
            title = getArguments().getString(ARG_PARAM3);
            classPos = getArguments().getInt(ARG_PARAM1);
            cls = ((MainActivity) getActivity()).getClasses().get(classPos);
        }
        albumPresenter = new AlbumPresenter(new AlbumInteractor(new Client(Constants.API_SLL_URL)));
        albumPresenter.setView(this);
        adapter = new AlbumAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_album, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            rl_toolbar.setPadding(0, Tools.getStatusBarHeight(getContext()), 0, 0);
        }

        if (photos != null && photos.getPhotos().size() > 0) {
            progress_loading.setVisibility(View.INVISIBLE);
        } else {
            progress_loading.setVisibility(View.VISIBLE);
            albumPresenter.onGetAlbum(cls.getId(), albumId, 100, 0);
        }
        txt_note_name.setText("ALBUM");
        txt_note_time.setText(title);
        btn_back.setOnClickListener(view1 -> getActivity().onBackPressed());

        rv_album.requestFocus();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        rv_album.setLayoutManager(mLayoutManager);
        rv_album.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(8), true));
        rv_album.setAdapter(adapter);
        adapter.setItemClickListener((items, item, position) -> {
            String[] images = new String[items.size()];
            for (int i = 0; i < items.size(); i++) {
                images[i] = items.get(i).getPath();
            }
            ((MainActivity) getActivity()).addViewImageFragment(images, position);
        });

        //
        fb_add_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.create(AlbumFragment.this) // Activity or Fragment
                        .start();
            }
        });
    }


    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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
    public void getDataSuccess(Photos photos) {
        this.photos = photos;
        progress_loading.setVisibility(View.INVISIBLE);
        adapter.setItems(photos.getPhotos());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getDataError(int statusCode) {

    }

    @Override
    public void uploadSuccess(ArrayList<String> photos) {
        imageUploaded += photos.size();
        Log.e("a", "ImageUploaded: " + imageUploaded + "/" + imageUpload);
        imagesUpload.addAll(photos);

        try {
            txt_uploading.setText("Uploading: " + imageUploaded + "/" + imageUpload);
            if (imageUploaded == imageUpload) {
                alertDialogAndroid.dismiss();
                Log.e("a", imagesUpload.size() + "....");
                PhotoContent photoContent = new PhotoContent(imagesUpload);
                albumPresenter.onCreateImage(cls.getSchoolId(), cls.getId(), albumId, photoContent);
            }
        } catch (Exception ex) {

        }
    }

    @Override
    public void createImageSuccess() {
        Log.e("a", "Create Image Success");
        ArrayList<Photo> photos = this.photos.getPhotos();
        Photo photo;
        for (int i = 0; i < imagesUpload.size(); i++) {
            photo = new Photo();
            photo.setId(i);
            photo.setName("" + i);
            photo.setPath(imagesUpload.get(i));
            photos.add(photo);
            Log.e("a", "Photo path: " + i + " " + photo.getPath());
        }
        adapter.setItems(photos);
        adapter.notifyDataSetChanged();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<Image> images = ImagePicker.getImages(data);
        if (images != null && !images.isEmpty()) {
            Log.e("a", "images: " + images.size() + " " + images.get(0).getPath());
            imageUpload = images.size();
            imageUploaded = 0;

            imagesUpload.clear();
            showDialogUploadImage();

            for (int i = 0; i < images.size(); i++) {
                String filepath = images.get(i).getPath();
                File imagefile = new File(filepath);
                String fileName = imagefile.getName();
                Log.e("a", fileName + "...");
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(imagefile);
                    uploadImage(getBytes(fis), fileName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
    }

    private void uploadImage(byte[] imageBytes, String fileName) {

        Log.e("a", "uploadImage: " + imageBytes.length);

        RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"), imageBytes);

        try {
            Log.e("a", (requestFile == null) + " " + requestFile.contentLength());
        } catch (IOException e) {
            e.printStackTrace();
        }

        MultipartBody.Part body = MultipartBody.Part.createFormData("files", fileName, requestFile);

//        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file1", "image1", requestFile);
//        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), "image1");


        RetrofitUrlManager.getInstance().putDomain("douban", Constants.API_UPLOAD_URL);
        albumPresenter.onUploadFile(((MainActivity) getActivity()).getToken(), body, requestFile);
    }

    private AlertDialog alertDialogAndroid;
    private TextView txt_uploading;

    private void showDialogUploadImage() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_upload, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
        alertDialogBuilderUserInput.setView(mView);

        final ProgressBar progress_loading = mView.findViewById(R.id.progress_uploading);
        txt_uploading = mView.findViewById(R.id.txt_uploading);
        txt_uploading.setText("Uploading: " + imageUploaded + "/" + imageUpload);

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setNegativeButton("Huỷ", (dialogBox, id) -> {
                    // ToDo get user input here

                });

        alertDialogAndroid = alertDialogBuilderUserInput.setCancelable(false).setTitle("Upload Image").create();
        alertDialogAndroid.show();
    }
}
