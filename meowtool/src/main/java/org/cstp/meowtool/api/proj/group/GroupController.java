package org.cstp.meowtool.api.proj.group;

import org.cstp.meowtool.database.Group;
import org.cstp.meowtool.database.GroupMapper;
import org.cstp.meowtool.database.ProjectMapper;
import org.cstp.meowtool.database.UserMapper;
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
import lombok.Data;

@Api(tags = "Project Group Operation")
@RestController
@RequestMapping("/proj/{prj_id}/grp")
public class GroupController {
    @Autowired
    GroupMapper groupMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    ProjectMapper projectMapper;

    @ApiOperation("List all group member (and their roles) of specified project.")
    @GetMapping
    public Result listGroupMember(@PathVariable("prj_id") Integer projId) {
        return Result.succ(groupMapper.selectGroupsByProject(projId));
    }

    @Data
    static class AddUserData {
        private Integer userId;
        private String role;
    }

    @ApiOperation("Add a user to group with specified role.")
    @PostMapping
    public Result addGroupMember(@PathVariable("prj_id") Integer projId, @RequestBody AddUserData data) {
        if (null == userMapper.selectId(data.getUserId())) return Result.fail(-400, "No such user.");
        if (null == projectMapper.selectProject(projId)) return Result.fail(-401, "Path error, no such a resource.");
        if (null != groupMapper.selectGroupByProjectUser(projId, data.userId)) return Result.fail("Already joined in group!");

        Group group = new Group();
        group.setProj_id(projId);
        group.setRole(data.getRole());
        group.setUser_id(data.getUserId());
        
        groupMapper.insertGroup(group);

        return Result.succ(null);
    }

    @ApiOperation("Remove a user from specified group.")
    @DeleteMapping("/{usr_id}")
    public Result removeGroupMember (@PathVariable("prj_id") Integer projId, @PathVariable("usr_id") Integer userId) {
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
        Group group = groupMapper.selectGroupByProjectUser(projId, userId);
        if (group == null) {
            group = new Group();
            group.setProj_id(projId);
            group.setUser_id(userId);
            group.setRole(role);

            groupMapper.insertGroup(group);
            return Result.succ("A new relationship established.");
        }
        group.setRole(role);
        groupMapper.updateGroupRole(group);
        return Result.succ("A new relationship established.");
    }
}
