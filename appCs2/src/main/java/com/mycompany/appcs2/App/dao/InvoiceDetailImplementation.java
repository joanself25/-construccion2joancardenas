/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.appcs2.App.dao;

import com.mycompany.appcs2.App.Dto.InvoiceDetailDTO;
import com.mycompany.appcs2.App.dao.interfaces.InvoiceDetailDao;
import com.mycompany.appcs2.App.config.MYSQLConnection;
import com.mycompany.appcs2.App.Helpers.Helpers;
import com.mycompany.appcs2.App.model.InvoiceDetail;
import com.mycompany.appcs2.App.model.Invoice;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDetailImplementation implements InvoiceDetailDao {

    @Override
    public void createInvoiceDetail(InvoiceDetailDTO detailDto) throws Exception {
        String sql = "INSERT INTO invoicedetail (INVOICEID, ITEM, DESCRIPTION, AMOUNT) VALUES (?, ?, ?, ?)";
        try (Connection connection = MYSQLConnection.getConnection(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            InvoiceDetail detail = Helpers.parse(detailDto);
            pstmt.setLong(1, detail.getInvoiceId().getId());
            pstmt.setInt(2, detail.getItem());
            pstmt.setString(3, detail.getDescription());
            pstmt.setDouble(4, detail.getAmount());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Error al crear el detalle de la factura: " + e.getMessage());
        }
    }

    @Override
    public InvoiceDetailDTO findInvoiceDetailById(long id) throws Exception {
        String sql = "SELECT * FROM invoicedetail WHERE ID = ?";
        try (Connection connection = MYSQLConnection.getConnection(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Helpers.parse(mapResultSetToInvoiceDetail(rs));
                }
            }
        } catch (SQLException e) {
            throw new Exception("Error al encontrar detalles de la factura: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<InvoiceDetailDTO> findInvoiceDetailsByInvoiceId(long invoiceId) throws Exception {
        String sql = "SELECT * FROM invoicedetail WHERE INVOICEID = ?";
        List<InvoiceDetailDTO> details = new ArrayList<>();
        try (Connection connection = MYSQLConnection.getConnection(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, invoiceId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    details.add(Helpers.parse(mapResultSetToInvoiceDetail(rs)));
                }
            }
        } catch (SQLException e) {
            throw new Exception("Error al encontrar los detalles de la factura: " + e.getMessage());
        }
        return details;
    }

    @Override
    public void updateInvoiceDetail(InvoiceDetailDTO detailDto) throws Exception {
        String sql = "UPDATE invoicedetail SET ITEM = ?, DESCRIPTION = ?, AMOUNT = ? WHERE ID = ?";
        try (Connection connection = MYSQLConnection.getConnection(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            InvoiceDetail detail = Helpers.parse(detailDto);
            pstmt.setInt(1, detail.getItem());
            pstmt.setString(2, detail.getDescription());
            pstmt.setDouble(3, detail.getAmount());
            pstmt.setLong(4, detail.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Error al actualizar el detalle de la factura: " + e.getMessage());
        }
    }

    @Override
    public void deleteInvoiceDetail(long id) throws Exception {
        String sql = "DELETE FROM invoicedetail WHERE ID = ?";
        try (Connection connection = MYSQLConnection.getConnection(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Error al eliminar detalle de factura: " + e.getMessage());
        }
    }

    private InvoiceDetail mapResultSetToInvoiceDetail(ResultSet rs) throws SQLException {
        InvoiceDetail detail = new InvoiceDetail();
        detail.setId(rs.getLong("ID"));
        detail.setItem(rs.getInt("ITEM"));
        detail.setDescription(rs.getString("DESCRIPTION"));
        detail.setAmount(rs.getDouble("AMOUNT"));

        Invoice invoice = new Invoice();
        invoice.setId(rs.getLong("INVOICEID"));
        detail.setInvoiceId(invoice);

        return detail;
    }
}
// select busca 
// insertar
// update buscar
// delete eliminiar 

