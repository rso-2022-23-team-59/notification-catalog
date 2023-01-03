package si.fri.rso.notificationcatalog.api.v1;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

import com.kumuluz.ee.cors.annotations.CrossOrigin;

@OpenAPIDefinition(
    info = @Info(title = "Notification catalog API", version = "v1",
    contact = @Contact(email = "av7916@student.uni-lj.si"),
    license = @License(name = "dev"), description = "API for managing notifications."),
    servers = @Server(url = "http://localhost:8080/"))
@ApplicationPath("/v1")
@CrossOrigin(allowOrigin = "*", supportedMethods = "GET, POST, HEAD, OPTIONS, PUT")
public class NotificationApplication extends Application {

}
