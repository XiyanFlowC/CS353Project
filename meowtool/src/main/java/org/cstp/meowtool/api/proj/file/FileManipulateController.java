package org.cstp.meowtool.api.proj.file;

import org.cstp.meowtool.database.templates.Category;
import org.cstp.meowtool.database.templates.CategoryMapper;
import org.cstp.meowtool.database.templates.File;
import org.cstp.meowtool.database.templates.FileMapper;
import org.cstp.meowtool.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Api(tags = "File Operation")
@RestController
@RequestMapping("/proj/file")
public class FileManipulateController {
    @Autowired
    FileMapper fileMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Data
    static class FileData {
        private String filename;
        private String converter;
        private String comment;
    }

    @ApiOperation("Create a new file in specified category.")
    @PostMapping()
    public Result createFile (Integer categoryId, @RequestBody FileData data) {
        File file = new File();
        file.setCategory_id(categoryId);
        file.setComment(data.comment);
        file.setConverter(data.converter);
        file.setName(data.filename);
        int ret = fileMapper.insertFile(file);
        if (ret == 1) return Result.succ(null);
        return new Result(-9003, "Server internal error: odd database response.", null);
    }

    @ApiOperation("Update the comment of the file.")
    @PutMapping("/{id}/comment")
    public Result updateFileComment (@PathVariable("id") Integer id, @RequestBody String newComment) {
        File file = fileMapper.selectFile(id);
        if (file == null) return Result.fail("No such a file.");
        file.setComment(newComment);
        int ret = fileMapper.updateFile(file);
        if (ret == 0) return Result.succ(null);
        return new Result(-9002, "Server internal error: odd database response.", null);
    }

    @ApiOperation("Update the comment of the file.")
    @PutMapping("/{id}/rename")
    public Result renameFileComment (@PathVariable("id") Integer id, @RequestBody String newName) {
        File file = fileMapper.selectFile(id);
        if (file == null) return Result.fail("No such a file.");
        file.setName(newName);
        int ret = fileMapper.updateFile(file);
        if (ret == 0) return Result.succ(null);
        return new Result(-9002, "Server internal error: odd database response.", null);
    }

    @ApiOperation("Update specified file.")
    @PutMapping("/{id}")
    public Result updateFile (@PathVariable("id") Integer id, @RequestBody File data) {
        data.setId(id);
        int ret = fileMapper.updateFile(data);
        if (ret == 1) return Result.succ(null);
        return new Result (-9002, "internal server error", null);
    }
}
