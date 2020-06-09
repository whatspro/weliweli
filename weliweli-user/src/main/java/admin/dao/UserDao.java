package admin.dao;

import admin.entity.User;

public interface UserDao {
    User get(long uid);
}
