package com.rd.modules.common.convert.strategyImpls;


import com.rd.modules.common.convert.entity.DocParams;
import com.rd.modules.common.convert.strategys.DocChange;
import com.rd.modules.common.convert.utils.FileChangeUtils;
import com.rd.modules.common.convert.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * doc 转 pdf
 */
@Component
public class Doc2PdfStrategyImpl implements DocChange<DocParams> {

    @Autowired
    private FileChangeUtils fileChangeUtils;

    @Value("${doc.convert.docpath}")
    private String docPath;

    @Override
    public String doJob(DocParams docParams) {
        String fileName = FileUtil.randomFileName("pdf");
        String pdfPath = docPath+fileName;

        fileChangeUtils.doc2pdf(pdfPath, docParams.getFilepath());
        return pdfPath;
    }
}
