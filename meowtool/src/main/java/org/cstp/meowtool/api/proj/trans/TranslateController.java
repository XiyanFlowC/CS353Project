package org.cstp.meowtool.api.proj.trans;

import org.cstp.meowtool.database.CategoryMapper;
import org.cstp.meowtool.database.FileMapper;
import org.cstp.meowtool.database.ProjectMapper;
import org.cstp.meowtool.database.Text;
import org.cstp.meowtool.database.TextMapper;
import org.cstp.meowtool.database.Translation;
import org.cstp.meowtool.database.TranslationMapper;
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

@Api(tags = "Translating")
@RestController
@RequestMapping("/trans")
public class TranslateController {
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
    TranslationMapper translationMapper;

    @Autowired
    AuthUtil authUtil;

    private int getProjectId (int textId) {
        return projectMapper.selectProject(categoryMapper.selectCategory(fileMapper.selectFile(textMapper.selectId(textId).getFileId()).getCategoryId()).getProjId()).getId();
    }

    private boolean checkAdmission(int textId) {
        if (authUtil.hasAuthority("ROLE_ADMIN")) return true;
        int projId = getProjectId(textId);

        return authUtil.hasProjectRole(projId, "SUPERVIOSR") || authUtil.hasProjectRole(projId, "TRANSLATOR");
    }

    @ApiOperation("Get a translation general information")
    @GetMapping("/trans/{id}")
    public Result getTranslationEntry(@PathVariable("id") Integer id) {
        if (!checkAdmission(id)) return PERMISSION_DENIED;

        Text text = textMapper.selectId(id);
        if (text == null) return Result.fail("specified text not found");

        Translation trans = translationMapper.selectByOriId(id);

        return Result.succ(trans);
    }

    @ApiOperation("Update an exsiting translation by its id.")
    @PutMapping("/trans/{id}")
    public Result updateTranslation(@PathVariable("id") Integer id, @RequestBody String translation) {
        if (!checkAdmission(id)) return PERMISSION_DENIED;

        Text text = textMapper.selectId(id);
        if (text == null) return Result.fail("Invalid text ID.");

        translationMapper.insertNewTranslation(id, translation, authUtil.getUser().getId());

        return Result.succ(null);
    }

    @ApiOperation("Clean an exisiting translation by its id.")
    @DeleteMapping("/trans/{id}")
    public Result removeTranslation(@PathVariable("id") Integer id) {
        if (!checkAdmission(id)) return PERMISSION_DENIED;

        Text text = textMapper.selectId(id);
        if (text == null) return Result.fail("Invalid text ID.");
        
        translationMapper.insertNewTranslation(id, "", authUtil.getUser().getId());

        return Result.succ(null);
    }

    @ApiOperation("Mark an translation is weird.")
    @PutMapping("/trans/{id}/mark")
    public Result markTranslation(@PathVariable("id") Integer id) {
        if (!checkAdmission(id)) return PERMISSION_DENIED;

        textMapper.markTranslation(id);

        return Result.succ(null);
    }
}
