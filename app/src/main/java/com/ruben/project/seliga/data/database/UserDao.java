package com.ruben.project.seliga.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ruben.project.seliga.data.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Query("SELECT * FROM user WHERE id = :id")
    User getUserById(int id);

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT name FROM user WHERE id = 1")
    String getUserName();
}