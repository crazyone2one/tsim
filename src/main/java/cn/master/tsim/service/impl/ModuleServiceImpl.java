package cn.master.tsim.service.impl;

import cn.master.tsim.entity.Module;
import cn.master.tsim.mapper.ModuleMapper;
import cn.master.tsim.service.ModuleService;
import cn.master.tsim.service.ProjectService;
import cn.master.tsim.util.UuidUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Autowired
    private ProjectService projectService;

//    public ModuleServiceImpl(ProjectService projectService) {
//        this.projectService = projectService;
//    }

    @Override
    public List<Module> findByPartialModuleName(String moduleName) {
        return baseMapper.selectList(new QueryWrapper<Module>().lambda()
                .like(StringUtils.isNotBlank(moduleName), Module::getModuleName, moduleName));
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
        return baseMapper.selectOne(new QueryWrapper<Module>().lambda()
                .eq(StringUtils.isNotBlank(moduleName), Module::getModuleName, moduleName));
    }

    @Override
    public Module getModuleByName(String proName, String moduleName) {
        return baseMapper.selectOne(new QueryWrapper<Module>().lambda()
                .eq(StringUtils.isNotBlank(proName), Module::getProjectId, projectService.addProject(proName).getId())
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
    public Module addModule(String projectName, String moduleName) {
        final Module module = getModuleByName(projectName, moduleName);
        if (Objects.nonNull(module)) {
            return module;
        }
        final Module build = Module.builder()
                .moduleName(moduleName)
                .moduleCode(UuidUtils.generate())
                .projectId(projectService.addProject(projectName).getId())
                .createDate(new Date())
                .delFlag("0")
                .build();
        baseMapper.insert(build);
        return build;
    }

    @Override
    public List<Module> listModule(String projectId) {
        return baseMapper.selectList(new QueryWrapper<Module>().lambda()
                .eq(StringUtils.isNotBlank(projectId), Module::getProjectId, projectId));
    }
}
