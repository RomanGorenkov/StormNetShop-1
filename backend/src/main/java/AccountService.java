import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingFormatArgumentException;
import java.util.Objects;

public class AccountService {

    private static final String SALT = "strormnet";
    private static final List<String> ACCESS_TOKENS = new ArrayList<>();

    public static String checkAccount(Account loginPassword) {

        String accessToken;

        Account accountJSON = AccountDao.findLogin(loginPassword.getName());
        if (accountJSON == null) {
            throw new MissingFormatArgumentException("There are no account in JSON");
        }

        if (!Objects.equals(loginPassword.getName(), accountJSON.getName()) ||
                !Objects.equals(loginPassword.getPassword(), accountJSON.getPassword())) {
            throw new IllegalArgumentException("Incorrect login or pass");
        }

        accessToken = DigestUtils.sha1Hex(loginPassword.getPassword() + SALT);
        ACCESS_TOKENS.add(accessToken);

        return accessToken;
    }

    public static List<String> getAccessTokens() {
        System.out.println(ACCESS_TOKENS);
        return ACCESS_TOKENS;
    }
}