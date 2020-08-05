package com.rd.modules.common.convert.context;

import com.rd.modules.common.convert.entity.DocParams;
import com.rd.modules.common.convert.strategys.DocChange;

public class DocContext<T extends DocParams> {

    private DocChange docChange;

    public DocContext(DocChange docChange) {
        this.docChange = docChange;
    }

    public String docConvert(T t){

        return this.docChange.doJob(t);
    }
}