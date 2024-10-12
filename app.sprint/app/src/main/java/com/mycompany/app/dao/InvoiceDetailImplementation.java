package com.mycompany.app.dao;

import com.mycompany.app.Dto.InvoiceDetailDTO;
import com.mycompany.app.dao.interfaces.InvoiceDetailDao;

import com.mycompany.app.Helpers.Helpers;
import com.mycompany.app.dao.repositories.InvoiceDetailRepository;
import com.mycompany.app.model.InvoiceDetail;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@Setter
@Getter

public class InvoiceDetailImplementation implements InvoiceDetailDao {

    @Autowired
    InvoiceDetailRepository invoiceDetailRepository;

    @Override
    public void createInvoiceDetail(InvoiceDetailDTO detailDto) throws Exception {
        InvoiceDetail detail = Helpers.parse(detailDto);
        invoiceDetailRepository.save(detail);
    }

    @Override
    public InvoiceDetailDTO findInvoiceDetailById(long id) throws Exception {
        InvoiceDetail detail = invoiceDetailRepository.findInvoiceDetailById(id);
        if (detail == null) {
            throw new Exception("Detalle de factura no encontrado con id: " + id);
        }
        return Helpers.parse(detail);
    }

    @Override
    public List<InvoiceDetailDTO> findInvoiceDetailsByInvoiceId(long invoiceId) throws Exception {
        List<InvoiceDetail> details = invoiceDetailRepository.findByInvoiceId_Id(invoiceId);
        if (details.isEmpty()) {
            throw new Exception("No se encontraron detalles de factura para el ID de factura: " + invoiceId);
        }

        List<InvoiceDetailDTO> detailDTOs = new ArrayList<>();
        for (InvoiceDetail detail : details) {
            detailDTOs.add(Helpers.parse(detail));
        }
        return detailDTOs;
    }

    @Override
    public void updateInvoiceDetail(InvoiceDetailDTO detailDto) throws Exception {
        if (!invoiceDetailRepository.existsById(detailDto.getId())) {
            throw new Exception("Detalle de factura no encontrado con id: " + detailDto.getId());
        }
        InvoiceDetail detail = Helpers.parse(detailDto);
        invoiceDetailRepository.save(detail);
    }

    @Override
    public void deleteInvoiceDetail(long id) throws Exception {
        if (!invoiceDetailRepository.existsById(id)) {
            throw new Exception("Detalle de factura no encontrado con id: " + id);
        }
        invoiceDetailRepository.deleteById(id);
    }

    /* private InvoiceDetail mapResultSetToInvoiceDetail(ResultSet rs) throws SQLException {
        InvoiceDetail detail = new InvoiceDetail();
        detail.setId(rs.getLong("ID"));

        Invoice invoice = new Invoice();
        invoice.setId(rs.getLong("INVOICEID"));
        detail.setInvoiceId(invoice);

        detail.setItem(rs.getInt("ITEM"));
        detail.setDescription(rs.getString("DESCRIPTION"));
        detail.setAmount(rs.getDouble("AMOUNT"));
        return detail;
    }
     */
}
// select busca 
// insertar
// update buscar
// delete eliminiar 

