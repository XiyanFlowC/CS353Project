package org.cstp.meowtool.api.proj;

import org.cstp.meowtool.database.Category;
import org.cstp.meowtool.database.CategoryMapper;
import org.cstp.meowtool.database.Project;
import org.cstp.meowtool.database.ProjectMapper;
import org.cstp.meowtool.utils.AuthUtil;
import org.cstp.meowtool.utils.DaoUtil;
import org.cstp.meowtool.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "Project management")
@RestController
@RequestMapping("/proj")
public class ProjectController {
    private static final Result INVALID_ID_FAIL_MSG = Result.fail(-401, "invalid id");

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    AuthUtil authUtil;

    @ApiOperation("Create a new category for this project.")
    @PostMapping("/{proj_id}/category")
    public Result createCategory(@PathVariable("proj_id") Integer projId, @RequestBody Category data) {
        if (null == projectMapper.selectProject(projId)) {
            return INVALID_ID_FAIL_MSG;
        }

        data.setProj_id(projId);
        return DaoUtil.uniqueUpdate(categoryMapper.insertCategory(data));
    }

    @ApiOperation("List categories with paging.")
    @GetMapping("/{id}/category")
    public Result listCategories(
        @PathVariable("id") Integer projId,
        @ApiParam(value = "The page number.", example = "1", allowableValues = "range[1,infinity]") Integer page,
        @ApiParam(value = "The amount of entries will be shown in a single page.", example = "10", allowableValues = "range[1,infinity]") Integer size) {
        if (page == null) page = 1;
        if (size == null) size = 10;
        if (page < 1 || size < 1) return Result.fail(-500, "invalid parameter(s).");
            
        if (null == projectMapper.selectProject(projId)) return INVALID_ID_FAIL_MSG;

        return Result.succ(categoryMapper.selectByProjectWithPaging(projId, page, size));
    }

    @ApiOperation("Create a new project.")
    @PostMapping
    public Result createProject(@RequestBody Project data) {
        if (null != projectMapper.selectProjectName(data.getName())) {
            return Result.fail(-400, "Project name has already used.");
        }

        data.setOwner(authUtil.getUser().getId());
        return DaoUtil.uniqueUpdate(projectMapper.insertProject(data));
    }

    @ApiOperation("List the projects with paging enabled.")
    @GetMapping
    public Result listProject(
        @ApiParam(value = "The page number.", example = "1", allowableValues = "range[1,infinity]") Integer page,
        @ApiParam(value = "The amount of entries will be shown in a single page.", example = "10", allowableValues = "range[1,infinity]") Integer size) {
        if (page == null) page = 1;
        if (size == null) size = 10;
        if (page < 1 || size < 1) return Result.fail(-500, "invalid parameter(s).");

        return Result.succ(projectMapper.selectWithPaging(page, size));
    }

    @ApiOperation("Delete the specified project.")
    @DeleteMapping("/{id}")
    public Result deleteProject(@PathVariable("id") Integer id) {
        Project project = projectMapper.selectProject(id);
        if (project == null) return INVALID_ID_FAIL_MSG;

        for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
            if (authority.getAuthority().compareTo("ROLE_ADMIN") == 0)
                return Result.succ(projectMapper.deleteProject(id)); // need to sure that delete is idempotent.
        }
        if (authUtil.getUser().getId().equals(project.getOwner())) {
            return Result.fail(-101, "only owner or admin can delete the project.");
        }
        
        return Result.succ(projectMapper.deleteProject(id));
    }
}
