import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

class InventoryItem {
    String itemNumber;
    int quantity;
    String supplier;
    String description;

    public InventoryItem(String itemNumber, int quantity, String supplier, String description) {
        this.itemNumber = itemNumber;
        this.quantity = quantity;
        this.supplier = supplier;
        this.description = description;
    }
}

public class Inventory {
    public static void main(String[] args) {
        ArrayList<InventoryItem> inventory = new ArrayList<>();
        ArrayList<String> transactions = new ArrayList<>();
        ArrayList<String> errors = new ArrayList<>();
        ArrayList<String> shipping = new ArrayList<>();

        // 读取库存
        readInventoryFile(inventory, "Inventory.txt");

        // 读取交易
        readTransactionsFile(transactions, "Transactions.txt");

        // 处理交易
        processTransactions(inventory, transactions, errors, shipping);

        // 写入发货信息
        writeFile(shipping, "Shipping.txt");

        // 写入错误信息
        writeFile(errors, "Errors.txt");

        // 更新库存
        updateInventory(inventory, "NewInventory.txt");
    }

    // 读取库存文件
    private static void readInventoryFile(ArrayList<InventoryItem> inventory, String fileName) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                inventory.add(new InventoryItem(parts[0], Integer.parseInt(parts[1]), parts[2], parts[3]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 读取交易文件
    private static void readTransactionsFile(ArrayList<String> transactions, String fileName) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                transactions.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 写入文件
    private static void writeFile(ArrayList<String> list, String fileName) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName))) {
            for (String line : list) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 处理交易
   private static void processTransactions(ArrayList<InventoryItem> inventory, ArrayList<String> transactions, ArrayList<String> errors, ArrayList<String> shipping) {
    HashMap<String, Integer> shippingMap = new HashMap<>();
    for (String transaction : transactions) {
        String[] parts = transaction.split("\t");
        char type = parts[0].charAt(0);
        String itemNumber = parts[1];
        int index = findItem(inventory, itemNumber);
        int quantity;

        switch (type) {
            case 'O':
                quantity = Integer.parseInt(parts[2]);
                String customer = parts[3];
                if (index != -1 && inventory.get(index).quantity >= quantity) {
                    inventory.get(index).quantity -= quantity;
                    String key = customer + "\t" + itemNumber;
                    shippingMap.put(key, shippingMap.getOrDefault(key, 0) + quantity);
                } else {
                    // 添加客户编号、货物编号和发货单数量到错误记录
                    errors.add(customer + "\t" + itemNumber + "\t" + quantity);
                }
                break;
            case 'R':
                quantity = Integer.parseInt(parts[2]);
                if (index != -1) {
                    inventory.get(index).quantity += quantity;
                } else {
                    errors.add("0" + "\t" + itemNumber + "\t" + quantity);
                }
                break;
            case 'A':
                String supplier = parts[2];
                String description = parts[3];
                inventory.add(new InventoryItem(itemNumber, 0, supplier, description));
                break;
            case 'D':
                if (index != -1) {
                    if (inventory.get(index).quantity == 0) {
                        inventory.remove(index);
                    } else {
                        // 添加客户编号（0）、货物编号和库存数量到错误记录
                        errors.add("0" + "\t" + itemNumber + "\t" + inventory.get(index).quantity);
                    }
                }
                break;
        }
    }

	// 将发货信息添加到shipping列表中
    for (Map.Entry<String, Integer> entry : shippingMap.entrySet()) {
       		 shipping.add(entry.getKey() + "\t" + entry.getValue());
    	}
	}


    // 在库存中查找货物
    private static int findItem(ArrayList<InventoryItem> inventory, String itemNumber) {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).itemNumber.equals(itemNumber)) {
                return i;
            }
        }
        return -1;
    }

    // 更新库存文件
    private static void updateInventory(ArrayList<InventoryItem> inventory, String fileName) {
        ArrayList<String> lines = new ArrayList<>();
        for (InventoryItem item : inventory) {
            lines.add(item.itemNumber + "\t" + item.quantity + "\t" + item.supplier + "\t" + item.description);
        }
        writeFile(lines, fileName);
    }
}
