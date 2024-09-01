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
import com.mycompany.appcs2.App.Dto.UserDTO;
import com.mycompany.appcs2.App.dao.interfaces.PartnerDao;
import com.mycompany.appcs2.App.Dto.InvoiceDTO;
import com.mycompany.appcs2.App.Helpers.Helpers;
import com.mycompany.appcs2.App.model.Partner;
import com.mycompany.appcs2.App.model.User;
import java.sql.Date;
import java.sql.Statement;

public class Partnerimplementation implements PartnerDao {

    @Override
    public void createPartner(UserDTO userDTO) throws Exception {
        String sql = "INSERT INTO partner (USERID, AMOUNT, TYPE, CREATIONDATE) VALUES (?, ?, ?, ?)";
        try (Connection conn = MYSQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, userDTO.getId());
            pstmt.setDouble(2, 50000); // Initial fund for regular partners
            pstmt.setString(3, "regular");
            pstmt.setDate(4, new Date(System.currentTimeMillis()));
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long partnerId = generatedKeys.getLong(1);
                    // You might want to set this ID to the DTO or use it somehow
                }
            }
        }
    }

    @Override
    public void createPartne(PartnerDTO partnerDTO) throws Exception {
        String sql = "INSERT INTO partner (USERID, AMOUNT, TYPE, CREATIONDATE) VALUES (?, ?, ?, ?)";
        try (Connection conn = MYSQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, partnerDTO.getUserId().getId());
            pstmt.setDouble(2, partnerDTO.getFundsmoney());
            pstmt.setString(3, partnerDTO.getTypeSuscription());
            pstmt.setDate(4, new Date(partnerDTO.getDateCreated().getTime()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Error al crear el socio: " + e.getMessage(), e);
        }
    }

    @Override
    public void updatePartnerFunds(PartnerDTO partnerDto) throws Exception {
        String sql = "UPDATE partner SET AMOUNT = ? WHERE ID = ?";
        try (Connection conn = MYSQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, partnerDto.getFundsmoney());
            pstmt.setLong(2, partnerDto.getId());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void updatePartner(PartnerDTO partnerDto) throws Exception {
        String sql = "UPDATE partner SET USERID = ?, AMOUNT = ?, TYPE = ?, CREATIONDATE = ? WHERE ID = ?";
        try (Connection conn = MYSQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, partnerDto.getUserId().getId());
            pstmt.setDouble(2, partnerDto.getFundsmoney());
            pstmt.setString(3, partnerDto.getTypeSuscription());
            pstmt.setDate(4, new Date(partnerDto.getDateCreated().getTime()));
            pstmt.setLong(5, partnerDto.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Error al actualizar el socio: " + e.getMessage(), e);
        }
    }

    @Override
    public List<InvoiceDTO> getPendingInvoices(long id) throws Exception {
        List<InvoiceDTO> pendingInvoices = new ArrayList<>();
        String sql = "SELECT * FROM invoice WHERE PARTNERID = ? AND STATUS = 'pending'";
        try (Connection conn = MYSQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    InvoiceDTO invoiceDTO = new InvoiceDTO();
                    invoiceDTO.setId(rs.getLong("ID"));
                    invoiceDTO.setAmount(rs.getDouble("AMOUNT"));
                    invoiceDTO.setCreationDate(rs.getTimestamp("CREATIONDATE"));
                    invoiceDTO.setStatus(rs.getBoolean("STATUS"));
                    pendingInvoices.add(invoiceDTO);
                }
            }
        }
        return pendingInvoices;
    }

    @Override
    public void payInvoice(long id) throws Exception {
        String sql = "UPDATE invoice SET STATUS = 'paid' WHERE ID = ?";
        try (Connection conn = MYSQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<InvoiceDTO> getPaidInvoices(long partnerId) throws Exception {
        List<InvoiceDTO> paidInvoices = new ArrayList<>();
        String sql = "SELECT * FROM invoice WHERE PARTNERID = ? AND STATUS = 'paid'";
        try (Connection conn = MYSQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, partnerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    InvoiceDTO invoiceDTO = new InvoiceDTO();
                    invoiceDTO.setId(rs.getLong("ID"));
                    invoiceDTO.setAmount(rs.getDouble("AMOUNT"));
                    invoiceDTO.setCreationDate(rs.getTimestamp("CREATIONDATE"));
                    invoiceDTO.setStatus(rs.getBoolean("STATUS"));
                    paidInvoices.add(invoiceDTO);
                }
            }
        } catch (SQLException e) {
            throw new Exception("Error al obtener facturas pagadas: " + e.getMessage(), e);
        }
        return paidInvoices;
    }

    @Override
    public PartnerDTO findPartnerById(long partnerId) throws Exception {
        String sql = "SELECT p.*, u.USERNAME, u.ROLE FROM partner p JOIN user u ON p.USERID = u.ID WHERE p.ID = ?";
        try (Connection conn = MYSQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, partnerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPartnerDTO(rs);
                }
            }
        }
        return null;

    }

    @Override
    public void updatePartnerSubscription(long partnerId, String newType) throws Exception {
        String sql = "UPDATE partner SET TYPE = ? WHERE ID = ?";
        try (Connection conn = MYSQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newType);
            pstmt.setLong(2, partnerId);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void deactivatePartner(long partnerId) throws Exception {
        String sql = "UPDATE partner SET STATUS = 'inactive' WHERE ID = ?";
        try (Connection conn = MYSQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, partnerId);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void activateGuest(long guestId) throws Exception {
        String sql = "UPDATE guest SET STATUS = 'active' WHERE ID = ?";
        try (Connection conn = MYSQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, guestId);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void deactivateGuest(long guestId) throws Exception {
        String sql = "UPDATE guest SET STATUS = 'inactive' WHERE ID = ?";
        try (Connection conn = MYSQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, guestId);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void requestVIPPromotion(long partnerId) throws Exception {
        String sql = "UPDATE partner SET VIP_REQUEST_STATUS = 'pending' WHERE ID = ?";
        try (Connection conn = MYSQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, partnerId);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void uploadFunds(long partnerId, double amount) throws Exception {
        String sql = "UPDATE partner SET AMOUNT = AMOUNT + ? WHERE ID = ?";
        try (Connection conn = MYSQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setLong(2, partnerId);
            pstmt.executeUpdate();
        }
    }

    @Override

    public PartnerDTO mapResultSetToPartnerDTO(ResultSet rs) throws SQLException {
        PartnerDTO partnerDTO = new PartnerDTO();
        partnerDTO.setId(rs.getLong("ID"));

        UserDTO userDTO = new UserDTO();
        userDTO.setId(rs.getLong("USERID"));
        userDTO.setUsername(rs.getString("USERNAME"));
        userDTO.setRol(rs.getString("ROLE"));
        partnerDTO.setUserId(userDTO);

        partnerDTO.setFundsmoney(rs.getDouble("AMOUNT"));
        partnerDTO.setTypeSuscription(rs.getString("TYPE"));
        partnerDTO.setDateCreated(rs.getDate("CREATIONDATE"));

        return partnerDTO;
    }

    @Override
    public boolean isVIPSlotAvailable() throws Exception {
        String sql = "SELECT COUNT(*) as vip_count FROM partner WHERE TYPE = 'VIP'";
        try (Connection conn = MYSQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                int vipCount = rs.getInt("vip_count");
                return vipCount < 5; // Asumiendo un máximo de 5 espacios VIP
            }
        } catch (SQLException e) {
            throw new Exception("Error al comprobar la disponibilidad de espacios VIP: " + e.getMessage(), e);
        }
        return false;
    }

    @Override
    public List<PartnerDTO> getPendingVIPRequests() throws Exception {
        List<PartnerDTO> pendingRequests = new ArrayList<>();
        String sql = "SELECT p.*, u.USERNAME, u.ROLE FROM partner p JOIN user u ON p.USERID = u.ID WHERE p.TYPE = 'PENDING_VIP'";
        try (Connection conn = MYSQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                pendingRequests.add(Helpers.parse(mapResultSetToPartner(rs)));
            }
        } catch (SQLException e) {
            throw new Exception("Error al obtener solicitudes VIP pendientes: " + e.getMessage(), e);
        }
        return pendingRequests;
    }

    @Override
    public PartnerDTO findPartnerByUserId(long id) throws Exception {
        String sql = "SELECT p.*, u.USERNAME, u.ROLE FROM partner p JOIN user u ON p.USERID = u.ID WHERE p.USERID = ?";
        try (Connection conn = MYSQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Helpers.parse(mapResultSetToPartner(rs));
                }
            }
        } catch (SQLException e) {
            throw new Exception("Error al buscar socio por ID de usuario: " + e.getMessage(), e);
        }
        return null;
    }

    private Partner mapResultSetToPartner(ResultSet rs) throws SQLException {
        Partner partner = new Partner();
        partner.setId(rs.getLong("ID"));
        User user = new User();
        user.setId(rs.getLong("USERID"));
        user.setUsername(rs.getString("USERNAME"));
        user.setRol(rs.getString("ROLE"));
        partner.setUserId(user);
        partner.setFundsMoney(rs.getDouble("AMOUNT"));
        partner.setTypeSuscription(rs.getString("TYPE"));
        partner.setDateCreated(rs.getDate("CREATIONDATE"));
        return partner;
    }

}
