package message.dao;

import message.entity.MessageContact;

public interface MessageContactDao {
    MessageContact find(long userId);

    void save(MessageContact contact);

    void update(MessageContact contact);
}
