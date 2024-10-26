import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    private String accountId;
    private String holderName;
    private double balance;

    public Account(String accountId, String holderName, double initialDeposit) {
        this.accountId = accountId;
        this.holderName = holderName;
        this.balance = initialDeposit;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getHolderName() {
        return holderName;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited $" + amount + " into account " + accountId);
        } else {
            System.out.println("Deposit amount must be positive.");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            System.out.println("Withdrew $" + amount + " from account " + accountId);
        } else if (amount > balance) {
            System.out.println("Insufficient funds.");
        } else {
            System.out.println("Withdrawal amount must be positive.");
        }
    }

    public void displayAccountInfo() {
        System.out.println("Account ID: " + accountId + ", Holder: " + holderName + ", Balance: $" + balance);
    }
}

class BankY {
    private ArrayList<Account> accounts;
    private static final String DATA_FILE = "accounts.dat";

    public BankY() {
        accounts = new ArrayList<>();
        loadAccounts();
    }

    private void saveAccounts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(accounts);
        } catch (IOException e) {
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadAccounts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            accounts = (ArrayList<Account>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No existing data found, starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading accounts: " + e.getMessage());
        }
    }

    public void createAccount() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter account ID: ");
        String accountId = scanner.nextLine();
        System.out.print("Enter holder name: ");
        String holderName = scanner.nextLine();
        System.out.print("Enter initial deposit: ");
        double initialDeposit = scanner.nextDouble();

        Account newAccount = new Account(accountId, holderName, initialDeposit);
        accounts.add(newAccount);
        saveAccounts();
        System.out.println("Account created successfully.");
    }

    public Account findAccount(String accountId) {
        for (Account account : accounts) {
            if (account.getAccountId().equals(accountId)) {
                return account;
            }
        }
        return null;
    }

    public void depositFunds() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter account ID: ");
        String accountId = scanner.nextLine();
        Account account = findAccount(accountId);

        if (account != null) {
            System.out.print("Enter amount to deposit: ");
            double amount = scanner.nextDouble();
            account.deposit(amount);
            saveAccounts();
        } else {
            System.out.println("Account not found.");
        }
    }

    public void withdrawFunds() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter account ID: ");
        String accountId = scanner.nextLine();
        Account account = findAccount(accountId);

        if (account != null) {
            System.out.print("Enter amount to withdraw: ");
            double amount = scanner.nextDouble();
            account.withdraw(amount);
            saveAccounts();
        } else {
            System.out.println("Account not found.");
        }
    }

    public void transferFunds() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter sender account ID: ");
        String senderId = scanner.nextLine();
        System.out.print("Enter receiver account ID: ");
        String receiverId = scanner.nextLine();

        Account sender = findAccount(senderId);
        Account receiver = findAccount(receiverId);

        if (sender != null && receiver != null) {
            System.out.print("Enter amount to transfer: ");
            double amount = scanner.nextDouble();
            if (sender.getBalance() >= amount) {
                sender.withdraw(amount);
                receiver.deposit(amount);
                saveAccounts();
                System.out.println("Transfer successful.");
            } else {
                System.out.println("Insufficient funds in sender's account.");
            }
        } else {
            System.out.println("Sender or receiver account not found.");
        }
    }

    public void listAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
        } else {
            for (Account account : accounts) {
                account.displayAccountInfo();
            }
        }
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nBankY System");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit Funds");
            System.out.println("3. Withdraw Funds");
            System.out.println("4. Transfer Funds");
            System.out.println("5. List All Accounts");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> createAccount();
                case 2 -> depositFunds();
                case 3 -> withdrawFunds();
                case 4 -> transferFunds();
                case 5 -> listAccounts();
                case 6 -> {
                    System.out.println("Exiting BankY. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

public class BankYApp {
    public static void main(String[] args) {
        BankY bank = new BankY();
        bank.menu();
    }
}
