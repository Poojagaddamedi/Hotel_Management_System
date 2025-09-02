package com.hotel.erp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "account_year")
public class AccountYear {
    @Id
    @Column(name = "acc_year")
    private String accYear;

    @Column(name = "h_folio_no")
    private String hFolioNo;

    @Column(name = "h_reservation_no")
    private String hReservationNo;

    @Column(name = "h_bill_no")
    private String hBillNo;

    @Column(name = "h_receipt_no")
    private String hReceiptNo;

    @Column(name = "start_year")
    private String startYear;

    @Column(name = "end_year")
    private String endYear;

    @Column(name = "report_email")
    private String reportEmail;

    @Column(name = "report_pdf")
    private String reportPdf;

    @Column(name = "report_others")
    private String reportOthers;

    // Getters and Setters
    public String getAccYear() {
        return accYear;
    }

    public void setAccYear(String accYear) {
        this.accYear = accYear;
    }

    public String getHFolioNo() {
        return hFolioNo;
    }

    public void setHFolioNo(String hFolioNo) {
        this.hFolioNo = hFolioNo;
    }

    public String getHReservationNo() {
        return hReservationNo;
    }

    public void setHReservationNo(String hReservationNo) {
        this.hReservationNo = hReservationNo;
    }

    public String getHBillNo() {
        return hBillNo;
    }

    public void setHBillNo(String hBillNo) {
        this.hBillNo = hBillNo;
    }

    public String getHReceiptNo() {
        return hReceiptNo;
    }

    public void setHReceiptNo(String hReceiptNo) {
        this.hReceiptNo = hReceiptNo;
    }

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public String getEndYear() {
        return endYear;
    }

    public void setEndYear(String endYear) {
        this.endYear = endYear;
    }

    public String getReportEmail() {
        return reportEmail;
    }

    public void setReportEmail(String reportEmail) {
        this.reportEmail = reportEmail;
    }

    public String getReportPdf() {
        return reportPdf;
    }

    public void setReportPdf(String reportPdf) {
        this.reportPdf = reportPdf;
    }

    public String getReportOthers() {
        return reportOthers;
    }

    public void setReportOthers(String reportOthers) {
        this.reportOthers = reportOthers;
    }
}
