package com.esys.framework.base.service;

import com.esys.framework.base.currency.Moneys;
import com.esys.framework.base.dto.ProductDto;
import com.esys.framework.base.dto.ReportVersionDto;
import com.esys.framework.base.dto.SumCurrencyDto;
import com.esys.framework.base.entity.Product;
import com.esys.framework.base.entity.ReportVersion;
import com.esys.framework.base.report.ReportColumn;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.core.service.IBaseService;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
public interface IProductService extends IBaseService<ProductDto> {

    ProductDto getProductByCurrency(Long id, Moneys moneys);

    ReportVersion revert(Integer version);

    void saveOrUpdate(List<Product> products);

    void uploadProduct( MultipartFile file);

    List<ProductDto> getAllViaRevision(int revision);

    ModelResult<List<ReportVersionDto>> version();

    Map<String, ReportColumn> getColumnMap();

    SumCurrencyDto sum();
}
