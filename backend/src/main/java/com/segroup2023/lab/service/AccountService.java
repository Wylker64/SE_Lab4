package com.segroup2023.lab.service;

import com.segroup2023.lab.database.entity.Account;
import com.segroup2023.lab.database.entity.Shop;
import com.segroup2023.lab.database.entity.User;
import com.segroup2023.lab.database.repository.AccountRepository;
import com.segroup2023.lab.exception.type.BadRequestException;
import com.segroup2023.lab.exception.type.InsufficientBalanceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AccountService {
    private static AccountRepository accountRepository;
    private static Account adminPersonal, adminProfit;
    @Autowired
    private AccountService(AccountRepository accountRepository) {
        AccountService.accountRepository = accountRepository;
    }

    public static Account getPersonalAccount(Long userId) {
        List<Account> accounts = accountRepository.findByOwnerAndType(userId, Account.Type.PERSONAL);
        assert !accounts.isEmpty();
        return accounts.get(0);
    }

    public static void checkAdminAccount(User admin) {
        List<Account> personal = accountRepository.findByOwnerAndType(admin.getId(), Account.Type.PERSONAL);
        if(personal.isEmpty()) {
            adminPersonal = new Account(admin.getId(), Account.Type.PERSONAL);
            accountRepository.save(adminPersonal);
        } else {
            adminPersonal = personal.get(0);
        }
        List<Account> profit = accountRepository.findByOwnerAndType(admin.getId(), Account.Type.PROFIT);
        if (profit.isEmpty()) {
            adminProfit = new Account(admin.getId(), Account.Type.PROFIT);
            accountRepository.save(adminProfit);
        } else {
            adminProfit = profit.get(0);
        }
    }

    public static void createPersonalAccount(User user) {
        Account account = new Account(user.getId(), Account.Type.PERSONAL);
        accountRepository.save(account);
    }

    public static void createShopAccount(Shop shop) {
        Account account = new Account(shop.getOwner(), Account.Type.PROFIT);
        accountRepository.save(account);
        shop.setAccount(account.getId());
    }

    public static Account findById(Long id) throws BadRequestException {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if(optionalAccount.isEmpty())
            throw new BadRequestException("Account id not found.");
        return optionalAccount.get();
    }

    public static void charge(Account account, Double amount) throws BadRequestException {
        account.charge(amount);
        accountRepository.save(account);
    }

    public static boolean belongTo(User user, Account account) {
        return account.getOwner().equals(user.getId());
    }

    private static void transfer(Account src, Account dst, Double amount,String remark) throws InsufficientBalanceException {
        src.transferTo(dst, amount, remark);
        accountRepository.save(src);
        accountRepository.save(dst);
    }

    public static void transferToAdmin(Long userId, Double amount) throws InsufficientBalanceException {
        Account account = getPersonalAccount(userId);
        transfer(account, adminPersonal, amount,"Transfer to admin");
    }

    public static void undoTransferToAdmin(Long userId, Double amount) {
        Account account = getPersonalAccount(userId);
        try {
            transfer(adminPersonal, account, amount,"Undo transfer to admin");
        } catch (InsufficientBalanceException e) {
            throw new AssertionError("Insufficient balance in admin personal account.");
        }
    }

    public static void adminProfit(Double amount) {
        try {
            transfer(adminPersonal, adminProfit, amount,"Admin profit");
        } catch (InsufficientBalanceException e) {
            throw new AssertionError("Insufficient balance in admin personal account.");
        }
    }

    public static void adminExpend(Double amount) throws InsufficientBalanceException {
            transfer(adminProfit, adminPersonal, amount,"Admin expend");
    }

    public static void shopProfit(Long shopId, Double amount) {
        Shop shop = ShopService.findById(shopId);
        Account shopAccount = getAccount(shop.getAccount());
        try {
            transfer(adminPersonal, shopAccount, amount,"Shop profit");
        } catch (InsufficientBalanceException e) {
            throw new AssertionError("Insufficient balance in admin personal account.");
        }
    }

    public static void deleteShopAccount(Shop shop) throws BadRequestException {
        List<Account> ownerList = accountRepository.findByOwnerAndType(shop.getOwner(), Account.Type.PERSONAL);
        if(ownerList.isEmpty())
            throw new BadRequestException("Shop owner account not found.");
        Account ownerAccount = ownerList.get(0);
        Account shopAccount = findById(shop.getAccount());
        try {
            shopAccount.transferTo(ownerAccount, shopAccount.getBalance(),"Shop deletion,shop account balance transfer to owner account.");
        } catch (InsufficientBalanceException e) {
            //Impossible exception, ignore
        }
        accountRepository.delete(shopAccount);
    }

    public static Account getAdminProfit() throws BadRequestException {
        adminProfit = findById(adminProfit.getId());
        return adminProfit;
    }

    public static Account getAdminPersonal() throws BadRequestException {
        adminPersonal = findById(adminPersonal.getId());
        return adminPersonal;
    }

    private static Account getAccount(Long accountId) {
        Optional<Account> optional = accountRepository.findById(accountId);
        assert optional.isPresent():"Account not exist.";
        return optional.get();
    }

}
