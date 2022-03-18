package payments.domain.vo;

import lombok.NonNull;
import lombok.Value;

@Value
public class AccountId {
    @NonNull
    Integer id;
}
