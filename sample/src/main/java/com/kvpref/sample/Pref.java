package com.kvpref.sample;

import com.kvpref.annotations.ObjectType;
import com.kvpref.annotations.ParcelType;
import com.kvpref.annotations.PrefKey;
import com.kvpref.annotations.SharePref;
import com.kvpref.sample.data.User;

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
