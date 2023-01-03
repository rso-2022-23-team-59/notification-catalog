package si.fri.rso.notificationcatalog.models.converters;

import si.fri.rso.notificationcatalog.models.entities.NotificationEntity;
import si.fri.rso.notificationcatalog.lib.Notification;

public class NotificationConverter {

    public static Notification toDto(NotificationEntity entity) {

        Notification dto = new Notification();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setProductId(entity.getProductId());
        dto.setLastPrice(entity.getLastPrice());
        dto.setThresholdPrice(entity.getThresholdPrice());

        return dto;

    }

    public static NotificationEntity toEntity(Notification dto) {

        NotificationEntity entity = new NotificationEntity();
        entity.setEmail(dto.getEmail());
        entity.setLastPrice(dto.getLastPrice());
        entity.setThresholdPrice(dto.getThresholdPrice());
        entity.setProductId(dto.getProductId());

        return entity;

    }

}
