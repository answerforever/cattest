package com.answer.test.mapper;

import com.answer.test.entity.Auth;
import tk.mybatis.mapper.common.Mapper;

public interface AuthMapper  extends Mapper<Auth> {

    Auth getById(Integer id);

}
