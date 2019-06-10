package com.answer.test.manager;


import com.answer.test.dto.FrontUser;
import com.answer.test.entity.User;
import com.answer.test.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserManager {

    @Autowired
    private UserMapper userMapper;


    public FrontUser getUserById(Integer id)
    {
        FrontUser frontUser=null;
        if(id==null) return frontUser;
        User user = userMapper.getById(id);
        if(user!=null){
            frontUser=new FrontUser();
            BeanUtils.copyProperties(user,frontUser);
        }
        return frontUser;
    }

}
