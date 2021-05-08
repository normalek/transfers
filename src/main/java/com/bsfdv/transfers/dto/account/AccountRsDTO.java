package com.bsfdv.transfers.dto.account;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class AccountRsDTO {
    @ApiModelProperty(notes = "Amount of money for transferring.", example = "1000341")
    private Integer id;

    @ApiModelProperty(notes = "Status of the account.", example = "ACTIVE")
    private String status;

    @ApiModelProperty(notes = "Opened date of the account.", example = "2021-05-08T09:29:01.359Z")
    private LocalDateTime openedDate;

    @ApiModelProperty(notes = "Alias of the account.", example = "FIRST_ACCOUNT")
    private String alias;

    @ApiModelProperty(notes = "Type of the account.", example = "VIP")
    private String type;

    @ApiModelProperty(notes = "Current account's balance.", example = "20.15")
    private BigDecimal balance;
}
