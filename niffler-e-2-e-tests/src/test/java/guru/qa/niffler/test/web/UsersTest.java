package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_FRIEND;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_INCOME_REQUEST;

@ExtendWith(BrowserExtension.class)
@ExtendWith(UsersQueueExtension.class)
public class UsersTest {

    HashMap<UserType, StaticUser> map = new HashMap<>();

    @Test
    public void test(@UserType(WITH_FRIEND) StaticUser user0,
                     @UserType(WITH_INCOME_REQUEST) StaticUser user1) {

        System.out.println(user0);
        System.out.println(user1);
    }

}
