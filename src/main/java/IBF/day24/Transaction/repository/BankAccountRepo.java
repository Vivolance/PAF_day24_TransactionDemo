package IBF.day24.Transaction.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import IBF.day24.Transaction.model.BankAccount;

@Repository
public class BankAccountRepo {

    @Autowired
    JdbcTemplate jdbcTemplate;

    // SQL queries:
    private final String CHECK_BALANCE_SQL = "select balance from bankaccount where id =?";
    private final String GET_ACCOUNT_SQL = "select * from bankaccount where id =?";
    private final String WITHDRAW_SQL = "update bankaccount set balance = balance - ? where id = ?";
    private final String DEPOSIT_SQL = "update bankaccount set balance = balance + ? where id = ?";
    private final String CREATE_ACCOUNT_SQL = "insert into bankaccount (full_name, is_active, acct_type, balance) values (?, ?, ?, ?)";

    public Boolean checkBalance(Integer accountId, Float withdrawnAmount) {
        Boolean bWithdrawBalanceAvailable = false;

        Float returnedBalanced = jdbcTemplate.queryForObject(CHECK_BALANCE_SQL, Float.class, accountId);

        if (withdrawnAmount <= returnedBalanced) {
            bWithdrawBalanceAvailable = true;
        }

        return bWithdrawBalanceAvailable;
    }

    public BankAccount retrieveAccountDetails(Integer accountId) {
        BankAccount bankAccount = null;

        bankAccount = jdbcTemplate.queryForObject(GET_ACCOUNT_SQL, BeanPropertyRowMapper.newInstance(BankAccount.class), accountId);

        return bankAccount;

        
    }

    public Boolean withdrawAmount(Integer accountId, Float withdrawnAmount) {
        Boolean bWithdrawn = false;
        
        int iUpdated = 0;
        //pass in the ? above in string sql
        iUpdated = jdbcTemplate.update(WITHDRAW_SQL, withdrawnAmount, accountId);

        if (iUpdated > 0) {
            bWithdrawn = true;
        }

        return bWithdrawn;

    }

    public Boolean depositAmount(Integer accountId, Float depositAmount) {
        Boolean bDeposited = false;

        int iUpdated = 0;
        iUpdated = jdbcTemplate.update(DEPOSIT_SQL, depositAmount, accountId);

        if(iUpdated > 0) {
            bDeposited = true;
        }

        return bDeposited;
    }

    public Boolean createAccount(BankAccount bankAccount) {

        Boolean bCreated = false;
        bCreated = jdbcTemplate.execute(CREATE_ACCOUNT_SQL, new PreparedStatementCallback<Boolean>() {
        
            //(full_name, is_active, acct_type, balance)
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {

                ps.setString(1, bankAccount.getFullName());
                ps.setBoolean(2, bankAccount.getIsActive());
                ps.setString(3, bankAccount.getAcctType());
                ps.setFloat(4, bankAccount.getBalance());
                Boolean result = ps.execute();

                return result;

            }
        });

        return bCreated;
    }



    
}
