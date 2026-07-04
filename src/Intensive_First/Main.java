package Intensive_First;

public class Main {
    public static void main(String[] args) {

        MyHashMap<String, Integer> map = new MyHashMap<>();

        map.put("Apple", 10);
        map.put("Banana", 20);
        map.put("Orange", 30);

        System.out.println(map.get("Apple"));   // 10
        System.out.println(map.get("Orange"));  // 30

        map.put("Apple", 100); // обновление значения
        System.out.println(map.get("Apple"));   // 100

        System.out.println(map.remove("Banana")); // 20
        System.out.println(map.get("Banana"));    // null

        System.out.println(map.size()); // 2
    }
}
