package com.android.expensetracker.views.users;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_users")
public class UsersEntity implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "mobile")
    private String number;

    public UsersEntity(){

    }

    public UsersEntity(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.number);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.name = source.readString();
        this.number = source.readString();
    }

    protected UsersEntity(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.number = in.readString();
    }

    public static final Parcelable.Creator<UsersEntity> CREATOR = new Parcelable.Creator<UsersEntity>() {
        @Override
        public UsersEntity createFromParcel(Parcel source) {
            return new UsersEntity(source);
        }

        @Override
        public UsersEntity[] newArray(int size) {
            return new UsersEntity[size];
        }
    };
}
