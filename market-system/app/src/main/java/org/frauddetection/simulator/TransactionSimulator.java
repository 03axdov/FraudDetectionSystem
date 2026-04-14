package org.frauddetection.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.frauddetection.models.domain.Account;
import org.frauddetection.models.domain.Customer;
import org.frauddetection.models.domain.Device;
import org.frauddetection.models.domain.IpAddress;
import org.frauddetection.models.domain.Merchant;
import org.frauddetection.models.events.Transaction;

public class TransactionSimulator {
    private final Random random;
    private final CustomerGenerator customerGenerator;
    private final AccountGenerator accountGenerator;
    private final DeviceGenerator deviceGenerator;
    private final IpAddressGenerator ipAddressGenerator;
    private final MerchantGenerator merchantGenerator;
    private final TransactionGenerator transactionGenerator;

    public TransactionSimulator() {
        this(new Random());
    }

    public TransactionSimulator(Random random) {
        this.random = random;
        this.customerGenerator = new CustomerGenerator(random);
        this.accountGenerator = new AccountGenerator(random);
        this.deviceGenerator = new DeviceGenerator(random);
        this.ipAddressGenerator = new IpAddressGenerator(random);
        this.merchantGenerator = new MerchantGenerator(random);
        this.transactionGenerator = new TransactionGenerator(random);
    }

    public SimulationSnapshot simulateMarket(int customerCount, int maxAccountsPerCustomer, int transactionCount) {
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
    ) {
        List<Transaction> transactions = new ArrayList<>(transactionCount);
        for (int i = 0; i < transactionCount; i++) {
            transactions.add(transactionGenerator.generate(accounts, devices, ipAddresses, merchants));
        }
        return transactions;
    }
}
