package coe.wuti.wicket.core;

import org.apache.wicket.Application;
import org.apache.wicket.DefaultPageManagerProvider;
import org.apache.wicket.page.IManageablePage;
import org.apache.wicket.pageStore.IDataStore;
import org.apache.wicket.pageStore.IPageStore;

import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Created by jesus on 3/16/16.
 */
public class NoSerializationPageManagerProvider extends DefaultPageManagerProvider {

    public NoSerializationPageManagerProvider(Application application) {
        super(application);
    }

    @Override
    protected IPageStore newPageStore(IDataStore dataStore) {
        return new IPageStore() {

            @Override
            public void destroy() {
            }

            @Override
            public IManageablePage getPage(String sessionId, int pageId) {
                Logger.getGlobal().info("trying to get page from store");
                return null;
            }

            @Override
            public void removePage(String sessionId, int pageId) {
            }

            @Override
            public void storePage(String sessionId, IManageablePage page) {
                Logger.getGlobal().info("trying to store page " + page);
            }

            @Override
            public void unbind(String sessionId) {
            }

            @Override
            public Object restoreAfterSerialization(Serializable serializable) {
                Logger.getGlobal().info("trying to restore page ");
                return null;
            }

            @Override
            public IManageablePage convertToPage(Object page) {
                return null;
            }

            @Override
            public Serializable prepareForSerialization(String sessionId, Serializable page) {
                return null;
            }
        };
    }
}
