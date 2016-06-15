package controllers;

import play.libs.EventSource;
import play.libs.F;
import play.libs.WS;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;

import java.util.concurrent.TimeUnit;

public class Application extends Controller {

    public static Result index() {
        return ok(views.html.index.render("Hello GardenDroid"));
    }

    public static Result syncFoo() {
        return ok("sync foo");
    }

    public static F.Promise<Result> asyncFoo() {
        return F.Promise.promise(() -> ok("async foo"));
    }

    public static F.Promise<Result> asyncNonBlockingFoo() {
        return F.Promise.delayed(() -> ok("async non-blocking foo"), 5, TimeUnit.SECONDS);
    }

    public static F.Promise<Result> reactiveRequest() {
        F.Promise<WS.Response> typesafePromise = WS.url("http://www.typesafe.com").get();
        return typesafePromise.map(response -> ok(response.getBody()));
    }

    public static F.Promise<Result> reactiveComposition() {
        final F.Promise<WS.Response> twitterPromise = WS.url("http://www.twitter.com").get();
        final F.Promise<WS.Response> typesafePromise = WS.url("http://www.typesafe.com").get();

        return twitterPromise.flatMap((twitter) ->
                typesafePromise.map((typesafe) ->
                        ok(twitter.getBody() + typesafe.getBody())));
    }

    public static Result events() {
        EventSource eventSource = new EventSource() {
            public void onConnected() {
                sendData("hello");
            }
        };
        return ok(eventSource);
    }

    public static WebSocket<String> echo() {
        return new WebSocket<String>() {
            public void onReady(final In<String> in, final Out<String> out) {
                in.onMessage(out::write);
            }
        };
    }

}
