public class Good {
    private String name;
    private int count;
    private int price;

    public Good() { }

    public Good(String name, int count, int price) {
        this.name = name;
        this.count = count;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public int getPrice() {
        return price;
    }

    public void increaseCount() {
        count++;
    }

    public void decreaseCount() {
        count--;
    }

    public void setCount(int count) {
        this.count = count;
    }
}