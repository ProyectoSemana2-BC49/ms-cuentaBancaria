package com.nttdatabc.mscuentabancaria.service;

import static com.nttdatabc.mscuentabancaria.utils.AccountValidator.verifyCustomerExists;
import static com.nttdatabc.mscuentabancaria.utils.MovementValidator.validateAccountRegister;

import com.nttdatabc.mscuentabancaria.model.Account;
import com.nttdatabc.mscuentabancaria.model.BalanceAccounts;
import com.nttdatabc.mscuentabancaria.model.CustomerExt;
import com.nttdatabc.mscuentabancaria.model.Movement;
import com.nttdatabc.mscuentabancaria.model.SummaryAccountBalance;
import com.nttdatabc.mscuentabancaria.repository.AccountRepository;
import com.nttdatabc.mscuentabancaria.repository.MovementRepository;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service Report.
 */
@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
  @Autowired
  private AccountRepository accountRepository;
  @Autowired
  private MovementRepository movementRepository;
  @Autowired
  private CustomerApiExtImpl customerApiExtImpl;
  @Autowired
  private AccountServiceImpl accountServiceImpl;


  @Override
  public Single<BalanceAccounts> getBalanceAverageService(String customerId) {
    return Single.defer(() -> {
      CustomerExt customerFound = verifyCustomerExists(customerId, customerApiExtImpl);
      BalanceAccounts balanceAccounts = new BalanceAccounts();
      balanceAccounts.setCustomerId(customerId);

      LocalDate currentDate = LocalDate.now();
      int daysInMonth = currentDate.lengthOfMonth();
      int year = LocalDate.now().getYear();
      int mounth = LocalDate.now().getMonthValue();
      String dateFilter = String.format("%d-%s", year, String.valueOf(mounth).length() == 1 ? "0" + mounth : mounth);
      List<Account> findAccountByCustomer = accountRepository.findByCustomerId(customerId);
      List<SummaryAccountBalance> summaryAccountBalances = new ArrayList<>();

      for (Account account : findAccountByCustomer) {
        List<Movement> movements = movementRepository.findByAccountId(account.getId());

        List<Movement> movementsInCurrentMonth = movements.stream()
            .filter(movement -> movement.getFecha().contains(dateFilter))
            .collect(Collectors.toList());

        double totalBalance = movementsInCurrentMonth.stream()
            .mapToDouble(movement -> movement.getMount().doubleValue())
            .sum();

        BigDecimal averageDailyBalance = BigDecimal.valueOf(totalBalance / daysInMonth);

        SummaryAccountBalance summaryAccountBalance = new SummaryAccountBalance();
        summaryAccountBalance.setAccountId(account.getId());
        summaryAccountBalance.setBalanceAvg(averageDailyBalance);

        summaryAccountBalances.add(summaryAccountBalance);
      }

      balanceAccounts.setSummaryAccounts(summaryAccountBalances);
      return Single.just(balanceAccounts);
    });
  }

  @Override
  public Observable<List<Movement>> getMovementsWithFee(String accountId) {
    return Observable.defer(() -> {
      validateAccountRegister(accountId, accountServiceImpl);
      int year = LocalDate.now().getYear();
      int mounth = LocalDate.now().getMonthValue();
      String dateFilter = String.format("%d-%s", year, String.valueOf(mounth).length() == 1 ? "0" + mounth : mounth);

      List<Movement> filterByExistesFee = movementRepository.findByAccountId(accountId)
          .stream()
          .filter(movement -> movement.getFee().doubleValue() > 0)
          .filter(movement -> movement.getFecha().contains(dateFilter))
          .collect(Collectors.toList());

      return Observable.just(filterByExistesFee);
    });
  }
}
