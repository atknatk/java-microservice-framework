package com.esys.main.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.esys.main.controller.output.OutputDTO;
import com.esys.main.dto.PageDTO;

public interface PageService {

	OutputDTO<Page<PageDTO>> getPages(Pageable page);

	OutputDTO<PageDTO> createPage(PageDTO input);

	OutputDTO<PageDTO> updatePage(PageDTO input);

	OutputDTO removePage(PageDTO input);
	
}
