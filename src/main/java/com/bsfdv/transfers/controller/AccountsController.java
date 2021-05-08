package com.bsfdv.transfers.controller;

import com.bsfdv.transfers.dto.account.AccountRsDTO;
import com.bsfdv.transfers.dto.transfer.TransferRqDTO;
import com.bsfdv.transfers.dto.transfer.TransferRsDTO;
import com.bsfdv.transfers.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Api(value = "AccountsController", tags = "REST APIs related to Accounts and Transfers")
@RestController
@RequestMapping("/v1/accounts")
@RequiredArgsConstructor
public class AccountsController {

    private final AccountService accountService;

    @ApiOperation(value = "Accept money transfers to other accounts.", response = TransferRsDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 402, message = "Transfer amount exceeds available balance"),
            @ApiResponse(code = 404, message = "Account doesn't exists") })
    @PostMapping(value = "/transfer-money")
    public ResponseEntity<TransferRsDTO> transferMoney(@Valid @RequestBody TransferRqDTO dto) {
        return ResponseEntity.ok(accountService.transferMoney(dto));
    }

    @ApiOperation(value = "Getting the account details by its id.", response = AccountRsDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 404, message = "Account doesn't exists") })
    @GetMapping(value = "/{id}")
    public ResponseEntity<AccountRsDTO> getById(@NotNull @PathVariable Integer id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }
}
