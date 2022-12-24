package org.cstp.meowtool.api.proj.file;

import javax.websocket.server.PathParam;

import org.cstp.meowtool.database.Category;
import org.cstp.meowtool.database.CategoryMapper;
import org.cstp.meowtool.database.File;
import org.cstp.meowtool.database.FileMapper;
import org.cstp.meowtool.database.Project;
import org.cstp.meowtool.database.ProjectMapper;
import org.cstp.meowtool.database.Text;
import org.cstp.meowtool.database.TextMapper;
import org.cstp.meowtool.utils.AuthUtil;
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

@Api(tags = "File Operation")
@RestController
@RequestMapping("/proj/file")
public class FileManipulateController {
    /**
     * Error message.
     */
    private static final String PERMISION_DENINED = "permision denined.";

    /**
     * Error message for that no such a file exists.
     */
    private static final String NO_SUCH_FILE = "No such file.";

    /**
     * Default odd database error.
     */
    private static final Result RESULT = new Result(-9003, "Server internal error: odd database response.", null);

    @Autowired
    FileMapper fileMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    TextMapper textMapper;

    @Autowired
    AuthUtil authUtil;

    @Autowired
    ProjectMapper projectMapper;

    private boolean checkOwner(File file) {
        if (authUtil.hasAuthority("ROLE_ADMIN")) return true;

        Category category = categoryMapper.selectCategory(file.getCategoryId());
        if (authUtil.hasProjectRole(category.getProjId(), "SUPERVISOR")) return true;
        Project project = projectMapper.selectProject(category.getId());
        return authUtil.getUser().getId().equals(project.getOwner());
    }

    @ApiOperation("Get the file.")
    @GetMapping("/{id}")
    public Result getFile (@PathParam("id") Integer id) {
        File file = fileMapper.selectFile(id);
        if (file == null) return Result.fail(NO_SUCH_FILE);

        return Result.succ(file);
    }

    @ApiOperation("Update the comment of the file.")
    @PutMapping("/{id}/comment")
    public Result updateFileComment (@PathVariable("id") Integer id, @RequestBody String newComment) {
        File file = fileMapper.selectFile(id);
        if (file == null) return Result.fail(NO_SUCH_FILE);
        if (!checkOwner(file)) return Result.fail(-101, PERMISION_DENINED);

        file.setComment(newComment);
        int ret = fileMapper.updateFile(file);
        if (ret == 0) return Result.succ(null);
        return RESULT;
    }

    @ApiOperation("Rename the file.")
    @PutMapping("/{id}/name")
    public Result renameFile (@PathVariable("id") Integer id, @RequestBody String newName) {
        File file = fileMapper.selectFile(id);
        if (file == null) return Result.fail(NO_SUCH_FILE);
        if (!checkOwner(file)) return Result.fail(-101, PERMISION_DENINED);

        file.setName(newName);
        int ret = fileMapper.updateFile(file);
        if (ret == 0) return Result.succ(null);
        return RESULT;
    }

    @Data
    static class ContentInitData {
        private final String oriText;
        private final String comment;
        private final Boolean marked;
    }

    @ApiOperation("Import text values to the file.")
    @PostMapping("/{id}/contents")
    public Result importTexts (@PathVariable("id") Integer id, @RequestBody ContentInitData[] data) {
        File file = fileMapper.selectFile(id);
        if (file == null) return Result.fail(NO_SUCH_FILE);
        if (!checkOwner(file)) return Result.fail(-101, PERMISION_DENINED);

        for (ContentInitData datum : data) {
            Text text = new Text();
            text.setFileId(id);
            text.setCommiter(authUtil.getUser().getId());
            text.setComment(datum.getComment());
            text.setMarked(datum.getMarked());
            text.setOriText(datum.getOriText());
            text.setTranslation("");

            int ret = textMapper.insertText(text);
            if (ret != 1) return Result.fail(-9004, "loop insertion failed due to unknown error.");
        }

        return Result.succ(categoryMapper.selectCategory(id));
    }

    @ApiOperation("List all the texts linked to this file.")
    @GetMapping("/{id}/contents")
    public Result listTexts (@PathVariable("id") Integer id) {
        return Result.succ(textMapper.selectByFile(id));
    }

    @ApiOperation("Clear all data in the file.")
    @DeleteMapping("/{id}/contents")
    public Result clearTexts (@PathVariable("id") Integer id) {
        File file = fileMapper.selectFile(id);
        if (!checkOwner(file)) return Result.fail(-101, PERMISION_DENINED);

        return Result.succ(textMapper.deleteTextsByFile(id));
    }

    @ApiOperation("Update specified file.")
    @PutMapping("/{id}")
    public Result updateFile (@PathVariable("id") Integer id, @RequestBody File data) {
        File file = fileMapper.selectFile(id);
        if (file == null) return Result.fail(NO_SUCH_FILE);
        if (!checkOwner(file)) return Result.fail(-101, PERMISION_DENINED);

        data.setId(id);
        int ret = fileMapper.updateFile(data);
        if (ret == 1) return Result.succ(null);
        return RESULT;
    }
}
