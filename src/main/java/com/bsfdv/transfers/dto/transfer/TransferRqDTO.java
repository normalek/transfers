package com.bsfdv.transfers.dto.transfer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@SuperBuilder
@NoArgsConstructor
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferRqDTO extends TransferBaseDTO {
    @ApiModelProperty(notes = "Remark as a description.", example = "John Doe's transfer")
    private String remark;
}
