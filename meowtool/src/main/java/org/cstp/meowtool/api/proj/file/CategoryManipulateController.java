package org.cstp.meowtool.api.proj.file;

import org.cstp.meowtool.database.Category;
import org.cstp.meowtool.database.CategoryMapper;
import org.cstp.meowtool.database.File;
import org.cstp.meowtool.database.FileMapper;
import org.cstp.meowtool.database.Project;
import org.cstp.meowtool.database.ProjectMapper;
import org.cstp.meowtool.utils.AuthUtil;
import org.cstp.meowtool.utils.DaoUtil;
import org.cstp.meowtool.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Api(tags = "Category Operation")
@RestController
@RequestMapping("/category")
public class CategoryManipulateController {
    /**
     *
     */
    private static final String INVALID_ID = "invalid id";

    /**
     * Default odd database error.
     */
    private static final Result RESULT = new Result(-9003, "Server internal error: odd database response.", null);

    @Autowired
    AuthUtil authUtil;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    FileMapper fileMapper;

    @Autowired
    ProjectMapper projectMapper;

    private boolean checkOwner(Category category) {
        if (authUtil.hasAuthority("ROLE_ADMIN")) return true;

        Project project = projectMapper.selectProject(category.getProj_id());
        return authUtil.getUser().getId().equals(project.getOwner());
    }

    @Data
    static class FileData {
        private String filename;
        private String converter;
        private String comment;
    }

    @ApiOperation("Create a new file in specified category.")
    @PostMapping("/{id}/files")
    public Result createFile (@PathVariable("id") Integer categoryId, @RequestBody FileData data) {
        Category category = categoryMapper.selectCategory(categoryId);
        if (category == null) return Result.fail(-400, INVALID_ID);
        if (!checkOwner(category)) return Result.fail(-101, "Only the project owner could create new file.");

        File file = new File();
        file.setCategory_id(categoryId);
        file.setComment(data.comment);
        file.setConverter(data.converter);
        file.setName(data.filename);
        int ret = fileMapper.insertFile(file);
        if (ret == 1) return Result.succ(null);
        return RESULT;
    }

    @ApiOperation("List all files under the specified category.")
    @GetMapping("/{id}/files")
    public Result listFiles (@PathVariable("id") Integer id) {
        Category category = categoryMapper.selectCategory(id);
        if (category == null) return Result.fail(-400, INVALID_ID);

        return Result.succ(fileMapper.selectByCategory(id));
    }
    
    @ApiOperation("Update a category.")
    @PutMapping("/{id}")
    public Result updateCategory(@PathVariable("id") Integer id, @RequestBody Category data) {
        Category category = categoryMapper.selectCategory(id);
        if (category == null) return Result.fail(-400, INVALID_ID);
        if (!checkOwner(category)) return Result.fail(-101, "Only the project owner could modify files.");

        data.setId(id);

        return DaoUtil.uniqueUpdate(categoryMapper.updateCategory(data));
    }

    @ApiOperation("Delete a category.")
    @DeleteMapping("/{id}")
    public Result deleteCategory(@PathVariable("id") Integer id) {
        Category category = categoryMapper.selectCategory(id);
        if (category == null) return Result.fail(-400, INVALID_ID);
        if (!checkOwner(category)) return Result.fail(-101, "Only the project owner could delete files.");

        return Result.succ(categoryMapper.deleteCategory(id));
    }
}
