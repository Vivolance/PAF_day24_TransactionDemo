package IBF.day24.Transaction.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {

    //This 3 are parameters to pass into postman for testing
    private Integer accountFrom;

    private Integer accountTo;

    private Float amount;
    
}
