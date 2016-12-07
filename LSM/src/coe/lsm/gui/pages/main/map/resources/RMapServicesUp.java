package coe.lsm.gui.pages.main.map.resources;

import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.util.time.Duration;

import java.io.IOException;
import java.util.logging.Logger;


/**
 * Created by jesus on 3/29/16.
 */
public class RMapServicesUp extends AbstractResource {
    @Override
    protected ResourceResponse newResourceResponse(Attributes attributes) {
        ResourceResponse resourceResponse = new ResourceResponse();
        resourceResponse.setContentType("text");
        resourceResponse.setCacheDuration(Duration.seconds(0));
        resourceResponse.setWriteCallback(new WriteCallback() {
            @Override
            public void writeData(Attributes attributes) throws IOException {
                    try {
//                        attributes.getResponse().write(renderer.getActiveServices("|"));
                    } catch (Exception e) {
                        attributes.getResponse().write("");
                    }
            }
        });

        return resourceResponse;
    }
}
