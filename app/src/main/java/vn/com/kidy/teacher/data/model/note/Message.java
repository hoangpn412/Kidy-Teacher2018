package vn.com.kidy.teacher.data.model.note;

/**
 * Created by admin on 4/12/18.
 */

public class Message
{
    private String content;

    public String getContent() { return this.content; }

    public void setContent(String content) { this.content = content; }

    private boolean isFromParent;

    public boolean getIsFromParent() { return this.isFromParent; }

    public void setIsFromParent(boolean isFromParent) { this.isFromParent = isFromParent; }

    private String createdDate;

    public String getCreatedDate() { return this.createdDate; }

    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }

    private String childrenId;

    public String getChildrenId() { return this.childrenId; }

    public void setChildrenId(String childrenId) { this.childrenId = childrenId; }

    private String childName;

    public String getChildName() { return this.childName; }

    public void setChildName(String childName) { this.childName = childName; }

    private int messsagePos;

    public void setMesssagePos(int messsagePos) {
        this.messsagePos = messsagePos;
    }

    public int getMesssagePos() {
        return this.messsagePos;
    }
}
