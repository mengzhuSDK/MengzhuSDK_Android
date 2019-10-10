package com.mzmedia.presentation.dto;

import com.mengzhu.live.sdk.business.dto.BaseDto;

import java.util.List;

/**
 * @author xiefei
 * @description 商品入口
 */

public class MallGoodsListDto extends BaseDto {


    /**
     * list : [{"id":"100002068","name":"美国破产姐妹max Tarte哑光雾面口红笔有机唇膏笔envy滋润保湿","uid":"2991455","buy_url":"https://item.taobao.com/item.htm?ft=t&id=523836898501&ali_trackid=2:mm_26632614_0_0:1568255750_232_22560109&spm=a21bo.7925890.192091.1","price":"188.00","pic":"//gd1.alicdn.com/imgextra/i3/0/O1CN01fI0v8x2D3mVDxQ0sq_!!0-item_pic.jpg_400x400.jpg"}]
     * total : 1
     */

    private int total;
    private List<ListBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 100002068
         * name : 美国破产姐妹max Tarte哑光雾面口红笔有机唇膏笔envy滋润保湿
         * uid : 2991455
         * buy_url : https://item.taobao.com/item.htm?ft=t&id=523836898501&ali_trackid=2:mm_26632614_0_0:1568255750_232_22560109&spm=a21bo.7925890.192091.1
         * price : 188.00
         * pic : //gd1.alicdn.com/imgextra/i3/0/O1CN01fI0v8x2D3mVDxQ0sq_!!0-item_pic.jpg_400x400.jpg
         */

        private String id;
        private String name;
        private String uid;
        private String buy_url;
        private String price;
        private String pic;

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

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getBuy_url() {
            return buy_url;
        }

        public void setBuy_url(String buy_url) {
            this.buy_url = buy_url;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}
