package com.bsfdv.transfers;

import com.bsfdv.transfers.controller.AccountsController;
import com.bsfdv.transfers.dto.account.AccountRsDTO;
import com.bsfdv.transfers.dto.transfer.TransferRqDTO;
import com.bsfdv.transfers.dto.transfer.TransferRsDTO;
import com.bsfdv.transfers.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountsController.class)
public class AccountRestControllerIntegrationTest {

    private final static Integer ACCOUNT_ID = 1000340;
    private final AccountRsDTO account = AccountRsDTO.builder().id(ACCOUNT_ID).build();
    private final TransferRqDTO TRANSFER_RQ_DTO = TransferRqDTO.builder().fromAccount(ACCOUNT_ID).toAccount(1000342)
            .amount(BigDecimal.valueOf(3.12)).build();
    private final TransferRsDTO TRANSFER_RS_DTO = TransferRsDTO.builder().fromAccount(ACCOUNT_ID).toAccount(1000342)
            .amount(BigDecimal.valueOf(3.12)).build();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AccountService accountService;

    @Test
    public void whenGetAccountById_thenResponseWithAccount() throws Exception {
        given(accountService.getAccountById(ACCOUNT_ID)).willReturn(account);

        MvcResult result =mvc.perform(get("/v1/accounts/"+ACCOUNT_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(String.valueOf(ACCOUNT_ID)));
    }

    @Test
    public void whenPostTransfer_thenResponseWithAmount() throws Exception {
        given(accountService.transferMoney(TRANSFER_RQ_DTO)).willReturn(TRANSFER_RS_DTO);

        MvcResult result =mvc.perform(MockMvcRequestBuilders
                .post("/v1/accounts/transfer-money")
                .content(mapper.writeValueAsString(TRANSFER_RQ_DTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(String.valueOf(ACCOUNT_ID)));
        assertTrue(result.getResponse().getContentAsString().contains(String.valueOf(TRANSFER_RQ_DTO.getAmount())));
    }
}
