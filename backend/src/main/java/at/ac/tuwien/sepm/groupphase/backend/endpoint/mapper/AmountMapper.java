package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AmountDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Amount;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

/**
 * Mapper that maps Amount to AmountDtos.
 */
@Mapper
public interface AmountMapper {
    @Named("amount")
    AmountDto amountToAmountDto(Amount amount);

    @IterableMapping(qualifiedByName = "amount")
    List<AmountDto> amountToAmountDto(List<Amount> amounts);
}
