package com.esys.framework.base.reader;

import com.esys.framework.base.enums.ImportSectionEnum;

import java.io.InputStream;
import java.util.List;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
public interface ImportReader {
    List read(InputStream stream, ImportSectionEnum sectionEnum, Class exportType);
}
