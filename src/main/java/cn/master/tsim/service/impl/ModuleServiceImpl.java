package cn.master.tsim.service.impl;

import cn.master.tsim.entity.Module;
import cn.master.tsim.mapper.ModuleMapper;
import cn.master.tsim.service.ModuleService;
import cn.master.tsim.util.UuidUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-22
 */
@Service
public class ModuleServiceImpl extends ServiceImpl<ModuleMapper, Module> implements ModuleService {

    @Override
    public List<Module> findByPartialModuleName(String moduleName) {
        return baseMapper.selectList(new QueryWrapper<Module>().lambda()
                .like(StringUtils.isNotBlank(moduleName), Module::getModuleName, moduleName));
    }

    @Override
    public List<Module> findByPartialModuleName(HttpServletRequest request) {
        String projectId = request.getParameter("projectId");
        String moduleId = request.getParameter("moduleName");
        return baseMapper.selectList(new QueryWrapper<Module>().lambda()
                .eq(Module::getProjectId,projectId)
                .like(StringUtils.isNotBlank(moduleId), Module::getModuleName, moduleId));
    }

    @Override
    public Map<String, Module> moduleMap() {
        Map<String, Module> result = new LinkedHashMap<>();
        baseMapper.selectList(new QueryWrapper<>()).forEach(temp -> {
            result.put(temp.getId(), temp);
        });
        return result;
    }

    @Override
    public Module getModuleById(String id) {
        return baseMapper.selectById(id);
    }

    @Override
    public Module getModuleByName(String moduleName) {
        // FIXME: 2021/10/22 0022 TooManyResultsException 不同的项目存在相同模块
        return baseMapper.selectOne(new QueryWrapper<Module>().lambda()
                .eq(StringUtils.isNotBlank(moduleName), Module::getModuleName, moduleName));
    }

    @Override
    public Module getModuleByName(HttpServletRequest request, String projectId, String moduleName) {
        return baseMapper.selectOne(new QueryWrapper<Module>().lambda()
                .eq(StringUtils.isNotBlank(projectId), Module::getProjectId, projectId)
                .eq(StringUtils.isNotBlank(moduleName), Module::getModuleName, moduleName));
    }

    @Override
    public Module addModule(Module module) {
        final Module module1 = getModuleByName(module.getModuleName());
        if (Objects.nonNull(module1)) {
            return module1;
        }
        baseMapper.insert(module);
        return module;
    }

    @Override
    public Module addModule(HttpServletRequest request, String projectId, String moduleName) {
        final Module module = getModuleByName(request, projectId, moduleName);
        if (Objects.nonNull(module)) {
            return module;
        }
        final Module build = Module.builder().moduleName(moduleName).moduleCode(UuidUtils.generate())
                .projectId(projectId).createDate(new Date()).delFlag(0).build();
        baseMapper.insert(build);
        return build;
    }

    @Override
    public List<Module> listModule(String projectId) {
        return baseMapper.selectList(new QueryWrapper<Module>().lambda()
                .eq(StringUtils.isNotBlank(projectId), Module::getProjectId, projectId));
    }
}
