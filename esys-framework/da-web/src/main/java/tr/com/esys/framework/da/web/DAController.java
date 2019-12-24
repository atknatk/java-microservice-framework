package tr.com.esys.framework.da.web;

import com.esys.framework.client.contracts.da.DAContract;
import com.esys.framework.core.dto.organization.CompanyDto;
import com.esys.framework.core.entity.organization.Company;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.da.entity.Document;
import com.esys.framework.organization.service.ICompanyService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tr.com.esys.framework.da.service.DAService;

@Slf4j
@RestController
@RequestMapping("/documents")
public class DAController implements DAContract {

    @Autowired
    private DAService daService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    ICompanyService companyService;

    @RequestMapping(value = "/upload/{companyId}", method = RequestMethod.POST)
    public ModelResult<Document> add(@RequestParam("file") MultipartFile file, @PathVariable long companyId) throws Exception {
        try {
            final ModelResult<CompanyDto> companyDto = companyService.findOne(companyId);
            final User user = null;
            Company company = null;
            modelMapper.map(companyDto, company);
            final Document document = daService.add(company, user, file.getName(), file.getInputStream());
            return new ModelResult.ModelResultBuilder(messageSource).setData(document).build();
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public ModelResult<Document> get(@PathVariable long companyId, @PathVariable long documentId) throws Exception {
        return null;
    }


}
