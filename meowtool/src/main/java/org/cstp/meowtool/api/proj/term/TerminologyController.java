package org.cstp.meowtool.api.proj.term;

import org.cstp.meowtool.database.Terminology;
import org.cstp.meowtool.database.TerminologyMapper;
import org.cstp.meowtool.utils.AuthUtil;
import org.cstp.meowtool.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "Terminology Manipulation")
@RequestMapping("/term")
public class TerminologyController {
    @Autowired
    TerminologyMapper terminologyMapper;

    @Autowired
    AuthUtil authUtil;
    
    @ApiOperation("Select a terminology by ID")
    @GetMapping("/{id}")
    public Result selectTerminology(@PathVariable("id") Integer id) {
        return Result.succ(terminologyMapper.selectTerminology(id));
    }

    @ApiOperation("Update a terminology by ID")
    @PutMapping("/{id}")
    public Result updateTerminology(@PathVariable("id") Integer id, @RequestBody Terminology data) {
        if (!authUtil.hasProjectRole(data.getProjId(), "SUPERVISOR")) return Result.fail(-105, "Permission denied.");

        data.setCommiter(authUtil.getUser().getId());
        data.setId(id);
        return Result.succ(terminologyMapper.updateTerminology(data));
    }

    @ApiOperation("Delete a terminology by ID")
    @DeleteMapping("/{id}")
    public Result deleteTerminology(@PathVariable("id") Integer id) {
        if (!authUtil.hasProjectRole(id, "SUPERVISOR")) return Result.fail(-105, "Permission denied.");

        return Result.succ(terminologyMapper.deleteTerminology(id));
    }
}
