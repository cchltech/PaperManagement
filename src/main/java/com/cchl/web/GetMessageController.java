package com.cchl.web;

import com.cchl.dto.Result;
import com.cchl.eumn.Dictionary;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequestMapping(value = "/msg")
public class GetMessageController {

    @RequestMapping(value = "/{type}")
    public Result studentMsg(@PathVariable(value = "type", required = false) String type,
                             @SessionAttribute(value = "user_id") Integer userId) {
        try {
            if (type == null || !"student".equals(type) || !"teacher".equals(type))
                return new Result(Dictionary.ILLEGAL_VISIT);
            else if ("student".equals(type))
                return null;
            else
                return null;
        } catch (Exception e) {
            return new Result(Dictionary.SYSTEM_ERROR);
        }

    }
}
