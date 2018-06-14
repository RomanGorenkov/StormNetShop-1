import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingFormatArgumentException;
import java.util.Objects;
import java.util.Random;

public class AccountService {

    private static final Random random = new Random();
    private static final List<String> ACCESS_TOKENS = new ArrayList<>();

    public static String checkAccount(Account loginPassword) {

        String accessToken;

        Account accountJSON = AccountDao.findLogin(loginPassword.getName());
        if (accountJSON == null) {
            throw new MissingFormatArgumentException("There are no account in JSON");
        }

        if (Objects.equals(loginPassword.getName(), accountJSON.getName()) &&
                Objects.equals(loginPassword.getPassword(), accountJSON.getPassword())) {

            String randomNumbers = (String.valueOf(random.doubles()));
            String passordWithRandom = loginPassword.getPassword().concat(randomNumbers);
            accessToken = DigestUtils.sha1Hex(passordWithRandom);
            ACCESS_TOKENS.add(accessToken);

            return accessToken;

        } else {
            throw new IllegalArgumentException("Incorrect login or pass");
        }
    }

    public static List<String> getAccessTokens() {
        System.out.println(ACCESS_TOKENS);
        return ACCESS_TOKENS;
    }
}