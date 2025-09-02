package com.hotel.erp.dto;

import com.hotel.erp.entity.AdditionalCharges;
import com.hotel.erp.entity.Advances;
import com.hotel.erp.entity.BillSettlement;
import com.hotel.erp.entity.Checkin;
import com.hotel.erp.entity.FoBill;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Data Transfer Object for Bill Summary information
 */
@Data
public class BillSummaryDTO {
    private FoBill bill;
    private List<BillSettlement> settlements;
    private List<AdditionalCharges> additionalCharges;
    private List<Advances> advances;
    private BigDecimal totalSettlements;
    private BigDecimal totalAdditionalCharges;
    private BigDecimal totalAdvances;
    private BigDecimal balanceDue;
    private Checkin checkin;
}
