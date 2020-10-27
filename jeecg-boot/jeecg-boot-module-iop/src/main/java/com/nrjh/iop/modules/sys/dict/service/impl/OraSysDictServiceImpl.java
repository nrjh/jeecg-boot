package com.nrjh.iop.modules.sys.dict.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nrjh.iop.modules.sys.dict.entry.IopSysDict;
import com.nrjh.iop.modules.sys.dict.entry.IopSysDictItem;
import com.nrjh.iop.modules.sys.dict.mapper.IopSysDictItemMapper;
import com.nrjh.iop.modules.sys.dict.mapper.IopSysDictMapper;
import com.nrjh.iop.modules.sys.dict.model.IopTreeSelectModel;
import com.nrjh.iop.modules.sys.dict.service.IIopSysDictService;
import com.nrjh.iop.modules.sys.dict.service.IOraSysDictService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.vo.DictModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
@Service
@Slf4j
@DS("ora")
public class OraSysDictServiceImpl extends ServiceImpl<IopSysDictMapper, IopSysDict> implements IOraSysDictService {

    @Autowired
    private IopSysDictMapper IopSysDictMapper;
    @Autowired
    private IopSysDictItemMapper IopSysDictItemMapper;

    /**
     * 通过查询指定code 获取字典
     *
     * @param code
     * @return
     */
    @Override
    @Cacheable(value = CacheConstant.SYS_DICT_CACHE, key = "#code")
    public List<DictModel> queryDictItemsByCode(String code) {
        log.info("无缓存dictCache的时候调用这里！");
        return IopSysDictMapper.queryDictItemsByCode(code);
    }

    /**
     * 通过查询指定code 获取字典值text
     *
     * @param code
     * @param key
     * @return
     */

    @Override
    @Cacheable(value = CacheConstant.SYS_DICT_CACHE, key = "#code+':'+#key")
    public String queryDictTextByKey(String code, String key) {
        log.info("无缓存dictText的时候调用这里！");
        return IopSysDictMapper.queryDictTextByKey(code, key);
    }

    /**
     * 通过查询指定table的 text code 获取字典
     * dictTableCache采用redis缓存有效期10分钟
     *
     * @param table
     * @param text
     * @param code
     * @return
     */
    @Override
    //@Cacheable(value = CacheConstant.SYS_DICT_TABLE_CACHE)
    public List<DictModel> queryTableDictItemsByCode(String table, String text, String code) {
        log.info("无缓存dictTableList的时候调用这里！");
        return IopSysDictMapper.queryTableDictItemsByCode(table, text, code);
    }

    @Override
    public List<DictModel> queryTableDictItemsByCodeAndFilter(String table, String text, String code, String filterSql) {
        log.info("无缓存dictTableList的时候调用这里！");
        return IopSysDictMapper.queryTableDictItemsByCodeAndFilter(table, text, code, filterSql);
    }

    /**
     * 通过查询指定table的 text code 获取字典值text
     * dictTableCache采用redis缓存有效期10分钟
     *
     * @param table
     * @param text
     * @param code
     * @param key
     * @return
     */
    @Override
    @Cacheable(value = CacheConstant.SYS_DICT_TABLE_CACHE)
    public String queryTableDictTextByKey(String table, String text, String code, String key) {
        log.info("无缓存dictTable的时候调用这里！");
        return IopSysDictMapper.queryTableDictTextByKey(table, text, code, key);
    }

    /**
     * 通过查询指定table的 text code 获取字典，包含text和value
     * dictTableCache采用redis缓存有效期10分钟
     *
     * @param table
     * @param text
     * @param code
     * @param keyArray
     * @return
     */
    @Override
    @Cacheable(value = CacheConstant.SYS_DICT_TABLE_CACHE)
    public List<String> queryTableDictByKeys(String table, String text, String code, String[] keyArray) {
        List<DictModel> dicts = IopSysDictMapper.queryTableDictByKeys(table, text, code, keyArray);
        List<String> texts = new ArrayList<>(dicts.size());
        // 查询出来的顺序可能是乱的，需要排个序
        for (String key : keyArray) {
            for (DictModel dict : dicts) {
                if (key.equals(dict.getValue())) {
                    texts.add(dict.getText());
                    break;
                }
            }
        }
        return texts;
    }

    /**
     * 根据字典类型id删除关联表中其对应的数据
     */
    @Override
    public boolean deleteByDictId(IopSysDict IopSysDict) {
        IopSysDict.setDelFlag(CommonConstant.DEL_FLAG_1);
        return this.updateById(IopSysDict);
    }

    @Override
    @Transactional
    public void saveMain(IopSysDict IopSysDict, List<IopSysDictItem> sysDictItemList) {

        IopSysDictMapper.insert(IopSysDict);
        if (sysDictItemList != null) {
            for (IopSysDictItem entity : sysDictItemList) {
                entity.setDictId(IopSysDict.getId());
                IopSysDictItemMapper.insert(entity);
            }
        }
    }

    @Override
    public List<DictModel> queryAllDepartBackDictModel() {
        return baseMapper.queryAllDepartBackDictModel();
    }

    @Override
    public List<DictModel> queryAllUserBackDictModel() {
        return baseMapper.queryAllUserBackDictModel();
    }

    @Override
    public List<DictModel> queryTableDictItems(String table, String text, String code, String keyword) {
        return baseMapper.queryTableDictItems(table, text, code, "%" + keyword + "%");
    }

    @Override
    public List<IopTreeSelectModel> queryTreeList(Map<String, String> query, String table, String text, String code, String pidField, String pid, String hasChildField) {
        return baseMapper.queryTreeList(query, table, text, code, pidField, pid, hasChildField);
    }

    @Override
    public void deleteOneDictPhysically(String id) {
        this.baseMapper.deleteOneById(id);
        this.IopSysDictItemMapper.delete(new LambdaQueryWrapper<IopSysDictItem>().eq(IopSysDictItem::getDictId, id));
    }

    @Override
    public void updateDictDelFlag(int delFlag, String id) {
        baseMapper.updateDictDelFlag(delFlag, id);
    }

    @Override
    public List<IopSysDict> queryDeleteList() {
        return baseMapper.queryDeleteList();
    }
}