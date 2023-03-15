package IBF.day24.Transaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import IBF.day24.Transaction.repository.BankAccountRepo;

@Service
public class BankAccountService {
    @Autowired
    BankAccountRepo bankAcctRepo;

    //set isolation levels
    @Transactional(isolation)
    public transferMoney(Integer accountFrom, Integer accountTo, Float amount) {
        Boolean bTransferred = false;
        Boolean bWithdrawn = false;
        Boolean bDeposited = false;
        Boolean bCanWithdraw = false;

        //Logic flow of this function in the service layer:

        //1. check if accounts (withdrawere and depositer) are valid (active)
        //2. check withdrawn account has more money then withdrawal amount
        //3. perform withdrawal (requires transactions)
        //4. perform deposit (requires transactions)
        //5. check transactions successful?

        return null;

    }
    
}
