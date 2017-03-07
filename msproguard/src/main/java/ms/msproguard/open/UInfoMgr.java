package ms.msproguard.open;

import ms.msproguard.model.User;

/**
 * Created by Lucio on 17/3/7.
 */

public class UInfoMgr {

    public static IUser getUser(String name) {
        User user = new User();
        user.setName(name);
        user.setAge(10);
        return user;
    }

}
