package si.fri.rso.notificationcatalog.services.messaging;

import com.kumuluz.ee.amqp.common.annotations.AMQPConsumer;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import si.fri.rso.notificationcatalog.lib.Notification;
import si.fri.rso.notificationcatalog.models.converters.NotificationConverter;
import si.fri.rso.notificationcatalog.models.entities.NotificationEntity;
import si.fri.rso.notificationcatalog.services.beans.NotificationBean;
import si.fri.rso.notificationcatalog.services.config.EmailNotificationProperties;
import si.fri.rso.productcatalog.lib.ProductStore;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductMessageConsumer {
    @Inject
    private EmailNotificationProperties emailNotificationProperties;

    @Inject
    private EntityManager em;

    public Notification putNotification(Integer id, Notification notification) {

        NotificationEntity c = em.find(NotificationEntity.class, id);

        if (c == null) {
            return null;
        }

        NotificationEntity updatedNotificationEntity = NotificationConverter.toEntity(notification);

        try {
            beginTx();
            updatedNotificationEntity.setId(c.getId());
            updatedNotificationEntity = em.merge(updatedNotificationEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        return NotificationConverter.toDto(updatedNotificationEntity);
    }

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }

    public void notifyFollowers(ProductStore productStore){

        Integer id = productStore.getProduct().getId();

        TypedQuery<NotificationEntity> query = em.createNamedQuery("NotificationEntity.getByProductId", NotificationEntity.class).setParameter("productId", id);

        // send emails if threshold exceeded
        List<NotificationEntity> resultList = query.getResultList();
        resultList.stream().map(NotificationConverter::toDto).filter(x -> {
            return productStore.getPrice() <= x.getThresholdPrice();
        }).map(x -> {
            sendEmail(x.getEmail(), x.getProductId(), x.getLastPrice(), productStore.getPrice());
            return x;
        }).collect(Collectors.toList());

        // update last price
        resultList.stream().map(NotificationConverter::toDto).map(x -> {
            x.setLastPrice(productStore.getPrice().intValue());
            putNotification(x.getId(), x);
            return x;
        });
    }

    public void sendEmail(String to, Integer productId, Integer oldPrice, Double newPrice)  {
        String host = emailNotificationProperties.getEmailNotificationHost();
        String apiKey = emailNotificationProperties.getEmailNotificationApiKey();
        try {
            String subject = "New price reduction for product with ID: " + productId;
            String body = "Product with id " + productId + " has changed price from " + oldPrice + " to " + newPrice;

            HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + host + "/messages")
                    .basicAuth("api", apiKey)
                    .queryString("from", "info@" + host)
                    .queryString("to", to)
                    .queryString("subject", subject)
                    .queryString("text", body)
                    .asJson();
            System.out.println(request.getBody());
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }

    }

    @AMQPConsumer(host = "PSProduct", exchange = "productInfo", key = "newProduct")
    public void getInfoNewProductStore(ProductStore psm) {

        notifyFollowers(psm);


    }
}
