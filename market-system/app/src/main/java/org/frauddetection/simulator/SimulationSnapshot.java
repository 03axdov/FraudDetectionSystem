package org.frauddetection.simulator;

import java.util.List;

import org.frauddetection.models.domain.Account;
import org.frauddetection.models.domain.Customer;
import org.frauddetection.models.domain.Device;
import org.frauddetection.models.domain.IpAddress;
import org.frauddetection.models.domain.Merchant;
import org.frauddetection.models.events.Transaction;

public record SimulationSnapshot(
    List<Customer> customers,
    List<Account> accounts,
    List<Device> devices,
    List<IpAddress> ipAddresses,
    List<Merchant> merchants,
    List<Transaction> transactions
) {}
