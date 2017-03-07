package ms.msproguard.model;

import ms.msproguard.open.Friend;
import ms.msproguard.open.IUser;

/**
 * Created by Lucio on 17/3/7.
 */

public class User implements IUser {

    private String name;
    private int age;

    private Friend friend;

    public Friend getFriend() {
        return friend;
    }

    public void setFriend(Friend friend) {
        this.friend = friend;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String getUserName() {
        return name;
    }

    @Override
    public int getUserAge() {
        return age;
    }
}
