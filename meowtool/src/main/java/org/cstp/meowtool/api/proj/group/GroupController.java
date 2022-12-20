package org.cstp.meowtool.api.proj.group;

import org.cstp.meowtool.database.Group;
import org.cstp.meowtool.database.GroupMapper;
import org.cstp.meowtool.database.ProjectMapper;
import org.cstp.meowtool.database.UserMapper;
import org.cstp.meowtool.utils.AuthUtil;
import org.cstp.meowtool.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Api(tags = "Project Group Operation")
@RestController
@RequestMapping("/proj/{prj_id}/grp")
public class GroupController {
    /**
     * The message to return when the give perssion is denied.
     */
    private static final Result PERMISSION_DENIED = Result.fail(-105, "Project permission denied.");

    @Autowired
    GroupMapper groupMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    AuthUtil authUtil;

    private boolean checkPermission (int projectId) {
        if (authUtil.hasAuthority("ROLE_ADMIN")) return true;
        if (authUtil.hasProjectRole(projectId, "SUPERVISOR")) return true;
        if (authUtil.hasProjectRole(projectId, "ADMIN")) return true;
        if (projectMapper.selectProject(projectId).getOwner().equals(authUtil.getUser().getId())) return true;

        return false;
    }

    @ApiOperation("List all group member (and their roles) of specified project.")
    @GetMapping
    public Result listGroupMember(@PathVariable("prj_id") Integer projId) {
        return Result.succ(groupMapper.selectGroupsByProject(projId));
    }


    @ApiOperation("Add a user to group with specified role.")
    @PostMapping("/{usr_id}")
    public Result addGroupMember(@PathVariable("prj_id") Integer projId, @PathVariable("usr_id") Integer userId, @RequestBody String role) {
        if (!checkPermission(projId)) return PERMISSION_DENIED;

        if (null == userMapper.selectId(userId)) return Result.fail(-403, "No such user.");
        if (null == projectMapper.selectProject(projId)) return Result.fail(-405, "Path error, no such a resource.");
        if (null != groupMapper.selectGroupByProjectUser(projId, userId)) return Result.fail("Already joined in group!");

        Group group = new Group();
        group.setProjId(projId);
        group.setRole(role);
        group.setUserId(userId);
        
        groupMapper.insertGroup(group);

        return Result.succ(null);
    }

    @ApiOperation("Remove a user from specified group.")
    @DeleteMapping("/{usr_id}")
    public Result removeGroupMember (@PathVariable("prj_id") Integer projId, @PathVariable("usr_id") Integer userId) {
        if (!checkPermission(projId)) return PERMISSION_DENIED;

        return Result.succ(groupMapper.deleteGroupByProjectUser(projId, userId));
    }

    @ApiOperation("Query a relationship for it's role to specified project and user.")
    @GetMapping("/{usr_id}")
    public Result getGroupMember (@PathVariable("prj_id") Integer projId, @PathVariable("usr_id") Integer userId) {
        return Result.succ(groupMapper.selectGroupByProjectUser(projId, userId));
    }

    @ApiOperation("Modify a user's role in specified group.")
    @PutMapping("/{usr_id}")
    public Result updateGroupMemberRole (@PathVariable("prj_id") Integer projId, @PathVariable("usr_id") Integer userId, @RequestBody String role) {
        if (!checkPermission(projId)) return PERMISSION_DENIED;

        Group group = groupMapper.selectGroupByProjectUser(projId, userId);
        if (group == null) {
            group = new Group();
            group.setProjId(projId);
            group.setUserId(userId);
            group.setRole(role);

            groupMapper.insertGroup(group);
            return Result.succ("A new relationship established.");
        }
        group.setRole(role);
        groupMapper.updateGroupRole(group);
        return Result.succ("A new relationship established.");
    }
}
