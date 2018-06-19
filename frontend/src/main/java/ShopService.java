import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ShopService {

    public static void totalSum(Label viewSum, TableView list) {
        List<Good> product = changeListToListOfGoods(list);
        int totalSumByGoods = totalSumOfGoods(product);
        viewSum.setText("Общая сумма: " + ": " + totalSumByGoods + "$");
    }

    private static int totalSumOfGoods(List<Good> product) {

//        return product.stream()
//                .map(good -> good.getCount()  * good.getPrice())
//                .mapToInt(Integer::intValue)
//                .sum();

        int total = 0;
        for (Good good : product) {
            int countSum = good.getPrice() * good.getCount();
            total = countSum + total;
        }
        return total;
    }

    public static List<Good> changeListToListOfGoods(TableView list) {
        List<Good> product = new ArrayList<>();
        for (Object good : list.getItems()) {
            product.add((Good) good);
        }
        return product;
    }

    public static List<Good> changeListToListOfGoodsLambda(TableView list) {
        return (List<Good>) list.getItems().stream()
                .collect(Collectors.toList());
    }

    public static void afterBuyOfGoods(Label viewSum) {
        viewSum.setText("Общая сумма: " + ": " + "0$");
    }

    public static Good findByName(List<Good> listOfGood, String name) {
        for (Good good : listOfGood) {
            if (good.getName().equals(name)) {
                return good;
            }
        }
        return null;
    }

    public static Optional<Good> findByNameLambda(List<Good> listOfGood, String name) {
        return listOfGood.stream()
                .filter(good -> name.equals(good.getName()))
                .findFirst();
    }

    public static void updateOfGoods(ObservableList list) throws IOException {
        list.clear();
        list.addAll(AppClient.getServerGoods());
    }
}