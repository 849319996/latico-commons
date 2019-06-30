package com.latico.commons.common.util.compare;


import java.util.*;

/**
 * <PRE>
 * 两个队列的数据比较结果
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 *
 * @author <B><a href="mailto:latico@qq.com"> 蓝鼎栋 </a></B>
 * @version <B>V1.0 2018年6月22日</B>
 * @since <B>JDK1.6</B>
 */
public class CompareResult<T> {

    /**
     * newCount 新对象的总数量
     */
    private int newCount;

    /**
     * oldCount 旧历史数据总数量
     */
    private int oldCount;

    /**
     * newOldRatio 新数据数量和旧数据数量实际比较后的比例
     */
    private double newOldCountRatio;

    /**
     * newObjs 新增数据
     */
    private Collection<T> newObjs = new LinkedHashSet<T>();

    /**
     * updateObjs 需要更新的数据
     */
    private Collection<T> updateObjs = new LinkedHashSet<T>();

    /**
     * deleteObjs 过期可以删除的数据
     */
    private Collection<T> deleteObjs = new LinkedHashSet<T>();

    /**
     * sameObjs 差异比较后，相同数据
     */
    private Collection<T> sameObjs = new LinkedHashSet<T>();

    /**
     * 重复的对象，在新队列里面，相同的比较key，存在相同的记录，会把后面的加进来这里
     */
    private Collection<T> repeatedObjs = new LinkedHashSet<T>();

    /**
     * updateNewOldObjMap 更新的新老对象映射
     */
    private Map<T, T> updateNewOldObjMap = new LinkedHashMap<T, T>();

    /**
     * 添加一个新增对象
     *
     * @param newObj
     */
    public void addNewObj(T newObj) {
        if (newObj != null) {
            this.newObjs.add(newObj);
        }
    }

    /**
     * 批量添加新对象
     *
     * @param newObjs
     */
    public void addNewObjs(List<T> newObjs) {
        if (newObjs != null) {
            this.newObjs.addAll(newObjs);
        }
    }

    /**
     * 添加新旧对象映射
     *
     * @param newObj
     * @param oldObj
     */
    public void addUpdateNewOldObjMap(T newObj, T oldObj) {
        if (newObj != null && oldObj != null) {
            this.updateNewOldObjMap.put(newObj, oldObj);
        }
    }

    /**
     * 批量添加新旧对象映射
     *
     * @param newOldObjMap
     */
    public void addUpdateNewOldObjMap(Map<T, T> newOldObjMap) {
        if (newOldObjMap != null) {
            this.updateNewOldObjMap.putAll(newOldObjMap);
        }
    }

    /**
     * 添加一个相同的对象进队列
     *
     * @param sameObj
     */
    public void addSameObj(T sameObj) {
        if (sameObj != null) {
            this.sameObjs.add(sameObj);
        }
    }

    /**
     * 批量添加相同的对象进队列
     *
     * @param sameObjs
     */
    public void addSameObjs(List<T> sameObjs) {
        if (sameObjs != null) {
            this.sameObjs.addAll(sameObjs);
        }
    }

    /**
     * 添加一个更新对象
     *
     * @param updateObj
     */
    public void addUpdateObj(T updateObj) {
        if (updateObj != null) {
            this.updateObjs.add(updateObj);
        }
    }

    /**
     * 批量添加更新的对象进队列
     *
     * @param updateObjs
     */
    public void addUpdateObjs(List<T> updateObjs) {
        if (updateObjs != null) {
            this.updateObjs.addAll(updateObjs);
        }
    }

    /**
     * 添加一个删除对象
     *
     * @param delObj
     */
    public void addDeleteObj(T delObj) {
        if (delObj != null) {
            this.deleteObjs.add(delObj);
        }
    }

    /**
     * 批量添加删除的对象进队列
     *
     * @param delObjs
     */
    public void addDeleteObjs(Collection<T> delObjs) {
        if (delObjs != null) {
            this.deleteObjs.addAll(delObjs);
        }
    }

    /**
     * 添加一个重复的对象
     *
     * @param repeatedObj
     */
    public void addRepeatedObj(T repeatedObj) {
        if (repeatedObj != null) {
            this.repeatedObjs.add(repeatedObj);
        }
    }

    /**
     * 批量添加重复的对象进队列
     *
     * @param repeatedObjs
     */
    public void addRepeatedObjs(Collection<T> repeatedObjs) {
        if (repeatedObjs != null) {
            this.repeatedObjs.addAll(repeatedObjs);
        }
    }

    /**
     * 获取新数据
     *
     * @return
     */
    public Collection<T> getNewObjs() {
        return newObjs;
    }

    /**
     * 获取更新数据
     *
     * @return
     */
    public Collection<T> getUpdateObjs() {
        return updateObjs;
    }

    /**
     * 获取删除数据
     *
     * @return
     */
    public Collection<T> getDeleteObjs() {
        return deleteObjs;
    }

    /**
     * 获取相同数据
     *
     * @return
     */
    public Collection<T> getSameObjs() {
        return sameObjs;
    }

    public void setNewObjs(Collection<T> newObjs) {
        this.newObjs = newObjs;
    }

    public void setUpdateObjs(Collection<T> updateObjs) {
        this.updateObjs = updateObjs;
    }

    public void setDeleteObjs(Collection<T> deleteObjs) {
        this.deleteObjs = deleteObjs;
    }

    public void setSameObjs(Collection<T> sameObjs) {
        this.sameObjs = sameObjs;
    }

    /**
     * 获取新老数据的数量比例
     *
     * @return
     */
    public double getNewOldCountRatio() {
        return newOldCountRatio;
    }

    public void setNewOldCountRatio(double newOldCountRatio) {
        this.newOldCountRatio = newOldCountRatio;
    }

    public int getNewCount() {
        return newCount;
    }

    public void setNewCount(int newCount) {
        this.newCount = newCount;
    }

    public int getOldCount() {
        return oldCount;
    }

    public void setOldCount(int oldCount) {
        this.oldCount = oldCount;
    }

    public Map<T, T> getUpdateNewOldObjMap() {
        return updateNewOldObjMap;
    }

    public void setUpdateNewOldObjMap(Map<T, T> updateNewOldObjMap) {
        this.updateNewOldObjMap = updateNewOldObjMap;
    }

    @Override
    public String toString() {
        return concatObjInfo();
    }

    private String concatObjInfo() {
        StringBuilder sb = new StringBuilder();
        return sb.append("\r\n差异比较结果CompareResult [newCount=").append(newCount).append(", oldCount=").append(oldCount
        ).append(", newOldCountRatio=").append(newOldCountRatio
        ).append(", \r\n新增对象:newObjs=").append(newObjs
        ).append(", \r\n更新对象:updateObjs=").append(updateObjs).append(", \r\n删除对象:deleteObjs=").append(deleteObjs
        ).append(", \r\n相同对象:sameObjs=").append(sameObjs).append(", \r\n重复对象:repeatedObjs=").
                append(repeatedObjs).append(", \r\n更新的新老对象映射:updateNewOldObjMap=").
                append(updateNewOldObjMap).append("]").toString();
    }

}
