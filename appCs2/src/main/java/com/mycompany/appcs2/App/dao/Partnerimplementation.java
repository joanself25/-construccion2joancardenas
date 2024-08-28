/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.appcs2.App.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.mycompany.appcs2.App.config.MYSQLConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.appcs2.App.Dto.PartnerDTO;
import com.mycompany.appcs2.App.dao.interfaces.PartnerDao;
import com.mycompany.appcs2.App.Dto.InvoiceDTO;
import com.mycompany.appcs2.App.model.Partner;
import com.mycompany.appcs2.App.model.Invoice;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Partnerimplementation implements PartnerDao {

    @Override
    public void createPartner(PartnerDTO partnerDto) throws Exception {
        String sql = "INSERT INTO partner(user_id, funds_money, type_subscription, date_created) VALUES (?, ?, ?, ?)";
        try (Connection conn = MYSQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, partnerDto.getUserId().getId());
            pstmt.setDouble(2, partnerDto.getfundsMoney());
            pstmt.setString(3, partnerDto.getTypeSuscription());
            pstmt.setDate(4, partnerDto.getDateCreated());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void updatePartnerFunds(PartnerDTO partnerDto) throws Exception {
        String sql = "UPDATE partners SET funds_money = ? WHERE id = ?";
        try (Connection conn = MYSQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, partnerDto.getfundsMoney());
            pstmt.setLong(2, partnerDto.getId());
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<InvoiceDTO> getPendingInvoices(long partnerId) throws Exception {
        List<InvoiceDTO> pendingInvoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices WHERE partner_id = ? AND status = false ORDER BY creation_date ASC";
        try (Connection conn = MYSQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, partnerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    InvoiceDTO invoice = new InvoiceDTO();
                    invoice.setId(rs.getLong("id"));
                    invoice.setAmount(rs.getDouble("amount"));
                    invoice.setCreationDate(rs.getDate("creation_date"));
                    invoice.setStatus(rs.getBoolean("status"));
                    pendingInvoices.add(invoice);
                }
                return pendingInvoices;
            }

        }

    }

    @Override
    public void payInvoice(long invoiceId) throws Exception {
        String sql = "UPDATE invoices SET status = true WHERE id = ?";
        try (Connection conn = MYSQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, invoiceId);
            pstmt.executeUpdate();
        }
    }

    // eston son los metodos para el cambio de suscripcion : 
    @Override
    public PartnerDTO findPartnerById(long partnerId) throws Exception {

        return null;

    }

    @Override

    public boolean isVIPSlotAvailable() throws Exception {

        return true;

    }

    @Override

    public List<PartnerDTO> getPendingVIPRequests() throws Exception {

        return null;

    }

    @Override

    public PartnerDTO findPartnerByUserId(long id) throws Exception {

        return null;

    }

}
