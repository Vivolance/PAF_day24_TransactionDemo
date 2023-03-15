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

    //set isolation levels
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public Boolean transferMoney(Integer accountFrom, Integer accountTo, Float amount) {
        Boolean bTransferred = false;
        Boolean bWithdrawn = false;
        Boolean bDeposited = false;
        Boolean bCanWithdraw = false;

        BankAccount depositAccount = null;
        BankAccount withdrawalAccount = null;

        Boolean bDepositAccountValid = false;
        Boolean bWithdrawalAccountValid = false;
        Boolean bProceed = false;

        //Logic flow of this function in the service layer:

        //1. check if accounts (withdrawere and depositer) are valid (active)
        withdrawalAccount = bankAcctRepo.retrieveAccountDetails(accountFrom);
        depositAccount = bankAcctRepo.retrieveAccountDetails(accountTo);
        bWithdrawalAccountValid = withdrawalAccount.getIsActive();
        bDepositAccountValid = depositAccount.getIsActive();
        if (bWithdrawalAccountValid && bDepositAccountValid) {
            bProceed = true;
        }

        //2. check withdrawn account has more money then withdrawal amount
        if (bProceed) {
            if(withdrawalAccount.getBalance() >= amount) {
                bCanWithdraw = true;
            } else {
                bProceed = false;
            }
        }

        if (bProceed) {
            //3. perform withdrawal (requires transactions)
            bWithdrawn = bankAcctRepo.withdrawAmount(accountFrom, amount);

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
