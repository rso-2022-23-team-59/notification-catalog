package si.fri.rso.notificationcatalog.models.entities;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "notifications")
@NamedQueries(
    value = {
            @NamedQuery(name = "NotificationEntity.getAll", query = "SELECT notifications FROM NotificationEntity notifications"),
            @NamedQuery(name = "NotificationEntity.getByProductId", query = "SELECT n FROM NotificationEntity n WHERE n.productId = :productId")
    }
)
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "last_price")
    private Integer lastPrice;

    @Column(name = "threshold_price")
    private Integer thresholdPrice;

    @Column(name = "email", columnDefinition = "TEXT")
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