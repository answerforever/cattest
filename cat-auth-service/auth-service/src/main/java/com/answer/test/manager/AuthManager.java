package com.answer.test.manager;

import com.answer.test.dto.AuthGroupUser;
import com.answer.test.dto.FrontUser;
import com.answer.test.entity.Auth;
import com.answer.test.mapper.AuthMapper;
import com.answer.test.remote.UserRemote;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthManager {
    @Autowired
    private AuthMapper authMapper;
    @Autowired
    private UserRemote userRemote;

    public AuthGroupUser getAuthById(Integer id) {
        AuthGroupUser authGroupUser = null;
        if (id == null) return authGroupUser;
        Auth auth = authMapper.getById(id);
        if (auth != null) {
            authGroupUser = new AuthGroupUser();
            BeanUtils.copyProperties(auth, authGroupUser);

            FrontUser frontUser = userRemote.getUserInfo(id);
            if (frontUser != null) {
                frontUser.setUsername(frontUser.getUsername());
            }
        }
        return authGroupUser;
    }

}
