package cn.master.tsim.service;

import cn.master.tsim.entity.Module;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-22
 */
public interface ModuleService extends IService<Module> {

    List<Module> findByPartialModuleName(String moduleName);

    Map<String, Module> moduleMap();

    Module getModuleById(String id);

    Module getModuleByName(String moduleName);
    Module getModuleByName(String proName,String moduleName);

    Module addModule(Module module);

    Module addModule(String projectName, String moduleName);

    List<Module> listModule(String projectId);
}
