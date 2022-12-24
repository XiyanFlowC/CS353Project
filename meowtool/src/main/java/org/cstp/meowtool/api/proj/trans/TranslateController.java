package org.cstp.meowtool.api.proj.trans;

import org.cstp.meowtool.database.Category;
import org.cstp.meowtool.database.CategoryMapper;
import org.cstp.meowtool.database.File;
import org.cstp.meowtool.database.FileMapper;
import org.cstp.meowtool.database.Project;
import org.cstp.meowtool.database.ProjectMapper;
import org.cstp.meowtool.database.Text;
import org.cstp.meowtool.database.TextMapper;
import org.cstp.meowtool.database.Translation;
import org.cstp.meowtool.database.TranslationMapper;
import org.cstp.meowtool.utils.AuthUtil;
import org.cstp.meowtool.utils.Result;
import org.cstp.meowtool.utils.StringUtil;
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

@Api(tags = "Translating")
@RestController
@RequestMapping("/trans")
public class TranslateController {
    private static final Result INVALID_ID = Result.fail("Invalid text ID.");

    private static final Result PERMISSION_DENIED = Result.fail(-105, "Project permission denied.");

    @Autowired
    TextMapper textMapper;

    @Autowired
    FileMapper fileMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    AuthUtil authUtil;

    private int getProjectId (Text text) {
        if (text == null) return -1;
        File file = fileMapper.selectFile(text.getFileId());
        if (file == null) return -2;
        Category category = categoryMapper.selectCategory(file.getCategoryId());
        if (category == null) return -3;
        Project project = projectMapper.selectProject(category.getProjId());
        if (project == null) return -4;
        return 0;
    }

    private boolean checkAdmission(Text text) {
        if (authUtil.hasAuthority("ROLE_ADMIN")) return true;
        int projId = getProjectId(text);
        if (projId < 0) {
            return false;
        }

        return authUtil.hasProjectRole(projId, "SUPERVIOSR") || authUtil.hasProjectRole(projId, "TRANSLATOR") || authUtil.hasProjectRole(projId, "PROOFREADER");
    }

    @ApiOperation("Get a translation general information")
    @GetMapping("/trans/{id}")
    public Result getTranslationEntry(@PathVariable("id") Integer id) {
        Text text = textMapper.selectId(id);
        if (text == null) return INVALID_ID;

        if (!checkAdmission(text)) return PERMISSION_DENIED;

        return Result.succ(text);
    }

    @ApiOperation("Update an exsiting translation by its id.")
    @PostMapping("/trans/{id}")
    public Result updateTranslation(@PathVariable("id") Integer id, @RequestBody String translation) {
        Text text = textMapper.selectId(id);
        if (text == null) return INVALID_ID;
        
        if (!checkAdmission(text)) return PERMISSION_DENIED;

        text.setTranslation(translation);
        textMapper.updateText(text);

        return Result.succ(null);
    }

    @ApiOperation("Delete an exisiting text by its id.")
    @DeleteMapping("/trans/{id}")
    public Result removeTranslation(@PathVariable("id") Integer id) {
        Text text = textMapper.selectId(id);
        if (text == null) return INVALID_ID;
        
        if (!checkAdmission(text)) return PERMISSION_DENIED;
        
        textMapper.deleteText(id);

        return Result.succ(null);
    }

    @ApiOperation("Mark an translation is weird.")
    @PutMapping("/trans/{id}/mark")
    public Result markTranslation(@PathVariable("id") Integer id) {
        Text text = textMapper.selectId(id);
        if (text == null) return INVALID_ID;
        
        if (!checkAdmission(text)) return PERMISSION_DENIED;

        return Result.succ(textMapper.markTranslation(id));
    }

    @ApiOperation("Clean a mark of a text.")
    @DeleteMapping("/trans/{id}/mark")
    public Result cleanTranslationMark(@PathVariable("id") Integer id) {
        Text text = textMapper.selectId(id);
        if (text == null) return INVALID_ID;
        
        if (!checkAdmission(text)) return PERMISSION_DENIED;

        return Result.succ(textMapper.cleanMark(id));
    }
}
