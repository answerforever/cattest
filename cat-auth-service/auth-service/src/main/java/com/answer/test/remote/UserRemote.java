package com.answer.test.remote;

import com.answer.test.services.UserService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;

@Repository
@FeignClient(value = "answer-sadmin")
public interface UserRemote extends UserService {
}
