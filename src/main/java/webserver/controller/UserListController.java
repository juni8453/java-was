package webserver.controller;

import db.DataBase;
import db.SessionDataBase;
import model.User;
import webserver.request.Request;
import webserver.response.Response;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class UserListController implements BackController {

    private static final String STATUS200 = "200 Ok ";

    @Override
    public Response process(Request request) {
        Response response = new Response(request.getProtocol(), STATUS200);
        return showUser(request, response);
    }

    private Response showUser(Request request, Response response) {
        String sessionId = request.getSessionId();
        if(!SessionDataBase.findSessionByUser(sessionId)) {
            response.saveBody("/user/login.html");

            return response;
        }

        Collection<User> users = DataBase.findAll();

        response.saveBody("/user/list.html");

        byte[] body = response.getBody().orElseThrow();
        String bodyLine = new String(body, StandardCharsets.UTF_8);

        String[] split = bodyLine.split("\n");
        StringBuffer stringBuffer = new StringBuffer();

        for (String s : split) {
            if (s.contains("{{$model}}")) {
                stringBuffer
                        .append("<tBody>").append("\n")
                        .append("<tr>").append("\n");
                int userIdx = 0;
                for (User user : users) {
                    userIdx++;
                    String userId = user.getUserId();
                    String name = user.getName();
                    String email = user.getEmail();

                    stringBuffer
                            .append("<th scope=\"row\">").append(userIdx).append("</th>").append("\n")
                            .append("<td>").append(userId).append("</td>").append("\n")
                            .append("<td>").append(name).append("</td>").append("\n")
                            .append("<td>").append(email).append("</td>").append("\n")
                            .append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>").append("\n")
                            .append("</tr>").append("\n");
                }
                stringBuffer
                        .append("</tBody>")
                        .append("\n");
                continue;
            }

            stringBuffer.append(s).append("\n");
        }

        response.changeBodyWithModel(stringBuffer.toString().getBytes(StandardCharsets.UTF_8));

        return response;
    }
}
