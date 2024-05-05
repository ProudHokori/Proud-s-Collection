package proud.collection.utils;

import jakarta.servlet.http.HttpServletRequest;

public class UrlUtil {
    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}
