package com.rd.modules.common.convert.strategys;


import com.rd.modules.common.convert.entity.DocParams;

public interface DocChange<T extends DocParams> {

    String doJob(T t);
}
