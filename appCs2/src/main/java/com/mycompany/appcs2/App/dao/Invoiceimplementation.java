/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.appcs2.App.dao;

/**
 *
 * @author CLAUDIA
 */



import com.mycompany.appcs2.App.config.MYSQLConnection;
import com.mycompany.appcs2.App.dao.interfaces.InvoiceDao;
import com.mycompany.appcs2.App.Dto.*;
import com.mycompany.appcs2.App.model.*;
import com.mycompany.appcs2.App.Helpers.Helpers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Invoiceimplementation implements InvoiceDao {
    
    @Override
    public void createInvoice(InvoiceDTO invoiceDto) throws Exception {
        String sql = "INSERT INTO invoice (PERSONID, PARTNERID, CREATIONDATE, AMOUNT, STATUS) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = MYSQLConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            Invoice invoice = Helpers.parse(invoiceDto);
            pstmt.setLong(1, invoice.getPersonId().getId());
            pstmt.setLong(2, invoice.getPartnerId().getId());
            pstmt.setTimestamp(3, new Timestamp(invoice.getCreationDate().getTime()));
            pstmt.setDouble(4, invoice.getAmount());
            pstmt.setBoolean(5, invoice.isStatus());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating invoice failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    invoice.setId(generatedKeys.getLong(1));
                    invoiceDto = Helpers.parse(invoice);
                } else {
                    throw new SQLException("Creating invoice failed, no ID obtained.");
                }
            }
        }
    }

    @Override
    public List<InvoiceDTO> getInvoicesByPartnerId(long partnerId) throws Exception {
        List<InvoiceDTO> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoice WHERE PARTNERID = ?";
        try (Connection conn = MYSQLConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, partnerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Invoice invoice = mapResultSetToInvoice(rs);
                    invoices.add(Helpers.parse(invoice));
                }
            }
        }
        return invoices;
    }

    @Override
    public List<InvoiceDTO> getInvoicesByGuestId(long guestId) throws Exception {
        List<InvoiceDTO> invoices = new ArrayList<>();
        String sql = "SELECT i.* FROM invoice i JOIN guest g ON i.PERSONID = g.USERID WHERE g.ID = ?";
        try (Connection conn = MYSQLConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, guestId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Invoice invoice = mapResultSetToInvoice(rs);
                    invoices.add(Helpers.parse(invoice));
                }
            }
        }
        return invoices;
    }

    @Override
    public InvoiceDTO findInvoiceById(long invoiceId) throws Exception {
        String sql = "SELECT * FROM invoice WHERE ID = ?";
        try (Connection conn = MYSQLConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, invoiceId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Invoice invoice = mapResultSetToInvoice(rs);
                    return Helpers.parse(invoice);
                }
            }
        }
        return null;
    }

    @Override
    public void updateInvoice(InvoiceDTO invoiceDto) throws Exception {
        String sql = "UPDATE invoice SET PERSONID = ?, PARTNERID = ?, CREATIONDATE = ?, AMOUNT = ?, STATUS = ? WHERE ID = ?";
        try (Connection conn = MYSQLConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            Invoice invoice = Helpers.parse(invoiceDto);
            pstmt.setLong(1, invoice.getPersonId().getId());
            pstmt.setLong(2, invoice.getPartnerId().getId());
            pstmt.setTimestamp(3, new Timestamp(invoice.getCreationDate().getTime()));
            pstmt.setDouble(4, invoice.getAmount());
            pstmt.setBoolean(5, invoice.isStatus());
            pstmt.setLong(6, invoice.getId());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating invoice failed, no rows affected.");
            }
        }
    }

    @Override
    public long createInvoicess(InvoiceDTO invoiceDto) {
        String sql = "INSERT INTO invoice (PERSONID, PARTNERID, CREATIONDATE, AMOUNT, STATUS) VALUES (?, ?, ?, ?, ?)";
        long generatedId = -1;
        
        try (Connection conn = MYSQLConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            Invoice invoice = Helpers.parse(invoiceDto);
            pstmt.setLong(1, invoice.getPersonId().getId());
            pstmt.setLong(2, invoice.getPartnerId().getId());
            pstmt.setTimestamp(3, new Timestamp(invoice.getCreationDate().getTime()));
            pstmt.setDouble(4, invoice.getAmount());
            pstmt.setBoolean(5, invoice.isStatus());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating invoice failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Creating invoice failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return generatedId;
    }

    private Invoice mapResultSetToInvoice(ResultSet rs) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setId(rs.getLong("ID"));
        
        Person person = new Person();
        person.setId(rs.getLong("PERSONID"));
        invoice.setPersonId(person);
        
        Partner partner = new Partner();
        partner.setId(rs.getLong("PARTNERID"));
        invoice.setPartnerId(partner);
        
        invoice.setCreationDate(rs.getDate("CREATIONDATE"));
        invoice.setAmount(rs.getDouble("AMOUNT"));
        invoice.setStatus(rs.getBoolean("STATUS"));
        
        return invoice;
    }
}




// select busca 
// insertar
// update buscar
// delete eliminiar 


