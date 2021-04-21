package org.wallride.support.googlemap;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationParameter {

    //(required if markers not present) defines the center of the map, equidistant from all edges of the map.
    // This parameter takes a location as either a comma-separated {latitude,longitude} pair (e.g. "40.714728,-73.998672") or a
    // string address (e.g. "city hall, new york, ny") identifying a unique location on the face of the earth.
    private String center;

    //zoom (required if markers not present) defines the zoom level of the map, which determines the magnification level of the map.
    // This parameter takes a numerical value corresponding to the zoom level of the region desired.
    private Integer zoom;
}
