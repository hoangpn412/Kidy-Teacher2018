package vn.com.kidy.teacher.view.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.com.kidy.teacher.R;
import vn.com.kidy.teacher.data.Constants;
import vn.com.kidy.teacher.data.model.Kid;
import vn.com.kidy.teacher.data.model.note.Message;
import vn.com.kidy.teacher.data.model.note.Notes;
import vn.com.kidy.teacher.data.model.login.ClassInfo;
import vn.com.kidy.teacher.data.model.login.User;
import vn.com.kidy.teacher.data.model.notification.Notification;
import vn.com.kidy.teacher.data.model.notification.Notifications;
import vn.com.kidy.teacher.interactor.MainInteractor;
import vn.com.kidy.teacher.network.client.Client;
import vn.com.kidy.teacher.notification.Config;
import vn.com.kidy.teacher.notification.utils.NotificationUtils;
import vn.com.kidy.teacher.presenter.MainPresenter;
import vn.com.kidy.teacher.utils.Tools;
import vn.com.kidy.teacher.view.adapter.ViewPagerAdapter;
import vn.com.kidy.teacher.view.fragment.AddNoteFragment;
import vn.com.kidy.teacher.view.fragment.AlbumFragment;
import vn.com.kidy.teacher.view.fragment.ArticleFragment;
import vn.com.kidy.teacher.view.fragment.CalendarFragment;
import vn.com.kidy.teacher.view.fragment.ClassListFragment;
import vn.com.kidy.teacher.view.fragment.CommentDetailFragment;
import vn.com.kidy.teacher.view.fragment.FullCalendarFragment;
import vn.com.kidy.teacher.view.fragment.HomeFragment;
import vn.com.kidy.teacher.view.fragment.LoginFragment;
import vn.com.kidy.teacher.view.fragment.MediaFragment;
import vn.com.kidy.teacher.view.fragment.NewsFragment;
import vn.com.kidy.teacher.view.fragment.NotificationFragment;
import vn.com.kidy.teacher.view.fragment.PostCommentFragment;
import vn.com.kidy.teacher.view.fragment.SettingFragment;
import vn.com.kidy.teacher.view.fragment.ViewImageFragment;
import vn.com.kidy.teacher.view.widget.BottomNavigationViewHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, MainPresenter.View {

    @BindView(R.id.fr_login)
    FrameLayout fr_login;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.btn_menu)
    ImageView btn_menu;
    @BindView(R.id.rl_toolbar)
    RelativeLayout rl_toolbar;
    @BindView(R.id.txt_schoolName)
    TextView txt_schoolName;
    @BindView(R.id.txt_className)
    TextView txt_className;
    @BindView(R.id.kid_avatar)
    SimpleDraweeView kid_avatar;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    //Navigation Menu
    @BindView(R.id.txt_acc_name)
    TextView txt_acc_name;
    @BindView(R.id.acc_avatar)
    SimpleDraweeView acc_avatar;
    @BindView(R.id.ln_kid_acc)
    LinearLayout ln_kid_acc;
    @BindView(R.id.ln_setting)
    LinearLayout ln_setting;
    @BindView(R.id.ln_intro)
    LinearLayout ln_intro;
    @BindView(R.id.ln_logout)
    LinearLayout ln_logout;
    @BindView(R.id.txt_notification_size)
    TextView txt_notification_size;
    @BindView(R.id.txt_choose)
    TextView txt_choose;

    private LoginFragment loginFragment;
    //    private Parent parent;
    private User user;
    private ArrayList<ClassInfo> classes;

    private int classPos;
    private ClassInfo cls;
    private Menu bottom_menu;

    private int isLogin = 0;
    private Notes notes = new Notes();
    private Notifications notifications;

    private HomeFragment homeFragment;
    private CalendarFragment calendarFragment;
    private ClassListFragment classListFragment;
    private CalendarDay selectedDate;
    private MainPresenter mainPresenter;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private boolean isLogout;
    private boolean choosePressed = false;
    public String token;

    private ArrayList<vn.com.kidy.teacher.data.model.login.Kid> commentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initView();
        mainPresenter = new MainPresenter(new MainInteractor(new Client(Constants.API_SLL_URL)));
        mainPresenter.setView(this);

        selectedDate = CalendarDay.today();

        initFCM();
        readFirebaseMessage();
    }

    private void readFirebaseMessage() {
        Log.e("a", "read Firebase Messsage");
        if (getIntent().getExtras() != null) {
            String from = getIntent().getExtras().getString("from");
            String data = getIntent().getExtras().getString("data");
            Log.d("a", "Key: " + " Value: " + from + " " + data);
            if (from.contains("news")) {

            }
        }
    }

    private void initFCM() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    Log.e("a", message);
                }
            }
        };

        displayFirebaseRegId();
    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e("a", "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))
            Log.e("a", "Firebase Reg Id: " + regId);
    }

    private void initView() {
        btn_menu.setOnClickListener(this);
        kid_avatar.setOnClickListener(this);
        setIsLogout(false);
        addLoginFragment();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            rl_toolbar.setPadding(0, Tools.getStatusBarHeight(this), 0, 0);
        }
        bottom_menu = bottomNavigationView.getMenu();
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            for (int i = 0; i < bottom_menu.size(); i++) {
                if (item.getItemId() == bottom_menu.getItem(i).getItemId()) {
                    viewPager.setCurrentItem(i);
                    item.setChecked(true);
                    break;
                }
            }
            return false;
        });
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);

        ln_setting.setOnClickListener(this);
        ln_logout.setOnClickListener(this);
        txt_choose.setOnClickListener(this);
    }

    private void initViewPager() {
        bottomNavigationView.setSelectedItemId(bottom_menu.getItem(0).getItemId());

        if (homeFragment == null) {
            homeFragment = HomeFragment.newInstance(classPos);
        }
        if (calendarFragment == null) {
            calendarFragment = CalendarFragment.newInstance(classPos);
        }
        if (classListFragment == null) {
            classListFragment  = ClassListFragment.newInstance(classPos);
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(homeFragment, Constants.FragmentName.LOGIN_FRAGMENT);
        adapter.addFragment(calendarFragment, Constants.FragmentName.CALENDAR_FRAGMENT);
        adapter.addFragment(NewsFragment.newInstance(classPos), Constants.FragmentName.NEWS_FRAGMENT);
        adapter.addFragment(classListFragment, Constants.FragmentName.CLASSLIST_FRAGMENT);
        adapter.addFragment(MediaFragment.newInstance(classPos), Constants.FragmentName.MEDIA_FRAGMENT);

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 3) {
                    txt_choose.setVisibility(View.VISIBLE);
                    kid_avatar.setVisibility(View.INVISIBLE);
                } else {
                    txt_choose.setVisibility(View.INVISIBLE);
                    kid_avatar.setVisibility(View.VISIBLE);
                }
                bottomNavigationView.setSelectedItemId(bottom_menu.getItem(position).getItemId());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setCommentList(ArrayList<vn.com.kidy.teacher.data.model.login.Kid> lstKid) {
        commentList.clear();
        for (int i = 0; i <lstKid.size(); i ++) {
            commentList.add(lstKid.get(i));
        }
    }

    public void updatehasComment() {
        classListFragment.updatehasComment(commentList);
    }
    public ArrayList<vn.com.kidy.teacher.data.model.login.Kid> getCommentList() {
        return this.commentList;
    }
    public boolean getChoosePressed() {
        return this.choosePressed;
    }

    public void setIsLogout(boolean isLogout) {
        this.isLogout = isLogout;
    }

    public boolean getIsLogout() {
        return this.isLogout;
    }
//    public Parent getMyParent() {
//        return parent;
//    }

//    public void setMyParent(Parent parent) {
//        this.parent = parent;
//        isLogin = 1;
//    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
        isLogin = 1;
    }

    public ArrayList<ClassInfo> getClasses() {
        return this.classes;
    }

    public void setClasses(ArrayList<ClassInfo> classes) {
        this.classes = classes;
    }

    public int getClassPos() {
        return this.classPos;
    }

    public ClassInfo getCls() {
        return getClasses().get(getClassPos());
    }

    private void setCurClassData(int position) {
        this.classPos = position;
        this.cls = getClasses().get(classPos);
        txt_schoolName.setText(cls.getSchoolName());
        txt_className.setText(cls.getClassName());

//        if (cls.getAvatar() != null) {
//            kid_avatar.setImageURI(Uri.parse(cls.getAvatar()));
//        }
        if (user.getContact().getAvatar() != null) {
            acc_avatar.setImageURI(Uri.parse(user.getContact().getAvatar()));
        }

        initViewPager();
//        mainPresenter.onGetNotifications(cls.getBabyId());
//        mainPresenter.onGetClassInfo(cls.getClassId());
        txt_notification_size.setVisibility(View.INVISIBLE);
    }

    public void setCurClass(int position) {
        txt_acc_name.setText(user.getContact().getFullName());
        setCurClassData(position);
        ln_kid_acc.removeAllViews();
        View[] ln_kids = new View[getClasses().size()];
        SimpleDraweeView[] img_classes = new SimpleDraweeView[getClasses().size()];
        TextView[] txt_class_name_menus = new TextView[getClasses().size()], txt_class_birthday_menus = new TextView[getClasses().size()], txt_isActives = new TextView[getClasses().size()];
        View[] view_strokes = new View[getClasses().size()];

        for (int i = 0; i < getClasses().size(); i++) {
            ln_kids[i] = LayoutInflater.from(this).inflate(R.layout.item_menu_kid,
                    null, false);
            img_classes[i] = ln_kids[i].findViewById(R.id.kid_avatar_menu);
            txt_class_name_menus[i] = ln_kids[i].findViewById(R.id.txt_kid_name_menu);
            txt_class_birthday_menus[i] = ln_kids[i].findViewById(R.id.txt_kid_birthday_menu);
            txt_isActives[i] = ln_kids[i].findViewById(R.id.txt_isActive);
            view_strokes[i] = ln_kids[i].findViewById(R.id.view_stroke);

//            img_kids[i].setImageURI(Uri.parse(getClasses().get(i).getAvatar()));
            txt_class_name_menus[i].setText(getClasses().get(i).getClassName());
//            txt_class_birthday_menus[i].setText(getClasses().get(i).getBirthday());
            if (classPos == i) {
                txt_isActives[i].setVisibility(View.VISIBLE);
                view_strokes[i].setVisibility(View.VISIBLE);
            } else {
                txt_isActives[i].setVisibility(View.INVISIBLE);
                view_strokes[i].setVisibility(View.INVISIBLE);
            }
            ln_kids[i].setClickable(true);
            final int clickPos = i;
            ln_kids[i].setOnClickListener(view -> {
                txt_isActives[classPos].setVisibility(View.INVISIBLE);
                view_strokes[classPos].setVisibility(View.INVISIBLE);
                txt_isActives[clickPos].setVisibility(View.VISIBLE);
                view_strokes[clickPos].setVisibility(View.VISIBLE);
                classPos = clickPos;
                setCurClassData(classPos);
                drawerLayout.closeDrawer(Gravity.START);
            });
            ln_kid_acc.addView(ln_kids[i]);
        }
    }

    public void setNotes(Notes notes) {
        this.notes = notes;
    }

    public Notes getNotes() {
        return this.notes;
    }

    public void setSelectedDate(CalendarDay date) {
        if (this.selectedDate != date) {
            this.selectedDate = date;
            if (calendarFragment != null) {
                calendarFragment.refreshDateData(date);
            }
        }
    }

    public CalendarDay getSelectedDate() {
        return this.selectedDate;
    }

    public Notifications getNotifications() {
        return notifications;
    }

    public void setNotification(Notifications notifications) {
        this.notifications = notifications;
    }

    public void addNote(Message note) {
//        Log.e("a", this.notes.getData().size() + " Notes size 1");
        if (notes.getMessages() == null) {
            notes.setMessages(new ArrayList<>());
        }
        this.notes.getMessages().add(0, note);
//        homeFragment.addNote(note, true);
        calendarFragment.addNote(note, true);
        Log.e("a", this.notes.getMessages().size() + " Notes size");
    }

    private void addLoginFragment() {
        if (loginFragment == null) {
            loginFragment = new LoginFragment();
        }
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fr_login, loginFragment, Constants.FragmentName.LOGIN_FRAGMENT);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void addNoteFragment() {
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.replace(R.id.fr_login, AddNoteFragment.newInstance(classPos, Constants.ADD_NOTE_NAME), Constants.FragmentName.ADDNOTE_FRAGMENT);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void addPostCommentFragment() {
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.replace(R.id.fr_login, PostCommentFragment.newInstance(classPos), Constants.FragmentName.POSTCOMMENT_FRAGMENT);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void addRequestFragment() {
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.replace(R.id.fr_login, AddNoteFragment.newInstance(classPos, Constants.ADD_REQUEST_NAME), Constants.FragmentName.ADDNOTE_FRAGMENT);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void addFullCalendarFragment(int kidPos, long curDate) {
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.replace(R.id.fr_login, FullCalendarFragment.newInstance(kidPos, curDate), Constants.FragmentName.ADDNOTE_FRAGMENT);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void addCommentDetailFragment(int week, int year, String content, String fromDate, String toDate) {
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.replace(R.id.fr_login, CommentDetailFragment.newInstance(week, year, content, fromDate, toDate), Constants.FragmentName.CONTENTDETAIL_FRAGMENT);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void addArticleFragment(String title, String content, String date, String image) {
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.replace(R.id.fr_login, ArticleFragment.newInstance(title, content, date, image), Constants.FragmentName.ARTICLE_FRAGMENT);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void addAlbumFragment(String albumId, String title) {
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.replace(R.id.fr_login, AlbumFragment.newInstance(classPos, albumId, title), Constants.FragmentName.ALBUM_FRAGMENT);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void addViewImageFragment(String[] images, int position) {
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.replace(R.id.fr_login, ViewImageFragment.newInstance(images, position), Constants.FragmentName.VIEW_IMAGE_FRAGMENT);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void addSettingFragment() {
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.replace(R.id.fr_login, SettingFragment.newInstance(), Constants.FragmentName.VIEW_IMAGE_FRAGMENT);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void closeLoginFragment() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        }
        isLogin = 2;
    }

    public void showNotificationDialog() {
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top, R.anim.enter_from_top, R.anim.exit_to_bottom);
        ft.replace(R.id.fr_login, NotificationFragment.newInstance(classPos), Constants.FragmentName.NOTIFICATION_FRAGMENT);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void onNotificationClick(Notification item) {
        onBackPressed();
        switch (item.getType()) {
            case Constants.NOTIFICATION_TYPE.TYPE_NEW:
                addArticleFragment(item.getTitle(), item.getNotifyId(), item.getDate() + "", "");
                break;
            case Constants.NOTIFICATION_TYPE.TYPE_COMMENT:
//                addCommentDetailFragment(item.getTitle(), item.getNotifyId(), item.getDate());
                break;
            case Constants.NOTIFICATION_TYPE.TYPE_NOTE:
                addNoteFragment();
                break;
            case Constants.NOTIFICATION_TYPE.TYPE_ALBUM:
                addAlbumFragment(item.getNotifyId(), item.getTitle());
                break;
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            if (isLogin == 2) {
                fm.popBackStack();
                return;
            } else if (isLogin == 1) {
                System.exit(0);
            }
        }
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a myParent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (item.isCheckable()) {
            item.setChecked(!item.isChecked());
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_menu:
                onMenuClick();
                break;
            case R.id.kid_avatar:
                showNotificationDialog();
                break;
            case R.id.ln_setting:
                drawerLayout.closeDrawer(Gravity.START);
                addSettingFragment();
                break;
            case R.id.ln_logout:
                drawerLayout.closeDrawer(Gravity.START);
                setIsLogout(true);
                addLoginFragment();
                break;
            case R.id.txt_choose:
                Toast.makeText(this, "Choose", Toast.LENGTH_LONG).show();
                choosePressed = !choosePressed;
                if (choosePressed) {
                    txt_choose.setText("Bỏ chọn");
                    txt_choose.setBackgroundResource(R.drawable.orange_button);
                } else {
                    txt_choose.setText("Chọn");
                    txt_choose.setBackgroundResource(R.drawable.bolder_white_button);
                }
                classListFragment.onTextChoosePressed(choosePressed);
                break;
        }
    }

    private void onMenuClick() {
        drawerLayout.openDrawer(Gravity.START);
    }

    @Override
    public void getDataSuccess(Notifications notifications) {
        this.notifications = notifications;
        if (notifications.getNotifications().size() > 0) {
            txt_notification_size.setVisibility(View.VISIBLE);
            txt_notification_size.setText("" + notifications.getNotifications().size());
        } else {
            txt_notification_size.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void getDataError(int statusCode) {

    }

    @Override
    public void getClassInfoSuccess(ClassInfo classInfo) {
//        this.classes.get(classPos).setClassId(classInfo.getId());
        txt_className.setText(classInfo.getClassName());
    }

    @Override
    public Context context() {
        return null;
    }

    public boolean hasPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0;

    public void requestPermissions() {
        Log.e("a", "requestPermissions");
        if (!hasPermissions()) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
//                Snackbar.make(rl_contentMain, R.string.write_permission_denied,
//                        Snackbar.LENGTH_SHORT)
//                        .setAction("OK", view -> requestPermissions())
//                        .show();

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.

            } else {
                // Permission request was denied.
//                Snackbar.make(rl_contentMain, R.string.write_permission_denied,
//                        Snackbar.LENGTH_SHORT)
//                        .show();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

//    @Override
//    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
//        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
//            // Get a list of picked images
//            List<Image> images = ImagePicker.getImages(data);
//            // or get a single image only
//            Image image = ImagePicker.getFirstImageOrNull(data);
//
//            Log.e("a", images.size() + " .,");
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
}
