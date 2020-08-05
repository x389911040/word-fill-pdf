package com.rd.modules.common.convert.strategyImpls;

import com.rd.modules.common.convert.entity.DocParams;
import com.rd.modules.common.convert.strategys.DocChange;
import com.rd.modules.common.convert.utils.FileChangeUtils;
import com.rd.modules.common.convert.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Excel2PdfStrategyImpl implements DocChange<DocParams> {
    @Autowired
    private FileChangeUtils fileChangeUtils;
    @Value("${doc.convert.docpath}")
    private String docPath;

    @Override
    public String doJob(DocParams baseParams) {
        String fileName = FileUtil.randomFileName("pdf");
        String excelPath = docPath+fileName;

        try {
            fileChangeUtils.excel2pdf(excelPath,baseParams.getFilepath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return excelPath;
    }
}
