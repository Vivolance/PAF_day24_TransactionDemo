package IBF.day24.Transaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import IBF.day24.Transaction.model.BankAccount;
import IBF.day24.Transaction.repository.BankAccountRepo;

@Service
public class BankAccountService {
    @Autowired
    BankAccountRepo bankAcctRepo;

    //Transactional tab allows ALL the functions below to be executed, if one fails the whole function fails.
    //Some fail case includes withdrawing out 1000 but not depositing it somewhere, without the transation tab the 1000
    //only be withdrawn and not deposited, causing the 1000 to be "lost".
    //set isolation levels
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public Boolean transferMoney(Integer accountFrom, Integer accountTo, Float amount) {
        Boolean bTransferred = false;
        Boolean bWithdrawn = false;
        Boolean bDeposited = false;

        BankAccount depositAccount = null;
        BankAccount withdrawalAccount = null;

        Boolean bDepositAccountValid = false;
        Boolean bWithdrawalAccountValid = false;
        Boolean bProceed = false;

        //Logic flow of this function in the service layer:

        //1. check if accounts (withdrawer and depositer) are valid (active)
        withdrawalAccount = bankAcctRepo.retrieveAccountDetails(accountFrom);
        depositAccount = bankAcctRepo.retrieveAccountDetails(accountTo);
        bWithdrawalAccountValid = withdrawalAccount.getIsActive();
        bDepositAccountValid = depositAccount.getIsActive();
        if (bWithdrawalAccountValid && bDepositAccountValid) {
            bProceed = true;
        }

        //2. check withdrawn account has more money then withdrawal amount
        if (bProceed) {
            if (withdrawalAccount.getBalance() >= amount) {
                bProceed = true;
            } else {
                bProceed = false;
            }
        }

        if (bProceed) {
            //3. perform withdrawal (requires transactions)
            bWithdrawn = bankAcctRepo.withdrawAmount(accountFrom, amount);

            //bWithdrawn = false;
            if (!bWithdrawn) {
                throw new IllegalArgumentException("Simulate error before Withdrawal");
            }

            //4. perform deposit (requires transactions)
            bDeposited = bankAcctRepo.depositAmount(accountTo, amount);

        }
        
        //5. check transactions successful?
        if (bWithdrawn && bDeposited) {
            bTransferred = true;
        }

        return bTransferred;

    }
    
}
