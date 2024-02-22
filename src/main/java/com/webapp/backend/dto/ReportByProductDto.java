package com.webapp.backend.dto;


import com.webapp.backend.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportByProductDto {

    private Product product;

    private double dailyImportQuantity;;

    private double totalAmountByProduct;

    private double totalQuantitySaled;

}
