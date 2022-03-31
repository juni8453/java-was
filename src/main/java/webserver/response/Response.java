package webserver.response;

import webserver.ContentType;
import webserver.Cookie;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Response {

    private final String protocol;
    private final String status;
    private final Cookie cookie = new Cookie();
    private String viewPath;
    private ContentType contentType;
    private String location;

    private Response(String protocol, String status) {
        this.protocol = protocol;
        this.status = status;
    };

    public static Response of(String protocol, String status) {
        return new Response(protocol, status);
    }

    public String getProtocol() {
        return protocol;
    }

    public String getStatus() {
        return status;
    }

    public String getViewPath() {
        return viewPath;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public String getLocation() {
        return location;
    }

    public String getContentTypeAsString() {
        return contentType.getType();
    }

    public String getSession() {
        return cookie.getSessionId().orElse(null);
    }

    public void setViewPath(String viewPath) {
        this.viewPath = viewPath;
        this.contentType = ContentType.from(viewPath);
    }

    public void setCookie(String key, String value) {
        this.cookie.setCookies(key, value);
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
