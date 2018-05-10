package vn.com.kidy.teacher.data.model.calendar;

/**
 * Created by admin on 2/10/18.
 */

public class CalendarNote
{
    private long date;

    public long getDate() { return this.date; }

    public void setDate(long date) { this.date = date; }

    private boolean schedule;

    public boolean getSchedule() { return this.schedule; }

    public void setSchedule(boolean schedule) { this.schedule = schedule; }

    private boolean teacherNote;

    public boolean getTeacherNote() { return this.teacherNote; }

    public void setTeacherNote(boolean teacherNote) { this.teacherNote = teacherNote; }

    private boolean parentNote;

    public boolean getParentNote() { return this.parentNote; }

    public void setParentNote(boolean parentNote) { this.parentNote = parentNote; }
}

