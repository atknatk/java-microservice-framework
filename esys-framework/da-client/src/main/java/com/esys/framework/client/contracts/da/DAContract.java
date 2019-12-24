package com.esys.framework.client.contracts.da;

import com.esys.framework.core.model.ModelResult;
import com.esys.framework.da.entity.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface DAContract {

    @PostMapping("/upload/{companyId}")
    ModelResult<Document> add(@RequestParam("file") MultipartFile file, @PathVariable long companyId) throws Exception;

    @GetMapping("/{companyId}/{documentId}")
    ModelResult<Document> get(@PathVariable long companyId, @PathVariable long documentId) throws Exception;

}
