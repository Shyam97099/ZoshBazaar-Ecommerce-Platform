package com.zosh.service.serviceImpl;

import com.zosh.model.Seller;
import com.zosh.model.SellerReport;
import com.zosh.repository.SellerReportRepository;
import com.zosh.service.SellerReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SellerReportImpl implements SellerReportService {
private final SellerReportRepository sellerReportRepository;
    @Override
    public SellerReport getSellerReport(Seller seller) {
    SellerReport sr= sellerReportRepository.findBySellerId(seller.getId());
    if(sr==null){
    SellerReport newReport=new SellerReport();
    newReport.setSeller(seller);
    return sellerReportRepository.save(newReport);
    }
        return sr;
    }

    @Override
    public SellerReport updateSellerReport(SellerReport sellerReport) {
        return sellerReportRepository.save(sellerReport);
    }
}
