package coe.wuti.wicket.core;

import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.IRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

/**
 * Created by jesus on 3/16/16.
 */
public class MyRequestCycleListener implements IRequestCycleListener {
    @Override
    public void onBeginRequest(RequestCycle cycle) {
//        Logger.getGlobal().info("Begin request for '" + getURI(cycle) + "'");
    }

    private String getURI(RequestCycle cycle) {
        if (cycle.getRequest().getContainerRequest() instanceof HttpServletRequest) {
            HttpServletRequest containerRequest = (HttpServletRequest) cycle.getRequest().getContainerRequest();
            return containerRequest.getRequestURI();
        } else
            return "unknown";
    }

    @Override
    public void onUrlMapped(RequestCycle cycle, IRequestHandler handler, Url url) {
//        Logger.getGlobal().info("url " + url.getPath() + " just mapped and handled");
    }

    @Override
    public void onRequestHandlerResolved(RequestCycle cycle, IRequestHandler handler) {
//        Logger.getGlobal().info("handler created: " + handler.getClass());
    }

    @Override
    public void onRequestHandlerScheduled(RequestCycle cycle, IRequestHandler handler) {
//        Logger.getGlobal().info("handler scheduled " + cycle);
    }

    @Override
    public void onRequestHandlerExecuted(RequestCycle cycle, IRequestHandler handler) {
//        Logger.getGlobal().info("handler executed " + cycle);
    }


    @Override
    public void onEndRequest(RequestCycle cycle) {
//        Logger.getGlobal().info("request ended");
    }

    @Override
    public void onDetach(RequestCycle cycle) {
//        Logger.getGlobal().info("cycle detached");
    }


    @Override
    public IRequestHandler onException(RequestCycle cycle, Exception ex) {
//        Logger.getGlobal().severe(" ****** Exception: " + ex.getMessage());
        return null;
    }

    @Override
    public void onExceptionRequestHandlerResolved(RequestCycle cycle, IRequestHandler handler, Exception exception) {
//        Logger.getGlobal().info("found exception request handler " + handler.toString());
    }


}
