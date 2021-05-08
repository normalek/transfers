package com.bsfdv.transfers.dto.transfer;

import com.bsfdv.transfers.entity.TransferStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@SuperBuilder
public class TransferRsDTO extends TransferBaseDTO {
    @ApiModelProperty(notes = "Status of the transfer.")
    private TransferStatusEnum status;

    @ApiModelProperty(notes = "Transfer unique id for further reference.")
    private UUID transactionId;

    @ApiModelProperty(notes = "Date of transfer.")
    private LocalDateTime transactionDate;
}
