import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

class Toy {
    private int id;
    private String name;
    private int quantity;
    private double weight; // Частота выпадения игрушки в процентах (от 0 до 100)

    public Toy(int id, String name, int quantity, double weight) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Название: " + name + ", Количество: " + quantity + ", Частота выпадения: " + weight + "%";
    }
}

class ToyStore {
    private List<Toy> toys;

    public ToyStore() {
        toys = new ArrayList<>();
    }

    public void addToy(Toy toy) {
        toys.add(toy);
    }

    public void changeWeight(int toyId, double newWeight) {
        for (Toy toy : toys) {
            if (toy.getId() == toyId) {
                toy.setWeight(newWeight);
                return;
            }
        }
    }

    public Toy getRandomToy() {
        double totalWeight = toys.stream().mapToDouble(Toy::getWeight).sum();
        double randomValue = Math.random() * totalWeight;

        double cumulativeWeight = 0;
        for (Toy toy : toys) {
            cumulativeWeight += toy.getWeight();
            if (randomValue <= cumulativeWeight) {
                return toy;
            }
        }

        return null; // В случае ошибки или недостаточного веса
    }

    public void saveToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(filename)) {
            for (Toy toy : toys) {
                writer.println(toy.getId() + ";" + toy.getName() + ";" + toy.getQuantity() + ";" + toy.getWeight());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile(String filename) {
        toys.clear();
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(";");
                if (parts.length == 4) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    int quantity = Integer.parseInt(parts[2]);
                    double weight = Double.parseDouble(parts[3]);
                    toys.add(new Toy(id, name, quantity, weight));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void listToys() {
        for (Toy toy : toys) {
            System.out.println(toy);
        }
    }
}

public class ToyStoreApp {
    public static void main(String[] args) {
        ToyStore toyStore = new ToyStore();

        // Пример добавления игрушек
        toyStore.addToy(new Toy(1, "Мяч", 50, 30.0));
        toyStore.addToy(new Toy(2, "Кукла", 40, 20.0));
        toyStore.addToy(new Toy(3, "Пазл", 30, 10.0));

        // Пример изменения частоты выпадения игрушки
        toyStore.changeWeight(1, 40.0);

        // Пример сохранения списка игрушек в файл
        toyStore.saveToFile("toys.txt");

        // Пример загрузки списка игрушек из файла
        toyStore.loadFromFile("toys.txt");

        // Пример розыгрыша игрушек
        System.out.println("Розыгрыш игрушек:");
        for (int i = 0; i < 5; i++) {
            Toy randomToy = toyStore.getRandomToy();
            if (randomToy != null) {
                System.out.println("Победитель: " + randomToy.getName());
            } else {
                System.out.println("Ошибка при розыгрыше.");
            }
        }

        // Вывод списка игрушек
        System.out.println("Список игрушек:");
        toyStore.listToys();
    }
}
