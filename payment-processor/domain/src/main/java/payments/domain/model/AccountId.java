package payments.domain.model;

import lombok.NonNull;
import lombok.Value;

@Value
public class AccountId {
    @NonNull
    Integer id;
}
