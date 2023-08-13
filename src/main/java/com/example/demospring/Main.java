package com.example.demospring;
import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
public class Main {

    static EntityManagerFactory emf;
    public static EntityManager em;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            emf = Persistence.createEntityManagerFactory("JPATest");
            em = emf.createEntityManager();
            try {
                addExchangeRate();
                while (true) {
                    System.out.println("1: add customer ");
                    System.out.println("2: change between bank account");
                    System.out.println("3: to replenish");
                    System.out.println("4: balance");
                    System.out.println("5: view customers ");
                    System.out.println("6: view bank account ");
                    System.out.println("7: view exchange rate ");
                    System.out.println("8: view transactions ");
                    System.out.println("9: view balance for all");
                    System.out.print("-> ");

                    String s = sc.nextLine();
                    switch (s) {
                        case "1": // Done
                            addCustomerAndAccount(sc);
                            break;
                        case "2": // Done
                            betweenBankAccount(sc);
                            break;
                        case "3": // Done
                            toReplenish(sc);
                            break;
                        case "4": //Done
                            balance(sc);
                            break;
                        case "5": //Done
                            viewCustomer();
                            break;
                        case "6": //Done
                            viewBankAccount();
                            break;
                        case "7": //Done
                            calculateTotalBalanceInUan(sc);
                            break;
                        case "8": //Done
                            viewOperation(em);
                            break;
                        case "9": //Done
                            calculateTotalBalanceForAllCustomers();
                            break;
                        default:
                            return;
                    }
                }
            } finally {
                sc.close();
                em.close();
                emf.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static double calculateTotalBalanceForAllCustomersInUan(EntityManager em) {
        double totalBalance = 0.0;
        try {
            List<IdClientAndValutes> accounts = Main.em.createQuery("SELECT a FROM IdClientAndValutes a", IdClientAndValutes.class).getResultList();

            for (IdClientAndValutes account : accounts) {
                totalBalance += account.getUan();
                totalBalance += account.convertCurrencyToUan("usd", account.getUsd());
                totalBalance += account.convertCurrencyToUan("eur", account.getEur());
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return totalBalance;
    }
    public static EntityManager createEntityManager() {
        emf = Persistence.createEntityManagerFactory("JPATest");
        em = emf.createEntityManager();
        return em;
    }
    public static void calculateTotalBalanceForAllCustomers() {
        List<Clients> customers = em.createQuery("SELECT c FROM Clients c", Clients.class).getResultList();

        for (Clients customer : customers) {
            System.out.println("Customer: " + customer.getName());
            balanceForCustomer(customer.getName());
            System.out.println("=========================");
        }
    }

    private static void balanceForCustomer(String customerName) {
        try {
            IdClientAndValutes account = em.createQuery(
                            "SELECT a FROM IdClientAndValutes a WHERE a.owner = :customerName", IdClientAndValutes.class)
                    .setParameter("customerName", customerName)
                    .getSingleResult();

            System.out.println("Current balances for " + customerName + ":");
            System.out.println("UAN: " + account.getUan());
            System.out.println("USD: " + account.getUsd());
            System.out.println("EUR: " + account.getEur());
        } catch (NoResultException ex) {
            System.out.println("Customer not found.");
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void calculateTotalBalanceInUan(Scanner sc) {
        System.out.print("Enter customer name: ");
        String customerName = sc.nextLine();

        try {
            IdClientAndValutes account = em.createQuery(
                            "SELECT a FROM IdClientAndValutes a WHERE a.owner = :customerName", IdClientAndValutes.class)
                    .setParameter("customerName", customerName)
                    .getSingleResult();

            double uanBalance = account.getUan();
            double usdBalanceInUan = account.convertCurrencyToUan("usd", account.getUsd());
            double eurBalanceInUan = account.convertCurrencyToUan("eur", account.getEur());

            double totalBalanceInUan = uanBalance + usdBalanceInUan + eurBalanceInUan;

            System.out.println("Total balance in UAH for " + customerName + ": " + totalBalanceInUan);
        } catch (NoResultException ex) {
            System.out.println("Customer not found.");
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }




    private static void addCustomerAndAccount(Scanner sc) {
        System.out.print("Enter customer name: ");
        String customerName = sc.nextLine();

        System.out.print("Enter initial balance: ");
        double initialBalance = Double.parseDouble(sc.nextLine());

        System.out.print("Enter currency (Select : USD, EUR, UAN): ");
        String currency = sc.nextLine();

        try {
            em.getTransaction().begin();

            Clients client = new Clients();
            client.setName(customerName);
            em.persist(client);

            IdClientAndValutes account = new IdClientAndValutes();
            account.setOwner(client.getName());

            switch (currency) {
                case "UAN":
                    account.setUan(initialBalance);
                    break;
                case "USD":
                    account.setUsd(initialBalance);
                    break;
                case "EUR":
                    account.setEur(initialBalance);
                    break;
                default:
                    System.out.println("Invalid currency. Account not created.");
                    em.getTransaction().rollback();
                    return;
            }

            em.persist(account);

            em.getTransaction().commit();
            System.out.println("Customer and account added successfully.");
        } catch (Exception ex) {
            em.getTransaction().rollback();
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void betweenBankAccount(Scanner sc) {
        System.out.print("Enter sender's name: ");
        String senderName = sc.nextLine();

        System.out.print("Enter receiver's name: ");
        String receiverName = sc.nextLine();

        System.out.print("Enter amount: ");
        double amount = Double.parseDouble(sc.nextLine());

        System.out.print("Enter currency (e.g., USD, EUR, UAN): ");
        String currency = sc.nextLine();

        try {
            em.getTransaction().begin();

            IdClientAndValutes senderAccount = em.createQuery(
                            "SELECT a FROM IdClientAndValutes a WHERE a.owner = :senderName", IdClientAndValutes.class)
                    .setParameter("senderName", senderName)
                    .getSingleResult();

            IdClientAndValutes receiverAccount = em.createQuery(
                            "SELECT a FROM IdClientAndValutes a WHERE a.owner = :receiverName", IdClientAndValutes.class)
                    .setParameter("receiverName", receiverName)
                    .getSingleResult();

            double senderBalance = getAccountBalance(senderAccount, currency);
            double receiverBalance = getAccountBalance(receiverAccount, currency);

            if (senderBalance >= amount) {
                updateAccountBalance(senderAccount, currency, senderBalance - amount);
                updateAccountBalance(receiverAccount, currency, receiverBalance + amount);

                Transactions transaction = new Transactions(senderName, receiverName, amount, currency);
                em.persist(transaction);

                em.getTransaction().commit();
                System.out.println("Transaction completed successfully.");
            } else {
                System.out.println("Insufficient funds for the transaction.");
            }
        } catch (Exception ex) {
            em.getTransaction().rollback();
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void toReplenish(Scanner sc) {
        System.out.print("Enter customer name: ");
        String customerName = sc.nextLine();

        System.out.print("Enter amount: ");
        double amount = Double.parseDouble(sc.nextLine());

        System.out.print("Enter currency (e.g., USD, EUR, UAN): ");
        String currency = sc.nextLine();

        try {
            em.getTransaction().begin();

            IdClientAndValutes account = em.createQuery(
                            "SELECT a FROM IdClientAndValutes a WHERE a.owner = :customerName", IdClientAndValutes.class)
                    .setParameter("customerName", customerName)
                    .getSingleResult();

            double currentBalance = getAccountBalance(account, currency);
            updateAccountBalance(account, currency, currentBalance + amount);

            Transactions transaction = new Transactions(customerName, amount, currency);
            em.persist(transaction);

            em.getTransaction().commit();
            System.out.println("Replenishment completed successfully.");
        } catch (Exception ex) {
            em.getTransaction().rollback();
            System.out.println("Error: " + ex.getMessage());
        }
    }





    private static void balance(Scanner sc) {
        System.out.print("Enter customer name: ");
        String customerName = sc.nextLine();

        try {
            IdClientAndValutes account = em.createQuery(
                            "SELECT a FROM IdClientAndValutes a WHERE a.owner = :customerName", IdClientAndValutes.class)
                    .setParameter("customerName", customerName)
                    .getSingleResult();

            System.out.println("Current balances for " + customerName + ":");
            System.out.println("UAN: " + account.getUan());
            System.out.println("USD: " + account.getUsd());
            System.out.println("EUR: " + account.getEur());
        } catch (NoResultException ex) {
            System.out.println("Customer not found.");
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    private static void viewCustomer() {
        List<Clients> customers = em.createQuery("SELECT c FROM Clients c", Clients.class).getResultList();
        customers.forEach(System.out::println);
    }

    private static void viewBankAccount() {
        List<IdClientAndValutes> accounts = em.createQuery("SELECT a FROM IdClientAndValutes a", IdClientAndValutes.class).getResultList();
        accounts.forEach(System.out::println);
    }

    public static List<Transactions> viewOperation(EntityManager em) {
        List<Transactions> transactions = em.createQuery("SELECT t FROM Transactions t", Transactions.class).getResultList();
        transactions.forEach(System.out::println);
        return transactions;
    }

    private static double getAccountBalance(IdClientAndValutes account, String currency) {
        switch (currency) {
            case "UAN":
                return account.getUan();
            case "USD":
                return account.getUsd();
            case "EUR":
                return Objects.requireNonNullElse(account.getEur(), 0.0);
            default:
                throw new IllegalArgumentException("Invalid currency");
        }
    }

    private static void updateAccountBalance(IdClientAndValutes account, String currency, double newBalance) {
        switch (currency) {
            case "UAN":
                account.setUan(newBalance);
                break;
            case "USD":
                account.setUsd(newBalance);
                break;
            case "EUR":
                account.setEur(newBalance);
                break;
            default:
                throw new IllegalArgumentException("Invalid currency");
        }
    }


    private static void addExchangeRate() {
        try {
            em.getTransaction().begin();

            Rate usdToUan = new Rate("USD_UAN", 37.0);
            Rate usdToEur = new Rate("USD_EUR", 0.85);

            em.persist(usdToUan);
            em.persist(usdToEur);

            em.getTransaction().commit();
            System.out.println("Exchange rates added successfully.");
        } catch (Exception ex) {
            em.getTransaction().rollback();
            System.out.println("Error: " + ex.getMessage());
        }
    }




}
