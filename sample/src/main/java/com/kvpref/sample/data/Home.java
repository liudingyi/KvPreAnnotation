package com.kvpref.sample.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author： Liudy
 * @description：
 * @date： 2021-04-13
 */
public class Home<T> implements Parcelable {

    public T t;

    public Home(T t) {
        this.t = t;
    }

    protected Home(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Home> CREATOR = new Creator<Home>() {
        @Override
        public Home createFromParcel(Parcel in) {
            return new Home(in);
        }

        @Override
        public Home[] newArray(int size) {
            return new Home[size];
        }
    };
}
