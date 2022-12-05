package org.cstp.meowtool.api.proj.term;

import javax.websocket.server.PathParam;

import org.cstp.meowtool.database.Terminology;
import org.cstp.meowtool.database.TerminologyMapper;
import org.cstp.meowtool.utils.AuthUtil;
import org.cstp.meowtool.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.Data;

@Api(tags = "Project Terminology Management")
@RestController
@RequestMapping("/proj/{pid}/term")
public class ProjectTerminologyController {
    @Autowired
    private TerminologyMapper terminologyMapper;

    @Autowired
    private AuthUtil authUtil;


    @ApiOperation("Query all terminologies binded to the specified project")
    @GetMapping
    public Result selectAllTerminologyOfProject(@PathParam("pid") Integer projId) {
        return Result.succ(terminologyMapper.selectAllTerminologies(projId));
    }

    @Data
    private class TerminologyData {
        private String oriText;
        private String tarText;
    }

    @ApiOperation("Update all terminologies data in the specified project")
    @PostMapping
    public Result updateTerminologyOfProject(@PathParam("pid") Integer projId, @RequestBody TerminologyData[] data) {
        for (TerminologyData datum : data) {
            Terminology term = new Terminology();
            term.setOriWord(datum.oriText);
            term.setTarWord(datum.tarText);
            term.setCommiter(authUtil.getUser().getId());
            term.setProjId(projId);
            terminologyMapper.insertTerminology(term);
        }

        return Result.succ(null);
    }

    @GetMapping("/query")
    public Result queryTerminology(@PathParam("pid") Integer projId,String oriWord) {
        return Result.succ(terminologyMapper.selectByOriWord(projId, oriWord));
    }
}
