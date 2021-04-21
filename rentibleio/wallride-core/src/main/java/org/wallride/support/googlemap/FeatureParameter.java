package org.wallride.support.googlemap;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeatureParameter {

    // markers (optional) define one or more markers to attach to the image at specified locations.
    // This parameter takes a single marker definition with parameters separated by the pipe character (|).
    // Multiple markers may be placed within the same markers parameter as long as they exhibit the same style;
    // you may add additional markers of differing styles by adding additional markers parameters.
    // Note that if you supply markers for a map, you do not need to specify the (normally required) center and zoom parameters.
    private String markers;

    // path (optional) defines a single path of two or more connected points to overlay on the image at specified locations.
    // This parameter takes a string of point definitions separated by the pipe character (|), or an encoded polyline using the enc: prefix within the location declaration of the path.
    // You may supply additional paths by adding additional path parameters.
    // Note that if you supply a path for a map, you do not need to specify the (normally required) center and zoom parameters.
    private String path;

    // visible (optional) specifies one or more locations that should remain visible on the map, though no markers or other indicators will be displayed.
    // Use this parameter to ensure that certain features or map locations are shown on the Maps Static API.
    private String visible;

    // style (optional) defines a custom style to alter the presentation of a specific feature (roads, parks, and other features) of the map.
    // This parameter takes feature and element arguments identifying the features to style, and a set of style operations to apply to the selected features.
    // You can supply multiple styles by adding additional style parameters.
    private String style;
}
