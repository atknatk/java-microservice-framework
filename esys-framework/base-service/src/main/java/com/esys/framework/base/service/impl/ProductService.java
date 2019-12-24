package com.esys.framework.base.service.impl;

import com.esys.framework.base.currency.Moneys;
import com.esys.framework.base.dto.ProductDto;
import com.esys.framework.base.dto.ReportVersionDto;
import com.esys.framework.base.dto.SumCurrencyDto;
import com.esys.framework.base.entity.Currency;
import com.esys.framework.base.entity.Product;
import com.esys.framework.base.entity.ReportVersion;
import com.esys.framework.base.enums.ImportSectionEnum;
import com.esys.framework.base.enums.ReportName;
import com.esys.framework.base.exceptions.ReportGenerationException;
import com.esys.framework.base.reader.ImportReader;
import com.esys.framework.base.reader.excel.ExcelReader;
import com.esys.framework.base.report.JasperReportsPdfExporter;
import com.esys.framework.base.report.ReportColumn;
import com.esys.framework.base.repository.IProductRepository;
import com.esys.framework.base.repository.IReportVersionRepository;
import com.esys.framework.base.service.ICurrencyService;
import com.esys.framework.base.service.IProductService;
import com.esys.framework.base.service.IReportService;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.core.service.impl.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
@Service
@Slf4j
public  class ProductService extends BaseService<ProductDto, Product> implements IProductService {

    private transient IProductRepository productRepository;
    private transient IReportVersionRepository reportVersionRepository;
    private transient IReportService reportService;
    private transient ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
            .configure(DeserializationConfig.Feature.FAIL_ON_NUMBERS_FOR_ENUMS, false);
    private transient ImportReader reader;

    private final transient ICurrencyService currencyService;
    private final transient ModelMapper mapper;
    @Autowired
    public ProductService(ModelMapper mapper, IProductRepository productRepository, ICurrencyService currencyService,
            IReportService reportService, IReportVersionRepository reportVersionRepository) {
        super(mapper, log, ProductDto.class, Product.class, productRepository);
        this.productRepository = productRepository;
        this.currencyService = currencyService;
        this.reportService = reportService;
        this.mapper = mapper;
        this.reportVersionRepository = reportVersionRepository;
    }

    @Override
    protected boolean exist(ProductDto dto) {
        return productRepository.existsByName(dto.getName());
    }

    public ProductDto getProductByCurrency(Long id,Moneys moneys){
        ProductDto productDto= findById(id);
        Currency newcurrency =currencyService.getPrice(moneys);
        Currency oldcurrency =currencyService.getPrice(productDto.getMoney());
        productDto.setMoney(moneys);
        productDto.setPrice(productDto.getPrice() * (newcurrency.getBuyingPrice()/oldcurrency.getBuyingPrice()));
        return productDto;
    }


    private void revert(List<ProductDto> productDtos){
        List<ProductDto> clone = cloneList(productDtos);
        Iterable<Product>  products = productRepository.findAll();
        for (Product product: products) {
            Optional<ProductDto> dto =  clone.stream().filter(p -> p.getId().equals(product.getId())).findFirst();
            if(dto.isPresent()){
                if(!dto.get().equals(product)){
                    Product update=  mapper.map(dto.get(),Product.class);
                    productRepository.save(update);
                }
                clone.remove(dto.get());
            }else{
                productRepository.delete(product);
            }
        }
        clone.stream().forEach(c -> c.setId(null));
        productRepository.saveAll(toEntity(clone));
    }

    @Override
    public void saveOrUpdate(List<Product> products) {
        for (Product product : products) {
            Optional<Product> optional = productRepository.findByName(product.getName());
            if(optional.isPresent()){
                product.setId(optional.get().getId());
            }
            if(product.getMoney() == null){
                product.setMoney(Moneys.TURKISH_TL);
            }
        }
        productRepository.saveAll(products);
    }

    @Override
    public void uploadProduct(MultipartFile file) {
      reader = new ExcelReader();
        try {
            List<Product> products = reader.read(file.getInputStream(), ImportSectionEnum.PRODUCT,Product.class);
            saveOrUpdate(products);
            reportService.export(findAll(), new String[]{"name"}, getColumnMap(), null, new JasperReportsPdfExporter(), "products_report.pdf",false, ReportName.PRODUCT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<ProductDto> getAllViaRevision(int revision) {
        final Optional<ReportVersion> byReportNameAndVersion = this.reportVersionRepository.findByReportNameAndVersion(ReportName.PRODUCT, revision);
        if(byReportNameAndVersion.isPresent()){
            String data = byReportNameAndVersion.get().getData();
            try {
                return objectMapper.readValue(data,new TypeReference<List<ProductDto>>(){});
            } catch (IOException e) {
                log.error("Parse error", e);
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }

    @Override
    public ModelResult<List<ReportVersionDto>> version() {
        List<ReportVersionDto> list = reportVersionRepository.version(ReportName.PRODUCT);
        return new ModelResult.ModelResultBuilder<List<ReportVersionDto>>().setData(list).success();
    }

    public ReportVersion revert(Integer version){
        Optional<ReportVersion> reportVersion = reportVersionRepository.findByReportNameAndVersion(ReportName.PRODUCT,version);
        if(reportVersion.isPresent()){
            try {
                final ReportVersion dbReportVersion = reportVersion.get();
                List<ProductDto> list = objectMapper.readValue(dbReportVersion.getData(), new TypeReference<List<ProductDto>>(){});
                revert(list);
                this.reportVersionRepository.updateCurrent(ReportName.PRODUCT);
                dbReportVersion.setId(null);
                dbReportVersion.setCurrent(true);
                dbReportVersion.setVersion(reportVersionRepository.getVersionNoList(ReportName.PRODUCT).get(0)+1);
                final ReportVersion save = reportVersionRepository.save(dbReportVersion);
                return save;
            } catch (IOException e) {
                throw new ReportGenerationException();
            }

        }
        return null;
    }

    private List<ProductDto> cloneList(List<ProductDto> list) {
        List<ProductDto> clone = new ArrayList<ProductDto>(list.size());
        for (ProductDto item : list) clone.add(new ProductDto(item));
        return clone;
    }



    public Map<String, ReportColumn> getColumnMap() {
        Map<String, ReportColumn> columnMap = new HashMap<>();
        columnMap.put("id", new ReportColumn("product.report.id", "Id", 50, Long.class.getName()));
        columnMap.put("name", new ReportColumn("product.report.name", "Ad", 100, String.class.getName()));
        columnMap.put("moneyLocale", new ReportColumn("product.report.money", "Para Birimi", 100, String.class.getName()));
        columnMap.put("piece", new ReportColumn("product.report.piece", "Adet", 100, Float.class.getName(),false,"0"));
        columnMap.put("price", new ReportColumn("product.report.price", "Fiyat", 100, Float.class.getName(),false,"0.00 TL"));
        columnMap.put("total", new ReportColumn("product.report.total", "Toplam", 100, Float.class.getName(),false,"0.00 TL"));
        return columnMap;
    }

    @Override
    public SumCurrencyDto sum() {
        final List<ProductDto> all = findAll();
        final List<Currency> today = currencyService.today();

        SumCurrencyDto  dto = new SumCurrencyDto();
        AtomicReference<Float> sum = new AtomicReference<>((float) 0);
        all.stream().forEach(l -> sum.updateAndGet(v -> new Float((float) (v + (l.getPrice() * l.getPiece())))));


        dto.setTurkishPrice(sum.get());

        final Optional<Currency> dollar = today.stream().filter(c -> c.getName().equals(Moneys.US_DOLLAR)).findFirst();
        if(dollar.isPresent()){
            dto.setDollarPrice( dollar.get().getBuyingPrice() * sum.get());
        }

        final Optional<Currency> euro = today.stream().filter(c -> c.getName().equals(Moneys.EURO)).findFirst();
        if(euro.isPresent()){
            dto.setEuroPrice( euro.get().getBuyingPrice() * sum.get());
        }


        final Optional<Currency> gbp = today.stream().filter(c -> c.getName().equals(Moneys.POUND_STERLING)).findFirst();
        if(gbp.isPresent()){
            dto.setGbpPrice( gbp.get().getBuyingPrice() * sum.get());
        }

        final Optional<Currency> riyal = today.stream().filter(c -> c.getName().equals(Moneys.SAUDI_RIYAL)).findFirst();
        if(riyal.isPresent()){
            dto.setRiyaPrice( riyal.get().getBuyingPrice() * sum.get());
        }

        return dto;
    }
}
