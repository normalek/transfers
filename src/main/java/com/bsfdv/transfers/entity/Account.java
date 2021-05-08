package com.bsfdv.transfers.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
public class Account {
    @Id
    private Integer id;
    private String status;
    @Column(name="opened_date")
    private LocalDateTime openedDate;
    private String alias;
    private BigDecimal balance;

    @Version
    private Long version;
}
