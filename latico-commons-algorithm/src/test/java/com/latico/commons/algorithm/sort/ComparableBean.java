package com.latico.commons.algorithm.sort;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-03-26 0:03
 * @Version: 1.0
 */
public class ComparableBean implements Comparable<ComparableBean> {

    private int id;

    public ComparableBean(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ComparableBean{");
        sb.append("id=").append(id);
        sb.append('}');
        return sb.toString();
    }

    public ComparableBean() {
    }

    @Override
    public int compareTo(ComparableBean o) {
        if (this.id > o.id) {
            return 1;
        } else if (this.id == o.id) {
            return 0;
        } else {
            return -1;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
