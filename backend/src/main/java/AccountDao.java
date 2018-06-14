import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AccountDao {

    private static final ObjectMapper mapper = new ObjectMapper();//преобразует файлы.
    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    public static synchronized List<Account> findAccount() {
        try {
            FileInputStream fis = new FileInputStream("account.json");// открытие потока, для чтения файла goods.json
            return mapper.readValue(fis, new TypeReference<List<Account>>() {
            });
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    public static synchronized Account findLogin(String name) {
        List<Account> clientList = findAccount();
        for (Account client : clientList) {
            if (client.getName().equals(name)) {
                return client;
            }
        }
        return null;
    }

    public static synchronized Optional<Account> findLoginLambda(String name) {
        return findAccount().stream()
                .filter(client -> name.equals(client.getName()))
                .findFirst();
    }

}
