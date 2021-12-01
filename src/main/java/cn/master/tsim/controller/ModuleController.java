package cn.master.tsim.controller;


import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.Module;
import cn.master.tsim.service.ModuleService;
import cn.master.tsim.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-22
 */
@Controller
@RequestMapping("/module")
public class ModuleController {

    private final ModuleService moduleService;

    @Autowired
    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @RequestMapping(value = "/queryList")
    @ResponseBody
    public ResponseResult queryList(HttpServletRequest request) {
        ResponseResult result = ResponseUtils.success("成功");
        try {
            final List<Module> modules = moduleService.findByPartialModuleName("");
            result.setData(modules);
        } catch (Exception e) {
            ResponseUtils.error("数据查询失败");
        }
        return result;
    }

    @RequestMapping(value = "/queryList/{id}")
    @ResponseBody
    public ResponseResult queryListByProject(@PathVariable String id) {
        ResponseResult result = ResponseUtils.success("成功");
        try {
            final List<Module> modules = moduleService.listModule(id);
            result.setData(modules);
        } catch (Exception e) {
            ResponseUtils.error("数据查询失败");
        }
        return result;
    }

    @RequestMapping(value = "/addModule/{projectId}/{moduleName}")
    @ResponseBody
    public ResponseResult addModule(@PathVariable String projectId, @PathVariable String moduleName) {
        ResponseResult result = ResponseUtils.success("数据添加成功");
        try {
            final Module module = moduleService.addModule(null, projectId, moduleName);
            result.setData(module.getId());
        } catch (Exception e) {
            ResponseUtils.error("数据添加失败");
        }
        return result;
    }
}

