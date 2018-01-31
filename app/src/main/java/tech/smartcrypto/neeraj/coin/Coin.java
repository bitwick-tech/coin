package tech.smartcrypto.neeraj.coin;

import java.io.Serializable;

/**
 * Created by neeraj on 28/1/18.
 */

class Coin implements Serializable {
    private String id;
    private String description;
    private float openPrice;
    private float currentPrice;
    private String currency;
    private TagEnum tag;

    public TagEnum getTag() {
        return tag;
    }

    public void setTag(TagEnum tag) {
        this.tag = tag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(float openPrice) {
        this.openPrice = openPrice;
    }

    public float getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(float currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public enum TagEnum {
        BLACK(R.color.black,"Black"), RED(R.color.red, "Red"),
        GREEN(R.color.green, "Green"), BLUE(R.color.blue, "Blue"),YELLOW(R.color.yellow,"Yellow");
        private int code;
        private String name;
        private TagEnum(int code, String name) {
            this.code = code;
            this.name = name;
        }
        public int getTagColor() {
            return this.code;
        }
    }


}
