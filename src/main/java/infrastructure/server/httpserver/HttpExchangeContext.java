package infrastructure.server.httpserver;

import com.sun.net.httpserver.HttpExchange;
import infrastructure.server.AbstractHttpContext;
import infrastructure.server.HttpContext;
import infrastructure.server.HttpResponse;

class HttpExchangeContext extends AbstractHttpContext{


    private HttpExchangeContext(HttpExchangeRequest request, HttpResponse response) {
        super(request, response);
    }

    public static HttpContext from(HttpExchange httpExchange){
        HttpExchangeRequest request = HttpExchangeRequest.from(httpExchange);
        HttpResponse response = HttpExchangeResponse.from(httpExchange);
        return new HttpExchangeContext(request, response);
    }
}
