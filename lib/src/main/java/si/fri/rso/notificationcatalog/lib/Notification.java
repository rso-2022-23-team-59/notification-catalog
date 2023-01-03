package si.fri.rso.notificationcatalog.lib;

public class Notification {
    
    private Integer id;

    private Integer productId;

    private Integer lastPrice;

    private Integer thresholdPrice;

    private String email;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(Integer lastPrice) {
        this.lastPrice = lastPrice;
    }

    public Integer getThresholdPrice() {
        return thresholdPrice;
    }

    public void setThresholdPrice(Integer thresholdPrice) {
        this.thresholdPrice = thresholdPrice;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
