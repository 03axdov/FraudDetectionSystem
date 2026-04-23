package org.frauddetection.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.frauddetection.kafka.producer.TransactionProducer;
import org.frauddetection.models.domain.Account;
import org.frauddetection.models.domain.Customer;
import org.frauddetection.models.domain.Device;
import org.frauddetection.models.domain.IpAddress;
import org.frauddetection.models.domain.Merchant;
import org.frauddetection.models.events.Transaction;
import org.frauddetection.repositories.AccountRepository;
import org.frauddetection.repositories.CustomerRepository;
import org.frauddetection.repositories.DeviceRepository;
import org.frauddetection.repositories.IpAddressRepository;
import org.frauddetection.repositories.MerchantRepository;
import org.frauddetection.repositories.TransactionRepository;
import org.neo4j.driver.Driver;

public class MarketSimulator {
    private final Random random;
    private final CustomerGenerator customerGenerator;
    private final AccountGenerator accountGenerator;
    private final DeviceGenerator deviceGenerator;
    private final IpAddressGenerator ipAddressGenerator;
    private final MerchantGenerator merchantGenerator;
    private final TransactionGenerator transactionGenerator;
    private final TransactionProducer transactionProducer;

    public MarketSimulator(Driver driver) {
        this(new Random(), driver);
    }

    public MarketSimulator(Random random, Driver driver) {
        this.random = random;
        AccountRepository accountRepository = new AccountRepository(driver);
        CustomerRepository customerRepository = new CustomerRepository(driver);
        DeviceRepository deviceRepository = new DeviceRepository(driver);
        IpAddressRepository ipAddressRepository = new IpAddressRepository(driver);
        MerchantRepository merchantRepository = new MerchantRepository(driver);
        TransactionRepository transactionRepository = new TransactionRepository(driver);

        this.customerGenerator = new CustomerGenerator(customerRepository, random);
        this.accountGenerator = new AccountGenerator(accountRepository, random);
        this.deviceGenerator = new DeviceGenerator(deviceRepository, random);
        this.ipAddressGenerator = new IpAddressGenerator(ipAddressRepository, random);
        this.merchantGenerator = new MerchantGenerator(merchantRepository, random);
        this.transactionGenerator = new TransactionGenerator(transactionRepository, accountRepository, random);

        this.transactionProducer = new TransactionProducer();
    }

    public SimulationSnapshot simulateMarket(int customerCount, int maxAccountsPerCustomer, int transactionCount) throws InterruptedException {
        if (customerCount <= 0) {
            throw new IllegalArgumentException("customerCount must be greater than zero.");
        }
        if (maxAccountsPerCustomer <= 0) {
            throw new IllegalArgumentException("maxAccountsPerCustomer must be greater than zero.");
        }
        if (transactionCount < 0) {
            throw new IllegalArgumentException("transactionCount must not be negative.");
        }

        List<Customer> customers = generateCustomers(customerCount);
        List<Account> accounts = generateAccounts(customers, maxAccountsPerCustomer);
        List<Device> devices = deviceGenerator.generateMany(Math.max(customerCount * 2, 10));
        List<IpAddress> ipAddresses = ipAddressGenerator.generateMany(Math.max(customerCount * 2, 10));
        List<Merchant> merchants = merchantGenerator.generateMany(Math.max(customerCount / 2, 5));
        List<Transaction> transactions = generateTransactions(transactionCount, accounts, devices, ipAddresses, merchants);

        return new SimulationSnapshot(customers, accounts, devices, ipAddresses, merchants, transactions);
    }

    private List<Customer> generateCustomers(int customerCount) {
        List<Customer> customers = new ArrayList<>(customerCount);
        for (int i = 0; i < customerCount; i++) {
            customers.add(customerGenerator.generate());
        }
        return customers;
    }

    private List<Account> generateAccounts(List<Customer> customers, int maxAccountsPerCustomer) {
        List<Account> accounts = new ArrayList<>();
        for (Customer customer : customers) {
            int accountCount = 1 + random.nextInt(maxAccountsPerCustomer);
            for (int i = 0; i < accountCount; i++) {
                accounts.add(accountGenerator.generate(customer));
            }
        }
        return accounts;
    }

    private List<Transaction> generateTransactions(
        int transactionCount,
        List<Account> accounts,
        List<Device> devices,
        List<IpAddress> ipAddresses,
        List<Merchant> merchants
    ) throws InterruptedException {
        List<Transaction> transactions = new ArrayList<>(transactionCount);
        for (int i = 0; i < transactionCount; i++) {
            Transaction currentTransaction = transactionGenerator.generate(accounts, devices, ipAddresses, merchants);
            transactionProducer.send(currentTransaction, "transactions");
            transactions.add(currentTransaction);
            Thread.sleep(200);
        }
        return transactions;
    }
}
