package org.cstp.meowtool.api.proj.trans;

import org.cstp.meowtool.database.templates.Text;
import org.cstp.meowtool.database.templates.TextMapper;
import org.cstp.meowtool.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Translating")
@RestController
@RequestMapping("/trans")
public class TranslateController {
    @Autowired
    TextMapper textMapper;

    @ApiOperation("Update an exsiting translation by its id.")
    @PostMapping("/trans/{id}")
    public Result updateTranslation(@PathVariable("id") Integer id, @RequestBody String translation) {
        Text text = textMapper.selectId(id);
        if (text == null) return Result.fail("Invalid text ID.");

        text.setTrans(translation);
        int ret = textMapper.updateText(text);
        if (ret == 1) return Result.succ(null);
        if (ret == 0) return Result.fail("unkown error");
        return new Result(-9002, "Server internal error: database corrupted.", null);
    }
}
