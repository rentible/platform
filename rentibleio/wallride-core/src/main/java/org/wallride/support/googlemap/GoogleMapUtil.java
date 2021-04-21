package org.wallride.support.googlemap;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.env.Environment;
import org.wallride.exception.GoogleMapException;
import org.wallride.support.ContextProvider;

public class GoogleMapUtil {

    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/staticmap?";
    private static final String AND = "&";

    /**
     * Build the url of static img url based on Google Map API
     *
     * @param googleMapParam
     * @return
     */
    public static String buildStaticImgUrl(GoogleMapParam googleMapParam) {

        try {
            checkGoogleMapParams(googleMapParam);
            StringBuilder sb = new StringBuilder(BASE_URL);

            sb.append(buildLocationParameters(googleMapParam.getLocationParameter()));

            if (googleMapParam.getMapParameter() != null) {
                appendSeparatorCharacterIfNeeded(sb, AND);
                sb.append(buildMapParameters(googleMapParam.getMapParameter()));
            }

            if (googleMapParam.getFeatureParameter() != null) {
                appendSeparatorCharacterIfNeeded(sb, AND);
                sb.append(buildFeatureParameters(googleMapParam.getFeatureParameter()));
            }
            appendSeparatorCharacterIfNeeded(sb, AND);
            sb.append(buildApiKeyParam());
            return sb.toString();
        } catch (GoogleMapException e) {
            return "";
        }
    }

    private static void checkGoogleMapParams(GoogleMapParam googleMapParam) throws GoogleMapException {
        if (googleMapParam.getFeatureParameter() == null || StringUtils.isBlank(googleMapParam.getFeatureParameter().getMarkers())) {
            if (StringUtils.isBlank(googleMapParam.getLocationParameter().getCenter())) {
                throw new GoogleMapException("center parameter is required (if markers not present), but it's missing");
            }

            if (googleMapParam.getLocationParameter().getZoom() == null) {
                throw new GoogleMapException("zoom parameter is required(if markers not present), but it's missing");
            }
        }

        if (StringUtils.isBlank(googleMapParam.getMapParameter().getSize())) {
            throw new GoogleMapException("size parameter is required, but it's missing");
        }
    }

    /**
     * @param locationParameter
     * @return
     */
    private static StringBuilder buildLocationParameters(LocationParameter locationParameter) {
        StringBuilder sb = new StringBuilder();
        if (locationParameter != null) {
            if (StringUtils.isNotBlank(locationParameter.getCenter())) {
                sb.append("center=");
                sb.append(locationParameter.getCenter());
            }

            if (locationParameter.getZoom() != null) {
                appendSeparatorCharacterIfNeeded(sb, AND);
                sb.append("zoom=");
                sb.append(locationParameter.getZoom());
            }
        }

        return sb;
    }

    /**
     * @param mapParameter
     * @return
     */
    private static StringBuilder buildMapParameters(MapParameter mapParameter) {
        StringBuilder sb = new StringBuilder();

        if (StringUtils.isNotBlank(mapParameter.getSize())) {
            sb.append("size=");
            sb.append(mapParameter.getSize());
        }

        if (mapParameter.getScale() != null) {
            appendSeparatorCharacterIfNeeded(sb, AND);

            sb.append("scale=");
            sb.append(mapParameter.getScale());
        }

        if (StringUtils.isNotBlank(mapParameter.getFormat())) {
            appendSeparatorCharacterIfNeeded(sb, AND);

            sb.append("format =");
            sb.append(mapParameter.getFormat());
        }

        if (StringUtils.isNotBlank(mapParameter.getMapType())) {
            appendSeparatorCharacterIfNeeded(sb, AND);

            sb.append("maptype=");
            sb.append(mapParameter.getMapType());
        }

        if (StringUtils.isNotBlank(mapParameter.getLanguage())) {
            appendSeparatorCharacterIfNeeded(sb, AND);

            sb.append("language=");
            sb.append(mapParameter.getLanguage());
        }

        if (StringUtils.isNotBlank(mapParameter.getRegion())) {
            appendSeparatorCharacterIfNeeded(sb, AND);

            sb.append("region=");
            sb.append(mapParameter.getRegion());
        }

        return sb;
    }

    /**
     * @param featureParameter
     * @return
     */
    private static StringBuilder buildFeatureParameters(FeatureParameter featureParameter) {
        StringBuilder sb = new StringBuilder();

        if (StringUtils.isNotBlank(featureParameter.getMarkers())) {
            sb.append("markers=");
            sb.append(featureParameter.getMarkers());
        }

        if (StringUtils.isNotBlank(featureParameter.getPath())) {
            appendSeparatorCharacterIfNeeded(sb, AND);
            sb.append("path=");
            sb.append(featureParameter.getPath());
        }

        if (StringUtils.isNotBlank(featureParameter.getVisible())) {
            appendSeparatorCharacterIfNeeded(sb, AND);
            sb.append("visible=");
            sb.append(featureParameter.getVisible());
        }

        if (StringUtils.isNotBlank(featureParameter.getStyle())) {
            appendSeparatorCharacterIfNeeded(sb, AND);
            sb.append("style=");
            sb.append(featureParameter.getStyle());
        }

        return sb;
    }

    private static StringBuilder buildApiKeyParam() {
        StringBuilder sb = new StringBuilder();

        sb.append("key=");
        sb.append(ContextProvider.getBean(Environment.class).getProperty("google.map.static.api-key"));

        return sb;
    }

    private static void appendSeparatorCharacterIfNeeded(StringBuilder sb, String separator) {
        if (sb.length() > 0) {
            sb.append(separator);
        }
    }

    private GoogleMapUtil() {
    }
}
