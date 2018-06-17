import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import static spark.Spark.*;

public class Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {

        get("/getAllGoods", getAllGoodsRoute());
        get("/findGoodByName", getFindGoodByNameRoute());

        post("/authorization", (request, response) -> {

            try {
                String body = request.body(); // извлекаем тело запроса
                // тело запроса это по сути посланный JSON файл с именем и паролем
//                {
//                    "name": "admin",
//                        "password": "qwerty123"
//                }
                Account loginPassword = mapper.readValue(body, Account.class); // преобразовываем посланный JSON к нашей модели классу Accoubt

                String accessToken = AccountService.checkAccount(loginPassword); // проверяем данные введенные пользователем
                // и получаем сгенерированный для пользователя токен
                return accessToken; // возвращаем наш токен

            } catch (JsonParseException | JsonMappingException e) {
                LOGGER.error(e.getMessage(), e);
            }
            response.status(400);
            return "error";
        });

        post("/addGood", getAddGoodRoute());

    }

    private static Route getAllGoodsRoute() {
        Route getRoute = (request, response) -> {
            String json = mapper.writeValueAsString(ShopDao.findAll());
            return json;
        };
        return getRoute;
    }

    private static Route getFindGoodByNameRoute() {
        Route getRoute = (request, response) -> {
            String goodName = request.queryParams("goodName");
            if (StringUtils.isBlank(goodName)) {
                return "Please specify correct good name";
            }

            Good good = ShopDao.findByName(goodName);
            if (good == null) {
                return "Good with name " + goodName + " not found";
            }

            String json = mapper.writeValueAsString(good);
            return json;
        };
        return getRoute;
    }

    private static Route getAddGoodRoute() {
        return (Request request, Response response) -> {
            // достаем из запроса параметры товара
            String goodName = request.queryParams("name");
            String count = request.queryParams("count");
            String price = request.queryParams("price");
            String isValid = validateParametersForGood(goodName); // валидируем параметры товара
            if (StringUtils.isNotBlank(isValid)) {
                return isValid;
            }
            Integer priceInt = convertStringToInt(price);
            if (priceInt == null) {
                return "Please specify price as a number";
            }
            Integer contInt = convertStringToInt(count);
            if (contInt == null) {
                return "Please specify count as a number";
            }
            Good good = new Good(goodName, contInt, priceInt);

            String accessToken = request.headers("Access-Control-Allow-Headers"); // получаем из запроса токен пользователя
            System.out.println(accessToken);

            return ShopService.addGood(good, accessToken);
        };
    }

    public static String validateParametersForGood(String goodName) {
        if (StringUtils.isBlank(goodName)) {
            return "Please specify correct good name";
        }
        if (StringUtils.isBlank(goodName)) {
            return "Please specify correct good count";
        }
        if (StringUtils.isBlank(goodName)) {
            return "Please specify correct good price";
        }
        return StringUtils.EMPTY;
    }

    public static Integer convertStringToInt(String string) {
        try {
            return Integer.valueOf(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
