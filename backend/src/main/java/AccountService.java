import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingFormatArgumentException;
import java.util.Objects;

public class AccountService {

    private static final String SALT = "strormnet";
    private static final List<String> ACCESS_TOKENS = new ArrayList<>();

    public static String checkAccount(Account userEnteredAccount) {

        String accessToken;

        Account accountJSON = AccountDao.findLogin(userEnteredAccount.getName()); //ищем по имени
        // такой же аккаунт в нашем файле (Базе Данных)
        if (accountJSON == null) {
            // если не нашли кидаем ошибку
            throw new MissingFormatArgumentException("There are no account in JSON");
        }

        if (!Objects.equals(userEnteredAccount.getName(), accountJSON.getName()) ||
                !Objects.equals(userEnteredAccount.getPassword(), accountJSON.getPassword())) {
            // если введеный логин и пароль не совпадают с тем что у нас в БД тоже кидаем ошибку
            throw new IllegalArgumentException("Incorrect login or pass");
        }

        accessToken = DigestUtils.sha1Hex(userEnteredAccount.getPassword() + SALT);// генерируем токен
        // путем присоединения к парою специального слово соли и также шерирования полученной строки
        ACCESS_TOKENS.add(accessToken); // добавляем полученный токен в список токенов которым будет разрешен
        // доступ к нашей БД

        return accessToken;
    }

    public static List<String> getAccessTokens() {
        System.out.println(ACCESS_TOKENS);
        return ACCESS_TOKENS;
    }
}