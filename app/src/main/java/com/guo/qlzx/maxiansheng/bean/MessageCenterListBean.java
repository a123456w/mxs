package com.guo.qlzx.maxiansheng.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 李 on 2018/4/27.
 */

public class MessageCenterListBean implements Parcelable {

    /**
     * message_id : 1
     * title : 马鲜生火锅店上市了
     * send_time : 1
     * status : 0
     * content : 马先生贼强贼厉害马先生贼强贼厉害马先生贼强贼厉害马先生贼强贼厉害马先生贼强贼厉害马先生贼强贼厉害
     * order_id
     */

    private String message_id;
    private String title;
    private String send_time;
    private int status;
    private String content;
    private String order_id;
    private String order_goods;
    public String getOrder_goods() {
        return order_goods;
    }

    public void setOrder_goods(String order_goods) {
        this.order_goods = order_goods;
    }


    public int getType_url() {
        return type_url;
    }

    public void setType_url(int type_url) {
        this.type_url = type_url;
    }

    private  int type_url;

    protected MessageCenterListBean(Parcel in) {
        message_id = in.readString();
        title = in.readString();
        send_time = in.readString();
        status = in.readInt();
        content = in.readString();
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public static final Creator<MessageCenterListBean> CREATOR = new Creator<MessageCenterListBean>() {
        @Override
        public MessageCenterListBean createFromParcel(Parcel in) {
            return new MessageCenterListBean(in);
        }

        @Override
        public MessageCenterListBean[] newArray(int size) {
            return new MessageCenterListBean[size];
        }
    };

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message_id);
        dest.writeString(title);
        dest.writeString(send_time);
        dest.writeInt(status);
        dest.writeString(content);
    }
}
