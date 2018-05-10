package vn.com.kidy.teacher.presenter;

import android.util.Log;

import java.util.ArrayList;

import vn.com.kidy.teacher.data.Constants;
import vn.com.kidy.teacher.data.model.login.Account;
import vn.com.kidy.teacher.data.model.login.ClassInfo;
import vn.com.kidy.teacher.data.model.login.Login;
import vn.com.kidy.teacher.data.model.login.User;
import vn.com.kidy.teacher.interactor.LoginInteractor;

/**
 * Created by Family on 6/2/2017.
 */

public class LoginPresenter extends Presenter<LoginPresenter.View> {

    private LoginInteractor loginInteractor;

    public LoginPresenter(LoginInteractor loginInteractor) {
        this.loginInteractor = loginInteractor;
    }

    public void onGetLogin(Account account) {
        loginInteractor.getLogin(account).subscribe(login -> {
            Log.e("a", "Login: " + login.getIsSuccess() + " " + login.getToken());
            if (login == null) {
                getView().loginError(Constants.STATUS_CODE.SERVER_ERROR, "Server error");
            }
            if (!login.getIsSuccess()) {
                getView().loginError(Constants.STATUS_CODE.LOGIN_ERROR, "Wrong User");
            } else {
                getView().loginSuccess(login);
            }
        }, throwable -> {
            throwable.printStackTrace();
            getView().loginError(Constants.STATUS_CODE.SERVER_ERROR, "Server error");
        });
    }

    public interface View extends Presenter.View {
        void loginSuccess(Login login);

        void loginError(int statusCode, String message);

        void getUserSuccess(User user);

        void getClassListSuccess(ArrayList<ClassInfo> classList);
    }

    public void onGetUserContact(String token) {
        loginInteractor.getLoginContact(token).subscribe(user -> {
            if (user == null) {
                getView().loginError(Constants.STATUS_CODE.SERVER_ERROR, "Server Error");
            } else {
                if (user.getIsSuccess()) {
                    getView().getUserSuccess(user);
                } else {
                    getView().loginError(Constants.STATUS_CODE.TOKEN_EXPIRED, "Server Error");
                }
            }
        }, throwable -> {
            throwable.printStackTrace();
            getView().loginError(Constants.STATUS_CODE.SERVER_ERROR, "Server Error");
        });
    }

//    public void onGetKids(String parrentId) {
//        loginInteractor.getLoginKids(parrentId).subscribe(kids -> {
//            getView().getKidsSuccess(kids);
//        }, throwable -> {
//            throwable.printStackTrace();
//            getView().loginError(Constants.STATUS_CODE.SERVER_ERROR, "Server Error");
//        });
//    }

    public void onGetClassList(String userId) {
        loginInteractor.getClassList(userId).subscribe(classList -> {
            getView().getClassListSuccess(classList);
        }, throwable -> {
            throwable.printStackTrace();
            getView().loginError(Constants.STATUS_CODE.SERVER_ERROR, "Server Error");
        });
    }
}
