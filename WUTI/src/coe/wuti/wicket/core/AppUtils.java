package coe.wuti.wicket.core;

/**
 * Created by jesus on 3/16/16.
 */
public class AppUtils {
    // For the Google crawler (due to one pass render strategy)
//    setPageRendererProvider(new IPageRendererProvider() {
//        @Override
//        public PageRenderer get(RenderPageRequestHandler handler) {
//            return new WebPageRenderer(handler) {
//                @Override
//                protected boolean isOnePassRender() {
//                    // To avoid 302s with Google Bot and have good SEO.
//                    String userAgent = ((HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest()).getHeader("User-Agent");
//                    if (StringUtils.contains(userAgent, "Googlebot")) {
//                        return true;
//                    } else {
//                        return super.isOnePassRender();
//                    }
//                }
//            };
//        }
//    });

    // Obfuscate non bookmarkable pages
//        getSecuritySettings().setCryptFactory(new KeyInSessionSunJceCryptFactory()) ;
//        IRequestMapper cryptoMapper = new CryptoMapper(getRootRequestMapper(), this);

    // Use interface AjaxIndicatorAware in order to: 1) Avoid double clicking on Ajax link, 2) Show a busy indicator while processing Ajax request
    // See: Wicket javadoc and this example: http://wicketinaction.com/2008/12/preventing-double-ajax-requests-in-3-lines-of-code/

    // To avoid double calling from the browser
    // getRequestCycleSettings().addResponseFilter(new EmptySrcAttributeCheckFilter());
}
