package com.pref.sample;

import com.pref.annotations.ObjectType;
import com.pref.annotations.ParcelType;
import com.pref.annotations.PrefKey;
import com.pref.annotations.SharePref;
import com.pref.sample.data.User;

import java.util.List;

@SharePref
public class Pref {

    @PrefKey(key = "age")
    public int age;

    @PrefKey(key = "isMan")
    public boolean isMan;

    @PrefKey(key = "money")
    public long money;

    @PrefKey(key = "height")
    public float height;

    @PrefKey(key = "user_name")
    public String username;

    @PrefKey(key = "user")
    @ObjectType
    public User user;

    @PrefKey(key = "user_list")
    @ObjectType
    public List<User> userList;

    @PrefKey(key = "user1")
    @ParcelType
    public User user1;

}
