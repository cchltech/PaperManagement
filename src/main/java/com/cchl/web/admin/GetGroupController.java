package com.cchl.web.admin;

import com.cchl.service.admin.AdminHandle;
import com.cchl.service.admin.GroupingHandle;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping(value = "/admin")
public class GetGroupController {

    @Autowired
    private GroupingHandle groupingHandle;
    @Autowired
    private AdminHandle adminHandle;

    @RequestMapping(value = "/getGroup")
    public ResponseEntity<byte[]> getGroup(@SessionAttribute(value = "user_id")Integer userId,
                                           @RequestParam(value = "new")Integer n) {
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            File file;
            if (groupingHandle.getFile(userId, n)) {
                int departmentId = adminHandle.getDepartmentIdByUserId(userId);
                file = new File("/home/beiyi/file/excel/" + departmentId + "/group.xlsx");
                String downloadFileName = new String("group.xlsx".getBytes("UTF-8"), "iso-8859-1");
                httpHeaders.setContentDispositionFormData("attachment", downloadFileName);
                httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                return new ResponseEntity<>(FileUtils.readFileToByteArray(file), httpHeaders, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
