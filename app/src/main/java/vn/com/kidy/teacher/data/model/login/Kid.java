package vn.com.kidy.teacher.data.model.login;

import java.util.Calendar;

/**
 * Created by admin on 3/26/18.
 */

public class Kid {
    private String babyId;

    public String getBabyId() {
        return this.babyId;
    }

    public void setBabyId(String babyId) {
        this.babyId = babyId;
    }

    private String parentId;

    public String getParentId() {
        return this.parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    private String fullName;

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    private String nickName;

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    private String birthday;

    public String getBirthday() {
        return this.birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    private String avatar;

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    private int gender;

    public int getGender() {
        return this.gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    private int age;

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private String babyAge;

    public String getBabyAge() {
        return this.babyAge;
    }

    public void setBabyAge(String babyAge) {
        this.babyAge = babyAge;
    }

    private String classId;

    public String getClassId() {
        return this.classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    private String className;

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    private SchoolInfo schoolInfo;

    public SchoolInfo getSchoolInfo() {
        return this.schoolInfo;
    }

    public void setSchoolInfo(SchoolInfo schoolInfo) {
        this.schoolInfo = schoolInfo;
    }

    private String babySLLUrl;

    public String getBabySLLUrl() {
        return this.babySLLUrl;
    }

    public void setBabySLLUrl(String babySLLUrl) {
        this.babySLLUrl = babySLLUrl;
    }

    private String commentDate;

    public String getCommentDate() {
        return this.commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    private Calendar commentDateCal;

    public Calendar getCommentDateCal() {
        return this.commentDateCal;
    }

    public void setCommentDateCal(Calendar commentDateCal) {
        this.commentDateCal = commentDateCal;
    }

    private boolean hasComment;

    public boolean getHasComment() {
        return this.hasComment;
    }

    public void setHasComment(boolean hasComment) {
        this.hasComment = hasComment;
    }

    private boolean isChecked;

    public boolean getIsChecked() {
        return this.isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
