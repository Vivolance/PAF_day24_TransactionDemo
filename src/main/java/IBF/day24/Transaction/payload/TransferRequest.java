package IBF.day24.Transaction.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {

    private Integer accountFrom;

    private Integer accountTo;

    private Float amount;
    
}
