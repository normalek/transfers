package com.bsfdv.transfers.dto.transfer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Data
@SuperBuilder
@Jacksonized
@NoArgsConstructor
public class TransferBaseDTO {
    @ApiModelProperty(notes = "Id of the account transferring money from.", required = true, example = "1000341")
    private Integer fromAccount;

    @ApiModelProperty(notes = "Id of the account transferring money to.", required = true, example = "1000342")
    private Integer toAccount;

    @ApiModelProperty(notes = "Amount of money for transferring.", required = true, example = "10.12")
    private BigDecimal amount;
}
