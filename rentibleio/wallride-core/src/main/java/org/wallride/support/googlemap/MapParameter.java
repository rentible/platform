package org.wallride.support.googlemap;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapParameter {
    // size (required) defines the rectangular dimensions of the map image. This parameter takes a string of the form {horizontal_value}x{vertical_value}.
    // For example, 500x400 defines a map 500 pixels wide by 400 pixels high. Maps smaller than 180 pixels in width will display a reduced-size Google logo.
    // This parameter is affected by the scale parameter, described below; the final output size is the product of the size and scale values.
    private String size;

    // scale (optional) affects the number of pixels that are returned. scale=2 returns twice as many pixels as scale=1 while retaining the same coverage area
    // and level of detail (i.e. the contents of the map don't change). This is useful when developing for high-resolution displays,
    // or when generating a map for printing. The default value is 1. Accepted values are 2 and 4 (4 is only available to Google Maps APIs Premium Plan customers.)
    private Integer scale;

    // format (optional) defines the format of the resulting image. By default, the Maps Static API creates PNG images. There are several possible formats including GIF, JPEG and PNG types.
    // Which format you use depends on how you intend to present the image. JPEG typically provides greater compression, while GIF and PNG provide greater detail.
    private String format;

    // maptype (optional) defines the type of map to construct. There are several possible maptype values, including roadmap, satellite, hybrid, and terrain.
    private String mapType;

    // language (optional) defines the language to use for display of labels on map tiles. Note that this parameter is only supported for some country tiles;
    // if the specific language requested is not supported for the tile set, then the default language for that tileset will be used.
    private String language;

    // region (optional) defines the appropriate borders to display, based on geo-political sensitivities.
    // Accepts a region code specified as a two-character ccTLD ('top-level domain') value.
    private String region;
}
