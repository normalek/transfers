package com.bsfdv.transfers;

import com.bsfdv.transfers.dto.account.AccountRsDTO;
import com.bsfdv.transfers.dto.exception.AccountNotFoundException;
import com.bsfdv.transfers.dto.exception.AmountOverdrawnException;
import com.bsfdv.transfers.dto.exception.DataValidationException;
import com.bsfdv.transfers.dto.transfer.TransferRqDTO;
import com.bsfdv.transfers.entity.Account;
import com.bsfdv.transfers.entity.ErrorEnum;
import com.bsfdv.transfers.repository.AccountRepository;
import com.bsfdv.transfers.service.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TransfersTests {

    private final int THREADS_NUMBER = 2;
    final ExecutorService executor = Executors.newFixedThreadPool(THREADS_NUMBER);

    private final TransferRqDTO TRANSFER_DTO = TransferRqDTO.builder().fromAccount(1000341).toAccount(1000342)
            .amount(BigDecimal.valueOf(3.12)).build();
    private final TransferRqDTO TRANSFER_ASYNC_DTO = TransferRqDTO.builder().fromAccount(1000344).toAccount(1000343)
            .amount(BigDecimal.valueOf(2.71)).build();
    private final TransferRqDTO TRANSFER_AMOUNT_OVERDRAWN_DTO = TransferRqDTO.builder().fromAccount(1000340).toAccount(1000342)
            .amount(BigDecimal.valueOf(5.12)).build();
    private final TransferRqDTO TRANSFER_WRONG_ACCOUNT_DTO = TransferRqDTO.builder().fromAccount(1000333).toAccount(1000342)
            .amount(BigDecimal.valueOf(5.12)).build();
    private final TransferRqDTO TRANSFER_SAME_ACCOUNTS_DTO = TransferRqDTO.builder().fromAccount(1000333).toAccount(1000333)
            .amount(BigDecimal.valueOf(5.12)).build();

    @Resource
    private AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;

    @Test
    public void whenInitializeSQLData_thenSizeIsCorrect() {
        List<Account> allAccounts = accountRepository.findAll();
        assertEquals(5, allAccounts.size());
    }

    @Test
    public void whenTransferMoneyWithoutConcurrency_thenBalanceGotChanged() {
        final Account fromAccountBefore = accountRepository.findById(TRANSFER_DTO.getFromAccount()).get();
        final Account toAccountBefore = accountRepository.findById(TRANSFER_DTO.getToAccount()).get();

        accountService.transferMoney(TRANSFER_DTO);

        final Account toAccountAfter = accountRepository.findById(TRANSFER_DTO.getToAccount()).get();
        final Account fromAccountAfter = accountRepository.findById(TRANSFER_DTO.getFromAccount()).get();

        assertAll(
                () -> assertEquals(fromAccountBefore.getVersion()+1, toAccountAfter.getVersion()),
                () -> assertEquals(toAccountBefore.getVersion()+1, fromAccountAfter.getVersion()),
                () -> assertEquals(toAccountBefore.getBalance().add(TRANSFER_DTO.getAmount()), toAccountAfter.getBalance()),
                () -> assertEquals(fromAccountBefore.getBalance().subtract(TRANSFER_DTO.getAmount()), fromAccountAfter.getBalance())
        );
    }

    @Test
    public void whenTransferMoneyWithConcurrency_thenBalanceGotChanged() throws InterruptedException {
        final Account toAccountBefore = accountRepository.findById(TRANSFER_ASYNC_DTO.getToAccount()).get();
        final Account fromAccountBefore = accountRepository.findById(TRANSFER_ASYNC_DTO.getFromAccount()).get();

        for (int i = 0; i < THREADS_NUMBER; i++) {
            executor.execute(() -> accountService.transferMoney(TRANSFER_ASYNC_DTO));
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        final Account toAccountAfter = accountRepository.findById(TRANSFER_ASYNC_DTO.getToAccount()).get();
        final Account fromAccountAfter = accountRepository.findById(TRANSFER_ASYNC_DTO.getFromAccount()).get();

        assertAll(
                () -> assertEquals(fromAccountBefore.getVersion()+THREADS_NUMBER, toAccountAfter.getVersion()),
                () -> assertEquals(toAccountBefore.getVersion()+THREADS_NUMBER, fromAccountAfter.getVersion())
        );
    }

    @Test
    public void whenGetAccountAfterTransfer_thenAccountWithChangedBalance() {
        final Account fromAccountBefore = accountRepository.findById(TRANSFER_DTO.getFromAccount()).get();

        accountService.transferMoney(TRANSFER_DTO);

        AccountRsDTO account = accountService.getAccountById(TRANSFER_DTO.getFromAccount());

        assertNotEquals(fromAccountBefore.getBalance(), account.getBalance());
    }

    @Test
    public void whenGetAccountAfterTransfer_thenAmountOverdrawnExceptionThrown() {
        Exception exception = assertThrows(AmountOverdrawnException.class, () -> {
            accountService.transferMoney(TRANSFER_AMOUNT_OVERDRAWN_DTO);
        });

        assertTrue(exception.getMessage().equalsIgnoreCase(ErrorEnum.AMOUNT_OVERDRAWN.getMessage()));
    }

    @Test
    public void whenTransferWithWrongAccount_thenAccountNotFoundExceptionThrown() {
        Exception exception = assertThrows(AccountNotFoundException.class, () -> {
            accountService.transferMoney(TRANSFER_WRONG_ACCOUNT_DTO);
        });

        assertTrue(exception.getMessage().equalsIgnoreCase(ErrorEnum.ACCOUNT_NOT_FOUND.getMessage()));
    }

    @Test
    public void whenTransferWithSameAccounts_thenDataValidationExceptionThrown() {
        Exception exception = assertThrows(DataValidationException.class, () -> {
            accountService.transferMoney(TRANSFER_SAME_ACCOUNTS_DTO);
        });

        assertTrue(exception.getMessage().equalsIgnoreCase(ErrorEnum.FROM_TO_ACCOUNT_IS_SAME.getMessage()));
    }
}
