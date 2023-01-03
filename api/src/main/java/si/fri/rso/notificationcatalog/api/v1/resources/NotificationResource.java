package si.fri.rso.notificationcatalog.api.v1.resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import si.fri.rso.notificationcatalog.lib.Notification;
import si.fri.rso.notificationcatalog.services.beans.NotificationBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/notifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotificationResource {

    private Logger log = Logger.getLogger(NotificationResource.class.getName());

    @Inject
    private NotificationBean notificationBean;

    @Context
    protected UriInfo uriInfo;

    @Operation(description = "Get all notifications.", summary = "Get all notifications")
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "List of notifications",
            content = @Content(schema = @Schema(implementation = Notification.class, type = SchemaType.ARRAY)),
            headers = {@Header(name = "X-Total-Count", description = "Number of notifications in list")}
        )
    })
    @GET
    public Response getAllNotifications() {
        // notificationBean.sendEmail("av7916@student.uni-lj.si",1, 10, 5.0);
        List<Notification> notifications = notificationBean.getNotificationFilter(uriInfo);
        return Response.ok(notifications).header("X-Total-Count", notifications.size()).build();
    }

    @Operation(description = "Get data for a single notification.", summary = "Get data for a single notification")
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Single notification information.",
            content = @Content(schema = @Schema(implementation = Notification.class))
        ),
        @APIResponse(
            responseCode = "404",
            description = "Store could not be found."
        ),
    })
    @GET
    @Path("/{notificationId}")
    public Response getSingleStore(@Parameter(description = "Store ID.", required = true)
                                     @PathParam("notificationId") Integer notificationId) {

        Notification notification = notificationBean.getNotification(notificationId);
        if (notification == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(notification).build();
    }

    @Operation(description = "Insert new notification.", summary = "Insert new notification")
    @APIResponses({
        @APIResponse(
            responseCode = "201",
            description = "Store successfully added.",
            content = @Content(schema = @Schema(implementation = Notification.class))
        ),
        @APIResponse(
            responseCode = "400",
            description = "The notification could not be inserted. Incorrect or missing parameters."
        ),
    })
    @POST
    public Response createStore(@RequestBody(
            description = "Object with notification information.",
            required = true, content = @Content(
            schema = @Schema(implementation = Notification.class))) Notification notification) {

        if ((notification.getLastPrice() == null || notification.getProductId() == null || notification.getEmail() == null || notification.getThresholdPrice() == null)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            notification = notificationBean.createNotification(notification);
        }

        return Response.status(Response.Status.CREATED).entity(notification).build();

    }


    @Operation(description = "Update single notification.", summary = "Update notification")
    @APIResponses({
        @APIResponse(
            responseCode = "204",
            description = "Store successfully updated.",
            content = @Content(schema = @Schema(implementation = Notification.class))
        ),
        @APIResponse(
            responseCode = "404",
            description = "Store could not be found."
        )
    })
    @PUT
    @Path("{notificationId}")
    public Response putStore(@Parameter(description = "Store ID.", required = true)
                               @PathParam("notificationId") Integer notificationId,
                               @RequestBody(
                                       description = "DTO object with notification information.",
                                       required = true, content = @Content(
                                       schema = @Schema(implementation = Notification.class)))
                             Notification notification) {

        notification = notificationBean.putNotification(notificationId, notification);

        if (notification == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(notification).build();
    }

    @Operation(description = "Delete notification.", summary = "Delete notification")
    @APIResponses({
        @APIResponse(
            responseCode = "204",
            description = "Store successfully deleted."
        ),
        @APIResponse(
            responseCode = "404",
            description = "Store could not be found."
        )
    })
    @DELETE
    @Path("{notificationId}")
    public Response deleteStore(@Parameter(description = "Store ID.", required = true)
                                  @PathParam("notificationId") Integer notificationId) {

        boolean deleted = notificationBean.deleteNotification(notificationId);

        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


}
