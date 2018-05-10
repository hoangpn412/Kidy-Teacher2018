package vn.com.kidy.teacher.data.model.comment;

import java.util.ArrayList;

/**
 * Created by admin on 5/5/18.
 */

public class CommentContent {
    final String teacherId;
    final int week, year;
    final ArrayList<String> childrenIds;
    final String content;

    public CommentContent(String teacherId, int week, int year, ArrayList<String> childrenIds, String content) {
        this.teacherId = teacherId;
        this.week = week;
        this.year = year;
        this.childrenIds = childrenIds;
        this.content = content;
    }
}
