package com.ruben.project.seliga.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.ruben.project.seliga.data.database.AppDatabase;
import com.ruben.project.seliga.data.database.UserDao;
import com.ruben.project.seliga.data.model.User;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UserRepository {
    private final UserDao userDao;
    private final ExecutorService executorService;

    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        userDao = db.userDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(User user) {
        executorService.execute(() -> userDao.insert(user));
    }

    public void update(User user) {
        executorService.execute(() -> userDao.update(user));
    }

    public void delete(User user) {
        executorService.execute(() -> userDao.delete(user));
    }

    public User getUserById(int id) {
        return userDao.getUserById(id);
    }

    public String getUserName() {
        Future<String> future = executorService.submit(userDao::getUserName);
        try {
            return future.get();
        } catch (Exception e) {
            return "";
        }
    }

    public LiveData<List<User>> getAllUsers() {
        return userDao.getAllUsers();
    }
}