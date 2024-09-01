/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.appcs2.App.dao;

/**
 *
 * @author CLAUDIA
 *
 */
import com.mycompany.appcs2.App.config.MYSQLConnection;
import com.mycompany.appcs2.App.dao.interfaces.GuestDao;
import com.mycompany.appcs2.App.Dto.GuestDTO;
import com.mycompany.appcs2.App.Dto.UserDTO;
import com.mycompany.appcs2.App.Dto.PartnerDTO;
import com.mycompany.appcs2.App.Helpers.Helpers;
import com.mycompany.appcs2.App.model.Guest;
import com.mycompany.appcs2.App.model.User;
import com.mycompany.appcs2.App.model.Partner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GuestImplementation implements GuestDao { // actualizar el guest en el helpers

    private Connection connection;

    public GuestImplementation() throws SQLException {
        this.connection = MYSQLConnection.getConnection();
    }

    @Override
    public GuestDTO findGuestById(long guestId) throws Exception {
        String query = "SELECT * FROM guest WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, guestId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToGuestDTO(rs);
                }
            }
        } catch (SQLException e) {
            throw new Exception("Error finding guest by ID", e);
        }
        return null;
    }

    @Override
    public void updateGuest(GuestDTO guest) {
        String query = "UPDATE guest SET USERID = ?, PARTNERID = ?, STATUS = ? WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, guest.getUserId().getId());
            pstmt.setLong(2, guest.getPartnerId().getId());
            pstmt.setString(3, guest.isStatus() ? "ACTIVE" : "INACTIVE");
            pstmt.setLong(4, guest.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating guest", e);
        }
    }

    @Override
    public GuestDTO findGuestByUserId(long id) throws Exception {
        String query = "SELECT * FROM guest WHERE USERID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToGuestDTO(rs);
                }
            }
        } catch (SQLException e) {
            throw new Exception("Error finding guest by user ID", e);
        }
        return null;
    }

    @Override
    public void deleteGuest(long guestId) throws Exception {
        String query = "DELETE FROM guest WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, guestId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Error deleting guest", e);
        }
    }

    @Override
    public void createGuest(GuestDTO guestDTO) throws Exception {
        String query = "INSERT INTO guest (USERID, PARTNERID, STATUS) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, guestDTO.getUserId().getId());
            pstmt.setLong(2, guestDTO.getPartnerId().getId());
            pstmt.setString(3, guestDTO.isStatus() ? "ACTIVE" : "INACTIVE");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Error creating guest", e);
        }
    }

    private GuestDTO mapResultSetToGuestDTO(ResultSet rs) throws SQLException {
        GuestDTO guestDTO = new GuestDTO();
        guestDTO.setId(rs.getLong("ID"));

        UserDTO userDTO = new UserDTO();
        userDTO.setId(rs.getLong("USERID"));
        guestDTO.setUserId(userDTO);

        PartnerDTO partnerDTO = new PartnerDTO();
        partnerDTO.setId(rs.getLong("PARTNERID"));
        guestDTO.setPartnerId(partnerDTO);

        guestDTO.setStatus("ACTIVE".equals(rs.getString("STATUS")));
        return guestDTO;
    }

    // Helper method to convert GuestDTO to Guest model
    private Guest convertToModel(GuestDTO dto) {
        Guest guest = new Guest();
        guest.setId(dto.getId());

        User user = new User();
        user.setId(dto.getUserId().getId());
        guest.setUserId(user);

        Partner partner = new Partner();
        partner.setId(dto.getPartnerId().getId());
        guest.setPartnerId(partner);

        guest.setStatus(dto.isStatus());
        return guest;
    }

    // Helper method to convert Guest model to GuestDTO
    private GuestDTO convertToDTO(Guest guest) {
        GuestDTO dto = new GuestDTO();
        dto.setId(guest.getId());

        UserDTO userDTO = new UserDTO();
        userDTO.setId(guest.getUserId().getId());
        dto.setUserId(userDTO);

        PartnerDTO partnerDTO = new PartnerDTO();
        partnerDTO.setId(guest.getPartnerId().getId());
        dto.setPartnerId(partnerDTO);

        dto.setStatus(guest.isStatus());
        return dto;
    }
}


// select busca 
// insertar
// update buscar
// delete eliminiar 

