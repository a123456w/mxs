package com.guo.qlzx.maxiansheng.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/16.
 */

public class TwoCategoryBean {

    /**
     * id : 1
     * name : 水果蔬菜
     * parent_id :2
     * son : [{"id":1,"name":"水果蔬菜"}]
     */

    private String id;
    private String name;
    private String parent_id;
    private boolean isTrue;

    public TwoCategoryBean() {
        this.isTrue = false;
    }

    public boolean isTrue() {
        return isTrue;
    }

    public void setTrue(boolean aTrue) {
        isTrue = aTrue;
    }

    private List<SonBean> son;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public List<SonBean> getSon() {
        return son;
    }

    public void setSon(List<SonBean> son) {
        this.son = son;
    }

    public static class SonBean {
        /**
         * id : 1
         * name : 水果蔬菜
         */

        private String id;
        private String name;
        private String parent_id;
        private boolean isSelect;

        public SonBean() {
            this.isSelect = false;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
        }
    }
}
