import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

public class ShopService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShopService.class);

    public static String addGood(Good good, String accessToken) {

        if (accessToken == null) { // если у пользователя нет токена значит он не залогинился и не имеет права модифицировать БД
            return  "user isn't logged in";
        }

        boolean isUserLoggedIn = Optional.ofNullable(AccountService.getAccessTokens()) // получаем все токены что у нас есть
                // для которых разрешен доступ к БД
                .map(Collection::stream) // так как выше был получен лист преобразуем его к потоку
                .map(stringStream -> stringStream.anyMatch(accessToken::equals)) //с помощью anyMatch ищем совпадение в потоке с
                // токеном пользователя, если оно найдется вернется true
                .orElse(false);

        // если такого токена у нас нет то возвращаем false
        if (!isUserLoggedIn) {
            return "user isn't logged in";
        }

        Good goodInFile = ShopDao.findByName(good.getName()); // пытаемся найти товар с введенным пользователем названием
        try {
            if (goodInFile == null) { // если такого товара нет
                return ShopDao.save(good); // то создаем его
            } else {
                ShopDao.deleteByName(good.getName()); // если есть то удаляем
                Good newGood = new Good(goodInFile.getName(), goodInFile.getCount() + good.getCount(), good.getPrice());
                return ShopDao.save(newGood); // и записываем с новыми данными введенными юзером
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return e.getMessage() + " during adding process";
        }
    }

}



