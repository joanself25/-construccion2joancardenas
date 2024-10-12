package com.mycompany.app.dao.repositories;

import com.mycompany.app.model.InvoiceDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, Long> {
    
    
    
    
    public InvoiceDetail findInvoiceDetailById(long id);

    public List<InvoiceDetail> findByInvoiceId_Id(Long invoiceId);
    

}
