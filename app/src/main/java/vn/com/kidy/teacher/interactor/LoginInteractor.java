package vn.com.kidy.teacher.interactor;

import java.util.ArrayList;

import io.reactivex.Observable;
import vn.com.kidy.teacher.data.model.login.Account;
import vn.com.kidy.teacher.data.model.login.ClassInfo;
import vn.com.kidy.teacher.data.model.login.Kid;
import vn.com.kidy.teacher.data.model.login.Login;
import vn.com.kidy.teacher.data.model.login.User;
import vn.com.kidy.teacher.network.client.Service;

/**
 * Created by Family on 6/2/2017.
 */

public class LoginInteractor {
    private Service service;

    public LoginInteractor(Service service) {
        this.service = service;
    }

    public Observable<Login> getLogin(Account account) {
        return service.getLogin(account);
    }

    public Observable<User> getLoginContact(String token) {
        return service.getLoginContact(token);
    }

    public Observable<ArrayList<Kid>> getLoginKids(String parent) {
        return service.getLoginKids(parent);
    }

    public Observable<ArrayList<ClassInfo>> getClassList(String userId) {
        return service.getClassList(userId);
    }
}
